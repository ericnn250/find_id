package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.models.User;

public class ViewAdmin extends AppCompatActivity {
    private static final String TAG="ViewDoctoractivity";
    private TextView names,email,phone,province,district;
    private String doctor_id;
    private Button btn_edit,btn_delete;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_admin);
        toolbar=(Toolbar) findViewById(R.id.toolbar9_view_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent user=getIntent();
        doctor_id=user.getStringExtra("userid");
        names=(TextView)findViewById(R.id.textView_doctorname);
        email=(TextView)findViewById(R.id.textViewedoctoremail);
        phone=(TextView)findViewById(R.id.textViewedoctorcontact);
        province=(TextView)findViewById(R.id.textViewedoctorprov);
        district=(TextView)findViewById(R.id.textViewedoctordistr);
        btn_delete=(Button)findViewById(R.id.deletedoctor);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDoctor();
            }
        });

        getUserAccountData();
    }
    private void deleteDoctor() {
        if(doctor_id != null){
            Log.d(TAG, "onClick: deleting Doctor: " + doctor_id);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child(getString(R.string.dbnode_users))
                    .child(doctor_id)
                    .removeValue();
             Toast.makeText(ViewAdmin.this,"Deleted",Toast.LENGTH_LONG).show();
            startActivity(new Intent(ViewAdmin.this,SuperAdmin.class));
            finish();

        }

    }

    private void getUserAccountData(){
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*
            ---------- QUERY Method 1 ----------
         */
        Log.d(TAG, "getUserAccountData: doctor id"+doctor_id);
        Query query1 = reference.child(getString(R.string.dbnode_users))
                .orderByKey()
                .equalTo(doctor_id);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(User.class).toString());
                    User user = singleSnapshot.getValue(User.class);
                    names.setText(user.getNames());
                    phone.setText(user.getPhone());
                    email.setText(user.getEmail());
                    province.setText(user.getSector());
                    district.setText(user.getDistrict());
                    toolbar.setTitle(user.getNames());

                    // ImageLoader.getInstance().displayImage(user.getProfile_image(), mProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}