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

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase mSQLDB;
    private static DatabaseHelper mInstance;
    public static final String dbName = "sql4u.db";
    public static final int dbVersion = 9;

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
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.UserColumn.EMAIL + " TEXT," +
                DatabaseInfo.UserColumn.PASSWORD + " TEXT);"
        );

        db.execSQL("CREATE TABLE " + DatabaseInfo.CourseTable.COURSETABLE + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseInfo.CourseColumn.NAME + " TEXT," +
                DatabaseInfo.CourseColumn.CREDITS + " INTEGER," +
                DatabaseInfo.CourseColumn.GRADE + " DOUBLE," +
                DatabaseInfo.CourseColumn.PERIOD + " INTEGER," +
                DatabaseInfo.CourseColumn.YEAR + " INTEGER," +
                DatabaseInfo.CourseColumn.ISOPT + " INTEGER," +
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
}