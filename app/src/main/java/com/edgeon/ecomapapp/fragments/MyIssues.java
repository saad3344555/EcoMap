package com.edgeon.ecomapapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.activities.IssueDetails;
import com.edgeon.ecomapapp.adapters.HomeAdapter;
import com.edgeon.ecomapapp.models.IssueObj;
import com.edgeon.ecomapapp.utils.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SAQ on 10/12/2017.
 */

public class MyIssues extends Fragment {
    View rootView;
    @BindView(R.id.myissues_recycler)
    RecyclerView myissuesRecycler;
    Unbinder unbinder;
    HomeAdapter homeAdapter;
    LinearLayoutManager myIssueLayout;
    ArrayList<IssueObj> myIssueArray = new ArrayList<IssueObj>();
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog loader;
    FirebaseUser firebaseUser;


    String[] types = {"All", "Solid Waste", "Sewage", "Encroachments", "Tree Cutting", "Other"};
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.issue_type)
    Spinner issueType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_myissues, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_bg2, R.id.issue_desc, types);
        issueType.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.single_text_recycler_row);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase User
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        myIssueLayout = new LinearLayoutManager(getActivity());
        myissuesRecycler.setLayoutManager(myIssueLayout);

        homeAdapter = new HomeAdapter(getActivity(), myIssueArray);
        myissuesRecycler.setAdapter(homeAdapter);

        myissuesRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                IssueObj issueObj = myIssueArray.get(position);
                Intent detailsIntent =new Intent(getActivity(), IssueDetails.class);
                detailsIntent.putExtra("Image", issueObj.getPicture());
                detailsIntent.putExtra("Area",issueObj.getIssueArea());
                detailsIntent.putExtra("Lat",Double.valueOf(issueObj.getLat()));
                detailsIntent.putExtra("Long",Double.valueOf(issueObj.getLng()));
                detailsIntent.putExtra("Type",issueObj.getIssueType());
                detailsIntent.putExtra("Description",issueObj.getDescription());
                startActivity(detailsIntent);
            }
        }));

        loadData();

        issueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    loadData();
                } else {
                    loadDataFilter(types[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    private void loadDataFilter(final String type) {
        myIssueArray.clear();
        homeAdapter.refresh(myIssueArray);
        databaseReference.child("issues").orderByChild("user").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                myIssueArray.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                        if (currentSnapshot.child("issueType").getValue(String.class).equals(type)){
                            myIssueArray.add(currentSnapshot.getValue(IssueObj.class));
                            homeAdapter.refresh(myIssueArray);
                        }
                    }
                } else {
                    homeAdapter.refresh(myIssueArray);
                    Toast.makeText(getActivity(), "No issues available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadData() {
        myIssueArray.clear();
        homeAdapter.refresh(myIssueArray);
        databaseReference.child("issues").orderByChild("user").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                myIssueArray.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                        myIssueArray.add(currentSnapshot.getValue(IssueObj.class));
                        homeAdapter.refresh(myIssueArray);
                    }
                } else {
                    homeAdapter.refresh(myIssueArray);
                    Toast.makeText(getActivity(), "No issues available", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
