package com.example.firstapp;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID =  "com.example.firstapp.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_COURSE_TITLE =  "com.example.firstapp.ORIGINAL_NOTE_COURSE_TITLE";
    public static final String ORIGINAL_NOTE_COURSE_TEXT =  "com.example.firstapp.ORIGINAL_NOTE_COURSE_TEXT";
    public String originalNoteCourseId, originalNoteTitle, originalNoteText;
    public boolean isNew = true;


//    below two methods are needed if activity is destroyed and recreated
    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_COURSE_TITLE, originalNoteTitle);
        outState.putString(ORIGINAL_NOTE_COURSE_TEXT, originalNoteText);
    }

// restore ViewModel in case activity is destroyed and recreated
    public void restoreState(Bundle inState) {
        originalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalNoteTitle = inState.getString(ORIGINAL_NOTE_COURSE_TITLE);
        originalNoteText = inState.getString(ORIGINAL_NOTE_COURSE_TEXT);
    }
}
