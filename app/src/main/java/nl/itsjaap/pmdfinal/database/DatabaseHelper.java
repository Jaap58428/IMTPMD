package nl.itsjaap.pmdfinal.database;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.telecom.Call;
import android.util.Log;

import static android.provider.BaseColumns._ID;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase mSQLDB;
    private static DatabaseHelper mInstance;
    public static final String dbName = "sql4u.db";
    public static final int dbVersion = 12;

    private DatabaseHelper(Context ctx) {
        super(ctx, dbName, null, dbVersion);
    }

    public static synchronized DatabaseHelper getHelper(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx);
            mSQLDB = mInstance.getWritableDatabase();
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseInfo.UserTable.USERTABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.UserColumn.EMAIL + " TEXT," +
                DatabaseInfo.UserColumn.PASSWORD + " TEXT);"
        );

        db.execSQL("CREATE TABLE " + DatabaseInfo.CourseTable.COURSETABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.CourseColumn.NAME + " TEXT," +
                DatabaseInfo.CourseColumn.CREDITS + " INTEGER," +
                DatabaseInfo.CourseColumn.GRADE + " TEXT," +
                DatabaseInfo.CourseColumn.PERIOD + " INTEGER," +
                DatabaseInfo.CourseColumn.YEAR + " INTEGER," +
                DatabaseInfo.CourseColumn.ISOPT + " INTEGER," +
                DatabaseInfo.CourseColumn.ISACTIVE + " INTEGER," +
                DatabaseInfo.CourseColumn.USER + " TEXT," +
                DatabaseInfo.CourseColumn.NOTE + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseInfo.UserTable.USERTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseInfo.CourseTable.COURSETABLE);
        onCreate(db);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void insert(String table, String nullColumnHack, ContentValues values) {
        mSQLDB.insert(table, nullColumnHack, values);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectArgs, String groupBy, String having, String orderBy) {
        return mSQLDB.query(table, columns, selection, selectArgs, groupBy, having, orderBy);
    }

    public void updateCourse(String grade, String notes, String user, String course, String credits, String period, String year, String isOpt, String isAct){

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.NAME, course);
        values.put(DatabaseInfo.CourseColumn.CREDITS, credits);
        values.put(DatabaseInfo.CourseColumn.GRADE, grade);
        values.put(DatabaseInfo.CourseColumn.PERIOD, period);
        values.put(DatabaseInfo.CourseColumn.YEAR, year);
        values.put(DatabaseInfo.CourseColumn.ISOPT, isOpt);
        values.put(DatabaseInfo.CourseColumn.ISACTIVE, isAct);
        values.put(DatabaseInfo.CourseColumn.USER, user);
        values.put(DatabaseInfo.CourseColumn.NOTE, notes);

        Log.d("data inserted to DB", course+grade+user);
        Log.d("data values", values.toString());

        // Update the details object where id matches
        mSQLDB.delete(DatabaseInfo.CourseTable.COURSETABLE,"user=? AND name=?", new String[] { user, course });
        long id = mSQLDB.insert(DatabaseInfo.CourseTable.COURSETABLE, null,values);
        Log.d("id of new data entry", ""+id);
        Cursor cs = mSQLDB.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND name=?", new String[] { user, course }, null, null, null);
        cs.moveToFirst();
        String newEntry = cs.getString(cs.getColumnIndex(DatabaseInfo.CourseColumn.NOTE));
        Log.d("data from DB", newEntry);
    }
}