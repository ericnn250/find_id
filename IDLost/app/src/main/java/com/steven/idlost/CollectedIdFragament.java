package com.steven.idlost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.steven.idlost.adapters.CollectedAdapter;
import com.steven.idlost.adapters.WaitAdapter;
import com.steven.idlost.models.IdCard;

import java.util.ArrayList;


public class CollectedIdFragament extends Fragment {
    private static final String  TAG="WAITFRAGMENT";
    private RecyclerView mreceiverwaitListView;
    private ProgressBar progressBar;
    //  varable
    private ArrayList<IdCard> idList=new ArrayList<>();
    private CollectedAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CollectedIdFragament() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_collected_id_fragament, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar= view.findViewById(R.id.progressBarcollected);
        mreceiverwaitListView=(RecyclerView) view.findViewById(R.id.collecedcardrecyclerview);
        mreceiverwaitListView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
       // init();
    }

    private void init() {
        mreceiverwaitListView.setLayoutManager(mLayoutManager);

        getUserAccountData();
//        mAdapter.setOnItemClickListener(new CollectedAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                //viewUser(position);
//            }
//        });
    }

    private void viewUser(int position) {
        IdCard card=idList.get(position);
        Intent in=new Intent(getContext(),ViewId.class);
        in.putExtra("id",card.getId());
        startActivity(in);


    }

    private void getUserAccountData() {
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


                    IdCard idwait = singleSnapshot.getValue(IdCard.class);
                    String status = idwait.getStatus();
                    if (idwait.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    if(!status.equals(getString(R.string.status_pending))){

                        Log.d(TAG, "onDataChange: Request is waiting."+idwait.toString());
                        idList.add(idwait);
                    }
                    }

                }
                mAdapter = new CollectedAdapter(idList);
                mreceiverwaitListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collectedmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search_collected);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(getContext(),newText,Toast.LENGTH_SHORT).show();

                mAdapter.getFilter().filter(newText);
                //mReceiverwaitAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        idList.clear();
        init();
       // mAdapter.notifyDataSetChanged();
    }
}