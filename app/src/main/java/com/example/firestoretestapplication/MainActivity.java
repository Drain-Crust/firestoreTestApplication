package com.example.firestoretestapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private DocumentReference theDocument;
    Note note;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
        theDocument = firebaseFirestore.collection("NoteBook").document("My first note");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        theDocument.addSnapshotListener(this, (documentSnapshot, error) -> {
            if (error != null) {
                Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error while loading: " + error.toString());
                return;
            }

            assert documentSnapshot != null;
            if (documentSnapshot.exists()) {
                note = documentSnapshot.toObject(Note.class);
                assert note != null;
                textViewData.setText("Title: " + note.getTitle() + "\nDescription: " + note.getDescription());
            } else {
                textViewData.setText("");
            }
        });
    }

    public void save(View view) {
        save();
    }

    public void load(View view) {
        load();
    }

    public void updateDescription(View view) {
        updateDescription();
    }

    public void deleteDescription(View view){
        deleteDescription();
    }

    public void deleteNote(View view){
        deleteNote();
    }

    private void save() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        note = new Note(title, description);

        theDocument.set(note)
                .addOnSuccessListener(unused -> Log.d(TAG, "isSuccessful"))
                .addOnFailureListener(e -> Log.d(TAG, "isFailure: " + e.toString()));
    }

    @SuppressLint("SetTextI18n")
    private void load() {
        theDocument.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        note = documentSnapshot.toObject(Note.class);
                        assert note != null;
                        textViewData.setText("Title: " + note.getTitle() + "\nDescription: " + note.getDescription());
                    } else {
                        Log.d(TAG, "Document does not exist");
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "isFailure: " + e.toString()));
    }

    private void updateDescription() {
        String description = editTextDescription.getText().toString();
        theDocument.update(KEY_DESCRIPTION,description);
    }

    private void deleteDescription(){
        theDocument.update(KEY_DESCRIPTION, FieldValue.delete()); // can add an onSuccessListener or onFailureListener
    }

    private void deleteNote(){
        theDocument.delete();
    }
}