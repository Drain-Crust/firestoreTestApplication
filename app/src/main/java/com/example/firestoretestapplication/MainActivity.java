package com.example.firestoretestapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseFirestore firebaseFirestore;

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //s
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);

        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void save(View view){
        save();
    }

    private void save(){
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);

        firebaseFirestore.collection("NoteBook").document("My first note").set(note)
                .addOnSuccessListener(unused -> Log.d(TAG, "isSuccessful"))
                .addOnFailureListener(e -> Log.d(TAG, "isFailure: " + e.toString()));
    }
}