package net.anguslong.todolist;

import java.util.UUID;

import net.anguslong.todolist.model.Task;
import net.anguslong.todolist.model.ToDoList;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

/**
 * used to be CrimeFragment
 * @author anguslong
 *
 */
public class TaskFragment extends Fragment {


	public static final String EXTRA_TASK_ID = "criminalintent.CRIME_ID";
public static final String DIALOG_DATE = "date";

    Task mTask;
    EditText mTitleField;
    EditText mNotesField;

    CheckBox mCompleteCheckBox;

    Button mSaveButton;
    
    public static TaskFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TASK_ID, taskId);

        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // tell android that this fragment has an options menu
        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_TASK_ID);
        mTask = ToDoList.get(getActivity()).getTask(crimeId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, parent, false);
        
        //set up button for Gingerbread and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	if (NavUtils.getParentActivityName(getActivity()) != null) {
        	getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        	}
        }
        
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mTask.getTitle());
        mTitleField.requestFocus();
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mTask.setTitle(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        
        // adding a new notes field
        mNotesField = (EditText)v.findViewById(R.id.notes_edittext);
        mNotesField.setText(mTask.getNotes());
        mNotesField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                mTask.setNotes(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });
        
        
        mCompleteCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mCompleteCheckBox.setChecked(mTask.isComplete());
        mCompleteCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // set the iscompleted property
                mTask.setComplete(isChecked);
            }
        });       
  
        mSaveButton = (Button)v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				
			}
		});
        
        return v; 
    }
    
    @Override
	public void onPause() {
		super.onPause();
		ToDoList.get(getActivity()).saveTasks();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    
}
