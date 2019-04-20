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

import java.util.HashMap;

public class MatchProfileActivity extends AppCompatActivity {

    private TextView matchName;
    private TextView mondayArrival;
    private TextView tuesdayArrival;
    private TextView wednesdayArrival;
    private TextView thursdayArrival;
    private TextView fridayArrival;
    private TextView mondayDeparture;
    private TextView tuesdayDeparture;
    private TextView wednesdayDeparture;
    private TextView thursdayDeparture;
    private TextView fridayDeparture;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);

        matchName = findViewById(R.id.matchName);
        mondayArrival = findViewById(R.id.mondayArrivalMatch);
        tuesdayArrival = findViewById(R.id.tuesdayArrivalMatch);
        wednesdayArrival = findViewById(R.id.wednesdayArrivalMatch);
        thursdayArrival = findViewById(R.id.thursdayArrivalMatch);
        fridayArrival = findViewById(R.id.fridayArrivalMatch);
        mondayDeparture = findViewById(R.id.mondayDepartureMatch);
        tuesdayDeparture = findViewById(R.id.tuesdayDepartureMatch);
        wednesdayDeparture = findViewById(R.id.wednesdayDepartureMatch);
        thursdayDeparture = findViewById(R.id.thursdayDepartureMatch);
        fridayDeparture = findViewById(R.id.fridayDepartureMatch);

        final DocumentReference docRef = db.collection("users").document(getIntent().getStringExtra("matchID"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // document.getData();
                        // TODO: Populate the profile
                        matchName.setText(document.getData().get("name").toString());

                        HashMap<String, String> schedule = (HashMap<String, String>) document.getData().get("schedule");
                        mondayArrival.setText(schedule.get("monday").split("-")[0]);
                        mondayDeparture.setText(schedule.get("monday").split("-")[1]);

                        tuesdayArrival.setText(schedule.get("tuesday").split("-")[0]);
                        tuesdayDeparture.setText(schedule.get("tuesday").split("-")[1]);

                        wednesdayArrival.setText(schedule.get("wednesday").split("-")[0]);
                        wednesdayDeparture.setText(schedule.get("wednesday").split("-")[1]);

                        thursdayArrival.setText(schedule.get("thursday").split("-")[0]);
                        thursdayDeparture.setText(schedule.get("thursday").split("-")[1]);

                        fridayArrival.setText(schedule.get("friday").split("-")[0]);
                        fridayDeparture.setText(schedule.get("friday").split("-")[1]);
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
