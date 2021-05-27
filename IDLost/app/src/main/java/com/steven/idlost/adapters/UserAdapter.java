package com.steven.idlost.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.steven.idlost.R;
import com.steven.idlost.models.User;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private ArrayList<User> mUserList;
    private ArrayList<User> mUserListfull;


    public UserAdapter(ArrayList<User> UserList) {
        mUserList = UserList;
        mUserListfull=new ArrayList<>(UserList);
    }
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextname;
        public TextView mTextcontact,user_role;
        public UserViewHolder(View itemView, final OnItemClickListener listener) {

            super(itemView);
            mTextname = itemView.findViewById(R.id.textView_name_row_u);
            mTextcontact= itemView.findViewById(R.id.textView_row_u_email);
            //user_role=itemView.findViewById(R.id.user_role);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }




    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_one_row, parent, false);
        UserViewHolder evh = new UserViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User currentItem = mUserList.get(position);
        String userole="";
//        if (currentItem.getSecurity_level().equals("10")){
//            userole="Admin";
//        }
//        else{
//            userole="";
//        }
        holder.mTextname.setText(currentItem.getNames());
        holder.mTextcontact.setText(currentItem.getPhone());
        //holder.user_role.setText(userole);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mUserListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User item : mUserListfull) {
                    if (item.getNames().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //mUserList.clear();
            mUserList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
