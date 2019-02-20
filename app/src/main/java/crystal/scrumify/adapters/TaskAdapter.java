package crystal.scrumify.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.models.Task;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    private List<Task> items = new ArrayList<>();
    private int itemLayout = R.layout.item_task;

    public TaskAdapter(List<Task> items) {
        this.items = items;
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<Task> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView assignee;

        public TaskHolder(View itemView) {
            super(itemView);
            bindView();
        }

        private void bindView() {
            title = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            assignee = itemView.findViewById(R.id.task_assignee);
        }

        public void bind(Task item) {
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            assignee.setText("Assigned to: " + item.getAssignee());
        }
    }
}