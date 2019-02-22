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
import com.google.android.gms.common.SignInButton;
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

import crystal.scrumify.utils.PreferenceUtils;

import static crystal.scrumify.utils.PermissionUtils.isPermissionGranted;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

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
    private SignInButton mSignInButton;
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
                .requestIdToken("985205803004-qrt67dtq0ad9spdci4ql8ulakv2nlap2.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mSignInButton = findViewById(R.id.sign_in_button);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                login();
                break;
            case R.id.auth_login:
                startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
                Log.d(TAG,"MASUK");
                break;
            // ...
        }
    }

    public void login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG,"MASUK");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"MASUKResult");
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
            Log.d(TAG,account.getEmail());
            Log.d(TAG,account.getIdToken());



            serverLogin(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more info

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
                            Log.d(TAG,"berhasil");
                            Log.d(TAG,"berhasil");
                            Log.d(TAG,"berhasil");
                            try {
                                Log.d(TAG, response.raw().body().string());
                            } catch (Exception e) {
                                Log.d(TAG, e.toString());
                            }
                            Log.d(TAG,"berhasil");
                            Log.d(TAG,"berhasil");
                            Log.d(TAG,"berhasil");



                            if (response.isSuccessful()) {
                                User user = response.body().getData();
                                String email = user.getEmail();
                                String nameUser = user.getName();
                                String token = user.getToken();
                                int userId = user.getId();
                                PreferenceUtils.setEmail(AuthActivity.this,email);
                                PreferenceUtils.setLogin(AuthActivity.this,true);
                                PreferenceUtils.setName(AuthActivity.this,nameUser);
                                PreferenceUtils.setToken(AuthActivity.this,token);
                                PreferenceUtils.setUserId(AuthActivity.this,userId);
                                startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.d(TAG,"gagal");
                            Log.d(TAG,"gagal");
                            Log.d(TAG,"gagal");
                            Log.d(TAG,"gagal");
                            Log.d(TAG,"gagal");
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
