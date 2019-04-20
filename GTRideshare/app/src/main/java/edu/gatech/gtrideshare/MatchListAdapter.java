package edu.gatech.gtrideshare;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
    private static List<UserData> mDataset;
    private UserData mUser;
    private static Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvSchedule;
        public TextView tvDistance;

        public MyViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.listName);
            tvSchedule = (TextView) v.findViewById(R.id.listSchedule);
            tvDistance = (TextView) v.findViewById(R.id.listDistance);

        }
    }

    public static List getDataSet() {return mDataset;}

    // Constructor for user list
    public MatchListAdapter(List<UserData> myDataset, UserData currentUser, Context context) {
        mDataset = myDataset;
        mUser = currentUser;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MatchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_content, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tvName.setText(mDataset.get(position).fullname);
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List dataset = MatchListAdapter.getDataSet();
                UserData match = (UserData) dataset.get(position);
                Intent matchProfileIntent = new Intent(context, MatchProfileActivity.class);
                matchProfileIntent.putExtra("matchID", match.id);
                v.getContext().startActivity(matchProfileIntent);
            }
        });

        String percentMatch = calcSchedulePercentage(position);
        //TODO complete pinning functionality
        holder.tvSchedule.setText("schedule match: " + percentMatch + "%");
        holder.tvDistance.setText("distance from you: " + calcDistanceToMatch(position)); //TODO: make a function that calculates distance


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String calcSchedulePercentage(int position) {
        HashMap<String, String> userSchedule = mUser.schedule;
        HashMap<String, String> matchSchedule = mDataset.get(position).schedule;
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

        return String.format("%.1f", (matchedTimes / totalTimes * 100));
    }

    private String calcDistanceToMatch(int position) {
        GeoPoint userLocation = mUser.location;
        double userLat = userLocation.getLatitude();
        double userLng = userLocation.getLongitude();

        GeoPoint matchLocation = mDataset.get(position).location;
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

        return String.format("%.2f miles", distance / 1609.344);
    }

}