package edu.gatech.gtrideshare;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CarpoolSearchActivity extends AppCompatActivity {

    private Spinner spinnerOptions;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<UserData> users;
    private UserData currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_search);
        spinnerOptions = findViewById(R.id.spinnerOptions);
        spinnerOptions.setSelection(getIntent().getIntExtra("index", 0));
        final int pos = spinnerOptions.getSelectedItemPosition();

        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != pos) {
                    Intent carpoolSearchIntent = new Intent(CarpoolSearchActivity.this, CarpoolSearchActivity.class);
                    carpoolSearchIntent.putExtra("sort", spinnerOptions.getSelectedItem().toString());
                    carpoolSearchIntent.putExtra("index", position);
                    startActivity(carpoolSearchIntent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        users = new ArrayList<>();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("carpoolSearch", user.getUid() + " " + document.getId());

                        if (document.getId().equals(user.getUid())) {
                            currentUser = new UserData(document.getId(), document.getData());
                        } else {
                            users.add(new UserData(document.getId(), document.getData()));
                        }
                    }
                    Log.d("CarpoolSearch", "" + users.size());
                    recyclerView = (RecyclerView) findViewById(R.id.rvMatchList);
//                    recyclerView.setHasFixedSize(true);
                    setupRecyclerView(recyclerView);

                } else {
                    Log.d("CarpoolSearch", "couldn't get users");
                }
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        Collections.sort(users, new Comparator<UserData>() {
            String sort = getIntent().getStringExtra("sort");
            @Override
            public int compare(UserData o1, UserData o2) {
//                if (sort != null)
//                    Log.d("sort", sort);
                if ("Distance".equals(sort)) {
                    return calcDistanceToMatch(o1) > calcDistanceToMatch(o2) ? 1 : -1;

                } else {
                    return calcSchedulePercentage(o1) < calcSchedulePercentage(o2) ? 1 : -1;
                }
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MatchListAdapter(users, currentUser, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }

    private float calcSchedulePercentage(UserData o) {
        HashMap<String, String> userSchedule = currentUser.schedule;
        HashMap<String, String> matchSchedule = o.schedule;
        float matchedTimes = 0;
        int totalTimes = 10;

        for (String day : userSchedule.keySet()) {
            String userDay = userSchedule.get(day);
            String matchDay = matchSchedule.get(day);
            String[] userTimes = userDay.split("-");
            String[] matchTimes = matchDay.split("-");

            if (userTimes[0].equals(matchTimes[0])) {matchedTimes++;}
            if (userTimes[1].equals(matchTimes[1])) {matchedTimes++;}
        }

        return matchedTimes / totalTimes * 100;
    }

    private float calcDistanceToMatch(UserData o) {
        GeoPoint userLocation = currentUser.location;
        double userLat = userLocation.getLatitude();
        double userLng = userLocation.getLongitude();

        GeoPoint matchLocation = o.location;
        double matchLat = matchLocation.getLatitude();
        double matchLng = matchLocation.getLongitude();

        Location locationUser = new Location("point of the user");

        locationUser.setLatitude(userLat);
        locationUser.setLongitude(userLng);

        Location locationMatch = new Location("point of the match");

        locationMatch.setLatitude(matchLat);
        locationMatch.setLongitude(matchLng);

        //distance in meters
        float distance = locationUser.distanceTo(locationMatch);

        return distance;
    }
}
