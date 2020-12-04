package com.dinaredition.doitsimple2;

import androidx.fragment.app.Fragment;

import com.dinaredition.doitsimple2.task.TaskFragment;
import com.dinaredition.doitsimple2.task.TaskListFragment;

public class TaskActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }
}
