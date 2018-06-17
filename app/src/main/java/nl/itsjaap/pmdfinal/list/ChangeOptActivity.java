package nl.itsjaap.pmdfinal.list;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nl.itsjaap.pmdfinal.R;
import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class ChangeOptActivity extends AppCompatActivity {

    private ListView mListView;
    private ListAdapterOpt mAdapter;
    private List<CourseModel> courseModels = new ArrayList<>();

    String CURRENTUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_opt);

        CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));

        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        // query to get all courses that are from this user and optional
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND isOpt=?", new String[] { CURRENTUSER, "1"}, null, null, DatabaseInfo.CourseColumn.YEAR);

        mListView = (ListView) findViewById(R.id.my_list_view_opt);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // grab the clicked item
                CourseModel item = (CourseModel) mListView.getItemAtPosition(position);

                DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());

                String username = item.getUser();
                String coursename = item.getName();

                // return the new value from the DB
                String newValue = db.switchOptValue(username, coursename);

                // display feedback based of newValue
                String toast;
                if (newValue.equals("1")) {
                    toast = getString(R.string.courseOpt_active);
                } else {
                    toast = getString(R.string.courseOpt_inactive);
                }
                Toast.makeText(getApplicationContext(), toast + " " + coursename,Toast.LENGTH_SHORT).show();

                // recreate the listview to show the new DB data
                mListView.invalidate();
                recreate();

            }
        });

        // fill the arrayList while looping over the DB results
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


        mAdapter = new ListAdapterOpt(ChangeOptActivity.this, 0, courseModels);
        mListView.setAdapter(mAdapter);
    }
}
