package com.dinaredition.doitsimple2.task;

import android.content.Context;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//Класс в котором храняться вcе не выполненные задания
// для конкретного пользователя
public class TaskLab {
    private static TaskLab sTaskLab;
    private Context mContext;
    //private HashMap<Integer, Task> mTasks = new HashMap<>();
    private List<Task> mTaskList = new ArrayList<>();

    private TaskLab(Context context) {
        for (int i = 0; i<100; i++){
            Random random = new Random();
            Task task = new Task();
            task.setCompleted(i % 2 == 0);
            task.setTaskText("Taskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk №" + (i + 1));
            task.setTaskID(i);
            task.setTaskPriority(random.nextInt(11));
            task.setTaskGroupId(random.nextInt(10));
            task.setTaskDeadline(new Date());
            //mTasks.put(i,task);
            mTaskList.add(task);
        }
        mContext = context.getApplicationContext();
    }

    public void setTasks(List<Task> tasks){
        mTaskList = tasks;
    }

    public static TaskLab get(Context context){
        if (sTaskLab == null){
            sTaskLab = new TaskLab(context);
        }

        return sTaskLab;
    }

    public int findTask(Task task){
        for(int i = 0; i<mTaskList.size(); i++){
            if(mTaskList.get(i).equals(task)) return i;
        }
        return  -1;
    }

    public List<Task> getTasks(int id){
        List<Task> tasks = new ArrayList<>();

        for(int i = 0; i<mTaskList.size(); i++)
        {
            if(mTaskList.get(i).getTaskGroupId() == id) tasks.add(mTaskList.get(i));
        }
        return tasks;
    }

    public Task getTask(int ID){
        return mTaskList.get(ID);
    }

    public void addTask(Task task){
        //Тут ид буду генерироваться БД по этому не стоит париться пока
        //mTasks.put(task.getTaskID(), task);
        mTaskList.add(task);
    }

    public void removeTask(Task task){
        //Тут ид буду генерироваться БД по этому не стоит париться пока
        mTaskList.remove(task);
    }



}
