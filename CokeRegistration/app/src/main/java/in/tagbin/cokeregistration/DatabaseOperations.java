package in.tagbin.cokeregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin pc on 04-04-2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {

    public DatabaseOperations(Context context) {
        super(context, TableData.Tableinfo.DATABASE_NAME, null, database_version);
    }

    public static final int database_version = 2;
    public String CREATE_QUERY = "CREATE TABLE " + TableData.Tableinfo.TABLE_NAME + "(" + TableData.Tableinfo.NAME + " TEXT," + TableData.Tableinfo.EMAIL + " TEXT,"+ TableData.Tableinfo.NUMBER + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_QUERY);
        Log.d("Database operations", "Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperations dop, String name, String pass,String number)

    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.NAME, name);
        cv.put(TableData.Tableinfo.EMAIL, pass);
        cv.put(TableData.Tableinfo.NUMBER, number);
        long k = SQ.insert(TableData.Tableinfo.TABLE_NAME, null, cv);
        Log.d("Database operations", "One raw inserted");
    }

    public Cursor getInformation(DatabaseOperations dop) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] coloumns = {TableData.Tableinfo.NAME, TableData.Tableinfo.EMAIL, TableData.Tableinfo.NUMBER};
        Cursor CR = SQ.query(TableData.Tableinfo.TABLE_NAME, coloumns, null, null, null, null, null);
        return CR;


    }
}
