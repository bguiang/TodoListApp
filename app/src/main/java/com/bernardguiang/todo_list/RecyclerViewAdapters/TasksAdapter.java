package com.bernardguiang.todo_list.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bernardguiang.todo_list.Database.AppDatabase;
import com.bernardguiang.todo_list.Helper.OnSwipeTouchListener;
import com.bernardguiang.todo_list.Model.Task;
import com.bernardguiang.todo_list.R;

import java.util.List;

/**
 * This adapter handles both In Progress and Finished Tasks
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>
{
    public static final int ORANGE = 0;
    public static final int GREEN = 1;

    Context context;
    List<Task> data;
    LayoutInflater layoutInflater;
    AppDatabase db;

    public TasksAdapter(Context context, List<Task> data)
    {
        this.context = context;
        this.data = data;

        layoutInflater = LayoutInflater.from(context);

        db = AppDatabase.getDatabase(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.cardview_task_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Task currentItem = data.get(position);
        holder.setTitleViewText(currentItem.getTitle());
        if(currentItem.getStatus() == Task.IN_PROGRESS)
        {
            holder.setColor(ORANGE);
        }
        else
        {
            holder.setColor(GREEN);
            holder.finishBtn.setVisibility(View.GONE);
        }
        holder.setDescriptionViewText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItemAtPosition(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View itemView;
        TextView titleTV, descriptionTV;
        ImageButton deleteBtn;
        Button finishBtn;

        public ViewHolder(final View itemView)
        {
            super(itemView);

            this.itemView = itemView;

            titleTV = itemView.findViewById(R.id.title);
            descriptionTV = itemView.findViewById(R.id.description);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
            finishBtn = itemView.findViewById(R.id.finishButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finishBtn.getVisibility() == View.GONE && deleteBtn.getVisibility() == View.GONE)
                    {
                        titleTV.setSingleLine(false);
                        descriptionTV.setSingleLine(false);

                        descriptionTV.setVisibility(View.VISIBLE);
                        deleteBtn.setVisibility(View.VISIBLE);

                        //Only Show Finish Button on Tasks that are In-Progress
                        if(data.get(getAdapterPosition()).getStatus() ==  Task.IN_PROGRESS)
                            finishBtn.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        titleTV.setSingleLine(true);
                        descriptionTV.setSingleLine(true);
                        titleTV.setMaxLines(1);
                        descriptionTV.setMaxLines(1);

                        descriptionTV.setVisibility(View.GONE);
                        deleteBtn.setVisibility(View.GONE);
                        finishBtn.setVisibility(View.GONE);
                    }
                }
            });

            finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int position = getAdapterPosition();

                    final Task taskToFinish = data.get(position);
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

                    removeItemAtPosition(position);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Task taskToDelete = data.get(position);

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

                    removeItemAtPosition(position);
                }
            });
        }

        public void setColor(int color)
        {
            if(color == ORANGE)
            {
                //This using setBackgroundColor() on a cardview removes the margins and rounded corner effect. Use setCardBackgroundColor() instead
                //itemView.setBackgroundColor(context.getResources().getColor(R.color.appOrange));
                ((CardView) itemView).setCardBackgroundColor(context.getResources().getColor(R.color.appOrange));
            }
            else if(color == GREEN)
            {
                //itemView.setBackgroundColor(context.getResources().getColor(R.color.appGreen));
                ((CardView) itemView).setCardBackgroundColor(context.getResources().getColor(R.color.appGreen));
            }
        }

        public void setTitleViewText(String title)
        {
            titleTV.setText(title);
        }

        public void setDescriptionViewText(String description)
        {
            descriptionTV.setText(description);
        }
    }
}
