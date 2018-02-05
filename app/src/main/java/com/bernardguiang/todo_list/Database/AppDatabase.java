package com.bernardguiang.todo_list.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.bernardguiang.todo_list.DAO.TaskDao;
import com.bernardguiang.todo_list.Model.Task;

/**
 * Created by berna on 1/24/2018.
 */

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract TaskDao taskDao();

    private static AppDatabase firstInstance;

    public static AppDatabase getDatabase(Context context)
    {
        if(firstInstance == null) {
            firstInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "com.bernardguiang.todo_list_db").build();
        }
        return firstInstance;
    }

    //Migration example
    /*static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `Fruit` (`id` INTEGER, "
                    + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };*/

}
