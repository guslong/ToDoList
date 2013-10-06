package net.anguslong.todolist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PrefsActivity extends Activity {

	static final String TAG = "PrefsActivity";
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * addPreferencesFromResource(R.xml.prefs); // deprecated in API 11
		 *  
		 * If your app supports versions of Android older than 3.0 (API level 10
		 * and lower), you must build the activity as an extension of the
		 * PreferenceActivity class.
		 * 
		 * On Android 3.0 and later, you should instead use a traditional
		 * Activity that hosts a PreferenceFragment that displays your app
		 * settings. However, you can also use PreferenceActivity to create a
		 * two-pane layout for large screens when you have multiple groups of
		 * settings.
		 */

		// Display the fragment as the main content
		
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefsFragment()).commit();
	}

	public static class PrefsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.prefs);
		}
	}
}
