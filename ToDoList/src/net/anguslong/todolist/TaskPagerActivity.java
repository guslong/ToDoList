package net.anguslong.todolist;

import java.util.ArrayList;
import java.util.UUID;

import net.anguslong.todolist.R;
import net.anguslong.todolist.model.Task;
import net.anguslong.todolist.model.ToDoList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;


/**
 * used to be CrimePagerActivity
 * 
 * responsible for flipping through the individual TaskFragments
 * @author anguslong
 *
 */
public class TaskPagerActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private ArrayList<Task> mTasks;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mTasks = ToDoList.get(this).getTasks();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentStatePagerAdapter fspa = new FragmentStatePagerAdapter(fragmentManager) {

			@Override
			public Fragment getItem(int arg0) {
				Task crime = mTasks.get(arg0);
				return TaskFragment.newInstance(crime.getId());

			}

			@Override
			public int getCount() {
				return mTasks.size();
			}
			
		};
		mViewPager.setAdapter(fspa);
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			public void onPageSelected(int pos) {
				Task crime = mTasks.get(pos);
				if (crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
				
			}
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// empty method implementation
				
			}
			
			public void onPageScrollStateChanged(int state) {
				// empty method implementation
				
			}
		});
		
		UUID crimeId = (UUID)getIntent().getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
		for (int i = 0; i < mTasks.size(); i++) {
			if (mTasks.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
