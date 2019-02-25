package crystal.scrumify.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.CommentAdapter;
import crystal.scrumify.models.Comment;
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.ConstantUtils;
import crystal.scrumify.utils.PreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends BaseActivity {

    /*** XML View Component ***/
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Activity Data ***/
    private CommentAdapter adapter;
    private List<Comment> comments;
    private int taskId;


    private Button mCommentButton;
    private EditText mCommentText;

    public CommentActivity() {
        super(R.layout.activity_comment);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(comments);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        taskId = intent.getIntExtra(ConstantUtils.TASK_ID_KEY, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void bindView() {
        recyclerView = findViewById(R.id.comment_recycler);
        emptyView = findViewById(R.id.comment_empty_view);
        mCommentButton = findViewById(R.id.comment_post);
        mCommentText = findViewById(R.id.comment_input);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setupView() {
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindData() {
        this.getComment();
    }

    @Override
    public void bindListener() {
        super.bindListener();
        mCommentButton.setOnClickListener(createPostListener);
    }

    public void getComment() {
        ApiService.getApi().getComments(taskId).enqueue(new Callback<ApiResponse<List<Comment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Comment>>> call, Response<ApiResponse<List<Comment>>> response) {
                if (response.isSuccessful()) {
                    adapter.setItems(response.body().getData());
                    setupView();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Comment>>> call, Throwable t) {

            }
        });
    }

    private View.OnClickListener createPostListener = v -> {
        ApiService.getApi().createComment(PreferenceUtils.getUserId(this),taskId,mCommentText.getText().toString().trim()).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    getComment();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                getComment();
            }
        });
    };

}
