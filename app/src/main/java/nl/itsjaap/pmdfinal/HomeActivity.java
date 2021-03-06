package nl.itsjaap.pmdfinal;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;
import nl.itsjaap.pmdfinal.gson.GsonRequest;
import nl.itsjaap.pmdfinal.gson.VolleyHelper;
import nl.itsjaap.pmdfinal.list.ChangeOptActivity;
import nl.itsjaap.pmdfinal.list.CourseListActivity;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class HomeActivity extends AppCompatActivity {

    String CURRENTUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));

        Button getJsonBtn = findViewById(R.id.getJsonBtn);
        getJsonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = DatabaseHelper.getHelper(getApplicationContext());
                Cursor rs = dbHelper.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=?", new String[] { CURRENTUSER }, null, null, null);

                // check if the DB is actually empty and needs filling
                if (rs.getCount() == 0) {
                    requestSubjects();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.home_toast_db_exists, Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button tutorialBtn = findViewById(R.id.homeBtnTutorial);
        tutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TutorialActivity.class);

                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);
                intent.putExtras(b);

                startActivity(intent);            }
        });

        Button courseBtn = findViewById(R.id.homeBtnCourse);
        courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CourseListActivity.class);

                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);
                intent.putExtras(b);

                startActivity(intent);
            }
        });

        Button graphBtn = findViewById(R.id.homeBtnGraph);
        graphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GraphActivity.class);

                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);

                intent.putExtras(b);
                startActivity(intent);
            }
        });

        Button optBtn = findViewById(R.id.changeOptCourseBtn);
        optBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChangeOptActivity.class);

                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);

                intent.putExtras(b);
                startActivity(intent);
            }
        });

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
            cv.put(DatabaseInfo.CourseColumn.ISOPT, cm.getIsOpt());
            cv.put(DatabaseInfo.CourseColumn.ISACTIVE, "0");  // by default no optional classes are active
            cv.put(DatabaseInfo.CourseColumn.USER , CURRENTUSER);
            cv.put(DatabaseInfo.CourseColumn.NOTE, "");
            dbHelper.insert(DatabaseInfo.CourseTable.COURSETABLE, null, cv);
        }

        Cursor rs = dbHelper.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, null, null, null, null, null);
        rs.moveToFirst();   // kan leeg zijn en faalt dan
        DatabaseUtils.dumpCursor(rs);

        Toast.makeText(getApplicationContext(), R.string.home_toast_json_success, Toast.LENGTH_SHORT).show();

    }

    private void processRequestError(VolleyError error){
        Toast.makeText(getApplicationContext(), "" + R.string.error + "\n" +  error , Toast.LENGTH_LONG).show();
    }

}
