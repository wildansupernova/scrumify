package crystal.scrumify.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.KanbanAdapter;
import crystal.scrumify.fragments.KanbanColumn;
import crystal.scrumify.utils.ConstantUtils;

public class KanbanActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*** XML View Component ***/
    private Toolbar toolbar;
    private FloatingActionButton actionButton;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public KanbanActivity() {
        super(R.layout.activity_kanban);
    }

    @Override
    public void bindView() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionButton = findViewById(R.id.kanban_fab);
        tabLayout = findViewById(R.id.kanban_tab_layout);
        viewPager = findViewById(R.id.kanban_view_pager);
        tabLayout = findViewById(R.id.kanban_tab_layout);

        toggleButton = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
    }

    @Override
    public void setupView() {
        setSupportActionBar(toolbar);
        toggleButton.syncState();
    }

    @Override
    public void bindData() {
        List<KanbanColumn> kanbanColumns = new ArrayList<>();
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_BACKLOG_FRAG_ARG));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_TODO_FRAG_ARG));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_PROGRESS_FRAG_ARG));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_DONE_FRAG_ARG));

        for (KanbanColumn column : kanbanColumns) {
            System.out.println(String.valueOf(column.getFragmentArg()) + "::" + column.getColumnTitle());
            tabLayout.addTab(tabLayout.newTab().setText(column.getColumnTitle()));
        }

        KanbanAdapter adapter = new KanbanAdapter(getSupportFragmentManager(), kanbanColumns);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void bindListener() {
        actionButton.setOnClickListener(actionButtonListener);
        drawerLayout.addDrawerListener(toggleButton);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    @Override
    public void unbindListener() {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*** Activity Listener ***/
    private View.OnClickListener actionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
