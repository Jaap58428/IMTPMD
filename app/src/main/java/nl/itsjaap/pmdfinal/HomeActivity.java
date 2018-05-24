package nl.itsjaap.pmdfinal;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import nl.itsjaap.pmdfinal.Database.DatabaseHelper;
import nl.itsjaap.pmdfinal.Database.DatabaseInfo;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**
         *
         * Required aspects, everything can be used by user
         *
         * List of all courses + EC needed to graduate
         * - Mandatory vs. Optional
         * - Filter by year
         * - Per course: completed? + grade + notes
         *
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
