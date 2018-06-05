package nl.itsjaap.pmdfinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

public class CourseViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);

        final String CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));
        final String courseTitle = getIntent().getExtras().getString("courseTitle");
        final String courseGrade = getIntent().getExtras().getString("courseGrade");
        final String courseCredits = getIntent().getExtras().getString("courseCredits");
        final String courseYear = getIntent().getExtras().getString("courseYear");
        final String coursePeriod = getIntent().getExtras().getString("coursePeriod");
        final String courseNotes = getIntent().getExtras().getString("courseNotes");

        TextView vTitle = findViewById(R.id.courseViewTitleTextView);
        vTitle.setText(courseTitle);

        String gradeValue;
        if (courseGrade == null) {
            gradeValue = getString(R.string.courseList_blank_grade);
        } else {
            gradeValue = courseGrade;
        }
        TextView vGrade = findViewById(R.id.courseView_grade);
        String sGrade = vGrade.getText() + " " + gradeValue;
        vGrade.setText(sGrade);

        TextView vCredits = findViewById(R.id.courseView_credits);
        String sCredits = vCredits.getText() + " " + courseCredits;
        vCredits.setText(sCredits);

        TextView vPeriod = findViewById(R.id.courseView_period);
        String sPeriod = vPeriod.getText() + " " + coursePeriod;
        vPeriod.setText(sPeriod);

        TextView vYear = findViewById(R.id.courseView_year);
        String sYear = vYear.getText() + " " + courseYear;
        vYear.setText(sYear);

        String courseNotesData;
        if(courseNotes.equals("")){
            courseNotesData = getString(R.string.courseView_blankNotes);
        } else {
            courseNotesData = courseNotes;
        }
        TextView vNotes = findViewById(R.id.courseView_notes);
        String sNotes = vNotes.getText() + "\n" + courseNotesData;
        vNotes.setText(sNotes);

        FloatingActionButton editBtn = findViewById(R.id.courseView_editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseViewActivity.this, CourseEditActivity.class);
                Bundle b = new Bundle();
                b.putString(getString(R.string.currentUser), CURRENTUSER);
                b.putString("courseTitle", courseTitle);
                b.putString("courseGrade", courseGrade);
                b.putString("courseCredits", courseCredits);
                b.putString("courseYear", courseYear);
                b.putString("coursePeriod", coursePeriod);
                b.putString("courseNotes", courseNotes);
                intent.putExtras(b);

                startActivity(intent);


            }
        });
    }
}
