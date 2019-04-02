package edu.gatech.gtrideshare;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {
    private List<UserData> mDataset;

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

    // Constructor for user list
    public MatchListAdapter(List<UserData> myDataset) {
        mDataset = myDataset;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.tvName.setText(mDataset.get(position).fullname);
        holder.tvSchedule.setText("schedule match%"); //TODO: make a function that calculates % match
        holder.tvDistance.setText("distance from you"); //TODO: make a function that calculates distance
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}