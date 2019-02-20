package crystal.scrumify.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import crystal.scrumify.R;
import crystal.scrumify.utils.ConstantUtils;

public class KanbanColumn extends Fragment {

    private int fragmentArg;
    private View rootView;

    public KanbanColumn() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_kanban_column, container, false);
        TextView textView = rootView.findViewById(R.id.fragment_title);
        textView.setText(getColumnTitle());
        return rootView;
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
