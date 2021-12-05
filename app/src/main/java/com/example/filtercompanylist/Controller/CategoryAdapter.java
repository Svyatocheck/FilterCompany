package com.example.filtercompanylist.Controller;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.filtercompanylist.Model.Note;
import com.example.filtercompanylist.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.PersonHolder> {

    // List to store all the contact details
    private ArrayList<Note> noteItems;
    private final OnNoteListener mOnNoteListener;
    private final RemoveListener removeListener;

    public CategoryAdapter(ArrayList<Note> notesList, OnNoteListener onNoteListener,
                           RemoveListener removeListener) {
        this.noteItems = notesList;
        this.mOnNoteListener = onNoteListener;
        this.removeListener = removeListener;
    }


    @Override
    public int getItemCount() {
        return noteItems == null? 0: noteItems.size();
    }

    @NonNull
    @Override
    public PersonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.item_list, parent, false);
        return new PersonHolder(view, mOnNoteListener, removeListener, noteItems);
    }

    // This method is called when binding the data to the views being created in RecyclerView
    public void onBindViewHolder(@NonNull CategoryAdapter.PersonHolder holder, final int position) {
        final Note note = noteItems.get(position);

        // Set the data to the views here
        holder.setCompanyName(note.getCompanyName());
        holder.setFounder(note.getFounder());
        holder.setProduct(note.getProduct());
        holder.setCategory(note.getCategory());
        holder.setPrice(note.getPrice());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<Note> filteredList) {
        noteItems = filteredList;
        notifyDataSetChanged();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public interface RemoveListener {
        void onRemoveClick(int position);
    }
    // This is your ViewHolder class that helps to populate data to the view
    public class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView companyName;
        private final TextView founder;
        private final TextView product;
        private final TextView categoryField;
        private final TextView priceField;

        private final ImageView companyIcon;

        OnNoteListener onNoteListener;
        RemoveListener removeListener;

        public PersonHolder(View itemView, OnNoteListener onNoteListener,
                            RemoveListener removeListener, ArrayList<Note> people) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyName);
            founder = itemView.findViewById(R.id.founderName);
            product = itemView.findViewById(R.id.productName);
            categoryField = itemView.findViewById(R.id.productCategory);
            priceField = itemView.findViewById(R.id.priceValues);

            companyIcon = itemView.findViewById(R.id.avatarField);

            ImageView deleteItem = itemView.findViewById(R.id.removeItem);

            this.onNoteListener = onNoteListener;
            this.removeListener = removeListener;

            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(v -> {
                //Toast.makeText(itemView.getContext(), "Delete button pressed", Toast.LENGTH_SHORT).show();
                int position = getAdapterPosition();
                removeListener.onRemoveClick(position);

                people.remove(position);
                notifyItemRemoved(position);
            });

        }

        public void setCompanyName(String name) {
            companyName.setText(name);
        }

        public void setFounder(String founder) {
            this.founder.setText(founder);

            try {
                companyIcon.setImageResource(R.drawable.company);

            } catch (Exception ignore){}
        }

        public void setProduct(String product) {
            this.product.setText(product);
        }
        public void setPrice(String product) {
            this.priceField.setText(product);
        }
        public void setCategory(String product) {
            this.categoryField.setText(product);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
            //Log.e("Note", "Note clicked!");
        }

    }
}