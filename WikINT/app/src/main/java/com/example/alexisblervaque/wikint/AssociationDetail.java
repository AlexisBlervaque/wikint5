package com.example.alexisblervaque.wikint;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AssociationDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Recovery of data from Association list

        Bundle bundle = getIntent().getBundleExtra("Association");
        Association asso = bundle.getParcelable("Asso");


        // Automaticaly create the template of the association asso

        String name = asso.getName();
        TextView nom_asso = (TextView)findViewById(R.id.nom_asso);
        nom_asso.setText(name);

        String description = asso.getDescription();
        TextView descriptionView = (TextView)findViewById(R.id.description);
        descriptionView.setText(description);

        String president = asso.getPresident();
        TextView pres = (TextView)findViewById(R.id.nom_prez);
        pres.setText(president);

        String local = asso.getLocal();
        TextView emplacement_local = (TextView)findViewById(R.id.lieu_local);
        emplacement_local.setText(local);

        String pp = asso.getPictures().get(0);
        final ImageView photo_profil = (ImageView) findViewById(R.id.profil_picture);
        photo_profil.getBackground().setAlpha(150);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(pp + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(AssociationDetail.this).load(uri.toString()).into(photo_profil);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        String cover = asso.getPictures().get(1);
        final ImageView cover_pict = (ImageView) findViewById(R.id.cover_picture);
        storageReference = FirebaseStorage.getInstance().getReference(cover + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(AssociationDetail.this).load(uri.toString()).into(cover_pict);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        // Attach the different buttons to the different activities

        Button button2 = (Button) findViewById(R.id.button_event);

        Button button = (Button) findViewById(R.id.button_photos);

    }

}
