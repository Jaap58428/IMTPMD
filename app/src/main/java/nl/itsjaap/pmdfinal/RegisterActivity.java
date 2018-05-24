package nl.itsjaap.pmdfinal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.itsjaap.pmdfinal.Database.DatabaseHelper;
import nl.itsjaap.pmdfinal.Database.DatabaseInfo;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerBtn = findViewById(R.id.registerConfirmBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEditText = findViewById(R.id.registerEmailEditText);
                String sMail = emailEditText.getText().toString();

                EditText pwdEditText = findViewById(R.id.registerPwdEditText);
                String sPwd = pwdEditText.getText().toString();

                EditText pwdConfEditText = findViewById(R.id.registerPwdConfEditText);
                String sPwdConf = pwdConfEditText.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(sMail).matches()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_invalid_mail), Toast.LENGTH_SHORT).show();
                } else if (sPwd.length() < 8 ) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_short_pwd), Toast.LENGTH_SHORT).show();
                } else if (!sPwd.matches(sPwdConf)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_pwd_match), Toast.LENGTH_SHORT).show();
                } else {
                    validate(sMail, sPwd);



                    // Auto login
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }

            }
        });
    }

    public void validate(String email, String pwd) {
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.UserTable.USERTABLE, new String[]{"*"}, null, null, null, null, null);

        // If there are users: check if email isn't already taken
        if (rs.getCount() < 0) {
            rs.moveToFirst();
            for (int i = 0 ; i < rs.getCount() ; i++) {
                if (email.matches(rs.getColumnName(rs.getColumnIndex("email")))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_user_exists), Toast.LENGTH_SHORT).show();
                    return;
                }
                rs.moveToNext();
            }
        }

        // Hash pwd
        for (byte b : pwd.getBytes()) {
            // Find implementation
        }

        // Insert values into DB
        ContentValues cv = new ContentValues();
        cv.put(DatabaseInfo.UserColumn.EMAIL, email);
        cv.put(DatabaseInfo.UserColumn.PASSWORD, pwd);
        db.insert(DatabaseInfo.UserTable.USERTABLE, null, cv);

    }

}
