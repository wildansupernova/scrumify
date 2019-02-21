package crystal.scrumify.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import crystal.scrumify.R;
import crystal.scrumify.models.ApiResponse;
import crystal.scrumify.models.LoginResponse;
import crystal.scrumify.models.User;
import crystal.scrumify.services.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity {

    /*** XML View Component ***/
    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmInput;
    private Button loginButton;
    private Button registerButton;
    private TextView switchText;

    /*** Activity Data ***/
    private String name;
    private String email;
    private String password;
    private String confirm;
    private boolean isLoginState = true;

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
    public void bindData() {
        name = nameInput.getText().toString().trim();
        email = emailInput.getText().toString().trim();
        password = passwordInput.getText().toString().trim();
        confirm = confirmInput.getText().toString().trim();
    }

    @Override
    public void bindListener() {

    }

    @Override
    public void unbindListener() {

    }

    public void login(View view) {
        ApiService.getApi().login("eyJhbGciOiJSUzI1NiIsImtpZCI6IjdkNjgwZDhjNzBkNDRlOTQ3MTMzY2JkNDk5ZWJjMWE2MWMzZDVhYmMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiNTUwMDQzOTE0OTcyLTAxcWwwYnNiMjlsZnUxdnV0ZGtsajFyYWQzcDFlb2lmLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXVkIjoiNTUwMDQzOTE0OTcyLTAxcWwwYnNiMjlsZnUxdnV0ZGtsajFyYWQzcDFlb2lmLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTExOTkwNDk5ODI5NDQyNzMyNzM5IiwiZW1haWwiOiJhbG5hdGFyYXdAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJ2V2JTRFAxN25VZ3JoOEFiNXhldm5BIiwibmFtZSI6IldpbGRhbiBEaWNreSBBbG5hdGFyYSIsInBpY3R1cmUiOiJodHRwczovL2xoNS5nb29nbGV1c2VyY29udGVudC5jb20vLW5HU29FZ2s4QzFjL0FBQUFBQUFBQUFJL0FBQUFBQUFBQWtVL1RlV2RrTC1UYllVL3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJXaWxkYW4gRGlja3kiLCJmYW1pbHlfbmFtZSI6IkFsbmF0YXJhIiwibG9jYWxlIjoiZW4iLCJpYXQiOjE1NTA3Mjc4MjgsImV4cCI6MTU1MDczMTQyOCwianRpIjoiNjk3ZTQxY2I2NmZlY2MxOWQzYzQ2ZGYzNmU4ZTEwMmU1NmQ5NzY4OSJ9.bRH4ZCmfbips3TrccZshUVrtXyTumAfpPoPctrkUkOY27BkRa5V-mqx7cWW3Q9eZytV_7Pvnpjzy8tKuVMWSyk7VfeC4vwgHwfLl6agCF6in5n1Q-7JKu2j6hn3UCIGuVYAUREB2zwvhB6KouGqy6atTlCILNKdhv7qMsgMEmVg8yjMtX9TNw26JI4JYh8sQqw1cKGIJojO0gU0yuqWe3E124c8F1pGTb830EAEIht0JikxFteyZo3jTU5mmML6R4JkoTzLbmAgZZWTGMF5GMC0EXVckIK2qPCg3LMxfNM0oh_EzrjGBTmlL14j_Sg2n6QIZrt93-CXEvc6ly-0o2w")
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            User user = response.body().getUser();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AuthActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("token", response.body().getToken());
                            editor.apply();
                            System.out.println(user.getEmail());
                            startActivity(new Intent(AuthActivity.this, KanbanActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(AuthActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void register(View view) {
        startActivity(new Intent(this, KanbanActivity.class));
    }

    public void switchAction(View view) {
        isLoginState = !isLoginState;
        setupView();
    }
}
