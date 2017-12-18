package com.example.alexisblervaque.wikint;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 14/12/2017.
 */

@SuppressLint("ValidFragment")
public class ManageAssoEvent extends Fragment {
    View view;
    Data data;
    private User user;
    private LinearLayout AssoContainer;


    @SuppressLint("ValidFragment")
    public ManageAssoEvent(Data data, User user)
    {
        this.data = data;
        this.user = user;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manage_asso_event,container,false);



        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            /*String value = dataSnapshot.getValue(String.class);
            Log.d(TAG, "Value is: " + value);*/
                data = new Data();

                ArrayList<Association> assoResult = new ArrayList<>();
                ArrayList<Event> eventResult = new ArrayList<>();


                DataSnapshot snapAsso = dataSnapshot.child("Associations");
                for (DataSnapshot ds: snapAsso.getChildren())
                {
                    Association asso = new Association(ds);
                    assoResult.add(asso);
                }

                DataSnapshot snapEvent = dataSnapshot.child("Events");
                for (DataSnapshot ds: snapEvent.getChildren())
                {
                    Event event = new Event(ds);
                    eventResult.add(event);
                }

                data.setAssociations(assoResult);
                data.setEvents(eventResult);

                TextView indication = (TextView) view.findViewById(R.id.indicationAdd);
                ArrayList<Association> associations = data.getAssociations();
                AssoContainer = (LinearLayout) view.findViewById(R.id.associationAccess);

                if (user.getAssociationsId().get(0) == -1)
                {
                    indication.setText("Vous n'avez accès à aucune association.");
                }
                else
                {
                    AssoContainer.removeAllViews();
                    indication.setText("Voici les associations auxquelles vous avez accès");
                    for (int i : user.getAssociationsId())
                    {
                        LinearLayout linear = createLinear(associations.get(i),view.getContext());
                        AssoContainer.addView(linear);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        TextView indication = (TextView) view.findViewById(R.id.indicationAdd);
        ArrayList<Association> associations = data.getAssociations();
        AssoContainer = (LinearLayout) view.findViewById(R.id.associationAccess);

        if (user.getAssociationsId().get(0) == -1)
        {
            indication.setText("Vous n'avez accès à aucune association.");
            return view;
        }
        else
        {
            indication.setText("Voici les associations auxquelles vous avez accès");
            for (int i : user.getAssociationsId())
            {
                LinearLayout linear = createLinear(associations.get(i),view.getContext());
                AssoContainer.addView(linear);
            }
            return view;

        }






    }


    private LinearLayout createLinear(final Association asso, Context context)
    {

        //Container
        LinearLayout result = new LinearLayout(context);
        LinearLayout.LayoutParams paramsResult = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsResult.setMargins(10,10,10,10);
        result.setLayoutParams(paramsResult);
        result.setBackground(context.getResources().getDrawable(R.drawable.border));
        result.setOrientation(LinearLayout.VERTICAL);


        TextView nameOfTheAsso = new TextView(context);
        LinearLayout.LayoutParams paramsNameOfTheAsso = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsNameOfTheAsso.gravity = Gravity.CENTER;
        nameOfTheAsso.setLayoutParams(paramsNameOfTheAsso);
        nameOfTheAsso.setGravity(Gravity.CENTER);
        nameOfTheAsso.setPadding(20,20,20,20);
        nameOfTheAsso.setText(asso.getName());



        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsButton.setMargins(5,5,5,5);

        Button modifyAsso = new Button(context);
        modifyAsso.setLayoutParams(paramsButton);
        modifyAsso.setText("Modifier l'association");
        modifyAsso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ModifyAsso.class);
                Bundle b = new Bundle();
                b.putParcelable("Asso",asso);
                i.putExtra("Association", b);
                i.putExtra("id_Events", asso.getId_Events());
                startActivity(i);
            }
        });



        Button addEvent = new Button(context);
        addEvent.setLayoutParams(paramsButton);
        addEvent.setText("Ajouter un évènement");
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AddEvent.class);
                i.putExtra("nameOfNewEvent",String.valueOf(data.getEvents().size()));
                startActivity(i);
            }
        });


        LinearLayout modifyEventContainer = new LinearLayout(context);
        modifyEventContainer.setLayoutParams(paramsButton);

        Spinner spinnerEvent = new Spinner(context);
        LinearLayout.LayoutParams paramsSpinnerEvent = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsSpinnerEvent.weight = (float)0.65;
        spinnerEvent.setLayoutParams(paramsSpinnerEvent);
        ArrayList<Event> spinnerArray = new ArrayList<>();
        for (int i: asso.getId_Events())
        {
            spinnerArray.add(data.getEvents().get(i));
        }
        ArrayAdapter<Event> spinnerArrayAdapter = new ArrayAdapter<Event>(context,   android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerEvent.setAdapter(spinnerArrayAdapter);


        Button modifyEvent = new Button(context);
        LinearLayout.LayoutParams paramsModifyEvent = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsModifyEvent.weight = (float)0.35;
        modifyEvent.setLayoutParams(paramsModifyEvent);
        modifyEvent.setText("Modifier");

        modifyEventContainer.addView(spinnerEvent);
        modifyEventContainer.addView(modifyEvent);


        result.addView(nameOfTheAsso);
        result.addView(modifyAsso);
        result.addView(addEvent);
        result.addView(modifyEventContainer);

        return result;
    }

}
