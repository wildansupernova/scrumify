package crystal.scrumify.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import crystal.scrumify.fragments.KanbanColumn;

public class KanbanAdapter extends FragmentStatePagerAdapter {

    private List<KanbanColumn> kanbanColumns;

    public KanbanAdapter(FragmentManager fm, List<KanbanColumn> fragments) {
        super(fm);
        this.kanbanColumns = fragments;
    }


    @Override
    public Fragment getItem(int i) {
        return kanbanColumns.get(i);
    }

    @Override
    public int getCount() {
        return kanbanColumns.size();
    }
}
