package net.anguslong.todolist;

import java.util.ArrayList;

import net.anguslong.todolist.model.Task;
import net.anguslong.todolist.model.ToDoList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * used to be CrimeListFragment
 * 
 * controller fragment for the main task list
 * 
 * @author anguslong
 * 
 */
public class TaskListFragment extends ListFragment {

	private ArrayList<Task> mTasks;
	private ArrayList<Task> mUncheckedTasks;
	private TaskAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.list_view, null);

		// register the items for context menu on long press
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		registerForContextMenu(listView);

		return v;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true); // tell the fragment manager that there is an
									// options menu to deal with
		getActivity().setTitle(R.string.title);
		mTasks = ToDoList.get(getActivity()).getTasks();

		setListAdapterAndRefresh();

	}

	/**
	 * attempting to use the onresume method to update the list contents
	 */
	@Override
	public void onResume() {
		super.onResume();
		// if flagShowChecked is true then pass the unfiltered array to the
		// adapter
		setListAdapterAndRefresh(); // added this to refresh the screen
	}

	/**
	 * refactoring out the methods called in onCreate and onResume to get the
	 * appropriate list depending on the preferences
	 */
	private void setListAdapterAndRefresh() {
		mUncheckedTasks = getUncheckedTasks(); // create a filtered array

		if (((ToDoListApplication) getActivity().getApplication())
				.isShowCheckedItems() == true) {
			adapter = new TaskAdapter(mTasks);
		} else {
			adapter = new TaskAdapter(mUncheckedTasks);
		}

		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// intent for the preferences screen
		Intent intentPrefs = new Intent(getActivity().getApplicationContext(),
				PrefsActivity.class);

		switch (item.getItemId()) {
		case R.id.menu_item_new_task:
			Task task = new Task();
			ToDoList.get(getActivity()).addTask(task);
			Intent i = new Intent(getActivity(), TaskPagerActivity.class);
			i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
			startActivityForResult(i, 0);
			return true;

			// if the setting button is pressed
		case R.id.menu_item_settings:
			startActivity(intentPrefs);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * inflate the context menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// gets the location of the item where the context menu was selected
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int position = info.position;
		TaskAdapter adapter = (TaskAdapter) getListAdapter();
		Task task = adapter.getItem(position);

		// delete the item
		switch (item.getItemId()) {
		case R.id.menu_item_delete_task:
			ToDoList.get(getActivity()).deleteTask(task);
			adapter.notifyDataSetChanged();
			return true;
		}

		return super.onContextItemSelected(item);
	}

	/**
	 * this goes trhough the mTasks array and picks out only the ones that are
	 * unchecked
	 * 
	 * @return a new arraylist
	 */
	private ArrayList<Task> getUncheckedTasks() {
		ArrayList<Task> result = new ArrayList<Task>();
		for (Task task : mTasks) {
			if (task.isComplete() == false) {
				result.add(task);
			}
		}
		return result;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_task_list, menu);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		// get the Task from the adapter
		Task c = ((TaskAdapter) getListAdapter()).getItem(position);
		// start an instance of TaskPagerActivity
		Intent i = new Intent(getActivity(), TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, c.getId());
		startActivityForResult(i, 0);
		
		
	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((TaskAdapter) getListAdapter()).notifyDataSetChanged();
	}

	private class TaskAdapter extends ArrayAdapter<Task> {
		public TaskAdapter(ArrayList<Task> tasks) {
			super(getActivity(), android.R.layout.simple_list_item_1, tasks);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// if we weren't given a view, inflate one
			if (null == convertView) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_crime, null);
			}

			// configure the view for this Crime
			final Task c = getItem(position);

			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			
			CheckBox solvedCheckBox = (CheckBox) convertView
					.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isComplete());
			
			// adds a checkbox changed listener to the checkbox in the listview
			solvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					c.setComplete(isChecked);
					
				}
				
			});

			return convertView;
		}
	}
	 
	
}
