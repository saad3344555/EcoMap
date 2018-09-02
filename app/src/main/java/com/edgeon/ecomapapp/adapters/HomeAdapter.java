package com.edgeon.ecomapapp.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.models.IssueObj;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Activity activity;
    ArrayList<IssueObj> obj;


    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeAdapter(Activity activity, ArrayList<IssueObj> obj) {
        this.activity = activity;
        this.obj = new ArrayList<IssueObj>(obj);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.myissues_recycler_row, parent, false);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        viewHolder.description.setText(obj.get(position).getDescription());
        Picasso.with(activity).load(obj.get(position).getPicture()).fit().centerCrop().placeholder(R.drawable.picture).into(viewHolder.image);
        viewHolder.issueType.setText(obj.get(position).getIssueType());
        viewHolder.issueArea.setText(obj.get(position).getIssueArea());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return obj.size();
    }

    public void refresh(ArrayList<IssueObj> data) {
        obj.clear();
        obj.addAll(data);
        notifyDataSetChanged();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView description;
        public ImageView image;
        public TextView issueArea;
        public TextView issueType;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            description = (TextView) itemLayoutView
                    .findViewById(R.id.description);
            image = (ImageView) itemLayoutView
                    .findViewById(R.id.image);
            issueArea = (TextView) itemLayoutView
                    .findViewById(R.id.issueArea);
            issueType = (TextView) itemLayoutView
                    .findViewById(R.id.issueType);

        }
    }

}