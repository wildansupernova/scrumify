package crystal.scrumify.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.TaskAdapter;
import crystal.scrumify.models.Task;
import crystal.scrumify.utils.ConstantUtils;

public class KanbanColumn extends Fragment {

    /*** XML View Component ***/
    private View rootView;
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Fragment Data ***/
    private int fragmentArg;
    private List<Task> tasks;
    private TaskAdapter adapter;

    public KanbanColumn() {
        tasks = new ArrayList<>();
        adapter = new TaskAdapter(tasks);
    }

    public static KanbanColumn newInstance(int fragmentArg) {
        KanbanColumn fragment = new KanbanColumn();
        fragment.setFragmentArg(fragmentArg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_kanban_column, container, false);

        bindView();
        setupView();
        bindData();
        bindListener();

        return rootView;
    }

    private void bindView() {
        recyclerView = rootView.findViewById(R.id.kanban_column_recycler);
        emptyView = rootView.findViewById(R.id.kanban_column_empty);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupView() {
        if (tasks.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void bindData() {
        tasks = new ArrayList<>();
        for (int i=0; i<10; i++) {
            tasks.add(new Task(
                    "Blockchain implementation",
                    "No description for this task",
                    "Dion Saputra",
                    i%4)
            );
        }
        adapter.setItems(tasks);
        setupView();
    }

    private void bindListener() {

    }

    public String getColumnTitle() {
        switch (fragmentArg) {
            case ConstantUtils.KANBAN_BACKLOG_FRAG_ARG  : return "Backlog";
            case ConstantUtils.KANBAN_TODO_FRAG_ARG     : return "Todo";
            case ConstantUtils.KANBAN_PROGRESS_FRAG_ARG : return "Progress";
            case ConstantUtils.KANBAN_DONE_FRAG_ARG     : return "Done";
        }
        return "";
    }

    public int getFragmentArg() {
        return fragmentArg;
    }

    public void setFragmentArg(int fragmentArg) {
        this.fragmentArg = fragmentArg;
    }
}
