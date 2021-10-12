package com.example.firstapp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataManagerTest {

    private static DataManager dm;

    @BeforeClass
    public static void setupDataManager(){
        dm = DataManager.getInstance();
        dm.initializeExampleNotes();
    }

    @After
    public void clearDataManager(){
        dm.getNotes().clear();
    }


    @Test
    public void createNewNote() {
        CourseInfo courseinfo = dm.getCourse("android_intents");
        String noteTitle = "this is note title";
        String noteText = "this is note text";
        int newNoteIndex = dm.createNewNote();

        NoteInfo newNote = dm.getNotes().get(newNoteIndex);
        newNote.setCourse(courseinfo);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote = dm.getNotes().get(newNoteIndex);
        assertEquals(compareNote.getCourse(), courseinfo);
        assertEquals(compareNote.getTitle(), noteTitle);
        assertEquals(compareNote.getText(), noteText);


    }
}