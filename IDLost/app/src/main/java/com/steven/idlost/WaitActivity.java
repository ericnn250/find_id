package com.steven.idlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.models.User;

public class WaitActivity extends AppCompatActivity {
    private static final String TAG = "waitActivity";
    private static final String ACTIVE = "yes";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mProgressBar;
    public static boolean isActivityRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        mProgressBar=(ProgressBar)  findViewById(R.id.progressBar_in_wait);

        Log.d(TAG, "onCreate started");
        setupFirebaseAuth();



        hideSoftKeyboard();

    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        ----------------------------- Firebase setup ---------------------------------
     */
    private void setupFirebaseAuth(){
        showDialog();
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //check if email is verified
                    if(user.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        //Toast.makeText(Login.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        //is admin
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child(getString(R.string.dbnode_users))
                                .orderByChild(getString(R.string.field_user_id))
                                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: datasnapshot: " + dataSnapshot);
                                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                                int securityLevel = Integer.parseInt(singleSnapshot.getValue(User.class).getSecurity_level());
                                String inum=singleSnapshot.getValue(User.class).getNational_id();
                                    if( securityLevel == 10){
                                        Log.d(TAG, "onDataChange: user is an admin.");
                                        Intent intentadmin = new Intent(WaitActivity.this, AdminHome.class);
                                        intentadmin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentadmin);
                                        finish();
                                    }
                                    else if(securityLevel==1) {
                                        Intent intentuser = new Intent(WaitActivity.this, UserHome.class);
                                        intentuser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intentuser.putExtra("idnum",inum);
                                        startActivity(intentuser);
                                        finish();
                                    }
                                    else if(securityLevel==9){
                                        Log.d(TAG, "onDataChange: user is an admin.");
                                        Intent intentadmins = new Intent(WaitActivity.this, SuperAdmin.class);
                                        intentadmins.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentadmins);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(WaitActivity.this,"Your account not exit",Toast.LENGTH_LONG).show();
                                        signOut();
                                    }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//                        if(isAdmin()){
//
//                        }else{
//
//                        }


                    }else{
                        //Toast.makeText(Login.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                       Toast.makeText(WaitActivity.this,"Email is not Verified\nCheck your Inbox",Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent lgn=new Intent(WaitActivity.this,Login.class);
                    startActivity(lgn);
                    finish();
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        isActivityRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isActivityRunning = false;
        }
    }

    private void signOut(){
        Toast.makeText(WaitActivity.this, "You have no right to access this account, May have deleted contact admin for help",Toast.LENGTH_LONG).show();
        Log.d("USERhomeActivity", "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(WaitActivity.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
