package com.example.alexisblervaque.wikint;

import android.annotation.SuppressLint;
import android.app.Fragment;
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
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 14/12/2017.
 */

@SuppressLint("ValidFragment")
public class AssociationList extends Fragment {

    private GridLayout AssoContainer;
    private Data data;
    private View view;
    private LinearLayout isLoadingLinear;
    private ScrollView dataLoaded;


    @SuppressLint("ValidFragment")
    public AssociationList(Data data)
    {
        this.data = data;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.association_list,container,false);


        ArrayList<Association> associations = data.getAssociations();

        AssoContainer = (GridLayout)view.findViewById(R.id.AssoContainer);


        isLoadingLinear = (LinearLayout)view.findViewById(R.id.isLoadingAssoLinear);
        dataLoaded = (ScrollView)view.findViewById(R.id.dataLoadedAsso);


        if (associations.size()!=0)
        {
            isLoadingLinear.setVisibility(View.GONE);
            dataLoaded.setVisibility(View.VISIBLE);
            AssoContainer.setRowCount(associations.size()/2 + associations.size()%2);
            int i = 0;
            for (Association asso : associations)
            {
                GridLayout.Spec titleTxtSpecColumn = GridLayout.spec(i%2,GridLayout.FILL);
                GridLayout.Spec titleRowSpec = GridLayout.spec(i/2);
                FrameLayout frame = createFrame(asso,view.getContext());
                AssoContainer.addView(frame, new GridLayout.LayoutParams(titleRowSpec , titleTxtSpecColumn) );
                i++;
            }
        }
        else
        {
            isLoadingLinear.setVisibility(View.GONE);
            dataLoaded.setVisibility(View.VISIBLE);
        }



        return view;
    }


    private FrameLayout createFrame(final Association asso, final Context context)
    {
        FrameLayout result = new FrameLayout(context);
        result.setLayoutParams(new FrameLayout.LayoutParams(AssoContainer.getWidth()/2, 350));

        ImageButton button = new ImageButton(context);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setPadding(5,5,5,5);
        button.setImageResource(R.drawable.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AssociationDetail.class);
                Bundle b = new Bundle();
                b.putParcelable("Asso",asso);
                i.putExtra("Association", b);
                startActivity(i);

            }
        });

        final ImageView image = new ImageView(context);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(asso.getPictures().get(0) + ".png");
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

        LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        imageParam.gravity = Gravity.CENTER;
        image.setLayoutParams(imageParam);
        // Reference to an image file in Cloud Storage
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setPadding(10,30,10,10);




        TextView text = new TextView(context);
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        textParam.gravity = Gravity.BOTTOM;
        text.setLayoutParams(textParam);
        text.setPadding(10,10,10,10);
        text.setText(asso.getName());
        text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);


        result.addView(button);
        result.addView(image);
        result.addView(text);


        return result;
    }


}
