package edu.gatech.gtrideshare;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;
import java.util.HashMap;

public class UserData {
    public String fullname;
    public GeoPoint location;
    public HashMap<String, String> schedule;
    public String seats;
    public boolean willingToDrive;

    public UserData() {

    }

    public UserData(Map<String, Object> data) {
        this.fullname = (String) data.get("name");
        this.location = (GeoPoint) data.get("location");
//        this.schedule = (HashMap<String, String>) data.get("schedule");
//        this.seats = (String) data.get("seats").toString();
        this.willingToDrive = (boolean) data.get("willingToDrive");
    }
}
