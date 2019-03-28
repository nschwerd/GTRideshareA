package edu.gatech.gtrideshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class InDepthRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "InDepthRegistrationActivity";

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    //Location
    private Location location;
    private LocationManager mLocationManager;
    protected static int FINE_LOCATION_REQUEST_CODE = 1354;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_in_depth_registration);

        /* Automatically get the location from the GPS */
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //FINE_Location is a runtine permission so we have to check if its enabled
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            // need to ask for location permissions
            ActivityCompat.requestPermissions( this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION  }, FINE_LOCATION_REQUEST_CODE);
        }

        // use the listener to get updates
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                0, mLocationListener);

        // update our location using gps
        location = mLocationManager.getLastKnownLocation(mLocationManager.GPS_PROVIDER);

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

        confirmInfoButton = findViewById(R.id.confirmInfoButton);
        confirmInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userinfo = new HashMap<>();

                boolean cancel = false;
                View focus = null;
                fullName.setError(null);
                phoneNumber.setError(null);

                if (TextUtils.isEmpty(fullName.getText())) {
                    fullName.setError("Must enter name");
                    focus = fullName;
                    cancel = true;
                }else if (TextUtils.isEmpty(phoneNumber.getText())) {
                    phoneNumber.setError("Must enter phone number");
                    focus = phoneNumber;
                    cancel = true;
                }

                if (cancel) {
                    focus.requestFocus();
                } else {
                    //Put the location in as a GeoPoint
                    userinfo.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));

                    //Continue with other info
                    userinfo.put("name", fullName.getText().toString());
                    userinfo.put("phone", phoneNumber.getText().toString());

                    HashMap<String, String> schedule = getSchedule();

                    userinfo.put("schedule", schedule);
                    userinfo.put("seats", numSeats.getSelectedItem());
                    userinfo.put("willingToDrive", willingToDrive.isChecked());

                    db.collection("users").document(user.getUid()).set(userinfo);

                    Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(profileIntent);
                }

            }
        });
    }

    private HashMap<String, String> getSchedule() {
        HashMap<String, String> schedule = new HashMap<>();
        String mon = mondayArrival.getSelectedItem().toString()
                + " - " + mondayDeparture.getSelectedItem().toString();
        String tues = tuesdayArrival.getSelectedItem().toString()
                + " - " + tuesdayDeparture.getSelectedItem().toString();
        String wed = wednesdayArrival.getSelectedItem().toString()
                + " - " + wednesdayDeparture.getSelectedItem().toString();
        String thurs = thursdayArrival.getSelectedItem().toString()
                + " - " + thursdayDeparture.getSelectedItem().toString();
        String fri = fridayArrival.getSelectedItem().toString()
                + " - " + fridayDeparture.getSelectedItem().toString();

        schedule.put("monday", mon);
        schedule.put("tuesday", tues);
        schedule.put("wednesday", wed);
        schedule.put("thursday", thurs);
        schedule.put("friday", fri);
        return schedule;
    }

    /**
     * This a listener that checks often for location updates
     */
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //when the location changes update lat and long
            Log.v(TAG, "Location updated: " + location.toString());
        }
        @Override public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override public void onProviderEnabled(String provider) { }
        @Override public void onProviderDisabled(String provider) { }
    };

}
