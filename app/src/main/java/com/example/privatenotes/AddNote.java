package com.example.privatenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle,noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Note");

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                {
                    getSupportActionBar().setTitle(s);  // changes text in toolbar when note title is changed
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        Log.d("DATE", "Date: " + todaysDate);
        currentTime = pad(c.get(Calendar.HOUR)) + ":" + pad(c.get(Calendar.MINUTE));
        Log.d("TIME", "Time: " + currentTime);
    }

    // pads time values with zeroes
    private String pad(int time) {
        if (time < 10)
        {
            return "0" + time;
        }
        return String.valueOf(time);
    }

    // creates menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    // for saving and deleting notes (buttons in toolbar)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.save)
        {
            if (noteTitle.getText().length() != 0)
            {
                Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(),todaysDate,currentTime);  // constructs note
                NoteDatabase sDB = new NoteDatabase(this);
                long id = sDB.addNote(note);     // adds note to database
                Note check = sDB.getNote(id);
                Log.d("inserted", "Note: " + id + " -> Title: " + check.getTitle() + " Date: " + check.getDate());
                onBackPressed();         // goes back to main note menu

                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();  // tells user button was pressed
            }
            else
            {
                noteTitle.setError("Title can not be blank");
            }
        }
        else if(item.getItemId() == R.id.delete)
        {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();     // tells user button was pressed
            goToMain();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}