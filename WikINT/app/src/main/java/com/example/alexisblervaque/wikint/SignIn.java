package com.example.alexisblervaque.wikint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by alexisblervaque on 14/12/2017.
 */

public class SignIn extends BaseActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    private static final String TAG = "AnonymousAuth";
    private EditText mLastName;
    private EditText mFirstName;
    private EditText mPasswordField;
    private Spinner mSpinner;
    private ProgressBar progressBar;
    private Button signInButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mSpinner = (Spinner) findViewById(R.id.spinner_scool_login);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scool, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
        progressBar = (ProgressBar)findViewById(R.id.progressBarSignIn);

        mLastName = (EditText) findViewById(R.id.last_name_login);
        mFirstName = (EditText) findViewById(R.id.first_name_login);
        mPasswordField = (EditText) findViewById(R.id.editTextPassword);


        if (mAuth.getCurrentUser()!=null)
        {
            final FirebaseUser userFire = mAuth.getCurrentUser();
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = mFirebaseDatabase.getReference("Users");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    DataSnapshot userData = dataSnapshot.child(userFire.getUid());
                    final User user = new User(userData);

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                    builder1.setMessage("Bienvenu " + user.getFirstName() + " " + user.getLastName());
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "D'accord",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent i = new Intent(SignIn.this,MainActivity.class);
                                    Bundle b = new Bundle();
                                    b.putParcelable("user",user);
                                    i.putExtra("listAssociation", user.getAssociationsId());
                                    i.putExtra("userBundle",b);
                                    startActivity(i);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    signInButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                    builder1.setMessage(error.getMessage());
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "D'accord",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    signInButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }


        signInButton = (Button) findViewById(R.id.button_login);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (mAuth.getCurrentUser() != null) {
                        final FirebaseUser userFire = mAuth.getCurrentUser();
                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = mFirebaseDatabase.getReference("Users");

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                DataSnapshot userData = dataSnapshot.child(userFire.getUid());
                                final User user = new User(userData);

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                                builder1.setMessage("Bienvenu " + user.getFirstName() + " " + user.getLastName());
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "D'accord",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent i = new Intent(SignIn.this,MainActivity.class);
                                                Bundle b = new Bundle();
                                                b.putParcelable("user",user);
                                                i.putExtra("listAssociation", user.getAssociationsId());
                                                i.putExtra("userBundle",b);
                                                startActivity(i);
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                signInButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);


                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                                builder1.setMessage(error.getMessage());
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "D'accord",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                signInButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                    else{
                        if (!(mLastName.getText().toString().matches("") && mFirstName.getText().toString().matches("") && mPasswordField.getText().toString().matches(""))) {

                            signInButton.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                            String scoolMail = ((mSpinner.getSelectedItemPosition() == 0) ? "telecom-sudparis" : "telecom-em");
                            String email = mFirstName.getText().toString() + "." + mLastName.getText().toString() + "@" + scoolMail + ".eu";


                            SignInUser(email, mPasswordField.getText().toString());


                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                            builder1.setMessage("Veuillez completer toutes les entêtes");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                    builder1.setMessage("Vous devez être connecté à internet");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "D'accord",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

        });

        Button button_create = (Button) findViewById(R.id.button_create_account);
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(v.getContext(), SignUp.class);
                startActivity(i2);
            }

        });
    }





        private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void SignInUser(String email, final String password) {

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Log.d("Connexion","XXXXXXXXXXXXXXXX");
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                            builder1.setMessage(task.getException().getMessage());
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "D'accord",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            signInButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            Log.d("Connexion", "Reussi");


                            final FirebaseUser userFire = mAuth.getCurrentUser();
                            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = mFirebaseDatabase.getReference("Users");

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    DataSnapshot userData = dataSnapshot.child(userFire.getUid());
                                    final User user = new User(userData);

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                                    builder1.setMessage("Bienvenu " + user.getFirstName() + " " + user.getLastName());
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "D'accord",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    Intent i = new Intent(SignIn.this,MainActivity.class);
                                                    Bundle b = new Bundle();
                                                    b.putParcelable("user",user);
                                                    i.putExtra("listAssociation", user.getAssociationsId());

                                                    i.putExtra("userBundle",b);
                                                    startActivity(i);
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                    signInButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);


                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException());
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignIn.this);
                                    builder1.setMessage(error.getMessage());
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "D'accord",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    signInButton.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });







                        }

                    }
                });


    }



}
