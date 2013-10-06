package net.anguslong.todolist;

import android.support.v4.app.Fragment;

/**
 * used to be CrimeListActivity
 * @author anguslong
 *
 */
public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
