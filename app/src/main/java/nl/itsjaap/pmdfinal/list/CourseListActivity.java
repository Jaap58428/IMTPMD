package nl.itsjaap.pmdfinal.list;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, null, null, null, null, null);

        mListView = (ListView) findViewById(R.id.my_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                 Toast t = Toast.makeText(CourseListActivity.this,"Click" + position,Toast.LENGTH_SHORT);
                 t.show();
                 }
             }
        );

        if (rs.getCount() > 0) {
            rs.moveToFirst();
            for (int i = 0 ; i < rs.getCount() ; i++) {
                String name = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.NAME));
                String credits = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.CREDITS));
                String period = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.PERIOD));
                String year = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.YEAR));
                String isOpt = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ISOPT));
                String grade = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.GRADE));
                courseModels.add(new CourseModel(name, credits, period, year, isOpt ));
                rs.moveToNext();
            }
        }


        mAdapter = new ListAdapter(CourseListActivity.this, 0, courseModels);
        mListView.setAdapter(mAdapter);
    }

}


