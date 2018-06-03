package nl.itsjaap.pmdfinal;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.app.Activity;
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

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

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
                String sMail = emailEditText.getText().toString().toLowerCase();

                EditText pwdEditText = findViewById(R.id.registerPwdEditText);
                String sPwd = pwdEditText.getText().toString();

                EditText pwdConfEditText = findViewById(R.id.registerPwdConfEditText);
                String sPwdConf = pwdConfEditText.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(sMail).matches()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_invalid_mail), Toast.LENGTH_SHORT).show();
                } else if (sPwd.length() < 8 ) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_short_pwd), Toast.LENGTH_SHORT).show();
                } else if (!sPwd.equals(sPwdConf)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_pwd_match), Toast.LENGTH_SHORT).show();
                } else {
                    validate(sMail, sPwd);

                }

            }
        });
    }

    public void validate(String emailInput, String pwdInput) {
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.UserTable.USERTABLE, new String[]{"*"}, null, null, null, null, null);

        boolean userExists = false;
        // If there are users: check if email isn't already taken
        if (rs.getCount() > 0) {
            rs.moveToFirst();
            for (int i = 0 ; i < rs.getCount() ; i++) {
                if (emailInput.equals(rs.getString(rs.getColumnIndex(DatabaseInfo.UserColumn.EMAIL)))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.register_toast_user_exists), Toast.LENGTH_SHORT).show();
                    userExists = true;
                }
                rs.moveToNext();
            }
        }

        // Hash pwd
//        for (byte b : pwd.getBytes()) {
//            // Find implementation
//        }

        if (!userExists) {
            insertNewUser(emailInput, pwdInput);
        }
    }

    public void insertNewUser(String emailInput, String pwdInput) {
        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());

        // Insert values into DB
        ContentValues cv = new ContentValues();
        cv.put(DatabaseInfo.UserColumn.EMAIL, emailInput);
        Log.d("inserted mail:", emailInput);
        cv.put(DatabaseInfo.UserColumn.PASSWORD, pwdInput);
        Log.d("inserted pwd:", pwdInput);
        db.insert(DatabaseInfo.UserTable.USERTABLE, null, cv);


        // Return the results to the login and finish
        Intent resultIntent = new Intent();
        resultIntent.putExtra("registeredEmail", emailInput);
        resultIntent.putExtra("registeredPwd", pwdInput);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
