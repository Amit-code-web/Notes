package com.example.amit2.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();//creating a list of notes
    static ArrayAdapter arrayAdapter;//creating arrayAdapter
    //both of the above things are static so that can be used in any of the activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);//getting the list view
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.amit2.notes", Context.MODE_PRIVATE);//getting access to shared preferences
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        //getting the access to the details which were stored in the shared preferences

        if(set == null){
            //if nothing stored previously
            notes.add("Example notes");
        }
        else{
            //if something stored we will get the access and store them in the arrayList
            notes = new ArrayList<>(set);
        }
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);//setting up the arrayAdapter
        listView.setAdapter(arrayAdapter);//setting the arrayAdapter with listView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //creating intent to reach another activity
                Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteId",position);//sending the position in extra
                startActivity(intent);//starting the new activity
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //setting up the Long click Listener

                final int itemToDelete = position;//item to be deleted on Long clicking an event
                new AlertDialog.Builder(MainActivity.this)//here we are setting up the alert dialog box to delete
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Do you want to delete this note ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(itemToDelete);//removing the item
                                arrayAdapter.notifyDataSetChanged();//telling the arrayAdapter that data is changed
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.amit2.notes", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(MainActivity.notes);//data is changed after deletion
                                sharedPreferences.edit().putStringSet("notes",set).apply();//again we will update the shared preference
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//creating the menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);//adding the menu

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //here we move to another activity if any of the menu items is selected
        if(item.getItemId() == R.id.add_note){
            Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
            startActivity(intent);//move to another intent

            return true;
        }
        return false;
    }
}