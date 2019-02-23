package crystal.scrumify.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import crystal.scrumify.R;
import crystal.scrumify.models.User;
import crystal.scrumify.responses.LoginResponse;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.ConstantUtils;
import crystal.scrumify.utils.PermissionUtils;
import crystal.scrumify.utils.PreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static crystal.scrumify.utils.PermissionUtils.isPermissionGranted;

public class AuthActivity extends BaseActivity {

    /*** XML View Component ***/
    private static final String TAG = AuthActivity.class.getSimpleName();

    /*** Activity Data ***/

    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private SignInButton signInButton;

    public AuthActivity() {
        super(R.layout.activity_auth);
    }

    @Override
    public void bindView() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("985205803004-qrt67dtq0ad9spdci4ql8ulakv2nlap2.apps.googleusercontent.com")
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        signInButton = findViewById(R.id.sign_in_button);
    }

    @Override
    public void bindData() {
        if (!isPermissionGranted(this, this,
                PermissionUtils.allPermissions)) {
            if (getIntent() == null) finish();
        }

        if (PreferenceUtils.isLogin(AuthActivity.this)) {
            startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
        }

    }

    @Override
    public void bindListener() {
        signInButton.setOnClickListener(signInClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantUtils.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "berhasil");
            Log.d(TAG, "berhasil");
            Log.d(TAG, "berhasil");
            Log.d(TAG, account.getIdToken());
            serverLogin(account.getIdToken());
        } catch (ApiException e) {
            serverLogin(null);
        }
    }

    private void serverLogin (String tokenGoogle) {
        Log.d(TAG, "PAnggil");
        Log.d(TAG, "PAnggil");
        Log.d(TAG, "PAnggil");
        Log.d(TAG, "PAnggil");
        if (tokenGoogle != null) {
            ApiService.getApi().login(tokenGoogle)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                User user = response.body().getData();
                                Log.d(TAG, "wOLDAN");
                                Log.d(TAG, "wOLDAN");
                                Log.d(TAG, "wOLDAN");
                                Log.d(TAG, "wOLDAN");
                                Log.d(TAG, user.getEmail());
                                Context context = AuthActivity.this;
                                PreferenceUtils.setEmail(context, user.getEmail());
                                PreferenceUtils.setLogin(context,true);
                                PreferenceUtils.setName(context, user.getName());
                                PreferenceUtils.setToken(context, user.getToken());
                                PreferenceUtils.setUserId(context, user.getId());

                                startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d(TAG, "GAGAL");
                            Log.d(TAG, "GAGAL");
                            Log.d(TAG, "GAGAL");
                            Log.d(TAG, "GAGAL");
                            Toast.makeText(AuthActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
//        startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
    }

    private View.OnClickListener signInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, ConstantUtils.RC_SIGN_IN);
        }
    };
}
