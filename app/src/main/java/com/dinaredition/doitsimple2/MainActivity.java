package com.dinaredition.doitsimple2;
import androidx.fragment.app.Fragment;
import com.dinaredition.doitsimple2.authorization.AuthorizationFragment;
import com.dinaredition.doitsimple2.authorization.RegistrationFragment;
import com.dinaredition.doitsimple2.database.ConnectToRemoteDb;
import com.dinaredition.doitsimple2.task.TaskListFragment;
import com.dinaredition.doitsimple2.task_type_group.TaskGroupFragment;
import com.dinaredition.doitsimple2.task_type_group.TaskGroupListFragment;


//Представление активности содержит место,
// в которое вставляется представление фрагмента.
public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TaskGroupListFragment();
    }
}

