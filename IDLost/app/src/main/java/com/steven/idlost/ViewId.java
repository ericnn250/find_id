package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.models.IdCard;
import com.steven.idlost.models.User;

public class ViewId extends AppCompatActivity {
    private TextView usercardid,names,dob,type,sec,dis,found,locdistrict,locsector,approved;
    private LinearLayout location;
    private Toolbar toolbar;
    private String idn,cardn;
    private RelativeLayout mProgressBar;
    private static final String TAG = "ViewId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_id);
        toolbar =  findViewById(R.id.toolbaruserhomecollected);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        usercardid=(TextView)findViewById(R.id.useridnumbercollectedview);
        names=(TextView)findViewById(R.id.locationnamesrealc);
        dob=(TextView)findViewById(R.id.locationdobrealc);
        type=(TextView)findViewById(R.id.locationtyperealc);
        dis=(TextView)findViewById(R.id.locationisuedistricrealc);
        sec=(TextView)findViewById(R.id.locationisuesectorrealc);
        found=(TextView)findViewById(R.id.locationsecrealsc);
        locdistrict=(TextView)findViewById(R.id.locationdistrictfc);
        locsector=(TextView)findViewById(R.id.locationsectorfc);
        location=(LinearLayout) findViewById(R.id.locationinfoc);
        approved=(TextView)findViewById(R.id.locationbtncollect);
        mProgressBar=(RelativeLayout) findViewById(R.id.progressBarviewid);
        Intent intent = getIntent();
        idn= intent.getStringExtra("id");
        cardn=intent.getStringExtra("idnum");
        usercardid.setText(cardn);
        getUserAccountData();
        approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comfirmTaken();
            }
        });
    }

    private void comfirmTaken() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        if(!idn.equals("")){
            reference.child(getString(R.string.dbnode_card))
                    .child(idn)
                    .child(getString(R.string.field_status))
                    .setValue(getString(R.string.taken));
        }
        Toast.makeText(ViewId.this,"Byemejwe Kuyitanga",Toast.LENGTH_LONG).show();
    }

    private void getUserAccountData(){
        showDialog();
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*
            ---------- QUERY Method 1 ----------
         */
        Query query1 = reference.child(getString(R.string.dbnode_card))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(idn);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(IdCard.class).toString());
                    IdCard card = singleSnapshot.getValue(IdCard.class);
                    ///if (card.getId_number().equals(idn)){
                        if (card.getStatus().equals(getString(R.string.status_pending))){
                            //idstatus.setText("");
                            names.setText(card.getNames());
                            dob.setText(card.getDob());
                            type.setText(card.getType());
                            sec.setText(card.getIsue_sector());
                            dis.setText(card.getIsue_district());
                            found.setText(card.getFoundsector());

                            location.setVisibility(View.VISIBLE);
                            getLocationInfo(card.getId_number());
                        }
                        else
                        {
                            location.setVisibility(View.INVISIBLE);
                           // idstatus.setText("Yarabonetse");
                        }


                        //myRequestList.add(myrequests);
                        Log.d(TAG, "onDataChange: added user: "
                                + card.toString());
                   // }
                }
                // mMyRequestAdapter.notifyDataSetChanged();
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideDialog();
            }
        });
    }

    private void getLocationInfo(String idnumb) {
       // Toast.makeText(ViewId.this,"information",Toast.LENGTH_LONG).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbnode_users));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: datasnapshot: " + dataSnapshot);
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                ///DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                //int securityLevel = Integer.parseInt(singleSnapshot.getValue(User.class).getSecurity_level());
                //Log.d(TAG, "onDataChange: founduser: " + singleSnapshot);
                User user=singleSnapshot.getValue(User.class);
                //Log.d(TAG, "onDataChangeasssssss: single user founduserids: " + user.getNational_id()+"\n");
                if (idnumb.equals(user.getNational_id())){
                    locdistrict.setText("Yes");
                    locsector.setText(user.getDesc());
                    //Toast.makeText(ViewId.this,"location found",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onDataChange:single founduser: " + user.toString());
                }
                else{

                   // Toast.makeText(ViewId.this,"location  not found",Toast.LENGTH_LONG).show();
                }
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void hideDialog() {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
}