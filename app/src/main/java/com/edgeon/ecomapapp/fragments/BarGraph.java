package com.edgeon.ecomapapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.adapters.BarGraphAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BarGraph extends Fragment {

    View rootView;
    Unbinder unbinder;
    FirebaseAuth firebaseAuth;
    ProgressDialog loader;
    DatabaseReference databaseReference;
//    @BindView(R.id.bargraph)
//    BarChart bargraph;
//    long saddar, korangi, shahfaisal, jamshed, gulshan, northnazimabad, newkarachi, malir, kemari, landhi, lyari, gadap, binqasim, site, baldia, gulberg, orangi, liaquatabad;
//    String[] areas = {"Saddar", "Korangi", "Shah Faisal", "Jamshed", "Gulshan", "North Nazimabad", "New Karachi", "Malir", "Keamari", "Landhi", "Lyari", "Gadap", "Bin Qasim", "SITE", "Baldia", "Gulberg", "Orangi", "Liaquatabad"};

    List<Long> issuePerecentage = new ArrayList<>();
    List<String> issueArea = new ArrayList<>();
    @BindView(R.id.barGraph)
    RecyclerView barGraph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bargraph, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        loader = new ProgressDialog(getActivity());
        loader.setMessage("Getting data...");
        loader.setCancelable(false);
        loader.show();



        setupBarGraph();


// Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

// Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadData();

        return rootView;
    }

    private void loadData() {

        issueArea = Arrays.asList("Saddar", "Korangi", "Shah Faisal", "Jamshed", "Gulshan", "North Nazimabad",
                "New Karachi", "Malir", "Keamari", "Landhi", "Lyari", "Gadap",
                "Bin Qasim", "SITE", "Baldia", "Gulberg", "Orangi", "Liaquatabad");


        databaseReference.child("issues").orderByChild("issueArea").equalTo("Saddar").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                issuePerecentage.add(dataSnapshot.getChildrenCount());
                databaseReference.child("issues").orderByChild("issueArea").equalTo("Korangi").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Shah Faisal").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Jamshed").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Gulshan").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("North Nazimabad").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("New Karachi").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Malir").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Keamari").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Landhi").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Lyari").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Gadap").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Bin Qasim").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("SITE").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                    @Override
                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Baldia").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                            @Override
                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Gulberg").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                                        databaseReference.child("issues").orderByChild("issueArea").equalTo("Orangi").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                                                databaseReference.child("issues").orderByChild("issueArea").equalTo("Liaquatabad").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                        issuePerecentage.add(dataSnapshot.getChildrenCount());
                                                                                                                                                        calculatePercentage();
                                                                                                                                                        barGraph.setAdapter(new BarGraphAdapter(getActivity()
                                                                                                                                                        ,issuePerecentage,issueArea));
                                                                                                                                                        barGraph.invalidate();
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

    private void calculatePercentage(){

        long total = 0;

        for(int i=0;i<issuePerecentage.size();i++){
            total += issuePerecentage.get(i);
        }

        if (total == 0){
            total = 1;
        }

        ArrayList<Long> temp = new ArrayList<>();

        for (int i=0;i<issuePerecentage.size();i++){
            double percent = (float)issuePerecentage.get(i)/(float) total;
            percent *= 100;
            Log.d("Percent",String.valueOf(percent));
            temp.add((long) percent);
        }

        issuePerecentage.clear();

        issuePerecentage = temp;

        Log.d("Percent 1",issuePerecentage.get(0).toString());

        loader.dismiss();

    }

    private void setupBarGraph() {

        BarGraphAdapter barGraphAdapter = new BarGraphAdapter(getActivity(),issuePerecentage,issueArea);
        barGraph.setLayoutManager(new GridLayoutManager(getContext(),5, LinearLayoutManager.VERTICAL,false));
        barGraph.setAdapter(barGraphAdapter);


//        List<BarEntry> entries = new ArrayList<BarEntry>();
//        entries.add(new BarEntry(saddar, 0));
//        entries.add(new BarEntry(korangi, 1));
//        entries.add(new BarEntry(shahfaisal, 2));
//        entries.add(new BarEntry(jamshed, 3));
//        entries.add(new BarEntry(gulshan, 4));
//        entries.add(new BarEntry(northnazimabad, 5));
//        entries.add(new BarEntry(newkarachi, 6));
//        entries.add(new BarEntry(malir, 7));
//        entries.add(new BarEntry(kemari, 8));
//        entries.add(new BarEntry(landhi, 9));
//        entries.add(new BarEntry(lyari, 10));
//        entries.add(new BarEntry(gadap, 11));
//        entries.add(new BarEntry(binqasim, 12));
//        entries.add(new BarEntry(site, 13));
//        entries.add(new BarEntry(baldia, 14));
//        entries.add(new BarEntry(gulberg, 15));
//        entries.add(new BarEntry(orangi, 16));
//        entries.add(new BarEntry(liaquatabad, 17));
//
//        ArrayList<String> labels = new ArrayList<String>();
//        // turn your data into Entry objects
//        labels.addAll(Arrays.asList(areas));

//        BarDataSet barDataSet = new BarDataSet(entries,"");
//        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//
//
//        BarData data = new BarData(barDataSet);
//        bargraph.setData(data); // set the data and list of lables into chart<br />
//
//        bargraph.invalidate();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
