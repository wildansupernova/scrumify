package crystal.scrumify.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    private int groupId = 0;

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
        inviteButton.setVisibility(View.GONE);
    }

    @Override
    public void bindData() {
        groupId = getIntent().getIntExtra("groupId", 0);
        Log.d(InviteMemberActivity.class.getSimpleName(), String.valueOf(groupId));
        resultName.setText("Find your partner with email");
        resultEmail.setText("");
    }

    @Override
    public void bindListener() {
        searchButton.setOnClickListener(searchButtonListener);
        inviteButton.setOnClickListener(inviteButtonListener);
    }

    @Override
    public void unbindListener() {
        searchButton.setOnClickListener(null);
        inviteButton.setOnClickListener(null);
    }

    public void searchUser(final String userEmail) {
        ApiService.getApi().getUser(userEmail).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    User user = response.body().getData();

                    resultName.setText(user.getName());
                    resultEmail.setText(user.getEmail());
                } else {
                    resultName.setText("Email Not Found");
                    resultEmail.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.d(InviteMemberActivity.class.getSimpleName(), t.getMessage());
            }
        });

    }

    public void inviteUser(String userEmail) {
        ApiService.getApi().createGroupMember(groupId, userEmail).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(inviteButton.getContext(), "Success to Invite Your Partner! :)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.d(InviteMemberActivity.class.getSimpleName(), t.getMessage());
            }
        });
    }

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchUser(emailInput.getText().toString().trim());
            inviteButton.setVisibility(View.VISIBLE);
        }
    };

    private View.OnClickListener inviteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            inviteUser(emailInput.getText().toString().trim());
        }
    };
}
