package crystal.scrumify.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import crystal.scrumify.R;
import crystal.scrumify.adapters.KanbanAdapter;
import crystal.scrumify.fragments.KanbanColumn;
import crystal.scrumify.models.Group;
import crystal.scrumify.responses.ApiResponse;
import crystal.scrumify.responses.GroupListResponse;
import crystal.scrumify.services.ApiService;
import crystal.scrumify.utils.ConstantUtils;
import crystal.scrumify.utils.PreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Spinner groupSpinner;
    private Button addGroupButton;

    /*** Activity Data ***/
    List<GroupListResponse> groupListResponses;
    int currentPosition = 1;

    public KanbanActivity() {
        super(R.layout.activity_kanban);
        groupListResponses = new ArrayList<>();
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
        groupSpinner = findViewById(R.id.kanban_group_spinner);
        addGroupButton = findViewById(R.id.button2);
        toggleButton = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
    }

    @Override
    public void setupView() {
        setSupportActionBar(toolbar);
        toggleButton.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void bindData() {
        /*** Setup Group Spinner ***/
        updateListGroupSpinner();

    }

    private void updateListGroupSpinner() {
        ApiService.getApi().getUserGroups(PreferenceUtils.getUserId(this)).enqueue(
                new Callback<ApiResponse<List<GroupListResponse>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<GroupListResponse>>> call, Response<ApiResponse<List<GroupListResponse>>> response) {
                        if (response.isSuccessful()) {
                            groupListResponses = response.body().getData();
                            List<String> groupNames = new ArrayList<>();

                            for (GroupListResponse group : groupListResponses) {
                                groupNames.add(group.getGroupName());
                            }

//                            groupNames.add("+ New Group");

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext()
                                    , R.layout.spinner_title_item, groupNames);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            groupSpinner.setAdapter(adapter);

                        } else {
                            Toast.makeText(KanbanActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<GroupListResponse>>> call, Throwable t) {
                        Log.d(KanbanActivity.class.getSimpleName(), t.getMessage());
                    }
                }
        );
    }
    private void setupKanbanColumn(int groupId) {
        List<KanbanColumn> kanbanColumns = new ArrayList<>();
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_BACKLOG_FRAG_ARG, groupId));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_TODO_FRAG_ARG, groupId));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_PROGRESS_FRAG_ARG, groupId));
        kanbanColumns.add(KanbanColumn.newInstance(ConstantUtils.KANBAN_DONE_FRAG_ARG, groupId));

        if (tabLayout.getTabCount() == 0) {
            for (KanbanColumn column : kanbanColumns) {
                tabLayout.addTab(tabLayout.newTab().setText(column.getColumnTitle()));
            }
        }

        KanbanAdapter adapter = new KanbanAdapter(getSupportFragmentManager(), kanbanColumns);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void bindListener() {
        actionButton.setOnClickListener(actionButtonListener);

        addGroupButton.setOnClickListener(addButtonListener);
        drawerLayout.addDrawerListener(toggleButton);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
        groupSpinner.setOnItemSelectedListener(spinnerSelectedListener);

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

        if (id == R.id.nav_invite) {
            Intent intent = new Intent(KanbanActivity.this, InviteMemberActivity.class);
            intent.putExtra("groupId", groupListResponses.get(currentPosition).getGroupId());
            startActivity(intent);
        } else if (id == R.id.nav_record) {
            startActivity(new Intent(KanbanActivity.this, RecordActivity.class));
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

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            View inflater = getLayoutInflater().inflate(R.layout.form_new_group, null);
            final EditText groupNameInput = inflater.findViewById(R.id.form_group_name);
            final EditText groupDescInput = inflater.findViewById(R.id.form_group_desc);

            AlertDialog.Builder builder = new AlertDialog.Builder(KanbanActivity.this);
            builder.setTitle("Create New Group")
                    .setView(inflater)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String groupName = groupNameInput.getText().toString().trim();
                            String groupDesc = groupDescInput.getText().toString().trim();
                            createNewGroup(groupName, groupDesc);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }
    };

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) { }
    };

    private Spinner.OnItemSelectedListener spinnerSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


//            if (position == groupListResponses.size()) {
//                View inflater = getLayoutInflater().inflate(R.layout.form_new_group, null);
//                final EditText groupNameInput = inflater.findViewById(R.id.form_group_name);
//                final EditText groupDescInput = inflater.findViewById(R.id.form_group_desc);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(KanbanActivity.this);
//                builder.setTitle("Create New Group")
//                        .setView(inflater)
//                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String groupName = groupNameInput.getText().toString().trim();
//                                String groupDesc = groupDescInput.getText().toString().trim();
//                                createNewGroup(groupName, groupDesc);
//                            }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create()
//                        .show();
//            } else {
                setupKanbanColumn(groupListResponses.get(position).getGroupId());
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            setupKanbanColumn(groupListResponses.get(0).getGroupId());
        }
    };

    private void createNewGroup(String groupName, String groupDesc) {
        ApiService.getApi().createGroup(groupName, groupDesc, PreferenceUtils.getUserId(this))
                .enqueue(new Callback<ApiResponse<Group>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Group>> call, Response<ApiResponse<Group>> response) {
                        updateListGroupSpinner();
                        if (response.isSuccessful()) {
//                            Intent intent = new Intent(KanbanActivity.this, InviteMemberActivity.class);
//                            intent.putExtra("groupId", response.body().getData().getGroupId());
//                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Group>> call, Throwable t) {
                        Log.d(KanbanActivity.class.getSimpleName(), t.getMessage());
                    }
                });
    }
}
