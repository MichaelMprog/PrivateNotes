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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        // sort notes
        if (filter == 1) {           // by title
            Collections.sort(allNotes, new TitleSorter());
        }
        else if (filter == 2) {      // by category
            Collections.sort(allNotes, new CategorySorter());
        }
        else {                      // by date
            Collections.sort(allNotes, new DateSorter());
        }
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

    // -1 for a less than (or before) b
    // 0 for equal
    // 1 for a greater than (or after) b
    public class DateSorter implements Comparator<Note> {
        public int compare(Note o1, Note o2) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date date1 = format.parse(o1.getDate(), new ParsePosition(0));
            Date date2 = format.parse(o2.getDate(), new ParsePosition(0));
            return date1.compareTo(date2);
        }
    }

    public class TitleSorter implements Comparator <Note> {
        public int compare(Note o1, Note o2) {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }

    public class CategorySorter implements Comparator <Note> {
        public int compare(Note o1, Note o2) {
            return o1.getCategory().compareToIgnoreCase(o2.getCategory());
        }
    }

}