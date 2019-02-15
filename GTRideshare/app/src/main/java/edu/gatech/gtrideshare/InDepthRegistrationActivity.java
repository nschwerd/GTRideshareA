package edu.gatech.gtrideshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class InDepthRegistrationActivity extends AppCompatActivity {

    private Button confirmInfoButton;
    private Spinner mondayArrival;
    private Spinner mondayDeparture;
    private Spinner tuesdayArrival;
    private Spinner tuesdayDeparture;
    private Spinner wednesdayArrival;
    private Spinner wednesdayDeparture;
    private Spinner thursdayArrival;
    private Spinner thursdayDeparture;
    private Spinner fridayArrival;
    private Spinner fridayDeparture;

    private EditText fullName;
    private EditText phoneNumber;
    private CheckBox willingToDrive;
    private Spinner numSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_depth_registration);
        confirmInfoButton = findViewById(R.id.confirmInfoButton);
        confirmInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profileIntent);
            }
        });
        mondayArrival = findViewById(R.id.mondayArrival);
        mondayDeparture = findViewById(R.id.mondayDeparture);
        tuesdayArrival = findViewById(R.id.tuesdayArrival);
        tuesdayDeparture = findViewById(R.id.tuesdayDeparture);
        wednesdayArrival = findViewById(R.id.wednesdayArrival);
        wednesdayDeparture = findViewById(R.id.wednesdayDeparture);
        thursdayArrival = findViewById(R.id.thursdayArrival);
        thursdayDeparture = findViewById(R.id.thursdayDeparture);
        fridayArrival = findViewById(R.id.fridayArrival);
        fridayDeparture = findViewById(R.id.fridayDeparture);

        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        willingToDrive = findViewById(R.id.willingToDrive);
        numSeats = findViewById(R.id.numSeats);
    }
}
