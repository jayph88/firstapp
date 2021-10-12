package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.databinding.ActivityNoteListBinding;

public class NoteListActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivityNoteListBinding binding;
    private FloatingActionButton fab;
    private NoteRecyclerAdapter mRecyclerAdapter;
    //    private ArrayAdapter<NoteInfo> adapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeDisplayContent();
        fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println("hello");
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
                startActivity(intent);

            }
        });
        Log.i(TAG, "Creating notelist");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerAdapter.notifyDataSetChanged();
    }



    private void initializeDisplayContent() {
        final RecyclerView recyclerNotes = findViewById(R.id.list_view_new);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerNotes.setLayoutManager(notesLayoutManager);

        mRecyclerAdapter = new NoteRecyclerAdapter(this, DataManager.getInstance().getNotes());
        recyclerNotes.setAdapter(mRecyclerAdapter);

    }

}