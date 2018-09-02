package com.edgeon.ecomapapp.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgeon.ecomapapp.R;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PieChart extends Fragment {
    View rootView;
    Unbinder unbinder;
    @BindView(R.id.piechart)
    com.github.mikephil.charting.charts.PieChart piechart;
    FirebaseAuth firebaseAuth;
    ProgressDialog loader;
    DatabaseReference databaseReference;
    long tree, other, encroachment, solid, sewage;
    String result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_piegraph, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        loader = new ProgressDialog(getActivity());
        loader.setMessage("Getting data...");
        loader.setCancelable(false);
        loader.show();


// Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

// Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadData();

        return rootView;
    }

    private void loadData() {

        databaseReference.child("issues").orderByChild("issueType").equalTo("Solid Waste").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                solid = dataSnapshot.getChildrenCount();
                databaseReference.child("issues").orderByChild("issueType").equalTo("Sewage").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sewage = dataSnapshot.getChildrenCount();
                        databaseReference.child("issues").orderByChild("issueType").equalTo("Tree Cutting").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                tree = dataSnapshot.getChildrenCount();
                                databaseReference.child("issues").orderByChild("issueType").equalTo("Encroachments").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        encroachment = dataSnapshot.getChildrenCount();
                                        databaseReference.child("issues").orderByChild("issueType").equalTo("Other").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                other = dataSnapshot.getChildrenCount();
                                                setupPieChart();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void setupPieChart() {

        long total = other + solid + tree + sewage + encroachment;
        double solidPerc = solid * 100.0 / total;
        double sewagePerc = sewage * 100.0 / total;
        double treePerc = tree * 100.0 / total;
        double encroachPerc = encroachment * 100.0 / total;
        double otherPerc = other * 100.0 / total;

        List<Float> dataPerc = new ArrayList<>();
        dataPerc.add((float) otherPerc);
        dataPerc.add((float) encroachPerc);
        dataPerc.add((float) sewagePerc);
        dataPerc.add((float) solidPerc);
        dataPerc.add((float) treePerc);

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        for (int i = 0; i < dataPerc.size(); i++) {
            PieEntry pieEntry = new PieEntry(dataPerc.get(i));
            values.add(pieEntry);
        }

        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.other));
        colors.add(getResources().getColor(R.color.enchroachments));
        colors.add(getResources().getColor(R.color.sewage));
        colors.add(getResources().getColor(R.color.solidwaste));
        colors.add(getResources().getColor(R.color.tree));

        // create pie data set
        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueFormatter(new LargeValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (value == 0) {
                    result = "";
                } else {
                    result = String.valueOf(Math.round(value)) + "%";
                }

                return result;
            }
        });

        piechart.setData(data);
        piechart.setDrawHoleEnabled(true);
        piechart.getLegend().setEnabled(false);
        piechart.getDescription().setEnabled(false);
        piechart.setTransparentCircleRadius(5.0f);
        piechart.setTransparentCircleColor(Color.BLACK);

        piechart.setHoleRadius(25);

        piechart.invalidate();

        loader.dismiss();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
