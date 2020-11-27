package com.example.privatenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int filter;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Adapter adapter;
    TextView noItemText;
    NoteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filter = 0;

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        noItemText = findViewById(R.id.noItemText);
        db = new NoteDatabase(this);
        List<Note> allNotes = db.getAllNotes();
        recyclerView = findViewById(R.id.listOfNotes);

        if(allNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }
        else {
            noItemText.setVisibility(View.GONE);
            displayList(allNotes);
        }
    }

    private void displayList(List<Note> allNotes) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,allNotes);
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
        // sort button
        if (item.getItemId() == R.id.sortDate) {
            Toast.makeText(this, "Sort by Date", Toast.LENGTH_SHORT).show();
            filter = 0;
        }
        else if (item.getItemId() == R.id.sortTitle) {
            Toast.makeText(this, "Sort by Title", Toast.LENGTH_SHORT).show();
            filter = 1;
        }
        else if (item.getItemId() == R.id.sortCategory) {
            Toast.makeText(this, "Sort by Category", Toast.LENGTH_SHORT).show();
            filter = 2;
        }

        // add note button
        if(item.getItemId() == R.id.add) {
            Toast.makeText(this, "Add New Note.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Note> getAllNotes = db.getAllNotes();
        if(getAllNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }else {
            noItemText.setVisibility(View.GONE);
            displayList(getAllNotes);
        }


    }
}