package com.example.privatenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle,noteDetails;
    Calendar c;
    String todaysDate;
    String currentTime;
    NoteDatabase db;
    Note note;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setTitle(note.getTitle());

        Intent i = getIntent();                               // get intent from Details.java
        id = i.getLongExtra("ID", 0);  // gets the id of the note as well
        db = new NoteDatabase(this);                  // database of notes
        note = db.getNote(id);                                // get the current node with id

        final String title = note.getTitle();
        String content = note.getContent();
        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                getSupportActionBar().setTitle(title);
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

        // open edit menu with existing title and content already there
        noteTitle.setText(title);
        noteDetails.setText(content);

        // set current date and time
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
            return "0"+time;
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
            if (noteTitle.getText().length() != 0){
                note.setTitle(noteTitle.getText().toString());
                note.setContent(noteDetails.getText().toString());
                int id = db.editNote(note);
                // need to check if note is updated or not
                // if updated, the id will be equal to current note id
                if (id == note.getID()) {
                    Toast.makeText(this, "Note is updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Error updating Note", Toast.LENGTH_SHORT).show();
                }

                // go back to refreshed note details
                Intent i = new Intent(getApplicationContext(), Details.class);
                i.putExtra("ID", note.getID());
                startActivity(i);
                Toast.makeText(this, "Note Updated.", Toast.LENGTH_SHORT).show();
            }
            else {
                noteTitle.setError("Title cannot be blank");
            }
        }
        else if(item.getItemId() == R.id.delete)
        {
            Toast.makeText(this, "Delete btn", Toast.LENGTH_SHORT).show();     // tells user button was pressed
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