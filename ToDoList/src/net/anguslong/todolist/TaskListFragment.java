package net.anguslong.todolist;

import java.util.ArrayList;

import net.anguslong.todolist.R;
import net.anguslong.todolist.model.Task;
import net.anguslong.todolist.model.ToDoList;


import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.ListFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** used to be CrimeListFragment
 * 
 * controller fragment for the main task list
 * 
 * @author anguslong
 *
 */
public class TaskListFragment extends ListFragment {

	private ArrayList<Task> mTasks;
	private ArrayList<Task> mUncheckedTasks;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_view, null);
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // tell the fragment manager that there is an options menu to deal with
        getActivity().setTitle(R.string.title);
        mTasks = ToDoList.get(getActivity()).getTasks();
        
        mUncheckedTasks = getUncheckedTasks(); // create a filtered array
        
        // if flagShowChecked is true then pass the unfiltered array to the adapter
        TaskAdapter adapter;
        
        
        if ( ((ToDoListApplication)getActivity().getApplication()).isShowCheckedItems() == true) {
        	adapter = new TaskAdapter(mTasks);
            
        }
        else {
        	adapter = new TaskAdapter(mUncheckedTasks);
        }
        
		setListAdapter(adapter);
             
                
        
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
    	// intent for the preferences screen
    	Intent intentPrefs = new Intent(getActivity().getApplicationContext(), PrefsActivity.class);
    	
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
     * this goes trhough the mTasks array and picks out only the ones that are unchecked
     * 
     * @return a new arraylist
     */
	private ArrayList<Task> getUncheckedTasks() {
		ArrayList<Task>result = new ArrayList<Task>();       
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
        Task c = ((TaskAdapter)getListAdapter()).getItem(position);
        // start an instance of TaskPagerActivity
        Intent i = new Intent(getActivity(), TaskPagerActivity.class);
        i.putExtra(TaskFragment.EXTRA_TASK_ID, c.getId());
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((TaskAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(ArrayList<Task> tasks) {
            super(getActivity(), android.R.layout.simple_list_item_1, tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                    .inflate(R.layout.list_item_crime, null);
            }

            // configure the view for this Crime
            Task c = getItem(position);

            TextView titleTextView =
                (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());
            
            CheckBox solvedCheckBox =
                (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isComplete());

   

            return convertView;
        }
    }
}
