package com.steven.idlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.steven.idlost.models.User;

public class Register extends AppCompatActivity {
    private EditText nid,email,password,cpassword,phone,desc;
    private Button reg;
    private TextView login;
    private static final String TAG = "RegisterActivity";
    private RelativeLayout mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hideSoftKeyboard();
        nid=(EditText) findViewById(R.id.idnumber);
        login=(TextView) findViewById(R.id.link_login);
        email=(EditText) findViewById(R.id.emailreg);
        phone=(EditText) findViewById(R.id.phonereg);
        desc=(EditText) findViewById(R.id.descreg);
        password=(EditText) findViewById(R.id.passwordreg);
        cpassword=(EditText) findViewById(R.id.passwordregconfirm);
        reg=(Button) findViewById(R.id.email_register_in_button);
        mProgressBar=(RelativeLayout)findViewById(R.id.progressBarregister) ;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectLoginScreen();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(email.getText().toString())
                        && !isEmpty(password.getText().toString())
                        && !isEmpty(phone.getText().toString())
                        && !isEmpty(cpassword.getText().toString())
                        && !isEmpty(nid.getText().toString())){

                    //check if user has a company email address

                        //check if passwords match
                        if(doStringsMatch(password.getText().toString(), cpassword.getText().toString())){

                            //Initiate registration task
                            registerNewEmail(
                                    email.getText().toString(),
                                    phone.getText().toString(),
                                    desc.getText().toString(),
                                    nid.getText().toString(),
                                    password.getText().toString());
                        }else{
                            Toast.makeText(Register.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }


                }else{
                    Toast.makeText(Register.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     */
    public void registerNewEmail(
                                 final String email,
                                 final String phones,
                                 final String descs,
                                 final String nid,
                                 final String password){

        showDialog();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: AuthState: " + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            //send email verificaiton
                            sendVerificationEmail();
                            //insert some default data
                            final User user = new User();
                            user.setNames("");
                            user.setPhone(phones);
                            user.setDesc(descs);
                            user.setDob("");
                            user.setDistrict("");
                            user.setSector("");
                            user.setNational_id(nid);
                            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            user.setSecurity_level("1");
                            user.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseDatabase.getInstance().getReference()
                                    .child(getString(R.string.dbnode_users))
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseAuth.getInstance().signOut();
                                            Log.i(TAG,user.toString());
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "Registration succeed.\n Check your email inbox to verify email",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            //redirect the user to the login screen
                                            redirectLoginScreen();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Register.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();

                                    //redirect the user to the login screen
                                    redirectLoginScreen();
                                }
                            });

//                            FirebaseAuth.getInstance().signOut();
//
//                            //redirect the user to the login screen
//                            redirectLoginScreen();
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();

                        // ...
                    }
                });
    }

    /**
     * sends an email verification link to the user
     */
    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(Register.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Toast.makeText(Register.this, "Couldn't Verification Send Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }



    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen(){
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
    /**
     * Return true if @param 's1' matches @param 's2'
     * @param s1
     * @param s2
     * @return
     */
    private boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
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
}