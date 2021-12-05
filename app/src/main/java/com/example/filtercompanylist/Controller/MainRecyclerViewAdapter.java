package com.example.filtercompanylist.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.R;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private final List<String> categories;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private final RemoveListener removeListener;

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

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myView;
        TextView myTextView;

        ViewHolder(View itemView, RemoveListener removeListener) {
            super(itemView);

            myView = itemView.findViewById(R.id.colorView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);

            ImageView deleteItem = itemView.findViewById(R.id.removeItem);
            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(v -> {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

                alert.setTitle("Delete");
                alert.setMessage("All products in this category will be deleted.\nContinue?");
                alert.setPositiveButton("Yes", (dialog, which) -> {
                    int position = getAdapterPosition();
                    removeListener.onRemoveClick(position);

                    categories.remove(position);
                    notifyItemRemoved(position);

                    dialog.dismiss();
                });

                alert.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                alert.show();
            });
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