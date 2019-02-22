package crystal.scrumify.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected int layout;

    public BaseActivity(int layout) {
        this.layout = layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        bindView();
        setupView();
        bindData();
        bindListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindListener();
    }

    public void bindView() {}

    public void setupView() {}

    public void bindData() {}

    public void bindListener() {}

    public void unbindListener() {}

}
