package crystal.scrumify.activities;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import crystal.scrumify.R;

public class AuthActivity extends BaseActivity {

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmInput;
    private Button loginButton;
    private Button registerButton;
    private TextView switchText;

    private String name;
    private String email;
    private String password;
    private String confirm;
    private boolean isLoginState = true;

    public AuthActivity(int layout) {
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

    }

    public void register(View view) {

    }

    public void switchAction(View view) {
        isLoginState = !isLoginState;
        setupView();
    }
}
