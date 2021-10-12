package com.example.firstapp;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
//    public ActivityScenarioRule<NoteListActivity> noteListActivityRule = new ActivityScenarioRule<>(NoteListActivity.class);
    static DataManager dataManager;

    @BeforeClass
    public static void classSetup(){
        dataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(NoteListActivity.class);

    @Test
    public void createNewNote() {
        final String noteTitle = "this is note title";
        ArrayList<String> list=new ArrayList<String>();
        final CourseInfo course = dataManager.getCourse("java_lang");

//        add new note
        onView(withId(R.id.list_view_new)).perform(click());

//        handle drop down
        onView(withId(R.id.course)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click());
        onView(withId(R.id.course)).check(matches(withSpinnerText(containsString(course.toString()))));

//        enter values in title and text
        onView(withId(R.id.noteTitle)).perform(typeText(noteTitle)).check(matches(withText(noteTitle)));
        onView(withId(R.id.noteText)).perform(typeText("this is note body"), closeSoftKeyboard());

//        save note
        pressBack();
        NoteInfo noteInfo = dataManager.getNotes().get(dataManager.getNotes().size()-1);

//        check if note is created and clickable
        onData(allOf(instanceOf(NoteInfo.class),equalTo(noteInfo))).perform(click());
    }

}