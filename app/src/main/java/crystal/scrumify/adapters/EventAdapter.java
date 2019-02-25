package crystal.scrumify.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.models.Event;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private List<Event> items;
    private int itemLayout = R.layout.item_event;

    public EventAdapter(List<Event> items) {
        this.items = items;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<Event> items) {
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

    class EventHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView time;

        EventHolder(View itemView) {
            super(itemView);

            bindView();
        }

        private void bindView() {
            name = itemView.findViewById(R.id.event_name);
            time = itemView.findViewById(R.id.event_time);
        }

        void bind(Event item) {
            name.setText(item.getEventName());
            time.setText(item.getEventTime());
        }
    }
}
