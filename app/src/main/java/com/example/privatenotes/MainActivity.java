package com.example.privatenotes;

// Debugging Notes:
//      May need to check themes for action bar color (see tutorial 2 beginning)
// classes with differences: AddNote, MainActivity, Edit

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    List<Note> notes;
    //TextView noItemText;
    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noItemText = findViewById(R.id.noItemText);
        db = new NoteDatabase(this);

        // recycler view is the 'main menu' list of notes with date and time
        notes = db.getNotes();
        recyclerView = findViewById(R.id.listOfNotes);
        // if(notes.isEmpty())...

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add)
        {
            Toast.makeText(this, "Add New Note.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // @Override
    // protected void onResume() {}
}