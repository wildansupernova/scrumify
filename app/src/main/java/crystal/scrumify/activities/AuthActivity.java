package crystal.scrumify.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import crystal.scrumify.R;
import crystal.scrumify.models.User;

public class AuthActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


    }

    public void login(View view) {

    }

    public void register(View view) {
    }

    public void switchAction(View view) {
    }
}
