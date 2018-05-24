package nl.itsjaap.pmdfinal;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.itsjaap.pmdfinal.Database.DatabaseHelper;
import nl.itsjaap.pmdfinal.Database.DatabaseInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Welcome the user back, a touch of UX
        Toast.makeText(getApplicationContext(), getString(R.string.welcome_message),Toast.LENGTH_SHORT).show();

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                EditText usernameEditText = findViewById(R.id.usernameEditText);
                String sUsername = usernameEditText.getText().toString();

                EditText pwdEditText = findViewById(R.id.pwdEditText);
                String sPwd = pwdEditText.getText().toString();

                // Remind the user they haven't filled in any credentials
                if (sUsername.matches("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.forgot_username),Toast.LENGTH_SHORT).show();
                } else if (sPwd.matches("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.forgot_password), Toast.LENGTH_SHORT).show();
                } else {
                    authenticate(sUsername, sPwd);
                }
            }
        });

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

    }

    public void authenticate(String username, String password) {
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        final Cursor rs = db.query(DatabaseInfo.UserTable.USERTABLE, new String[]{"*"}, null, null, null, null, null);

        // check if there are users registered
        if (rs.getCount() < 0) {
            rs.moveToFirst();
            // loop over users and check credentials
            for (int i = 0 ; i < rs.getCount() ; i++){
                if (username.matches(rs.getString(rs.getColumnIndex("name")))
                        && password.matches(rs.getString(rs.getColumnIndex("password"))))
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    return;
                }
            }
            // when there was no match in the DB
            Toast.makeText(getApplicationContext(), getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
        } else {
            // when there are no registered users
            Toast.makeText(getApplicationContext(), getString(R.string.no_users), Toast.LENGTH_SHORT).show();
        }

    }
}
