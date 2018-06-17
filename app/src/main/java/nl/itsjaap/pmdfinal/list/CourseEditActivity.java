package nl.itsjaap.pmdfinal.list;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

import nl.itsjaap.pmdfinal.R;
import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

public class CourseEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        // grab all the passed extras value and fill them in to format
        TextView title = findViewById(R.id.courseEdit_title);
        String courseTitle = getIntent().getExtras().getString("courseTitle");
        title.setText(courseTitle);

        EditText vNewGrade = findViewById(R.id.courseEdit_editTextGrade);
        String oldGrade = getIntent().getExtras().getString("courseGrade");
        // when there was an old value, put this already in the editText
        if (!("" + oldGrade).matches("")) {
            vNewGrade.setText(oldGrade , TextView.BufferType.EDITABLE);
        }

        EditText vNewNotes = findViewById(R.id.courseEdit_editTextNotes);
        String oldNote = getIntent().getExtras().getString("courseNote");
        if (!("" + oldNote).matches("")) {
            vNewNotes.setText(oldNote , TextView.BufferType.EDITABLE);
        }

        FloatingActionButton saveBtn = findViewById(R.id.courseEdit_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText vNewGrade = findViewById(R.id.courseEdit_editTextGrade);
                String sNewGrade = vNewGrade.getText().toString();

                EditText vNewNotes = findViewById(R.id.courseEdit_editTextNotes);
                String sNewNotes = vNewNotes.getText().toString();



                double enteredGrade;

                // check if the input is a double
                if(isDouble(sNewGrade)){
                    Double unroundedGrade = Double.valueOf(sNewGrade);

                    // takes away decimals beyond 2 points
                    int roundedGrade = (int) (unroundedGrade * 100);

                    // revert back to double
                    enteredGrade = (double) roundedGrade / 100;

                    // only grades between 1 and up to 10 are valid
                    if (1 <= enteredGrade && enteredGrade <= 10) {
                        String user = getIntent().getExtras().getString(getString(R.string.currentUser));
                        String course = getIntent().getExtras().getString("courseTitle");

                        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());

                        // convert grade to string
                        String dbGrade = new Double(enteredGrade).toString();

                        db.updateCourse(dbGrade, sNewNotes, user, course);

                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.courseEdit_toast_noGrade, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Shouldn't be possible due to numeric input
                    Toast.makeText(getApplicationContext(), R.string.courseEdit_toast_noDouble, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // check if an input is a double
    public boolean isDouble( String str ){
        try {
            Double.parseDouble(str);
            return true;
        }
        catch(Exception e ){
            return false;
        }
    }
}
