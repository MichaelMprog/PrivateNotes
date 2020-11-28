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

public class Login extends AppCompatActivity {
    // using shared preferences for saving to local device
    // SharedPreferences sharedPref;
    EditText enterPassword;
    TextView wrongPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String userPass = "password";
        // message for wrong password, hidden until needed
        wrongPass = findViewById(R.id.wrongPass);

        setContentView(R.layout.activity_login);
        enterPassword = findViewById(R.id.enterPassword);

        Button button = findViewById(R.id.btnLogin);
        button.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //sharedPref.edit().putString("user_id",)
               // get password from input when login is pressed
               String inputPass = enterPassword.getText().toString();
               // compare to saved password
               if (inputPass.compareTo(userPass) == 0) {
                   // if correct, go to MainActivity
                   Intent intent = new Intent(Login.this, MainActivity.class);
                   startActivity(intent);
               }
               else {
                   // if incorrect, tell user
                   wrongPass.setVisibility(View.VISIBLE);
               }
           }
        });
    }



}