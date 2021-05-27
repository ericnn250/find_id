package com.steven.idlost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.adapters.IdPagerAdapter;
import com.steven.idlost.models.User;

public class AdminHome extends AppCompatActivity {

    private static final String TAG="ViewUserActivity";
    private static final String NOT_TAKEN = "no";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tabcollected;
    private  TabItem tabwait;
    private IdPagerAdapter pageAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        toolbar=(Toolbar) findViewById(R.id.toolbaradminhome);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        tabwait = findViewById(R.id.tabwait);
        tabcollected = findViewById(R.id.tabcollected);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout_user_datails);
        viewPager=(ViewPager)findViewById(R.id.viewpager_userdetails);
        pageAdapter = new IdPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {



                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.optionSignOutad:
                signOut();
                return true;
            case R.id.optionAccountSettingsad:
                intent = new Intent(AdminHome.this, Setting.class);
                startActivity(intent);
                return true;
            case R.id.optionnewadmin:
                gotoauperadmin();
                return true;
//            case R.id.optionnewadminlist:
//                intent = new Intent(AdminHome.this, AdminList.class);
//                startActivity(intent);
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoauperadmin() {
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
           if(securityLevel==9){
                    Log.d(TAG, "onDataChange: user is an admin.");
                    Intent intentadmins = new Intent(AdminHome.this, SuperAdmin.class);
                    intentadmins.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentadmins);
                    finish();
                }
                else{
                    Toast.makeText(AdminHome.this,"Your are not Admin please",Toast.LENGTH_LONG).show();
                    //signOut();
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void signOut(){
        Toast.makeText(AdminHome.this,"Signed out", Toast.LENGTH_LONG).show();
        Log.d("USERhomeActivity", "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AdminHome.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}