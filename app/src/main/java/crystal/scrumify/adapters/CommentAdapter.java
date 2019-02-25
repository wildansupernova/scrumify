package crystal.scrumify.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.models.Comment;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> items;
    private int itemLayout = R.layout.item_comment;

    public CommentAdapter(List<Comment> items) {
        this.items = items;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<Comment> items) {
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

    class CommentHolder extends RecyclerView.ViewHolder {

        private TextView commentTextView;

        CommentHolder(View itemView) {
            super(itemView);
            bindView();
        }

        private void bindView() {
            commentTextView = itemView.findViewById(R.id.comment);
        }

        void bind(Comment item) {
            String comment = "<b>" + item.getCommentator() + "</b> " + item.getContent();
            commentTextView.setText(Html.fromHtml(comment));
        }
    }
}