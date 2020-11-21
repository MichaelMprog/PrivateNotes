package com.example.privatenotes;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {
    TextView mDetails;
    NoteDatabase db;
    Note note;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDetails = findViewById(R.id.detailsOfNote);        //content_details.xml

        Intent i = getIntent();
        id = i.getLongExtra("ID", 0);  // gets the id of a note

        // extract data from database
        db = new NoteDatabase(this);
        note = db.getNote(id);
        // set title of action bar of note details to the note's title
        getSupportActionBar().setTitle(note.getTitle());
        // get contents of note and display it
        mDetails.setText(note.getContent());
        mDetails.setMovementMethod(new ScrollingMovementMethod());  // adds scroll bar to details

        // fab is the delete note button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete note
                db.deleteNote(id);
                // or use db.deleteNote(id)??
                // redirect user to main activity
                Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    // for saving and deleting notes (buttons in toolbar)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editNote)
        {
            // direct user to edit activity
            Intent i = new Intent(this, Edit.class); // create intent to go to edit
            i.putExtra("ID", id);                  // pass note id along as well
            startActivity(i);                                      // start Edit activity

        }
        return super.onOptionsItemSelected(item);
    }
}