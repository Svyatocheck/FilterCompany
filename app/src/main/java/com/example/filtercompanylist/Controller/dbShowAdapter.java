package com.example.filtercompanylist.Controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filtercompanylist.Model.TableRow;
import com.example.filtercompanylist.R;

import java.util.ArrayList;

public class dbShowAdapter extends RecyclerView.Adapter<dbShowAdapter.PersonHolder> {

    // List to store all the contact details
    private final ArrayList<TableRow> noteItems;
    private final OnNoteListener mOnNoteListener;
    private final RemoveListener removeListener;

    public dbShowAdapter(ArrayList<TableRow> notesList, OnNoteListener onNoteListener,
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
        View view = layoutInflater.inflate(R.layout.db_item, parent, false);
        return new PersonHolder(view, mOnNoteListener, removeListener, noteItems);
    }

    // This method is called when binding the data to the views being created in RecyclerView
    public void onBindViewHolder(@NonNull dbShowAdapter.PersonHolder holder, final int position) {
        final TableRow note = noteItems.get(position);

        // Set the data to the views here
        holder.setCompanyName(note.getTableRow());

    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public interface RemoveListener {
        void onRemoveClick(int position);
    }
    // This is your ViewHolder class that helps to populate data to the view
    public class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView itemName;
        OnNoteListener onNoteListener;
        RemoveListener removeListener;

        public PersonHolder(View itemView, OnNoteListener onNoteListener,
                            RemoveListener removeListener, ArrayList<TableRow> people) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);

            ImageView deleteItem = itemView.findViewById(R.id.removeItem);

            this.onNoteListener = onNoteListener;
            this.removeListener = removeListener;

            itemView.setOnClickListener(this);

            deleteItem.setOnClickListener(v -> {
                int position = getAdapterPosition();
                removeListener.onRemoveClick(position);

                people.remove(position);
                notifyItemRemoved(position);
            });

        }

        public void setCompanyName(String name) {
            itemName.setText(name);
        }

        @Override
        public void onClick(View view) {
            Log.e("Note", "Note clicked!");
        }

    }
}