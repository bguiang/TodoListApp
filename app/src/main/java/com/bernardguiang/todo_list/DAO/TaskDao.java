package com.bernardguiang.todo_list.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bernardguiang.todo_list.Model.Task;

import java.util.List;

/**
 * Created by berna on 1/24/2018.
 */

@Dao
public interface TaskDao
{
    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE status = :status")
    List<Task> getAllTasksWithStatus(int status);

    @Query("SELECT * FROM tasks WHERE priority = :priority")
    List<Task> getAllTasksWithPriority(int priority);

    @Update
    void updateTask(Task task); //Update Task based on Primary Key

    @Insert
    void insertTask(Task... task);

    @Delete
    void deleteTask(Task task);
}
