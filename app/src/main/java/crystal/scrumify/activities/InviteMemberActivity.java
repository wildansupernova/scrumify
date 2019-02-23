package crystal.scrumify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import crystal.scrumify.R;

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

    public void searchUser(View view) {
        
    }

    public void inviteUser(View view) {
    }

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchUser(view);
        }
    };

    private View.OnClickListener inviteButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            inviteUser(view);
        }
    };
}
