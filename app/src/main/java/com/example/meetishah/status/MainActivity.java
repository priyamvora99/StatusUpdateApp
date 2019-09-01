package com.example.meetishah.status;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG="MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 234;
    SharedPreferences sp;
    Button b;
    EditText email,password;
    Button login;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("Login", Context.MODE_PRIVATE);
        if(sp.contains("email")){
            Intent i=new Intent(getApplicationContext(),HomePage.class);
            startActivity(i);
        }
        setTitle("Signup");
        b=findViewById(R.id.create);
        sb=findViewById(R.id.sign_in_button);
        email=findViewById(R.id.useremail);
        password=findViewById(R.id.userpassword);
        login=findViewById(R.id.login);
        login.setOnClickListener(this);
        b.setOnClickListener(this);
        sb.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, HomePage.class));
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void createAccount(final String email, String password) {
        if(!validate()){
            return;
        }
        // showProgressDialog();
        Log.d(TAG, "createAccount:" + email);
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "createUserWithEmail:success");
                            SharedPreferences.Editor e =sp.edit();
                            e.putString("email",email);
                            e.apply();
                            Intent i=new Intent(getApplicationContext(),HomePage.class);
                            startActivity(i);

                            FirebaseUser user = mAuth.getCurrentUser();




                        } else {
                            //  dismissProgressDialog();
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){

                                Toast.makeText(getApplicationContext(), "Email address is already in use by another application.",

                                        Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException() instanceof FirebaseAuthWeakPasswordException){
                                Toast.makeText(getApplicationContext(), "Password should be atleast 6 characters.",

                                        Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(),"Email is badly formatted",Toast.LENGTH_LONG).show();
                            }
                            if(task.getException() instanceof FirebaseNetworkException){
                                Toast.makeText(getApplicationContext(),"Please ensure you are connected to the Internet",Toast.LENGTH_LONG).show();
                            }


                        }




                        // [START_EXCLUDE]



                        // [END_EXCLUDE]

                    }

                });
    }
    private void signIn(final String email, String password) {

        Log.d(TAG, "signIn:" + email);

        if (!validate()) {

            return;

        }





        //showProgressDialog();

        // [START sign_in_with_email]

        mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithEmail:success");
                            SharedPreferences.Editor e =sp.edit();
                            e.putString("email",email);
                            e.apply();
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(getApplicationContext(),HomePage.class);
                            startActivity(i);
                            FirebaseUser user = mAuth.getCurrentUser();



                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(),"The password is invalid or the user does not have a password.",Toast.LENGTH_LONG).show();
                            }
                            if(task.getException() instanceof FirebaseAuthInvalidUserException){
                                Toast.makeText(getApplicationContext(),"No such user or email is  badly formed",Toast.LENGTH_LONG).show();
                            }
                            if(task.getException() instanceof FirebaseNetworkException){
                                Toast.makeText(getApplicationContext(),"Please ensure you are connected to the Internet",Toast.LENGTH_LONG).show();
                            }


                        }
                        // dismissProgressDialog();



                        // [START_EXCLUDE]




                        // [END_EXCLUDE]

                    }

                });

        // [END sign_in_with_email]

    }
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.e(TAG, "onActivityResult: " );
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e(TAG, "onActivityResult:catch " );
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.e(TAG, "firebaseAuthWithGoogle: " );
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i=new Intent(getApplicationContext(),HomePage.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }



    @Override
    public void onClick(View v) {
        String e,p;

        e=email.getText().toString();
        p=password.getText().toString();
        if(v==b) {


            createAccount(e,p);
        }else if(v==login){
            signIn(e,p);
        }else{
            signIn();
        }
    }
    public boolean validate(){
        boolean valid = true;



        String e = email.getText().toString();

        if (TextUtils.isEmpty(e)) {

            email.setError("Required.");

            valid = false;

        } else {

            email.setError(null);

        }



        String p = password.getText().toString();

        if (TextUtils.isEmpty(p)) {

            password.setError("Required.");

            valid = false;

        } else {

            password
                    .setError(null);

        }



        return valid;
    }


}


