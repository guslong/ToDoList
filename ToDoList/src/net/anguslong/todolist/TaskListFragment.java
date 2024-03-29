package net.anguslong.todolist;

import java.util.ArrayList;

import net.anguslong.todolist.model.Task;
import net.anguslong.todolist.model.ToDoList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

/**
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
		setListAdapterAndRefresh(); 

		
	}

	/**
	 * refactoring out the methods called in onCreate and onResume to get the
	 * appropriate list depending on the preferences
	 */
	private void setListAdapterAndRefresh() {
		mUncheckedTasks = getUncheckedTasks(); // create a filtered array

		if (((ToDoListApplication) getActivity().getApplication())
				.isShowCheckedItems() == true) {
			adapter = new TaskAdapter(getActivity(), mTasks);
		} else {
			adapter = new TaskAdapter(getActivity(), mUncheckedTasks);
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

			// puts a help icon which displays an activity showing the author's
			// name
		case R.id.menu_item_info:
			Intent intent = new Intent(getActivity(), InfoScreen.class);
			startActivity(intent);

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
		default:
			return super.onContextItemSelected(item);

		}

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

		// has the checkbox been touched?

		// start an instance of TaskPagerActivity
		Intent i = new Intent(getActivity(), TaskPagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, c.getId());
		startActivityForResult(i, 0);

	}

	@Override
	public void onPause() {
		super.onPause();
		ToDoList.get(getActivity()).saveTasks();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		ToDoList.get(getActivity()).saveTasks();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((TaskAdapter) getListAdapter()).notifyDataSetChanged();
	}

}
