package com.example.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.firstapp.databinding.ActivityNoteBinding;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    public static final int NO_POSITION = -1;
    private AppBarConfiguration appBarConfiguration;
    private ActivityNoteBinding binding;
    public static final String NOTE_POSITION = "com.example.firstapp.NOTE_POSITION";
    private EditText noteTitle, noteText;
    private NoteInfo note;
    private Boolean isNewNote;
    private int position;
    private Spinner spinnerCourses;
    private NoteActivityViewModel viewModel;
    private List<CourseInfo> coursesList;
    private Boolean isCancelled=false;
    private int newNoteNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

//        we want system to provide instance of NoteActivityViewModel depending if its new or
//        recreated activity, hence we use ViewModelProvider and ask it instance of NoteActivityViewModel
//        instead of directly initializing NoteActivityViewModel
        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        viewModel = viewModelProvider.get(NoteActivityViewModel.class);

//        viewModel persists if activity is recreated due to change in orientation
        if (savedInstanceState != null && viewModel.isNew) {
            Log.i(TAG, "Recreating activity from savedInstanceState");
            viewModel.restoreState(savedInstanceState);
        }

        viewModel.isNew = false;

        spinnerCourses = findViewById(R.id.course);
        coursesList = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coursesList);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);

        // is new note, then create new note
        if (isNewNote) {
           DataManager dm = DataManager.getInstance();
            newNoteNumber = dm.createNewNote();
           note = dm.getNotes().get(newNoteNumber);

        }
        else {
            // show existing note
            displayNote(spinnerCourses, noteTitle,  noteText);
        }
        // save original values in case user cancels it
        saveOriginal();
        Log.i(TAG, "Oncreate Note");
    }

    private void saveOriginal() {
        if (!isNewNote) {
           viewModel.originalNoteCourseId = note.getCourse().getCourseId();
            viewModel.originalNoteTitle = note.getTitle();
            viewModel.originalNoteText = note.getText();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // hits cancel button from top bar
        if (isCancelled) {
            if (isNewNote) {
                DataManager.getInstance().removeNote(newNoteNumber);
            }
            else {
                restoreOriginal();
            }
        }
        // when user hits back button and some edits in place
        else {
            saveNote();
        }
    }

    private void saveNote() {
        note.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        note.setTitle(noteTitle.getText().toString());
        note.setText(noteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText noteTitle, EditText noteText) {
        Log.i(TAG, "Displaying note at index: " + position);
        note = DataManager.getInstance().getNotes().get(position);
        int courseIndex =  DataManager.getInstance().getCourses().indexOf(note.getCourse());
        spinnerCourses.setSelection(courseIndex);
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        position = intent.getIntExtra(NOTE_POSITION, NO_POSITION);
        isNewNote = position == NO_POSITION;
        Log.i(TAG, "isNewNote : " + isNewNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
        }

        //cancel updated fields
        if (id==R.id.action_cancel) {
            isCancelled = true;
            finish(); // this would cause activity to close
        }
        if (id == R.id.action_next) {
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;
        if(position == lastNoteIndex) {
            item.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    private void moveNext() {
//        save the current note
        saveNote();
        ++position;
        note = DataManager.getInstance().getNotes().get(position);
        saveOriginal();
        displayNote(spinnerCourses, noteTitle,  noteText);
        invalidateOptionsMenu();
    }

    private void restoreOriginal() {
//        spinnerCourses.setSelection(coursesList.indexOf(DataManager.getInstance().getCourse(originalNoteCourseId)));
//        noteTitle.setText(originalNoteTitle);
//        noteText.setText(originalNoteText);
        note.setCourse(DataManager.getInstance().getCourse(viewModel.originalNoteCourseId));
        note.setTitle(viewModel.originalNoteTitle);
        note.setText(viewModel.originalNoteText);
    }

    private void sendEmail() {
        CourseInfo courseInfo = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = noteTitle.getText().toString();
        String body = "hey checkout what i have learned from course \"" +
                courseInfo.getTitle() + "\" \n" + noteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if(outState != null)
        viewModel.saveState(outState);
}
}