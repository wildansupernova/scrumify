package crystal.scrumify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.TaskAdapter;
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.responses.TaskResponse;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KanbanColumn extends Fragment {

    /*** XML View Component ***/
    private View rootView;
    private RecyclerView recyclerView;
    private TextView emptyView;

    /*** Fragment Data ***/
    private int fragmentArg;
    private int groupId;
    private List<TaskResponse> tasks;
    private TaskAdapter adapter;

    public KanbanColumn() {
        tasks = new ArrayList<>();
        adapter = new TaskAdapter(tasks);
    }

    public static KanbanColumn newInstance(int fragmentArg, int groupId) {
        KanbanColumn fragment = new KanbanColumn();
        fragment.setFragmentArg(fragmentArg);
        fragment.setGroupId(groupId);
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

    private String getStatusKanban() {
        switch (fragmentArg) {
            case 0: return "PRODUCT_BACKLOG";
            case 1: return "OPEN";
            case 2: return "WIP";
            case 3: return "DONE";
        }
        return "";
    }

    private void bindData() {
        ApiService.getApi().getTasks(groupId, getStatusKanban()).enqueue(
                new Callback<ApiResponse<List<TaskResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<TaskResponse>>> call, Response<ApiResponse<List<TaskResponse>>> response) {
                        if (response.isSuccessful()) {
                            tasks = response.body().getData();
                            adapter.setItems(tasks);
                            setupView();
                        } else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                            setupView();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<TaskResponse>>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        setupView();
                    }
                }
        );
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
