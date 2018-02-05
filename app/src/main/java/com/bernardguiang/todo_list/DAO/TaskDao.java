package com.bernardguiang.todo_list.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bernardguiang.todo_list.Model.Task;

import java.util.List;

/**
 * Task Data Access Object (DAO) interface lets us access the data in the database
 * Room automatically implements the queries defined in the @Query annotated functions.
 * The @Update annotation updates the Object in the Database associated with the primary key of the
 *   Object passed into the function
 * The @Insert annotation inserts a new row in the Database using the Object passed in.
 * The @Delete annotation deletes the Object in the Database associated with the primary key of the
 *   Object passed into the function
 *
 * Important: The database query functions of this DAO CANNOT be called in the main thread
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
