package com.bernardguiang.todo_list.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * The @Entity annotation defines a table in the database where Objects of this Class can be stored
 * The @PrimaryKey sets a variable as the table's primary key. Adding autoGenerate = true lets Room
 *   autogenerate primary keys by incrementing the highest primary key
 * The @ColumnInfo annotation creates the column in which the variable is stored. If no name is
 *   defined, it will use the variable's name for the column name.
 *
 * Important: Room requires all saved/stored variables have a getter and a setter
 */

@Entity(tableName = "tasks")
public class Task
{
    public static final int IN_PROGRESS = 0;
    public static final int FINISHED = 1;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name="status")
    private int status;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "due_date")
    private String dueDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
