package com.edgeon.ecomapapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edgeon.ecomapapp.R;
import com.edgeon.ecomapapp.models.IssueObj;
import com.edgeon.ecomapapp.utils.LocalDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Issue extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.capture_pic)
    TextView capturePic;

    String[] problems = {"Issue Type", "Solid Waste", "Sewage", "Encroachments", "Tree Cutting", "Other"};
    String[] areas = {"Issue Area", "Saddar", "Korangi", "Shah Faisal", "Jamshed", "Gulshan", "North Nazimabad", "New Karachi", "Malir", "Keamari", "Landhi", "Lyari", "Gadap", "Bin Qasim", "SITE", "Baldia", "Gulberg", "Orangi", "Liaquatabad"};
    private static final int CAMERA_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 0;
    @BindView(R.id.issue_type)
    Spinner issueType;
    @BindView(R.id.issue_area)
    Spinner issueArea;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    String toolbarTitle = "<font color=#A6D495>ECO</font><font color=#6B7177>MAP</font>";
    @BindView(R.id.detail)
    EditText detail;
    @BindView(R.id.submit)
    Button submit;
    String lat, lng;
    String issueTypeSelected, issueAreaSelected;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog loader;
    FirebaseUser firebaseUser;
    @BindView(R.id.capture_pic_image)
    ImageView capturePicImage;
    FirebaseStorage storage;
    StorageReference storageRef;
    Bitmap photoBitmap;
    byte[] byteArray;
    int typePos, areaPos;
    String mCurrentPhotoPath;
    public static final int PERMISSION_REQUEST = 2;
    Typeface font, fontBold;
    @BindView(R.id.closeBtn)
    RelativeLayout closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        ButterKnife.bind(this);

        font = Typeface.createFromAsset(getAssets(),
                "fonts/roboto.ttf");
        fontBold = Typeface.createFromAsset(getAssets(),
                "fonts/roboto_bold.ttf");

        detail.setTypeface(font);
        capturePic.setTypeface(font);
        submit.setTypeface(font);


        loader = new ProgressDialog(Issue.this);
        loader.setCancelable(false);

        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(Html.fromHtml(toolbarTitle));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        @SuppressLint("ResourceType") ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_bg, R.id.issue_desc, problems);
        adapter.setDropDownViewResource(R.layout.single_text_recycler_row);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_bg, R.id.issue_desc, areas);
        adapter2.setDropDownViewResource(R.layout.single_text_recycler_row);

        issueType.setAdapter(adapter);
        issueArea.setAdapter(adapter2);

        issueType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                issueTypeSelected = problems[i];
                typePos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        issueArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                issueAreaSelected = areas[i];
                areaPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase User
        firebaseUser = firebaseAuth.getCurrentUser();

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();

        // Initialize Storage Reference
        storageRef = storage.getReference();


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.statistics) {
            Log.d("Status", "Statistics");
            Intent in = new Intent(Issue.this, Statistics.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_solutions) {
            Log.d("Status", "Solutions");
            Intent in = new Intent(Issue.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.my_issues) {
            Log.d("Status", "Issues");
            Intent in = new Intent(Issue.this, Home.class);
            startActivity(in);
            this.finish();
        } else if (item.getItemId() == R.id.logout) {
            Log.d("Status", "Logout");
            AlertDialog.Builder builder = new AlertDialog.Builder(Issue.this);
            builder.setTitle("Log Out");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseAuth.signOut();
                    new LocalDatabase().clear(Issue.this);
                    Intent i = new Intent(Issue.this, Login.class);
                    startActivity(i);
                    finishAffinity();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (item.getItemId() == R.id.report) {
            Log.d("Status", "Report Issue");
            Intent in = new Intent(Issue.this, Map.class);
            startActivity(in);
            this.finish();
        }

        return true;
    }


    @OnClick({R.id.submit, R.id.capture_pic_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
                if (typePos == 0 || areaPos == 0 || detail.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "Please enter details to submit an issue.", Toast.LENGTH_SHORT).show();
                } else if (byteArray == null) {
                    Toast.makeText(this, "Please upload a picture to submit an issue.", Toast.LENGTH_SHORT).show();
                } else {
                    loader.show();
                    uploadImage(byteArray);
                }
                break;
            case R.id.capture_pic_image:
                AlertDialog.Builder builder = new AlertDialog.Builder(Issue.this);
                builder.setTitle("Capture Picture");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(Issue.this, new String[]{Manifest.permission.CAMERA},
                                        PERMISSION_REQUEST);
                            } else {
                                dispatchTakePictureIntent();
                            }
                        } else {
                            dispatchTakePictureIntent();
                        }
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
                builder.show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                dispatchTakePictureIntent();
            }
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.edgeon.ecomapapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            File f = new File(mCurrentPhotoPath);
            decodeUri(Uri.fromFile(f));
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            decodeUri(data.getData());
        }


    }

    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 800;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            photoBitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            assert photoBitmap != null;
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byteArray = stream.toByteArray();
            capturePic.setText("Picture selected.\nTap Submit to upload.");
            //uploadImage(byteArray);


        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }

    private void uploadImage(byte[] data) {
        loader.setMessage("Uploading picture...");
        String refString = new Date().toString() + "-" + issueTypeSelected + "-" + issueAreaSelected + "-" + firebaseAuth.getCurrentUser().getUid() + "issue.jpg";
        StorageReference picImagesRef = storageRef.child("issues/" + refString);

        UploadTask uploadTask = picImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                loader.dismiss();
                Toast.makeText(Issue.this, "There was an error. Try again.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                loader.setMessage("Submitting issue...");
                databaseReference.child("issues").push().setValue(new IssueObj(detail.getText().toString(), issueTypeSelected, issueAreaSelected, lat, lng, firebaseAuth.getCurrentUser().getUid(), String.valueOf(downloadUrl))).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loader.dismiss();
                        Toast.makeText(Issue.this, "Issue submitted", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(Issue.this, Home.class);
                        startActivity(in);
                        finish();
                    }
                });
            }
        });

    }

    @OnClick(R.id.closeBtn)
    public void onViewClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);
    }
}
