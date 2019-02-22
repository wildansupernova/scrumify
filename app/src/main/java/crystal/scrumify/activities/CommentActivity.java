package crystal.scrumify.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.CommentAdapter;
import crystal.scrumify.models.Comment;

public class CommentActivity extends BaseActivity {

    /*** XML View Component ***/
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Activity Data ***/
    private CommentAdapter adapter;
    private List<Comment> comments;

    public CommentActivity() {
        super(R.layout.activity_comment);
        comments = new ArrayList<>();
        adapter = new CommentAdapter(comments);
    }

    @Override
    public void bindView() {
        recyclerView = findViewById(R.id.comment_recycler);
        emptyView = findViewById(R.id.comment_empty_view);

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
        comments = new ArrayList<>();
        for (int i=0; i<10; i++) {
            comments.add(new Comment("Kirito Sama", getString(R.string.supporting)));
        }
        adapter.setItems(comments);
        setupView();
    }

}
