package com.example.alexisblervaque.navigationtool2;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by alexisblervaque on 13/12/2017.
 */

@SuppressLint("ValidFragment")
public class EventList extends Fragment{

    View myView;
    LinearLayout linearContent;
    View container;

    @SuppressLint("ValidFragment")
    public EventList(DecodeJson decodeJson) {
        this.decodeJson = decodeJson;
    }

    DecodeJson decodeJson;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        myView = inflater.inflate(R.layout.event, container, false);



        linearContent = (LinearLayout) myView.findViewById(R.id.eventContainer);

        ArrayList<Event> listeEvent = decodeJson.getEvents();



        for (Event event : listeEvent)
        {
            LinearLayout linear = eventLayout(event, decodeJson);
            linearContent.addView(linear);
        }

        return myView;
    }



    private LinearLayout eventLayout(final Event event, final DecodeJson JsonData)
    {
        LinearLayout result = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams paramsFirst = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsFirst.setMargins(0,0,0,20);
        result.setLayoutParams(paramsFirst);
        //result.setBackgroundResource(R.drawable.border);
        result.setOrientation(LinearLayout.VERTICAL);



        //Head of the event
        LinearLayout headLayout = new LinearLayout(container.getContext());
        headLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
        headLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Button logo for the head
        ImageButton logoButton = new ImageButton(container.getContext());
        logoButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        logoButton.setBackgroundColor(Color.TRANSPARENT);
        logoButton.setPadding(15,15,15,15);
        int imageLogoID = getResources().getIdentifier(JsonData.getAssociations().get(event.getId_association()).getPictures().get(0),"drawable", container.getContext().getPackageName());
        logoButton.setImageResource(imageLogoID);

        /*logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),AssociationDetail.class);
                Bundle b = new Bundle();
                Association asso = JsonData.getAssociations().get(event.getId_association());
                b.putParcelable("Asso",asso);
                i.putExtra("Association", b);
                startActivity(i);
            }
        });
*/

        //Description of the head
        LinearLayout descriptionHead = new LinearLayout(container.getContext());
        descriptionHead.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
        descriptionHead.setOrientation(LinearLayout.VERTICAL);
        descriptionHead.setPadding(5,5,5,5);

        TextView typeOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,40);
        param1.gravity = Gravity.CENTER;
        typeOfEvent.setLayoutParams(param1);
        typeOfEvent.setGravity(Gravity.CENTER_HORIZONTAL);

        String associationName = JsonData.getAssociations().get(event.getId_association()).getName();
        String type = "Evenement de " + associationName;
        typeOfEvent.setText(type);

        TextView nameOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param2= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,30);
        param2.gravity = Gravity.CENTER;
        nameOfEvent.setLayoutParams(param2);
        nameOfEvent.setGravity(Gravity.CENTER_HORIZONTAL);
        nameOfEvent.setText(event.getName());

        TextView dateOfEvent = new TextView(container.getContext());
        LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        ImageView image = new ImageView(container.getContext());
        LinearLayout.LayoutParams paramImage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
        paramImage.setMargins(20,0,20,0);
        image.setLayoutParams(paramImage);
        int idOfImage = getResources().getIdentifier(event.getImages().get(0),"drawable",container.getContext().getPackageName());
        image.setImageResource(idOfImage);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // Buttons for the event
        LinearLayout buttonLinear = new LinearLayout(container.getContext());
        buttonLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonLinear.setPadding(10,10,10,10);

        Button buttonNotification = new Button(container.getContext());
        buttonNotification.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonNotification.setGravity(Gravity.CENTER);
        buttonNotification.setText("Prévenir des ami(e)s");

        Button buttonDetail = new Button(container.getContext());
        buttonDetail.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        buttonDetail.setGravity(Gravity.CENTER);
        buttonDetail.setText("Détails");

/*        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),EventDetail.class);
                Bundle b = new Bundle();
                b.putParcelable("event",event);
                Association asso = JsonData.getAssociations().get(event.getId_association());
                b.putParcelable("asso",asso);
                i.putExtra("eventBundle", b);
                i.putExtra("firstDate",event.getFirstDate().getTime());
                i.putExtra("endDate",event.getEndDate().getTime());
                startActivity(i);
            }
        });*/


        buttonLinear.addView(buttonNotification);
        buttonLinear.addView(buttonDetail);



        //Add the view to the result
        result.addView(headLayout);
        result.addView(line);
        result.addView(image);
        result.addView(buttonLinear);


        return result;
    }
}
