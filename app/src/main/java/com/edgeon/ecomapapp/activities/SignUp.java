package com.edgeon.ecomapapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edgeon.ecomapapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_up)
    Button signUp;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loader = new ProgressDialog(SignUp.this);
        loader.setCancelable(false);
        loader.setMessage("Signing up...");

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }

    private void signUp() {

        firebaseAuth
                .createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loader.dismiss();
                        if (task.isSuccessful()) {
                            Log.i("LOG", "User registered");

                            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(email.getText().toString().trim());

                            Intent i = new Intent(SignUp.this, Map.class);
                            startActivity(i);
                            finishAffinity();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setMessage(task.getException().getMessage());
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();


                        }

                    }
                });

    }

    private void checkIfuserExists() {
        loader.show();
        databaseReference.child("users").orderByChild("email").equalTo(email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loader.dismiss();
                if (dataSnapshot.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("The email address is already in use by another account.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    signUp();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loader.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setMessage("There was an error. Try again later.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

    }


    @OnClick(R.id.sign_up)
    public void onViewClicked() {

        if (email.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
            Toast.makeText(this, "Please fill up both fields", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            checkIfuserExists();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;


            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
