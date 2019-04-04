package edu.gatech.gtrideshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MatchProfileActivity extends AppCompatActivity {

    private TextView matchWelcome;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        matchWelcome = findViewById(R.id.matchWelcome);

        final DocumentReference docRef = db.collection("users").document(getIntent().getStringExtra("matchID"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // document.getData();
                        // TODO: Populate the profile
                        matchWelcome.setText("Welcome to " + document.getData().get("name").toString() + "'s profile");
                    } else {
                        Log.d("matchprofile", "No such document");
                    }
                } else {
                    Log.d("matchprofile", "get failed with ", task.getException());
                }
            }
        });
    }
}
