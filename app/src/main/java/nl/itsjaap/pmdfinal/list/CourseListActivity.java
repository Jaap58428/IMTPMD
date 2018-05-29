package nl.itsjaap.pmdfinal.list;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import nl.itsjaap.pmdfinal.R;
import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;
import nl.itsjaap.pmdfinal.gson.GsonRequest;
import nl.itsjaap.pmdfinal.gson.VolleyHelper;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class CourseListActivity extends AppCompatActivity {

    private ListView mListView;
    private ListAdapter mAdapter;
    private List<CourseModel> courseModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        DatabaseHelper dbHelper = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = dbHelper.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, null, null, null, null, null);

        // When the DB is still empty request courses
        if (rs.getCount() == 0) {
            Log.d("", "Requesting JSON");
            requestSubjects();
        }



    }

    private void requestSubjects(){
        Type type = new TypeToken<List<CourseModel>>(){}.getType();

        GsonRequest<List<CourseModel>> request = new GsonRequest<List<CourseModel>>("https://itsjaap.nl/js/courses.json",
                type, null, new Response.Listener<List<CourseModel>>() {
            @Override
            public void onResponse(List<CourseModel> response) {
                processRequestSucces(response);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                processRequestError(error);
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }

    private void processRequestSucces(List<CourseModel> subjects ){
        DatabaseHelper dbHelper = DatabaseHelper.getHelper(getApplicationContext());

        // putting all received classes in my database.
        for (CourseModel cm : subjects) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseInfo.CourseColumn.NAME, cm.getName());
            cv.put(DatabaseInfo.CourseColumn.CREDITS, cm.getCredits());
            cv.put(DatabaseInfo.CourseColumn.GRADE, cm.getGrade());
            cv.put(DatabaseInfo.CourseColumn.PERIOD, cm.getPeriod());
            cv.put(DatabaseInfo.CourseColumn.YEAR, cm.getYear());
            cv.put(DatabaseInfo.CourseColumn.ISOPT , cm.getIsOpt());
            cv.put(DatabaseInfo.CourseColumn.USER , cm.getUser());
            dbHelper.insert(DatabaseInfo.CourseTable.COURSETABLE, null, cv);
        }

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, null, null, null, null, null);
        rs.moveToFirst();   // kan leeg zijn en faalt dan
        DatabaseUtils.dumpCursor(rs);
        Toast.makeText(getApplicationContext(), "The JSON has arrived!", Toast.LENGTH_SHORT).show();

    }

    private void processRequestError(VolleyError error){
        Toast.makeText(getApplicationContext(), "There was an error:\n" + error , Toast.LENGTH_LONG).show();
    }

}
