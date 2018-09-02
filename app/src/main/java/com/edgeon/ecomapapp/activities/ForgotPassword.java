package com.edgeon.ecomapapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.edgeon.ecomapapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.submit)
    Button submit;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Forgot password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loader = new ProgressDialog(ForgotPassword.this);
        loader.setCancelable(false);
        loader.setMessage("Sending email...");

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        loader.show();
        firebaseAuth.sendPasswordResetEmail(email.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loader.dismiss();
                        if (task.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
                            builder.setMessage("Email sent for password reset.\n");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            builder.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
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
