package com.dinaredition.doitsimple2;

import androidx.fragment.app.Fragment;

import com.dinaredition.doitsimple2.task_type_group.TaskGroupFragment;
import com.dinaredition.doitsimple2.task_type_group.TaskGroupListFragment;

public class TaskGroupActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new TaskGroupListFragment();
    }
}
