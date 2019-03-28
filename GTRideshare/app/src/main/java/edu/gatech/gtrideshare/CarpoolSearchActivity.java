package edu.gatech.gtrideshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

public class CarpoolSearchActivity extends AppCompatActivity {

    private Spinner spinnerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool_search);
    }
}
