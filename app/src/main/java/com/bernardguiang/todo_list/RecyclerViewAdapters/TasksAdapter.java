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
 * Created by berna on 1/28/2018.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>
{
    public static final int ORANGE = 0;
    public static final int GREEN = 1;

    Context context;
    List<Task> data;
    LayoutInflater layoutInflater;

    public TasksAdapter(Context context, List<Task> data)
    {
        this.context = context;
        this.data = data;

        layoutInflater = LayoutInflater.from(context);
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
        holder.setViewTags(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Task getItemAtPosition(int position)
    {
        return data.get(position);
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

            //descriptionTV.setVisibility(View.GONE);
            //deleteBtn.setVisibility(View.GONE);
            //finishBtn.setVisibility(View.GONE);

            //TODO: add Swipe lenft and right functionality and just remove the finish button
            /*itemView.setOnTouchListener(new OnSwipeTouchListener(context)
            {
                public void onSwipeRight() {
                    Toast.makeText(context, "right: " + titleTV.getText().toString(), Toast.LENGTH_SHORT).show();
                    *//*if(c.getStatus() == Task.IN_PROGRESS)
                    {
                        setColor(GREEN);
                        currentTask.setStatus(Task.FINISHED);
                        updateTask();
                    }*//*
                }

                public void onSwipeLeft() {
                    Toast.makeText(context, "left: " + titleTV.getText().toString(), Toast.LENGTH_SHORT).show();
                    *//*if(currentTask.getStatus() == Task.FINISHED)
                    {
                        setColor(ORANGE);
                        currentTask.setStatus(Task.IN_PROGRESS);
                        updateTask();
                    }*//*
                }
            });*/
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

        public void setViewTags(int position)
        {
            titleTV.setTag(position);
            descriptionTV.setTag(position);
            deleteBtn.setTag(position);
            finishBtn.setTag(position);
        }
    }
}
