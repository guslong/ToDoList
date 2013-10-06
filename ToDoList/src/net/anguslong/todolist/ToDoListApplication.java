package net.anguslong.todolist;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * application stores global preferences
 * 
 * 1) whether the list shows checked items or not (showCheckedItems)
 * 
 * @author anguslong
 *
 */
public class ToDoListApplication extends Application implements OnSharedPreferenceChangeListener {

	static final String TAG = "ToDoList";
	boolean showCheckedItems = true; 
	SharedPreferences prefs;
	

@Override
     public void onCreate() {
          super.onCreate();
          getPrefs();
     }

private void getPrefs() {
          prefs = PreferenceManager.getDefaultSharedPreferences(this);
          prefs.registerOnSharedPreferenceChangeListener(this);
          setShowCheckedItems();

     }

     private void setShowCheckedItems() {
	
    	 Boolean checkboxPreference = prefs.getBoolean("checked_pref_key", true);
         Log.d(TAG, "logging string checked_pref_key: " + checkboxPreference);

         showCheckedItems = checkboxPreference;

         Log.d(TAG, "showCheckedItems is set to: " + showCheckedItems);
}

	@Override
     public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
               String key) {

          getPrefs();
          Log.d(TAG, "sharedPreferences changed");

     }

	public boolean isShowCheckedItems() {
		return showCheckedItems;
	}
}
