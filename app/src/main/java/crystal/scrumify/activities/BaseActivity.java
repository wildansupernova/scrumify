package crystal.scrumify.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private int layout;

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

    public abstract void bindView();

    public abstract void setupView();

    public abstract void bindData();

    public abstract void bindListener();

    public abstract void unbindListener();

}
