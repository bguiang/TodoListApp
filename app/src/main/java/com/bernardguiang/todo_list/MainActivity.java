package com.bernardguiang.todo_list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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
    TextView inProgressTV, finishedTV;

    int displayStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        tasksRV = (RecyclerView) findViewById(R.id.tasksRV);

        db = AppDatabase.getDatabase(context);
        inProgressTV = toolbar.findViewById(R.id.inProgressTV);
        finishedTV = toolbar.findViewById(R.id.finishedTV);

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

        if(status == Task.IN_PROGRESS)
        {
            inProgressTV.setTextColor(getResources().getColor(R.color.appOrange));
            finishedTV.setTextColor(getResources().getColor(R.color.white));
        }
        else
        {
            finishedTV.setTextColor(getResources().getColor(R.color.appGreen));
            inProgressTV.setTextColor(getResources().getColor(R.color.white));
        }

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


    public void taskClick(View view)
    {
        TextView titleTV = view.findViewById(R.id.title);
        TextView descriptionTV = view.findViewById(R.id.description);
        ImageButton deleteBtn = view.findViewById(R.id.deleteButton);
        Button finishBtn = view.findViewById(R.id.finishButton);

        if(finishBtn.getVisibility() == View.GONE && deleteBtn.getVisibility() == View.GONE)
        {
            titleTV.setSingleLine(false);
            descriptionTV.setSingleLine(false);

            //descriptionTV.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            //Only Show Finish Button on Tasks that are In-Progress
            if(displayStatus == Task.IN_PROGRESS)
                finishBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            titleTV.setSingleLine(true);
            descriptionTV.setSingleLine(true);

            ViewGroup.LayoutParams params = titleTV.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            titleTV.setLayoutParams(params);

            //descriptionTV.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            finishBtn.setVisibility(View.GONE);
        }
    }

    public void finishClick(View view)
    {
        int position = (int) view.getTag();

        final Task taskToFinish = tasksAdapter.getItemAtPosition(position);
        taskToFinish.setStatus(Task.FINISHED);

        Thread updateTask = new Thread(new Runnable() {
            @Override
            public void run() {
                db.taskDao().updateTask(taskToFinish);
            }
        });
        updateTask.start();
        try {
            updateTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tasksAdapter.removeItemAtPosition(position);
    }

    public void deleteClick(View view)
    {
        int position = (int) view.getTag();
        final Task taskToDelete = tasksAdapter.getItemAtPosition(position);

        Thread deleteTask = new Thread(new Runnable() {
            @Override
            public void run() {
                db.taskDao().deleteTask(taskToDelete);
            }
        });
        deleteTask.start();
        try {
            deleteTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tasksAdapter.removeItemAtPosition(position);
    }

    public void inProgressTasksClick(View view)
    {
        selectTasks(Task.IN_PROGRESS);
    }

    public void finishedTasksClick(View view)
    {
        selectTasks(Task.FINISHED);
    }

}
