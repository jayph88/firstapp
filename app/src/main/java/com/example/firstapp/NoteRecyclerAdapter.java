package com.example.firstapp;

import static com.example.firstapp.NoteActivity.NOTE_POSITION;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{
    
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final List<NoteInfo> mNote;
    private final String TAG = getClass().getSimpleName();

    public NoteRecyclerAdapter(Context context, List<NoteInfo> mNote) {
        mContext = context;
        this.mNote = mNote;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = mLayoutInflater.inflate(R.layout.note_list_item,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mNoteTitle.setText(mNote.get(position).getCourse().getTitle());
        holder.mNoteText.setText(mNote.get(position).getTitle());
//        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mNoteTitle;
        TextView mNoteText;
        int mCurrentPosition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNoteTitle = itemView.findViewById(R.id.noteTitle);
            mNoteText = itemView.findViewById(R.id.noteText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent noteIntent = new Intent(mContext,NoteActivity.class);
                    noteIntent.putExtra(NOTE_POSITION, getAdapterPosition());
                    Log.i(TAG, "opening note at position" + getAdapterPosition());
                    mContext.startActivity(noteIntent);
                }
            });
        }
    }
}
