package crystal.scrumify.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.EventAdapter;
import crystal.scrumify.models.Event;

public class EventActivity extends BaseActivity {

    /*** XML View Component ***/
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Activity Data ***/
    private EventAdapter adapter;
    private List<Event> events;

    public EventActivity() {
        super(R.layout.activity_event);

        events = new ArrayList<>();
        adapter = new EventAdapter(events);
    }

    @Override
    public void bindView() {
        recyclerView = findViewById(R.id.event_recycler);
        emptyView = findViewById(R.id.event_empty);

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
        events = new ArrayList<>();
        for (int i=0; i<10; i++) {
            events.add(new Event("Sprint Planning", "Time: Tuesday, March 3 2019 (19:30)"));
        }
        adapter.setItems(events);
        setupView();
    }
}
