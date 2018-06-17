package nl.itsjaap.pmdfinal;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;
import nl.itsjaap.pmdfinal.models.BackupModel;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class TutorialActivity extends AppCompatActivity {

    String CURRENTUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // turn the reference into a hash since special characters aren't allowed, like @ . - etc.
        final DatabaseReference myRef = database.getReference("backup_" + CURRENTUSER.hashCode() );

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TextView latestBackupText = findViewById(R.id.helpTextLatestBackup);
                String oldBackup;

                // when there is a backup known set the timestamp to the user view
                if (dataSnapshot.getValue() != null) {

                    String day = dataSnapshot.child("timestamp").child("date").getValue().toString();
                    String month = dataSnapshot.child("timestamp").child("month").getValue().toString();
                    String year = dataSnapshot.child("timestamp").child("year").getValue().toString();
                    year = String.valueOf(Integer.valueOf(year) - 100);

                    String hour = dataSnapshot.child("timestamp").child("hours").getValue().toString();
                    String minute = dataSnapshot.child("timestamp").child("minutes").getValue().toString();
                    String second = dataSnapshot.child("timestamp").child("seconds").getValue().toString();

                    oldBackup = getString(R.string.tutorial_latestBackup)+"\n" + day + "-" + month + "-" + year + " " + hour+":"+minute+":"+second;

                } else {

                    oldBackup = getString(R.string.tutorial_latestBackup)+"\n"+ getString(R.string.tutorial_noBackup);

                }
                latestBackupText.setText(oldBackup);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // do nothing
            }
        });

        Button backupBtn =findViewById(R.id.backupNowBtn);
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBackup(myRef);
            }
        });

    }

    public void createBackup(DatabaseReference myRef) {

        // get all userdata from DB
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=?", new String[] { CURRENTUSER }, null, null, DatabaseInfo.CourseColumn.YEAR);

        // put all data of the current user in an array list
        ArrayList<CourseModel> userCourses = new ArrayList<>();
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
                userCourses.add(new CourseModel(name, credits, period, year, isOpt , grade, isAct, note, user));
                rs.moveToNext();
            }
        }

        // put data, and username in backup object to store and attach timestamp in class
        BackupModel userBackup = new BackupModel(userCourses, CURRENTUSER);

        // store backup
        myRef.setValue(userBackup);
    }
}
