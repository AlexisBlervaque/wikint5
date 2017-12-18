package com.example.alexisblervaque.wikint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Handler;

public class ModifyAsso extends AppCompatActivity {

    private EditText nameModifyAsso;
    private EditText presidentModifyAsso;
    private EditText localModifyAsso;
    private EditText descriptionModifyAsso;
    private ImageView logoImage;
    private Button logoButton;
    private ImageView coverImage;
    private Button coverButton;
    private Bitmap logoBitMap;
    private Bitmap coverBitMap;


    private int PICK_LOGO_REQUEST = 1;
    private int PICK_COVER_REQUEST = 2;

    private StorageReference mStorageRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_asso);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        Bundle bundle = getIntent().getBundleExtra("Association");
        final Association asso = bundle.getParcelable("Asso");
        asso.setId_Events(getIntent().getIntegerArrayListExtra("id_Events"));


        nameModifyAsso = (EditText)findViewById(R.id.nameModifyAsso);
        nameModifyAsso.setText(asso.getName());

        presidentModifyAsso = (EditText)findViewById(R.id.presidentModifyAsso);
        presidentModifyAsso.setText(asso.getPresident());

        localModifyAsso = (EditText) findViewById(R.id.localModifyAsso);
        localModifyAsso.setText(asso.getLocal());

        descriptionModifyAsso = (EditText) findViewById(R.id.descriptionModifyAsso);
        descriptionModifyAsso.setText(asso.getDescription());

        Button applyModifyAsso = (Button) findViewById(R.id.applyModifyAsso);
        applyModifyAsso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logoBitMap != null)
                {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    logoBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference mLogoRef = mStorageRef.child(asso.getPictures().get(0) + ".png");
                    UploadTask uploadTask = mLogoRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("Failed : ", exception.getLocalizedMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Success : ", "______________________________SUCESS____________________________________");

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
                }
                if (coverBitMap != null)
                {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    coverBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference mLogoRef = mStorageRef.child(asso.getPictures().get(1) + ".png");
                    UploadTask uploadTask = mLogoRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        }
                    });
                }


                asso.setName(nameModifyAsso.getText().toString());
                asso.setPresident(presidentModifyAsso.getText().toString());
                asso.setLocal(localModifyAsso.getText().toString());
                asso.setDescription(descriptionModifyAsso.getText().toString());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                myRef.child("Associations").child(String.valueOf(asso.getId())).setValue(asso);
                ModifyAsso.super.onBackPressed();
            }
        });

        Button dontApply = (Button) findViewById(R.id.dontApplyModifyAsso);
        dontApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyAsso.super.onBackPressed();
            }
        });


        logoImage = (ImageView)findViewById(R.id.imageLogoModifyAsso);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(asso.getPictures().get(0) + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(ModifyAsso.this).load(uri.toString()).into(logoImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        logoButton = (Button)findViewById(R.id.buttonLogoModifyAsso);
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_LOGO_REQUEST);
            }
        });


        coverImage = (ImageView)findViewById(R.id.imageCoverModifyAsso);
        storageReference = FirebaseStorage.getInstance().getReference(asso.getPictures().get(1) + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(ModifyAsso.this).load(uri.toString()).into(coverImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        coverButton = (Button)findViewById(R.id.buttonCoverModifyAsso);
        coverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_COVER_REQUEST);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_LOGO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                logoBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                logoImage.setImageBitmap(logoBitMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_COVER_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                coverBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                coverImage.setImageBitmap(coverBitMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
