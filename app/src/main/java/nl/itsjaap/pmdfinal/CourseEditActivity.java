package nl.itsjaap.pmdfinal;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

public class CourseEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        TextView title = findViewById(R.id.courseEdit_title);
        String courseTitle = getIntent().getExtras().getString("courseTitle");
        title.setText(courseTitle);

        EditText vNewGrade = findViewById(R.id.courseEdit_editTextGrade);
        String oldGrade = getIntent().getExtras().getString("courseGrade");
        if (("" + oldGrade).matches("")) {
            vNewGrade.setText(oldGrade , TextView.BufferType.EDITABLE);
        }

        EditText vNewNotes = findViewById(R.id.courseEdit_editTextNotes);
        String oldNote = getIntent().getExtras().getString("courseNote");
        if (("" + oldNote).matches("")) {
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

                insertNew(sNewGrade, sNewNotes);
            }
        });
    }

    private void insertNew(String grade, String notes) {

        String user = getIntent().getExtras().getString(getString(R.string.currentUser));
        String course = getIntent().getExtras().getString("courseTitle");

        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND name=?", new String[] { user, course }, null, null, null);

        rs.moveToFirst();

        ContentValues cv = new ContentValues();
        cv.put(DatabaseInfo.CourseColumn.GRADE, grade);
        cv.put(DatabaseInfo.CourseColumn.NOTE, notes);
        db.insert(DatabaseInfo.CourseTable.COURSETABLE, null, cv);

        finish();

    }
}
