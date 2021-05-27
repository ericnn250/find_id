package com.steven.idlost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.dialog.DatePickerFragment;
import com.steven.idlost.models.IdCard;
import com.steven.idlost.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegisterFound extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener{
    private EditText cardnumber,names,dob,district,sector,sector_foundn;
    private Button save;
    private RelativeLayout mProgressBar;
    private Toolbar toolbar;
    private ImageButton  button_chooseDate;
    private Spinner type;
    private static final String STATUS="no";
    private static final String TAG="ViewUserActivity";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String userphone;
    private String message;
    private boolean isadded=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_found);
        toolbar =  findViewById(R.id.toolbarfound);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hideSoftKeyboard();
//        ActivityCompat.requestPermissions(RegisterFound.this, new String[]{Manifest.permission.SEND_SMS,
//                Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        cardnumber=(EditText) findViewById(R.id.idnumberfound);
        names=(EditText)findViewById(R.id.namesfound);
        dob=(EditText)findViewById(R.id.dobfound);
        district=(EditText)findViewById(R.id.issuedistrict);
        sector=(EditText)findViewById(R.id.sectorissue);
        sector_foundn=(EditText)findViewById(R.id.sectorissue);
        save=(Button)findViewById(R.id.found_register_in_button);
        mProgressBar=(RelativeLayout)findViewById(R.id.progressBarregisterfound);
        button_chooseDate=(ImageButton)findViewById(R.id.selectdatefound);
        type=(Spinner) findViewById(R.id.spinnercardtype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);
        button_chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendSMSMessagetest();
                if (isEmpty(cardnumber.getText().toString())
                        ||isEmpty(names.getText().toString())
                        ||isEmpty(dob.getText().toString())
                        ||isEmpty(district.getText().toString())
                         ||isEmpty(sector.getText().toString())
                        ||isEmpty(district.getText().toString())||
                        isEmpty(sector.getText().toString())||
                        isEmpty(sector_foundn.getText().toString())
                         ){
                    Toast.makeText(RegisterFound.this,"All Feild are required",Toast.LENGTH_LONG).show();
                }
                else {
                    isalreadyAdded();

                }
            }
        });
    }

    private void isalreadyAdded() {
        showDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbnode_card));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: datasnapshot: " + dataSnapshot);
                boolean added=false;
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    IdCard idCard=singleSnapshot.getValue(IdCard.class);
                    if (cardnumber.getText().toString().trim().equals(idCard.getId_number())&& idCard.getStatus().equals(getString(R.string.status_pending))){
                        //isadded=true;
                        added=true;
                    }
                }
                 if (added){

                     Toast.makeText(RegisterFound.this,"Iyi karita warangije kuyandika",Toast.LENGTH_LONG).show();

                     hideDialog();
                 }
                 else{register(cardnumber.getText().toString(),
                         names.getText().toString(),
                         dob.getText().toString(),
                         district.getText().toString(),
                         sector.getText().toString(),
                         sector_foundn.getText().toString(),
                         type.getSelectedItem().toString()
                 );
                 }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendSMSMessagetest() {
        userphone="0788236167";
        message="hello test";
        //sendSMSMessage();
    }

    private void register(String idnumbers,
                             String name,
                             String dobs,
                             String districts,
                             String sectors,
                             String sectorfound,String t) {
        //sendUserMessage(idnumber);
        //showDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //get the new chatroom unique id
        String requestId = reference
                .child(getString(R.string.dbnode_card))
                .push().getKey();
        IdCard card=new IdCard();
        card.setId(requestId);
        card.setNames(name);
        card.setId_number(idnumbers);
        card.setIsue_district(districts);
        card.setIsue_sector(sectors);
        card.setStatus(getString(R.string.status_pending));
        card.setDob(dobs);
        card.setType(t);
        card.setFoundsector(sectorfound);
        card.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.dbnode_card))
                .child(requestId)
                .setValue(card)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //FirebaseAuth.getInstance().signOut();
                        //Toast.makeText(RequestOrganActivity.this, "Request  Succeed Wait For Response.", Toast.LENGTH_SHORT).show();
                        hideDialog();
                        names.setText("");

                        district.setText("");
                        sector.setText("");
                        dob.setText("");
                        sector_foundn.setText("");

                         Toast.makeText(RegisterFound.this, "Birabitswe neza.",Toast.LENGTH_LONG).show();
                        sendUserMessage(idnumbers);
                        cardnumber.setText("");
                        //redirect the user to the login screen
                        //redirectLoginScreen();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(RequestOrganActivity.this, "something went wrong Request Failed.", Toast.LENGTH_SHORT).show();
                hideDialog();
                 Toast.makeText(RegisterFound.this, "something went wrong Request Failed.",Toast.LENGTH_LONG).show();
                //FirebaseAuth.getInstance().signOut();

                //redirect the user to the login screen
                //redirectLoginScreen();
            }
        });




    }

    private boolean isEmpty(String string){
        return string.equals("");
    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog() {
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void sendUserMessage(String idnumbs) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.dbnode_users));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                User user=singleSnapshot.getValue(User.class);
                if (idnumbs.equals(user.getNational_id())){
                    String number=user.getPhone();
                    userphone=user.getPhone();
                    message="Muraho, ID Retrieval Yishimiye kubamenyesha ko ibyangonbwa byanyu byabonetse,  Koresha app umenye aho ihereye";
                    //try {
//
//                        SmsManager mySmsManager = SmsManager.getDefault();
//                        mySmsManager.sendTextMessage(number,null, message, null, null);
                       //Toast.makeText(RegisterFound.this,"User found",Toast.LENGTH_LONG).show();
                    sendSMSMessage(number,message);
//                    }
//                    catch (Exception e){
//                        Toast.makeText(RegisterFound.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                    }
                }
            }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    protected void sendSMSMessage(String num,String mess) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num, null, mess, null, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(userphone, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "User notified",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dob.setText(currentDateString);
    }
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Africa/Kigali"));
        return sdf.format(new Date());
    }
}