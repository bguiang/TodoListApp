package com.bernardguiang.todo_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bernardguiang.todo_list.Database.AppDatabase;
import com.bernardguiang.todo_list.Model.Task;
import com.bernardguiang.todo_list.RecyclerViewAdapters.TasksAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    Context context;
    AppDatabase db;
    private RecyclerView tasksRV;
    private TasksAdapter tasksAdapter;
    List<Task> tasks;
    Toolbar toolbar; //Custom support v7 library toolbar
    TabLayout toolbarTabLayout;
    TabLayout.Tab inProgressTab, finishedTab;

    int displayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTabLayout = toolbar.findViewById(R.id.tabLayout);
        toolbarTabLayout.setVisibility(View.VISIBLE);
        inProgressTab = toolbarTabLayout.newTab();
        inProgressTab.setText("In Progress");
        finishedTab = toolbarTabLayout.newTab();
        finishedTab.setText("Finished");
        toolbarTabLayout.addTab(inProgressTab);
        toolbarTabLayout.addTab(finishedTab);
        toolbarTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().equalsIgnoreCase("In Progress")) {
                    selectTasks(Task.IN_PROGRESS);
                    toolbarTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.appOrange));
                }
                else {
                    selectTasks(Task.FINISHED);
                    toolbarTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.appGreen));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tasksRV = (RecyclerView) findViewById(R.id.tasksRV);

        db = AppDatabase.getDatabase(context);

        displayStatus = Task.IN_PROGRESS;
        selectTasks(displayStatus);
    }

    public void loadTasksWithStatus(final int status)
    {
        Thread getTasks = new Thread(new Runnable() {
            @Override
            public void run() {
                tasks = db.taskDao().getAllTasksWithStatus(status);
            }
        });
        getTasks.start();

        try
        {
            getTasks.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        tasksAdapter = new TasksAdapter(context, tasks);

        //Set LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tasksRV.setLayoutManager(linearLayoutManager);

        //Set Adapter
        tasksRV.setAdapter(tasksAdapter);
    }

    public void selectTasks(int status)
    {
        displayStatus = status;
        loadTasksWithStatus(status);
    }


    public void insertTask(final Task newTask)
    {
        Thread insertTask = new Thread(new Runnable() {
            @Override
            public void run() {
                db.taskDao().insertTask(newTask);
            }
        });
        insertTask.start();
        try {
            insertTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //If you are on the In Progress "page" reload the recyclerview.
        if(displayStatus == Task.IN_PROGRESS)
        {
            loadTasksWithStatus(displayStatus);
        }
    }

    public void newTaskClick(View view)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(R.string.set_radius_dialog_fragment_title);
        final View v = inflater.inflate(R.layout.dialog_new_task, null);
        builder.setView(v);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText titleET = (EditText) v.findViewById(R.id.newTaskTitle);
                        EditText descriptionET = (EditText) v.findViewById(R.id.newTaskDescription);

                        if(titleET.getText().toString().isEmpty())
                        {
                            Toast.makeText(context, "Please fill in the title", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Task task = new Task();
                            task.setTitle(titleET.getText().toString());
                            task.setDescription(descriptionET.getText().toString());
                            task.setPriority(1);
                            task.setStatus(Task.IN_PROGRESS);

                            insertTask(task);
                        }
                    }
                });
        builder.setNegativeButton("Cancel", null);
        builder.setCancelable(false);
        builder.show();
    }

}
