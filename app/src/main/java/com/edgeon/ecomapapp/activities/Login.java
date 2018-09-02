package com.edgeon.ecomapapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.utils.LocalDatabase;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {


    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.createAccount)
    TextView createAccount;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.sign_in)
    ImageView signIn;
    @BindView(R.id.facebookBtn)
    ImageView facebookBtn;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog loader;
    FirebaseUser firebaseUser;
    Typeface font, fontBold;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        email.setTypeface(font);
        password.setTypeface(font);
        forgotPassword.setTypeface(fontBold);
        createAccount.setTypeface(fontBold);

        loader = new ProgressDialog(Login.this);
        loader.setCancelable(false);

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase User
        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseAuth.getCurrentUser() != null) {
            Intent up = new Intent(Login.this, Map.class);
            startActivity(up);
            finish();
        }

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Facebook Login button setup
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButtonSetup(callbackManager);


    }


    private void emailSignIn() {

        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill up both fields", Toast.LENGTH_SHORT).show();

        } else {

            loader.setMessage("Logging in...");
            loader.show();
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            loader.dismiss();
                            if (task.isSuccessful()) {
                                databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        new LocalDatabase().setUserEmail(Login.this, email.getText().toString().trim());

                                        Intent i = new Intent(Login.this, Map.class);
                                        startActivity(i);
                                        finishAffinity();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                        firebaseAuth.signOut();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                        builder.setTitle("Log In");
                                        builder.setMessage(task.getException().getMessage());
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.show();

                                    }
                                });

                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setTitle("Log In");
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

    }


    public void facebookLoginButtonSetup(CallbackManager callbackManager) {
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    checkIfFacebookUserExists(object.getString("email"), loginResult.getAccessToken());
                                } catch (JSONException e) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setTitle("Log In");
                                    builder.setMessage("There was an error. Try again later.");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    builder.show();
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Log In");
                builder.setMessage("There was an error. Try again later.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }

            @Override
            public void onError(FacebookException exception) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Log In");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void checkIfFacebookUserExists(final String email, final AccessToken token) {
        loader.setMessage("Checking account...");
        loader.show();
        databaseReference.child("users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    facebookLogIn(token);
                } else {

                    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                    firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    loader.dismiss();
                                    if (task.isSuccessful()) {
                                        Log.i("LOG", "User registered");

                                        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(email);

                                        Intent i = new Intent(Login.this, Map.class);
                                        startActivity(i);
                                        finishAffinity();
                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Log In");
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

    public void facebookLogIn(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    loader.dismiss();
                                    new LocalDatabase().setUserEmail(Login.this, email.getText().toString().trim());

                                    Intent i = new Intent(Login.this, Map.class);
                                    startActivity(i);
                                    finishAffinity();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    loader.dismiss();
                                    firebaseAuth.signOut();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                    builder.setTitle("Log In");
                                    builder.setMessage(task.getException().getMessage());
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();

                                }
                            });
                        } else {
                            loader.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            builder.setTitle("Log In");
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


    @OnClick({R.id.createAccount, R.id.forgotPassword, R.id.facebookBtn, R.id.sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createAccount:
                Intent up = new Intent(Login.this, SignUp.class);
                startActivity(up);
                break;
            case R.id.forgotPassword:
                Intent fp = new Intent(Login.this, ForgotPassword.class);
                startActivity(fp);
                break;
            case R.id.facebookBtn:
                LoginManager.getInstance().logOut();
                loginButton.performClick();
                break;
            case R.id.sign_in:
                emailSignIn();
                break;
        }
    }
}
