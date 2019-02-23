package crystal.scrumify.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.RecordAdapter;
import crystal.scrumify.models.Record;

public class RecordActivity extends BaseActivity {

    /*** XML View Component ***/
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Activity Data ***/
    private RecordAdapter adapter;
    private List<Record> records;

    public RecordActivity() {
        super(R.layout.activity_record);
        records = new ArrayList<>();
        adapter = new RecordAdapter((records));
    }

    @Override
    public void bindView() {
        recyclerView = findViewById(R.id.record_recycler);
        emptyView = findViewById(R.id.record_empty_view);

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
        records = new ArrayList<>();
        for (int i=0; i<10; i++) {
            records.add(new Record("Sprint Meeting " + i, "Recorded by: Muhammad Farhan"));
        }
        adapter.setItems(records);
        setupView();
    }
}
