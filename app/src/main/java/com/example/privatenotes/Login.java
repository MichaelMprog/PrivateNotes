package com.example.privatenotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    private EditText enterPassword, newPass, oldPass;
    private Button btnChange, btnNext;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PASSWORD = "text";
    private static final String KEY = "SometopSecretKey1235";
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI initialization
        enterPassword = (EditText) findViewById(R.id.enterPassword);
        newPass = (EditText) findViewById(R.id.newPass);
        oldPass = (EditText) findViewById(R.id.oldPass);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnNext = (Button) findViewById(R.id.btnLogin);

        // change password button listener
        btnChange.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when clicked, set password to text in newPass text field
                String oldInput = oldPass.getText().toString();
                loadPassword();
                // if user put nothing in old password, and current password is null, create new password
                if ( (oldInput == null || oldInput.isEmpty()) && (userPass == null || userPass.isEmpty()) )
                {
                    savePassword();
                    ((EditText) findViewById(R.id.newPass)).setText("");
                    ((EditText) findViewById(R.id.oldPass)).setText("");
                }
                else if (oldInput.compareTo(userPass) == 0)     // if old input is correct
                {
                    savePassword();
                    ((EditText) findViewById(R.id.newPass)).setText("");
                    ((EditText) findViewById(R.id.oldPass)).setText("");
                }
                else                                            // if old password incorrect
                {
                    Toast.makeText(Login.this, "Old password is invalid", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // login button listener
        btnNext.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // get password from input when login is pressed
               String inputPass = enterPassword.getText().toString();
               loadPassword();
               // if user hasn't set a password yet
               if (userPass.compareTo("") == 0)
               {
                    Toast.makeText(Login.this, "Please create a password first.", Toast.LENGTH_SHORT).show();
               }
               else if (inputPass.compareTo(userPass) == 0) {
                   // if correct, clear text fields and go to MainActivity
                   ((EditText) findViewById(R.id.enterPassword)).setText("");
                   ((EditText) findViewById(R.id.newPass)).setText("");
                   ((EditText) findViewById(R.id.oldPass)).setText("");
                   Intent intent = new Intent(Login.this, MainActivity.class);
                   startActivity(intent);
               }
               else {
                   // if incorrect, tell user
                   Toast.makeText(Login.this, "Invalid Password", Toast.LENGTH_SHORT).show();
               }
           }
        });
    }


    public void savePassword() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // saves text in newPass EditText field into PASSWORD shared preference
        editor.putString(PASSWORD, newPass.getText().toString());
        editor.apply();
        Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show();
    }

    public void loadPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        // sets userPass equal to string in PASSWORD, or if there is no value, make it empty
        userPass = sharedPreferences.getString(PASSWORD, "default");
    }


    /*
    public void savePassword() {
        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(this,
                this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
        sharedPreferences.edit().putString(PASSWORD, newPass.getText().toString()).apply();
        Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show();
    }

    public void loadPassword() {
        SharedPreferences sharedPreferences = new ObscuredSharedPreferences(this,
                this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
        // sets userPass equal to string in PASSWORD, or if there is no value, make it empty
        userPass = sharedPreferences.getString(PASSWORD, null);
    }
*/

}