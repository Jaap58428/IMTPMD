package nl.itsjaap.pmdfinal;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the value of the username from shared prefs if present
        SharedPreferences credentials = getSharedPreferences(getString(R.string.prefs_name), 0);
        String userName = credentials.getString(getString(R.string.prefs_lastUser), "");

        // check if a username is present
        if (userName.length() > 0) {
            // greet the user
            Toast.makeText(getApplicationContext(), getString(R.string.welcome_message) + " " + userName,Toast.LENGTH_SHORT).show();

            // set the username in the input
            EditText userNameInput = findViewById(R.id.usernameEditText);
            userNameInput.setText(userName, TextView.BufferType.EDITABLE);
        }


        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                EditText usernameEditText = findViewById(R.id.usernameEditText);
                String sUsername = usernameEditText.getText().toString().toLowerCase();

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
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivityForResult(i, 1);
            }
        });

    }

    public void authenticate(String username, String password) {
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        final Cursor rs = db.query(DatabaseInfo.UserTable.USERTABLE, new String[]{"*"}, null, null, null, null, null);

        // check if there are users registered
        if (rs.getCount() > 0) {
            rs.moveToFirst();
            boolean loginSuccess = false;
            // loop over users and check credentials
            for (int i = 0 ; i < rs.getCount() ; i++){
                String toCheckUser = rs.getString(rs.getColumnIndex(DatabaseInfo.UserColumn.EMAIL));
                String toCheckPwd = rs.getString(rs.getColumnIndex(DatabaseInfo.UserColumn.PASSWORD));
                if (username.equals(toCheckUser) && password.equals(toCheckPwd))
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                    Bundle b = new Bundle();
                    b.putString(getString(R.string.currentUser), username);
                    intent.putExtras(b);

                    SharedPreferences credentials = getSharedPreferences(getString(R.string.prefs_name), 0);
                    SharedPreferences.Editor editor = credentials.edit();
                    editor.putString(getString(R.string.prefs_lastUser),username);
                    editor.apply();

                    startActivity(intent);
                    loginSuccess = true;
                }
                rs.moveToNext();
            }
            if (!loginSuccess) {
                // when there was no match in the DB
                Toast.makeText(getApplicationContext(), getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
            }
        } else {
            // when there are no registered users
            Toast.makeText(getApplicationContext(), getString(R.string.no_users), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newEmail = data.getStringExtra("registeredEmail");
                    String newPwd = data.getStringExtra("registeredPwd");

                    EditText mail = findViewById(R.id.usernameEditText);
                    mail.setText(newEmail);

                    EditText pwd = findViewById(R.id.pwdEditText);
                    pwd.setText(newPwd);

                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_success), Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
