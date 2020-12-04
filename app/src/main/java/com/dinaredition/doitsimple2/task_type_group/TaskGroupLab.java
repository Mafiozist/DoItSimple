package com.dinaredition.doitsimple2.task_type_group;

import android.content.Context;

import com.dinaredition.doitsimple2.task.Task;
import com.dinaredition.doitsimple2.task.TaskLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskGroupLab {
    private static TaskGroupLab sTaskGroupLab;
    private Context mContext;
    private List<TaskGroup> mTaskGroupList = new ArrayList<>();

    private TaskGroupLab(Context context){
        for (int i =0; i<10; i++){
            Random random = new Random();
            TaskGroup  taskGroup = new TaskGroup();
            taskGroup.setTaskGroup(
                    random.nextInt(10),
                    "Рабочая задача " + (i + 1),
                    random.nextInt(11)
                    );
            mTaskGroupList.add(taskGroup);
        }
    }
    public static TaskGroupLab get(Context context){
        if (sTaskGroupLab == null){
            sTaskGroupLab = new TaskGroupLab(context);
        }
        return sTaskGroupLab;
    }
    public int findTask(TaskGroup taskGroup){
        for(int i = 0; i<mTaskGroupList.size(); i++){
            if(mTaskGroupList.get(i).equals(taskGroup)) return i;
        }
        return  -1;
    }
    public List<TaskGroup> getTasks(){
        return mTaskGroupList;
    }
    public TaskGroup getTask(int ID){
        return mTaskGroupList.get(ID);
    }
    public void addTask(TaskGroup taskGroup){
        //Тут ид буду генерироваться БД по этому не стоит париться пока
        //mTasks.put(task.getTaskID(), task);
        mTaskGroupList.add(taskGroup);
    }

    public void removeTask(TaskGroup taskGroup){
        //Тут ид буду генерироваться БД по этому не стоит париться пока
        mTaskGroupList.remove(taskGroup);
    }
}
