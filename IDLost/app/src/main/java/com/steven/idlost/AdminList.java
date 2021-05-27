package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.adapters.UserAdapter;
import com.steven.idlost.models.User;

import java.util.ArrayList;

public class AdminList extends AppCompatActivity {
    private Toolbar toolbar;
    private static final String TAG = "UserActivity";
    private static final String ADMIN = "10";
    private RecyclerView userListView;
    //  varable
    private ArrayList<User> userList;
    private UserAdapter mUserAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
        toolbar =  findViewById(R.id.toolbaradminlist);
        userListView=(RecyclerView)findViewById(R.id.userlist) ;
        progressBar=(ProgressBar) findViewById(R.id.progressBar_user);
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
        showDialog();
        userList=new ArrayList<>();
        userListView.setHasFixedSize(true);
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

//        Intent intent =new Intent(AdminList.this,ViewUser.class);
//
//        intent.putExtra("userid",userList.get(position).getUser_id());
//        startActivity(intent);
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
}