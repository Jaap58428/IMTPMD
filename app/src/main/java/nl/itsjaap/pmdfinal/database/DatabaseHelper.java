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
    public static final int dbVersion = 18;

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

        // datatypes zijn beperkt tot bekend werkende interactie
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

    // specifieke update functie voor het toevoegen of aanpassen van een cijfer of notitie
    public void updateCourse(String grade, String notes, String user, String course){

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.CourseColumn.GRADE, grade);
        values.put(DatabaseInfo.CourseColumn.NOTE, notes);

        // Update the details object where id matches
        mSQLDB.update(DatabaseInfo.CourseTable.COURSETABLE,values,"user=? AND name=?", new String[] { user, course });
    }

    // specifieke functie voor het switchen tussen true/false of 1/0 op keuzevakken
    public String switchOptValue(String user, String course){
        Cursor rs = mSQLDB.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND name=?", new String[] { user, course }, null, null, null);
        if (rs.getCount() > 0) {
            rs.moveToFirst();
            String oldActive = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ISACTIVE));

            // invert old value
            String newActive;
            if(oldActive.equals("1")) {
                newActive = "0";
            } else {
                newActive = "1";
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseInfo.CourseColumn.ISACTIVE, newActive);

            mSQLDB.update(DatabaseInfo.CourseTable.COURSETABLE,values,"user=? AND name=?", new String[] { user, course });
            return newActive;
        } else {
            // return -1 wanneer er geen mogelijke aanpassing is
            return "-1";
        }

    }
}