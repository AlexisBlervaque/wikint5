package com.example.alexisblervaque.wikint;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private User user;
    private boolean firstAppearing = true;
    private Data data;
    private ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser userFire = mAuth.getCurrentUser();
        userID = userFire.getUid();
        data = new Data();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (userFire != null) {
                    // User is signed in
                    Log.d(TAG,"___________CONNECTER______________________________");
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


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

                DataSnapshot snapUser = dataSnapshot.child("Users");
                for (DataSnapshot ds : snapUser.getChildren())
                {
                    User user = new User(ds);
                    userArrayList.add(user);
                }

                data.setAssociations(assoResult);
                data.setEvents(eventResult);

                EventList myFragmentEventList = (EventList) getFragmentManager().findFragmentByTag("0");
                if (myFragmentEventList != null && myFragmentEventList.isVisible()) {
                    if (firstAppearing)
                    {
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, new EventList(data, userArrayList),"0").commit();
                        firstAppearing = false;
                    }
                }
                AssociationList myFragmentAssociationList = (AssociationList) getFragmentManager().findFragmentByTag("1");
                if (myFragmentAssociationList != null && myFragmentAssociationList.isVisible()) {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });


        Bundle bundle = getIntent().getBundleExtra("userBundle");
        user = bundle.getParcelable("user");
        user.setAssociationsId(getIntent().getIntegerArrayListExtra("listAssociation"));





      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout navHeader = (LinearLayout)navigationView.getHeaderView(0);

        TextView nameNavigation = (TextView)navHeader.getChildAt(1);
        nameNavigation.setText(user.getFirstName() + " " + user.getLastName());
        TextView mailNavigation = (TextView)navHeader.getChildAt(2);
        mailNavigation.setText(mAuth.getCurrentUser().getEmail());




        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new EventList(data, userArrayList),"0").commit();

    }


    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("Ceci va vous déconnecter, êtes vous sûr?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Rester connecté", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        builder1.setNegativeButton("Me déconnecter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                mAuth.signOut();
                MainActivity.super.onBackPressed();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.events) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EventList(data, userArrayList),"0").commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AssociationList(data),"1").commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new UserAccount(),"2").commit();
        } else if (id == R.id.nav_manage) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ManageAssoEvent(data,user),"3").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
