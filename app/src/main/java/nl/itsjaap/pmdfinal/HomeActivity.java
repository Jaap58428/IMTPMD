package nl.itsjaap.pmdfinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nl.itsjaap.pmdfinal.Database.DatabaseHelper;
import nl.itsjaap.pmdfinal.Database.DatabaseInfo;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button tutorialBtn = findViewById(R.id.homeBtnTutorial);
        tutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, TutorialActivity.class));
            }
        });

        Button courseBtn = findViewById(R.id.homeBtnCourse);
        courseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /**
         *
         * Required aspects, everything can be used by user
         *
         * List of all courses + EC needed to graduate
         * - Mandatory vs. Optional
         * - Filter by year
         * - Per course: completed? + grade + notes
         *
         * https://itsjaap.nl/js/courses.json
         *
         * Tutorial screen with fragments
         * Visualize progress
         * Use of SQLite local DB
         * Use of online JSON (list of potential courses, hosted on itsjaap?)
         *
         * Optional:
         * Hash passwords in DB (self learned aspect?!)
         * Use git branches to show version control system
         *
         * */

    }
}
