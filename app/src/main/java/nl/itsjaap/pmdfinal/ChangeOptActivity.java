package nl.itsjaap.pmdfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChangeOptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_opt);

        // implement listView of only optional courses
        // when you tap one: give feedback the course switched from active to inactive (vica versa)
    }
}
