package crystal.scrumify.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.activities.CommentActivity;
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
        private Button comment;
        private Button move;

        private View.OnClickListener commentClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                context.startActivity(new Intent(context, CommentActivity.class));
            }
        };

        private View.OnClickListener moveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Move", Toast.LENGTH_SHORT).show();
            }
        };

        public TaskHolder(View itemView) {
            super(itemView);
            bindView();
            bindListener();
        }

        private void bindView() {
            title = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            assignee = itemView.findViewById(R.id.task_assignee);
            comment = itemView.findViewById(R.id.task_show_comment);
            move = itemView.findViewById(R.id.task_move);
        }

        private void bindListener() {
            comment.setOnClickListener(commentClickListener);
            move.setOnClickListener(moveClickListener);
        }

        public void bind(Task item) {
            if (!item.moveable()) {
                move.setVisibility(View.GONE);
            }

            title.setText(item.getTitle());
            description.setText(item.getDescription());
            assignee.setText("Assigned to: " + item.getAssignee());
        }


    }
}