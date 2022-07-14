package com.example.amit2.notes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = findViewById(R.id.editText);
        Intent intent = getIntent();//here we receive the intent
        noteId = intent.getIntExtra("noteId",-1);//receiving the noteId from the intent

        if(noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
            //if intent is sent from the edit text option i.e. on item click listener
        }
        else{
            //if  intent is sent from the menu option of add notes
            MainActivity.notes.add("");//here we add empty string
            noteId = MainActivity.notes.size()-1;//we give the noteId as the number of the newly added string
        }
        editText.addTextChangedListener(new TextWatcher() {//if the text is changed than this listener will work
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                MainActivity.notes.set(noteId,String.valueOf(s));//here we update the text in the notes
                MainActivity.arrayAdapter.notifyDataSetChanged();//here we update the arrayAdapter

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.amit2.notes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes",set).apply();//here we update the shared preferences
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
