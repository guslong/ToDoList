package net.anguslong.todolist;

import java.util.UUID;

import android.support.v4.app.Fragment;

/**
 * used to be CrimeActivity
 * @author anguslong
 *
 */
public class TaskActivity extends SingleFragmentActivity {
	@Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID)getIntent()
            .getSerializableExtra(TaskFragment.EXTRA_TASK_ID);
        return TaskFragment.newInstance(crimeId);
    }
}
