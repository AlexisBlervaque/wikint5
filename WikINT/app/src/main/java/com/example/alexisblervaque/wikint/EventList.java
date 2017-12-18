package com.example.alexisblervaque.wikint;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexisblervaque on 13/12/2017.
 */

@SuppressLint("ValidFragment")
public class EventList extends Fragment {

    private View myView;
    private LinearLayout linearContent;
    private View container;
    private LinearLayout isLoadingLinear;
    private ScrollView dataLoaded;
    private Data data;
    private ArrayList<User> userArrayList;
    private FirebaseStorage storage;

    @SuppressLint("ValidFragment")
    public EventList(Data data, ArrayList<User> userArrayList) {
        this.data = data;
        this.userArrayList = userArrayList;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        myView = inflater.inflate(R.layout.event, container, false);

        storage = FirebaseStorage.getInstance();

        linearContent = (LinearLayout) myView.findViewById(R.id.eventContainer);

        ArrayList<Event> listeEvent = data.getEvents();


        isLoadingLinear = (LinearLayout)myView.findViewById(R.id.isLoadingLinear);
        dataLoaded = (ScrollView)myView.findViewById(R.id.dataLoaded);


        if (listeEvent.size()!=0)
        {
            isLoadingLinear.setVisibility(View.GONE);
            dataLoaded.setVisibility(View.VISIBLE);
            for (Event event : listeEvent)
            {
                LinearLayout linear = eventLayout(event, data, myView.getContext());
                linearContent.addView(linear);
            }
        }
        else
        {
            isLoadingLinear.setVisibility(View.VISIBLE);
            dataLoaded.setVisibility(View.GONE);
        }


        return myView;
    }



    private LinearLayout eventLayout(final Event event, final Data data, final Context context)
    {
        LinearLayout result = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams paramsFirst = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsFirst.setMargins(0,0,0,20);
        result.setLayoutParams(paramsFirst);
        result.setBackgroundResource(R.drawable.border);
        result.setOrientation(LinearLayout.VERTICAL);



        //Head of the event
        LinearLayout headLayout = new LinearLayout(container.getContext());
        headLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Button logo for the head
        final ImageButton logoButton = new ImageButton(container.getContext());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(data.getAssociations().get(event.getId_association()).getPictures().get(0) + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(context).load(uri.toString()).into(logoButton);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParam.weight = (float)0.3;
        logoButton.setLayoutParams(buttonParam);

        logoButton.setBackgroundColor(Color.TRANSPARENT);
        logoButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        logoButton.setPadding(15,30,15,15);
        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AssociationDetail.class);
                Bundle b = new Bundle();
                Association asso = data.getAssociations().get(event.getId_association());
                b.putParcelable("Asso",asso);
                i.putExtra("Association", b);
                startActivity(i);
            }
        });


        //Description of the head
        LinearLayout descriptionHead = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams descriptionHeadParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionHeadParam.weight = (float)0.7;
        descriptionHead.setLayoutParams(descriptionHeadParam);
        descriptionHead.setOrientation(LinearLayout.VERTICAL);
        descriptionHead.setPadding(5,5,5,5);

        TextView typeOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param1.gravity = Gravity.CENTER;
        typeOfEvent.setLayoutParams(param1);
        typeOfEvent.setGravity(Gravity.CENTER_HORIZONTAL);

        String associationName = data.getAssociations().get(event.getId_association()).getName();
        String type = "Evenement de " + associationName;
        typeOfEvent.setText(type);

        TextView nameOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param2= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param2.gravity = Gravity.CENTER;
        nameOfEvent.setLayoutParams(param2);
        nameOfEvent.setGravity(Gravity.CENTER_HORIZONTAL);
        nameOfEvent.setText(event.getName());

        TextView dateOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param3.setMargins(0,0,8,0);
        param3.gravity = Gravity.END | Gravity.BOTTOM;
        dateOfEvent.setLayoutParams(param3);
        dateOfEvent.setGravity(Gravity.CENTER_HORIZONTAL);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDate.format(event.getFirstDate());
        dateOfEvent.setText(dateString);

        descriptionHead.addView(typeOfEvent);
        descriptionHead.addView(nameOfEvent);
        descriptionHead.addView(dateOfEvent);

        headLayout.addView(logoButton);
        headLayout.addView(descriptionHead);

        // Line in the midle
        LinearLayout line = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams paramLine = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2);
        paramLine.setMargins(40,20,40,20);
        line.setLayoutParams(paramLine);
        line.setBackgroundColor(Color.parseColor("#ff33b5e5"));

        // Image of the event
        final ImageView image = new ImageView(container.getContext());
        storageReference = FirebaseStorage.getInstance().getReference(event.getImages().get(0) + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(context).load(uri.toString()).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        paramImage.setMargins(20,0,20,0);
        image.setLayoutParams(paramImage);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);




        //Notification Container
        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsButton.setMargins(5,5,5,5);
        LinearLayout NotificationContainer = new LinearLayout(context);
        NotificationContainer.setLayoutParams(paramsButton);

        Spinner spinnerUsers = new Spinner(context);
        LinearLayout.LayoutParams paramsSpinnerEvent = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsSpinnerEvent.weight = (float)0.65;
        spinnerUsers.setLayoutParams(paramsSpinnerEvent);
        ArrayList<User> spinnerArray = new ArrayList<>();
        for (User user: userArrayList)
        {
            spinnerArray.add(user);
        }
        ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<User>(myView.getContext(),   android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerUsers.setAdapter(spinnerArrayAdapter);


        Button notifyButton = new Button(context);
        LinearLayout.LayoutParams paramsModifyEvent = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsModifyEvent.weight = (float)0.35;
        notifyButton.setLayoutParams(paramsModifyEvent);
        notifyButton.setText("Notifier");
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId 
            }
        });


        NotificationContainer.addView(spinnerUsers);
        NotificationContainer.addView(notifyButton);


        Button buttonDetail = new Button(container.getContext());
        buttonDetail.setLayoutParams(paramsButton);
        buttonDetail.setGravity(Gravity.CENTER);
        buttonDetail.setText("Détails");

        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EventDetail.class);
                Bundle b = new Bundle();
                b.putParcelable("event",event);
                Association asso = data.getAssociations().get(event.getId_association());
                b.putParcelable("asso",asso);
                i.putExtra("eventBundle", b);
                i.putExtra("firstDate",event.getFirstDate().getTime());
                i.putExtra("endDate",event.getEndDate().getTime());
                startActivity(i);


            }
        });





        //Add the view to the result
        result.addView(headLayout);
        result.addView(line);
        result.addView(image);
        result.addView(NotificationContainer);
        result.addView(buttonDetail);


        return result;
    }
}
