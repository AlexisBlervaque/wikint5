package com.example.alexisblervaque.wikint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

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
import java.sql.Time;
import java.util.Date;

public class AddEvent extends AppCompatActivity {

    private EditText nameModifyEvent;
    private EditText descriptionModifyEvent;
    private EditText lieuModifyEvent;
    private ImageView coverEvent;
    private DatePicker dateDebut;
    private TimePicker heureDebut;
    private TimePicker heureFin;
    private Button coverButton;
    private Bitmap coverBitmap;
    private int PICK_COVER_EVENT_REQUEST =1;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        final Event event = new Event();

        dateDebut = (DatePicker)findViewById(R.id.dateDebutPicker);
        heureDebut = (TimePicker)findViewById(R.id.heureDebutPicker);
        heureFin = (TimePicker)findViewById(R.id.heureFinPicker);
        nameModifyEvent = (EditText)findViewById(R.id.nameModifyEvent);
        descriptionModifyEvent = (EditText)findViewById(R.id.descriptionModifyEvent);
        lieuModifyEvent = (EditText) findViewById(R.id.EventLieu);
        descriptionModifyEvent = (EditText)findViewById(R.id.descriptionModifyEvent);

        Button applyModifyEvent = (Button) findViewById(R.id.applyModifyEvent);
        applyModifyEvent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (coverBitmap != null)
                {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    coverBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference mCoverRef = mStorageRef.child("cover_"+nameModifyEvent.getText().toString()+".png");
                    UploadTask uploadTask = mCoverRef.putBytes(data);
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
                Date date_debut_traitement = new Date(dateDebut.getYear(),dateDebut.getMonth(),dateDebut.getDayOfMonth(),heureDebut.getHour(),heureDebut.getMinute());
                Date date_fin_traitement = new Date(dateDebut.getYear(),dateDebut.getMonth(),dateDebut.getDayOfMonth(),heureFin.getHour(),heureFin.getMinute());
                event.setDescription(descriptionModifyEvent.getText().toString());
                event.setLieu(lieuModifyEvent.getText().toString());
                event.setName(nameModifyEvent.getText().toString());
                event.setFirstDate(date_debut_traitement);
                event.setEndDate(date_fin_traitement);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                String nameOfNewEvent = getIntent().getStringExtra("nameOfNewEvent");
                myRef.child("Events").child(nameOfNewEvent).setValue(event);
                AddEvent.super.onBackPressed();
            }
        });


        Button dontApply = (Button) findViewById(R.id.dontApplyModifyEvent);
        dontApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEvent.super.onBackPressed();
            }
        });


        coverEvent = (ImageView)findViewById(R.id.imageModifyEvent);


        coverButton = (Button)findViewById(R.id.buttonImageModifyEvent);
        coverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_COVER_EVENT_REQUEST);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_COVER_EVENT_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                coverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                coverEvent.setImageBitmap(coverBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
