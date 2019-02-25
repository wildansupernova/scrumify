package crystal.scrumify.activities;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
    int currentPosition = 0;

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

    public void setupKanbanColumnCurrent() {
        int groupId = groupListResponses.get(currentPosition).getGroupId();
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
            case R.id.action_logout:
                PreferenceUtils.setLogin(KanbanActivity.this, false);
                startActivity(new Intent(KanbanActivity.this, AuthActivity.class));
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
            startActivity(new Intent(KanbanActivity.this, LocationActivity.class));
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(KanbanActivity.this, EventActivity.class);
            intent.putExtra("groupId", groupListResponses.get(currentPosition).getGroupId());
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*** Activity Listener ***/
    private View.OnClickListener actionButtonListener = v -> {
        View inflater = getLayoutInflater().inflate(R.layout.form_new_task, null);
        final EditText taskNameInput = inflater.findViewById(R.id.form_task_name);
        final EditText taskDescInput = inflater.findViewById(R.id.form_task_desc);
        final EditText taskWorkHourInput = inflater.findViewById(R.id.form_task_work_hour);
        final Spinner spinner = (Spinner) inflater.findViewById(R.id.task_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(KanbanActivity.this, R.array.kanban_status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(KanbanActivity.this);
        builder.setTitle("Create New Task")
                .setView(inflater)
                .setPositiveButton("Create", (dialog, which) -> {
                    String taskName = taskNameInput.getText().toString().trim();
                    String taskDesc = taskDescInput.getText().toString().trim();
                    int taskWorkHour = Integer.parseInt(taskWorkHourInput.getText().toString().trim());
                    String kanbanStatus = spinner.getSelectedItem().toString().trim();

                    createNewTask(taskName, taskDesc, kanbanStatus, taskWorkHour);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    };

    private View.OnClickListener addButtonListener = v -> {
        View inflater = getLayoutInflater().inflate(R.layout.form_new_group, null);
        final EditText groupNameInput = inflater.findViewById(R.id.form_group_name);
        final EditText groupDescInput = inflater.findViewById(R.id.form_group_desc);
        final EditText totalSprintInput = inflater.findViewById(R.id.form_group_sprint);
        final EditText sprintTimeInput = inflater.findViewById(R.id.form_group_time);

        View.OnClickListener sprintTimeListener = view -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog;
            timePickerDialog = new TimePickerDialog(KanbanActivity.this, (timePicker, selectedHour, selectedMinute) ->
                    sprintTimeInput.setText(selectedHour + ":" + selectedMinute), hour, minute, false);
            timePickerDialog.setTitle("Select Time for Daily Meeting");
            timePickerDialog.show();
        };

        sprintTimeInput.setOnClickListener(sprintTimeListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(KanbanActivity.this);
        builder.setTitle("Create New Group")
                .setView(inflater)
                .setPositiveButton("Create", (dialog, which) -> {
                    String groupName = groupNameInput.getText().toString().trim();
                    String groupDesc = groupDescInput.getText().toString().trim();
                    String totalSprint = totalSprintInput.getText().toString().trim();
                    String sprintTime = sprintTimeInput.getText().toString();

                    createNewGroup(groupName, groupDesc, totalSprint, sprintTime);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
            setupKanbanColumn(groupListResponses.get(position).getGroupId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            setupKanbanColumn(groupListResponses.get(0).getGroupId());
        }
    };

    private void createNewGroup(String groupName, String groupDesc, String totalSprint, String sprintTime) {
        ApiService.getApi().createGroup(groupName, groupDesc, PreferenceUtils.getUserId(this))
                .enqueue(new Callback<ApiResponse<Group>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Group>> call, Response<ApiResponse<Group>> response) {
                        updateListGroupSpinner();
                        if (response.isSuccessful()) {
                            createNewEvent(response.body().getData().getGroupId(), totalSprint, sprintTime);
                            Intent intent = new Intent(KanbanActivity.this, InviteMemberActivity.class);
                            intent.putExtra("groupId", response.body().getData().getGroupId());
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Group>> call, Throwable t) {
                        Log.d(KanbanActivity.class.getSimpleName(), t.getMessage());
                    }
                });
    }

    private void createNewEvent(int groupId, String totalSprint, String sprintTime) {

    }

    private void createNewTask (String taskName, String taskDesc, String status_kanban, int taskWorkHour) {
        ApiService.getApi().createTask(groupListResponses.get(currentPosition).getGroupId(),taskName, taskDesc, status_kanban,taskWorkHour)
                .enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {

                        if (response.isSuccessful()) {
                            setupKanbanColumn(groupListResponses.get(currentPosition).getGroupId());
                        }

                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        Log.d(KanbanActivity.class.getSimpleName(), t.getMessage());
                    }
                });
    }
}
