package edu.gatech.gtrideshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CarpoolSearchActivity extends AppCompatActivity {

    private Spinner spinnerOptions;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<UserData> users;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_search);

        users = new ArrayList<>();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("CarpoolSearch", document.getId() + " " + document.getData());
                        users.add(new UserData(document.getData()));
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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MatchListAdapter(users);
        recyclerView.setAdapter(mAdapter);
    }
}
