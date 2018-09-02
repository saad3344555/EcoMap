package com.edgeon.ecomapapp.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edgeon.ecomapapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by SAQ on 26/12/2017.
 */

public class BarGraphAdapter extends RecyclerView.Adapter<BarGraphAdapter.ViewHolder>{
    Activity activity;
    List<Long> percentage = new ArrayList<>();
    List<String> area = new ArrayList<>();
    List<String> colors = new ArrayList<>();
    float scale = -1; //for dp of screen
    View itemLayoutView;


    // Provide a suitable constructor (depends on the kind of dataset)
    public BarGraphAdapter(Activity activity, List<Long> percentage,List<String> area) {

        this.activity = activity;
        this.area = area;
        this.percentage = percentage;

        this.colors = Arrays.asList("#4D4D4D","#5DA5DA","#60BD68","#DECF3F","#F15854","#FFFF7271","#FFFEAE72","#FF69C9BE","#FF9BD28A"
        ,"#bf8ad2");

        this.scale = activity.getResources().getDisplayMetrics().density;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public BarGraphAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bar_graph_item, parent, false);

        //create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Random random = new Random();
        int indexColor = (Math.abs(random.nextInt())) % 10;

        Log.d("ColorIndex",String.valueOf(indexColor));

        viewHolder.issueArea.setText(area.get(position));
        viewHolder.issueArea.setTextColor(Color.parseColor(colors.get(indexColor)));

        viewHolder.issuePercentage.setText(percentage.get(position)+"%");
        viewHolder.issuePercentage.setTextColor(Color.parseColor(colors.get(indexColor)));

        int dpWidthInPx  = (int) (30 * scale);
        int dpHeightInPx = (int) (percentage.get(position) * scale);


        viewHolder.bar.getLayoutParams().height = dpHeightInPx;
        viewHolder.bar.getLayoutParams().width = dpWidthInPx;

        viewHolder.bar.setBackgroundColor(Color.parseColor(colors.get(indexColor)));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return percentage.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView issuePercentage;
        public TextView issueArea;
        public ImageView bar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            issuePercentage = itemLayoutView
                    .findViewById(R.id.issuePercentage);
            issueArea = itemLayoutView
                    .findViewById(R.id.issueArea);
            bar = itemLayoutView
                    .findViewById(R.id.bar);

        }
    }

}
