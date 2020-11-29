package com.example.privatenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Login extends AppCompatActivity {
    private EditText enterPassword, newPass, oldPass;
    private Button btnChange, btnNext;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PASSWORD = "password";
    private String userPass;
    private SharedPreferences sharedPreferences;

    private static final String MASTER_KEY_ALIAS = "key";
    private static final int KEY_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Encrypted Shared Preferences
        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(KEY_SIZE)
                .build();
        MasterKey masterKey = null;
        try {
            masterKey = new MasterKey.Builder(Login.this)
                    .setKeyGenParameterSpec(spec)
                    .build();
            sharedPreferences = EncryptedSharedPreferences.create(
                Login.this,
                "sharedPrefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        }catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

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
                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                // sets userPass equal to string in PASSWORD, or if there is no value, make it empty
                userPass = sharedPreferences.getString(PASSWORD, "default");
                // if user put nothing in old password, and current password is null, create new password
                if ( (oldInput == null || oldInput.isEmpty()) && (userPass == null || userPass.isEmpty()) )
                {
                    editor.putString(PASSWORD, newPass.getText().toString());
                    editor.apply();
                    Toast.makeText(Login.this, "Password saved", Toast.LENGTH_SHORT).show();
                    ((EditText) findViewById(R.id.newPass)).setText("");
                    ((EditText) findViewById(R.id.oldPass)).setText("");
                }
                else if (oldInput.compareTo(userPass) == 0)     // if old input is correct
                {
                    editor.putString(PASSWORD, newPass.getText().toString());
                    editor.apply();
                    Toast.makeText(Login.this, "Password saved", Toast.LENGTH_SHORT).show();
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
               sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
               // sets userPass equal to string in PASSWORD, or if there is no value, make it empty
               userPass = sharedPreferences.getString(PASSWORD, "default");
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
        // saves text in newPass EditText field into PASSWORD shared preference

    }

    public void loadPassword() {

    }



}