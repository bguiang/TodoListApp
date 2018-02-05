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
 * This class defines the database used by the app with the @Database annotation. You must include
 *   all the Entity Classes and the Version number of the database
 * The database is saved on the device, inside the folder for this Application. Uninstalling the
 *   application also removes the database from the device
 * When Creating a new Version of the Database (By adding/removing columns and Entity classes) a
 *   Database Migration function has to be defined to transform the older database into the newer
 *   version. You only need to worry about this when your app is in production
 *
 * Important: You should follow the singleton design pattern when instantiating an AppDatabase
 *   object, as each RoomDatabase instance is fairly expensive, and you rarely need access to
 *   multiple instances.
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
