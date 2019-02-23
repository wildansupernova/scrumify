package crystal.scrumify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import crystal.scrumify.R;
import crystal.scrumify.models.User;
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.services.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteMemberActivity extends BaseActivity {

    private EditText emailInput;
    private ImageButton searchButton;
    private TextView resultName;
    private TextView resultEmail;
    private Button inviteButton;

    public InviteMemberActivity() {
        super(R.layout.activity_invite_member);
    }

    @Override
    public void bindView() {
        emailInput = findViewById(R.id.invite_email_input);
        searchButton = findViewById(R.id.invite_search_button);
        resultName = findViewById(R.id.invite_member_name);
        resultEmail = findViewById(R.id.invite_member_email);
        inviteButton = findViewById(R.id.invite_member_button);
    }

    @Override
    public void setupView() {

    }

    @Override
    public void bindData() {

    }

    @Override
    public void bindListener() {
        searchButton.setOnClickListener(searchButtonListener);
        inviteButton.setOnClickListener(inviteButtonListener);
    }

    @Override
    public void unbindListener() {
    }

    public void searchUser(String userEmail) {

        ApiService.getApi().getUser(userEmail).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    User user = response.body().getData();
                    Log.d(InviteMemberActivity.class.getSimpleName(), "CLICKED");

                    resultName.setText(user.getName());
                    resultEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                resultName.setText("Email Not Found");
                resultEmail.setText("");
                Log.d(InviteMemberActivity.class.getSimpleName(), t.getMessage());
            }
        });

    }

    public void inviteUser(String userEmail) {

    }

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchUser(emailInput.getText().toString());
        }
    };

    private View.OnClickListener inviteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            inviteUser(emailInput.getText().toString());
        }
    };
}
