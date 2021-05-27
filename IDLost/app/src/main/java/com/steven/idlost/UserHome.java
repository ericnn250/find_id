package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class UserHome extends AppCompatActivity {
    private TextView myid,idstatus,names,dob,type,sec,dis,found,locdistrict,locsector,msg,phone;
    private LinearLayout location;
    private Toolbar toolbar;
    private String idn;
    private RelativeLayout mProgressBar;
    private static final String TAG = "UserHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        toolbar =  findViewById(R.id.toolbaruserhome);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        myid=(TextView)findViewById(R.id.useridnumber);
        names=(TextView)findViewById(R.id.locationnamesreal);
        dob=(TextView)findViewById(R.id.locationdobreal);
        type=(TextView)findViewById(R.id.locationtypereal);
        phone=(TextView)findViewById(R.id.locationphonereal);
        dis=(TextView)findViewById(R.id.locationisuedistricreal);
        sec=(TextView)findViewById(R.id.locationisuesectorreal);
        msg=(TextView)findViewById(R.id.msgiffound);
        found=(TextView)findViewById(R.id.locationsecreals);
        locdistrict=(TextView)findViewById(R.id.locationdistrictf);
        locsector=(TextView)findViewById(R.id.locationsectorf);
        idstatus=(TextView)findViewById(R.id.useridstatus) ;
        location=(LinearLayout) findViewById(R.id.locationinfo);
        mProgressBar=(RelativeLayout) findViewById(R.id.progressBaruserhome);
        Intent intent = getIntent();
        idn= intent.getStringExtra("idnum");
        myid.setText(idn);
        getUserAccountData();
    }
    private void getUserAccountData(){
        showDialog();
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*
            ---------- QUERY Method 1 ----------
         */
        Query query1 = reference.child(getString(R.string.dbnode_card));
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(IdCard.class).toString());
                    IdCard card = singleSnapshot.getValue(IdCard.class);
                    if (card.getId_number().equals(idn)){
                        if (card.getStatus().equals(getString(R.string.status_pending))){
                            idstatus.setText("");
                            names.setText(card.getNames());
                            dob.setText(card.getDob());
                            type.setText(card.getType());
                            sec.setText(card.getIsue_sector());
                            dis.setText(card.getIsue_district());
                            found.setText(card.getFoundsector());

                            location.setVisibility(View.VISIBLE);
                            getLocationInfo(card.getUser_id());
                        }
                        else
                        {
                            location.setVisibility(View.INVISIBLE);
                            idstatus.setText("Yarabonetse");
                            msg.setText("");
                        }


                        //myRequestList.add(myrequests);
                        Log.d(TAG, "onDataChange: added user: "
                                + card.toString());
                    }
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

    private void getLocationInfo(String user_id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbnode_users))
                .orderByChild(getString(R.string.field_user_id))
                .equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: datasnapshot: " + dataSnapshot);
                DataSnapshot singleSnapshot = dataSnapshot.getChildren().iterator().next();
                int securityLevel = Integer.parseInt(singleSnapshot.getValue(User.class).getSecurity_level());
                User user=singleSnapshot.getValue(User.class);
                locdistrict.setText(user.getDistrict());
                locsector.setText(user.getSector());
                phone.setText(user.getPhone());


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usermenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.optionSignOutuser:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        Toast.makeText(UserHome.this,"Signed out", Toast.LENGTH_LONG).show();
        Log.d("USERhomeActivity", "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserHome.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}