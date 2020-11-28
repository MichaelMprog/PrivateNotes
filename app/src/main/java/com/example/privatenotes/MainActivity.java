package com.example.privatenotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
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

    List <Note> allNotes;


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
        allNotes = db.getAllNotes();
        recyclerView = findViewById(R.id.listOfNotes);

        if(allNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }
        else {
            noItemText.setVisibility(View.GONE);
            displayList();
        }
    }

    private void displayList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // sort notes
        if (filter == 1) {           // by title
            Log.d("COMPARE", "by title " + filter);
            Collections.sort(allNotes, new TitleSorter());
        }
        else if (filter == 2) {      // by category
            Log.d("COMPARE", "by category " + filter);
            Collections.sort(allNotes, new CategorySorter());
        }
        else if (filter == 3){                      // by date
            Log.d("COMPARE", "by dates " + filter);
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
            filter = 3;
            displayList();
        }
        else if (item.getItemId() == R.id.sortTitle) {
            Toast.makeText(this, "Sort by Title", Toast.LENGTH_SHORT).show();
            filter = 1;
            displayList();
        }
        else if (item.getItemId() == R.id.sortCategory) {
            Toast.makeText(this, "Sort by Category", Toast.LENGTH_SHORT).show();
            filter = 2;
            displayList();
        }

        // add note button
        if(item.getItemId() == R.id.add) {
            Toast.makeText(this, "Add New Note.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, AddNote.class);
            startActivity(i);
        }

        /* change password button
        if (item.getItemId() == R.id.changePass) {
            Toast.makeText(this, "Changing password.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, ChangePassword.class);
            startActivity(i);
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allNotes = db.getAllNotes();
        if(allNotes.isEmpty()){
            noItemText.setVisibility(View.VISIBLE);
        }else {
            noItemText.setVisibility(View.GONE);
            displayList();
        }
    }

    // sorting comparators
    public class DateSorter implements Comparator<Note> {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");

        public int compare(Note o1, Note o2) {
            String time1 = o1.getDate() + " " + o1.getTime();
            String time2 = o2.getDate() + " " + o2.getTime();
            Date date1 = format.parse(time1, new ParsePosition(0));
            Date date2 = format.parse(time2, new ParsePosition(0));
            return date1.compareTo(date2);
        }
    }

    public class TitleSorter implements Comparator <Note> {
        public int compare(Note o1, Note o2) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    }

    public class CategorySorter implements Comparator <Note> {
        public int compare(Note o1, Note o2) {
            return o1.getCategory().compareToIgnoreCase(o2.getCategory());
        }
    }

}