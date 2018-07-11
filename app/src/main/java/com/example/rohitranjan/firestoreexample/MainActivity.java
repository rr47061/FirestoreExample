package com.example.rohitranjan.firestoreexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private  String TAG = "MainActivity";
    private  String KEY_TITLE = "title";
    private  String KEY_DESCRIPTION = "description";

     EditText editTextTitle;
     EditText editTextDescription;
     TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
       

              /*  if(documentSnapshot.exists())
                {
                     first method ...
                    String title = documentSnapshot.getString(KEY_TITLE);
                    String desc = documentSnapshot.getString(KEY_DESCRIPTION);
                    textViewData.setText("title : "+title+"\n Description : "+desc);

                    //second method...
                   Note note= documentSnapshot.toObject(Note.class); // this will automactically create obejct of note class..
                    String title = note.getTitle();
                    String desc = note.getDescription();

                    textViewData.setText("title : "+title+"\n Description : "+desc);

                }
                else {
                    textViewData.setText("");
                }
            }
        });
 */   }

    public void saveNote(View v) {

        String title =  editTextTitle.getText().toString();
        String description =editTextDescription.getText().toString();
/* first method using hashmap
        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION, description);
*/

// Second method using custom class....
        Note note = new Note(title,description);

       // db.collection("Notebook").document("My First Note").set(note)
        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadNote(View v )
    {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists())
                        {
                            /* first method...
                            String title = documentSnapshot.getString(KEY_TITLE);
                            String desc = documentSnapshot.getString(KEY_DESCRIPTION);
                            */
                            // second method using custom class


                            Note note= documentSnapshot.toObject(Note.class); // this will automactically create obejct of note class..
                            String title = note.getTitle();
                            String desc = note.getDescription();

                            textViewData.setText("title : "+title+"\n Description : "+desc);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Document Does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    public void updateDescription(View v)
    {
        String desc = editTextDescription.getText().toString();
        // either this one
        Map<String,Object> note = new HashMap<>();
        note.put(KEY_DESCRIPTION,desc);
        // This will create the field if not exist
       // noteRef.set(note, SetOptions.merge());
       // This will only update the field...
       noteRef.update(note);

        //or this one directly
       // noteRef.update(KEY_DESCRIPTION,desc);

    }

    public void deleteNote(View v)
    {
        noteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                    Toast.makeText(MainActivity.this, "no note to delete", Toast.LENGTH_SHORT).show();
               // else
               // Toast.makeText(MainActivity.this, "note deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "no note to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteDescription(View v)
    {
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public  void addNotes(View v)
    {
        String title =  editTextTitle.getText().toString();
        String description =editTextDescription.getText().toString();
        Note note = new Note(title,description);
        notebookRef.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MainActivity.this, "added note", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void loadNotes(View v)
    {
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for(DocumentSnapshot documentSnapshots : queryDocumentSnapshots )
                        {
                                Note note  = documentSnapshots.toObject(Note.class);
                                String title = note.getTitle();
                                String desc = note.getDescription();
                                data +="title = "+title+"\ndesc = "+desc+"\n\n";

                        }
                        textViewData.setText(data);
                    }
                });

    }
}
