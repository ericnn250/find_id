package com.steven.idlost.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.steven.idlost.R;
import com.steven.idlost.models.IdCard;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CollectedAdapter  extends RecyclerView.Adapter<CollectedAdapter.CollectedViewHolder> implements Filterable {

    private ArrayList<IdCard> mExampleList;
    private ArrayList<IdCard> mExampleListfull;

    public CollectedAdapter(ArrayList<IdCard> exampleList) {
        mExampleList = exampleList;
        mExampleListfull=new ArrayList<>(exampleList);
    }
    private CollectedAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CollectedAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CollectedViewHolder extends RecyclerView.ViewHolder {
        //public ImageView mImageView;
        public TextView mTextView1number;
        public TextView mTextView1name;
        public TextView mTextView2type;


        public CollectedViewHolder(View itemView, final CollectedAdapter.OnItemClickListener listener) {

            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1number = itemView.findViewById(R.id.textViewnumberc);
            mTextView1name = itemView.findViewById(R.id.textViewnamec);
            mTextView2type = itemView.findViewById(R.id.textView2typec);
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
    public CollectedAdapter.CollectedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.colleced_one_row, parent, false);
        CollectedAdapter.CollectedViewHolder evh = new CollectedAdapter.CollectedViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(CollectedAdapter.CollectedViewHolder holder, int position) {
        IdCard currentItem = mExampleList.get(position);

        // holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1number.setText(currentItem.getId_number());
        holder.mTextView1name.setText(currentItem.getNames());
        holder.mTextView2type.setText(currentItem.getType());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<IdCard> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mExampleListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (IdCard item : mExampleListfull) {
                    if (item.getNames().toLowerCase().contains(filterPattern)||item.getId_number().toLowerCase().contains(filterPattern)) {
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
            mExampleList.clear();
            mExampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}

