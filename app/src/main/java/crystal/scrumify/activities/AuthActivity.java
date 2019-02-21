package crystal.scrumify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import crystal.scrumify.R;
import crystal.scrumify.models.ApiResponse;
import crystal.scrumify.models.LoginResponse;
import crystal.scrumify.models.User;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.PermissionUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static crystal.scrumify.utils.PermissionUtils.isPermissionGranted;

public class AuthActivity extends BaseActivity {

    /*** XML View Component ***/
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmInput;
    private Button loginButton;
    private Button registerButton;
    private TextView switchText;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "AuthActivity";
    /*** Activity Data ***/
    private String name;
    private String email;
    private String password;
    private String confirm;
    private boolean isLoginState = true;

    private GoogleSignInClient mGoogleSignInClient;
    public AuthActivity() {
        super(R.layout.activity_auth);
    }

    @Override
    public void bindView() {
        nameInput = findViewById(R.id.auth_name);
        emailInput = findViewById(R.id.auth_email);
        passwordInput = findViewById(R.id.auth_password);
        confirmInput = findViewById(R.id.auth_confirm);
        registerButton = findViewById(R.id.auth_register);
        loginButton = findViewById(R.id.auth_login);
        switchText = findViewById(R.id.auth_switch);
    }

    @Override
    public void setupView() {
        if (isLoginState) {
            nameInput.setVisibility(View.GONE);
            confirmInput.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            switchText.setText(getString(R.string.register));
        } else {
            nameInput.setVisibility(View.VISIBLE);
            confirmInput.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            switchText.setText(getString(R.string.login));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void bindData() {
        name = nameInput.getText().toString().trim();
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirm = confirmInput.getText().toString().trim();


        if (!isPermissionGranted(this, this,
                PermissionUtils.allPermissions)) {
            if (getIntent() == null) finish();
        }
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void unbindListener() {

    }

    public void login(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            serverLogin(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            serverLogin(null);
        }
    }


    private void serverLogin (String tokenGoogle) {
        if (tokenGoogle != null) {
            ApiService.getApi().login(tokenGoogle)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                User user = response.body().getData();
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AuthActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", user.getToken());
                                editor.putBoolean("isLogin", true);
                                editor.apply();
                                startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(AuthActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    public void register(View view) {
        startActivity(new Intent(this, KanbanActivity.class));
    }

    public void switchAction(View view) {
        isLoginState = !isLoginState;
        setupView();
    }
}
