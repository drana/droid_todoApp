package dipen.todoapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dipenrana on 6/21/16.
 */
public class ToDoItemDatabase extends SQLiteOpenHelper{

    public ToDoItemDatabase(Context context) {
        super(context, "todo.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
