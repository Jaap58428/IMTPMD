package nl.itsjaap.pmdfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CourseEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        TextView title = findViewById(R.id.courseEdit_title);
        String courseTitle = getIntent().getExtras().getString("courseTitle");
        Log.d("coursetitle", courseTitle);
        title.setText(courseTitle);
    }
}
