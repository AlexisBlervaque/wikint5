package com.example.alexisblervaque.wikint;

import android.app.Activity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by alexisblervaque on 14/12/2017.
 */

public class SignUp extends Activity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    private static final String TAG = "AnonymousAuth";
    private EditText mLastName;
    private EditText mFirstName;
    private EditText mPassword;
    private EditText mPasswordConfirmation;
    private Spinner mSpinner;
    private ProgressBar progressBar;
    private Button signUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mSpinner = (Spinner)findViewById(R.id.spinner_scool_signin);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scool, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);


        mLastName = (EditText)findViewById(R.id.last_name_signin);
        mFirstName = (EditText)findViewById(R.id.first_name_signin);
        mPassword = (EditText) findViewById(R.id.editTextPassword_signin);
        mPasswordConfirmation = (EditText) findViewById(R.id.editTextConfirmationPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressBarSignUp);


        signUpButton = (Button)findViewById(R.id.button_signIn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable())
                {
                    if (!(mLastName.getText().toString().matches("") && mFirstName.getText().toString().matches("") && mPassword.getText().toString().matches("")))
                    {
                        if (mPassword.getText().toString().matches(mPasswordConfirmation.getText().toString())) {
                            signUpButton.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);

                            String scoolMail = ((mSpinner.getSelectedItemPosition()==0) ? "telecom-sudparis" : "telecom-em");
                            String email = mFirstName.getText().toString()+"."+mLastName.getText().toString()+"@" + scoolMail+ ".eu";
                            signUpUser(email,mPassword.getText().toString(),mFirstName.getText().toString(),mLastName.getText().toString());
                        }
                        else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
                            builder1.setMessage("Les mots de passes ne sont pas les mêmes");
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
                    else
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
                        builder1.setMessage("Veuillez remplir toutes les informations");
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
                else
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
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
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void signUpUser(String email, String password, final String firstName, final String lastName)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();

                            ArrayList<Integer> AssociationIdDefault = new ArrayList<>();
                            AssociationIdDefault.add(-1);
                            final User userInformation = new User(firstName,lastName,AssociationIdDefault);
                            myRef.child("Users").child(user.getUid()).setValue(userInformation);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
                            builder1.setMessage("Bienvenu " + userInformation.getFirstName() + " " + userInformation.getLastName());
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "D'accord",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Intent i = new Intent(SignUp.this,MainActivity.class);
                                            Bundle b = new Bundle();
                                            b.putParcelable("user",userInformation);
                                            i.putExtra("userBundle",b);
                                            startActivity(i);
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            signUpButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String result = task.getException().getLocalizedMessage();
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUp.this);
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
                            signUpButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
