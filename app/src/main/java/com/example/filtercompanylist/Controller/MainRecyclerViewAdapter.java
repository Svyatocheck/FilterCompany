package com.example.filtercompanylist.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Model.dbWorker;
import com.example.filtercompanylist.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private final List<String> categories;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final RemoveListener removeListener;
    dbWorker dbHandler;

    // data is passed into the constructor
    public MainRecyclerViewAdapter(Context context, List<String> animals,  RemoveListener removeListener) {
        this.mInflater = LayoutInflater.from(context);
        this.categories = animals;
        this.removeListener = removeListener;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view, removeListener);
    }

    // binds the data to the view and textview in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String animal = categories.get(position);
        holder.myTextView.setText(animal);

    }

    public interface RemoveListener {
        void onRemoveClick(int position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return categories.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                List<String> list = (List<String>) results.values;
//                notifyDataSetChanged();
                Log.d("Results", results.toString());
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = categories;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<String> getFilteredResults(String constraint) {
        List<String> results = new ArrayList<>();

        for (String item : categories) {
            if (item.toLowerCase().contains(constraint)) {
                results.add(item);
                Log.d("Results", item);
            }
        }
        return results;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myView;
        TextView myTextView;
        TextView priceQuery;
        dbWorker dbHandler;

        RemoveListener removeListener;

        ViewHolder(View itemView, RemoveListener removeListener) {
            super(itemView);

            myView = itemView.findViewById(R.id.colorView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);

            ImageView deleteItem = itemView.findViewById(R.id.removeItem);
            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                    alert.setTitle("Delete");
                    alert.setMessage("All products in this category will be deleted.\nContinue?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(itemView.getContext(), "Delete button pressed", Toast.LENGTH_SHORT).show();
                            int position = getAdapterPosition();
                            removeListener.onRemoveClick(position);

                            categories.remove(position);
                            notifyItemRemoved(position);

                            dialog.dismiss();
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }});
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return categories.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}