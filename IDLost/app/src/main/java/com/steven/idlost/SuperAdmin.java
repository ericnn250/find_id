package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.steven.idlost.adapters.UserAdapter;
import com.steven.idlost.models.User;

import java.util.ArrayList;

public class SuperAdmin extends AppCompatActivity {

    private Toolbar toolbar;
    private static final String TAG = "UserActivity";
    private static final String ADMIN = "10";
    private RecyclerView userListView;
    //  varable
    private ArrayList<User> userList;
    private UserAdapter mUserAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private RelativeLayout add_doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);
        toolbar =  findViewById(R.id.toolbaradminlist);
        userListView=(RecyclerView)findViewById(R.id.userlist) ;
        progressBar=(ProgressBar) findViewById(R.id.progressBar_user);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        add_doctor=(RelativeLayout) findViewById(R.id.add_doctor);
        add_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent = new Intent(SuperAdmin.this, AddNewAdmin.class);
              startActivity(intent);
            }
        });
        showDialog();
        userList=new ArrayList<>();
        userListView.setHasFixedSize(true);
       //initRecyclerView();



    }

    private void initRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mUserAdapter = new UserAdapter(userList);

        userListView.setLayoutManager(mLayoutManager);
        userListView.setAdapter(mUserAdapter);
        getUserAccountData();
        mUserAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                viewUser(position);
            }
        });
    }

    private void viewUser(int position) {

        Intent intent =new Intent(SuperAdmin.this,ViewAdmin.class);

        intent.putExtra("userid",userList.get(position).getId());
        startActivity(intent);
    }

    private void getUserAccountData(){
        Log.d(TAG, "getUserAccountData: getting the user's account information");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        /*
            ---------- QUERY Method 1 ----------
         */
        Query query1 = reference.child(getString(R.string.dbnode_users));

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this loop will return a single result
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: (QUERY METHOD 1) found user: "
                            + singleSnapshot.getValue(User.class).toString());
                    User user = singleSnapshot.getValue(User.class);
                    if (user.getSecurity_level().equals(ADMIN)){
                        userList.add(user);
                    }
                }
                mUserAdapter.notifyDataSetChanged();
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //  mEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
    private void showDialog() {
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog() {
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.superadminmenu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.optionSignOutusersuper:
                signOut();
                return true;
            case R.id.optionviewasadmin:
                Log.d(TAG, "onDataChange: user is an admin.");
                Intent intentadmin = new Intent(SuperAdmin.this, AdminHome.class);
                intentadmin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentadmin);
                finish();
                return true;
            case R.id.optionAccountSettingsadmin:
                intent = new Intent(SuperAdmin.this, Setting.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        Toast.makeText(SuperAdmin.this,"Signed out", Toast.LENGTH_LONG).show();
        Log.d("USERhomeActivity", "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SuperAdmin.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userList.clear();
        initRecyclerView();
        mUserAdapter.notifyDataSetChanged();

    }

}