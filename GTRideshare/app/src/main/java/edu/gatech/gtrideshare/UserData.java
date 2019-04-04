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
    public String id;

    public UserData() {

    }

    public UserData(String id, Map<String, Object> data) {
        this.id = id;
        this.fullname = (String) data.get("name");
        this.location = (GeoPoint) data.get("location");
        //TODO make sure that the location isn't just wherever they signed up but their actual home location
        this.schedule = (HashMap<String, String>) data.get("schedule");
//        this.seats = (String) data.get("seats").toString();
        this.willingToDrive = (boolean) data.get("willingToDrive");
    }
}
