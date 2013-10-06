package net.anguslong.todolist.model;

import java.util.ArrayList;
import java.util.UUID;

import net.anguslong.todolist.JSONSerializer;


import android.content.Context;
import android.util.Log;

/**
 * This stores the actual task list
 * @author anguslong
 *
 */
public class ToDoList {
	
	private static final String TAG = "ToDoList";
	private static final String FILENAME = "todolist.json";
	
    private ArrayList<Task> mTasks;

    private JSONSerializer mSerializer;
    
    private static ToDoList sToDoList;
    private Context mAppContext;

    private ToDoList(Context appContext) {
        mAppContext = appContext;
        mSerializer = new JSONSerializer(mAppContext, FILENAME);

        try {
        	mTasks = mSerializer.loadTasks();
        	Log.d(TAG, "Restored from JSON file");
        } catch (Exception e) {
            mTasks = new ArrayList<Task>();
        	Log.e(TAG, "Error loading tasks", e);
        }
    }

    public static ToDoList get(Context c) {
        if (sToDoList == null) {
            sToDoList = new ToDoList(c.getApplicationContext());
        }
        return sToDoList;
    }

    public void addTask(Task task) {
    	mTasks.add(task);
    }
    
    public void deleteTask(Task task) {
    	mTasks.remove(task);
    }
    
    public Task getTask(UUID id) {
        for (Task c : mTasks) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    
    public ArrayList<Task> getTasks() {
        return mTasks;
    }
    
    public boolean saveTasks() {
    	try {
    		mSerializer.saveTasks(mTasks);
    		Log.d(TAG, "Serializer object: " + mSerializer);
    		Log.d(TAG, "Saved successfully");
    		return true;
    	} catch (Exception e) {
    		Log.e(TAG, "Something went wrong saving json object", e);
    		return false;
    	}
    }
}

