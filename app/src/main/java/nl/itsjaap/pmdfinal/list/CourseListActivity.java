package nl.itsjaap.pmdfinal.list;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nl.itsjaap.pmdfinal.R;
import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class CourseListActivity extends AppCompatActivity {

    private ListView mListView;
    private ListAdapter mAdapter;
    private List<CourseModel> courseModels = new ArrayList<>();

    String CURRENTUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));

        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND (isOpt=? OR (isOpt=? AND isActive=?))", new String[] { CURRENTUSER, "0", "1", "1"}, null, null, DatabaseInfo.CourseColumn.YEAR);

        mListView = (ListView) findViewById(R.id.my_list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), CourseViewActivity.class);

                CourseModel item = (CourseModel) mListView.getItemAtPosition(position);


                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);
                b.putString("courseTitle", item.getName());
                b.putString("courseGrade", item.getGrade());
                b.putString("courseCredits", item.getCredits());
                b.putString("courseYear", item.getYear());
                b.putString("coursePeriod", item.getPeriod());
                b.putString("courseNotes", item.getNote());
                b.putString("courseIsOpt", item.getIsOpt());
                b.putString("courseIsAct", item.getIsActive());
                intent.putExtras(b);

                startActivityForResult(intent, 3);

            }
        });

        if (rs.getCount() > 0) {
            rs.moveToFirst();
            for (int i = 0 ; i < rs.getCount() ; i++) {
                String name = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.NAME));
                String credits = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.CREDITS));
                String period = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.PERIOD));
                String year = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.YEAR));
                String isOpt = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ISOPT));
                String grade = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.GRADE));
                String isAct = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ISACTIVE));
                String note = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.NOTE));
                String user = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.USER));
                courseModels.add(new CourseModel(name, credits, period, year, isOpt , grade, isAct, note, user));
                rs.moveToNext();
            }
        }


        mAdapter = new ListAdapter(CourseListActivity.this, 0, courseModels);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (3) : {
                if (resultCode == Activity.RESULT_OK) {
                    mAdapter.notifyDataSetChanged();
                    recreate();
                }
                break;
            }
        }
    }

}


