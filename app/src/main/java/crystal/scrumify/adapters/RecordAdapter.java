package crystal.scrumify.adapters;

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
import crystal.scrumify.models.Record;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {

    private List<Record> items = new ArrayList<>();
    private int itemLayout = R.layout.item_record;

    public RecordAdapter(List<Record> items) {
        this.items = items;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<Record> items) {
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

    public class RecordHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView recorder;
        Button info;
        Button stop;
        Button start;
        Button resume;

        private View.OnClickListener infoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Info", Toast.LENGTH_SHORT).show();
            }
        };

        private View.OnClickListener stopClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Stop", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                resume.setVisibility(View.GONE);
            }
        };

        private View.OnClickListener startClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Start", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                resume.setVisibility(View.VISIBLE);
            }
        };

        private View.OnClickListener resumeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Resume", Toast.LENGTH_SHORT).show();
                stop.setVisibility(View.VISIBLE);
                start.setVisibility(View.VISIBLE);
                resume.setVisibility(View.GONE);
            }
        };

        public RecordHolder(View itemView) {
            super(itemView);
            bindView();
            bindListener();
        }


        private void bindView() {
            title = itemView.findViewById(R.id.record_title);
            recorder = itemView.findViewById(R.id.record_recorder);
            info = itemView.findViewById(R.id.record_info);
            stop = itemView.findViewById(R.id.record_stop);
            start = itemView.findViewById(R.id.record_start);
            resume = itemView.findViewById(R.id.record_resume);
        }

        private void bindListener() {
            info.setOnClickListener(infoClickListener);
            stop.setOnClickListener(stopClickListener);
            start.setOnClickListener(startClickListener);
            resume.setOnClickListener(resumeClickListener);
        }

        public void bind(Record item) {

        }
    }
}
