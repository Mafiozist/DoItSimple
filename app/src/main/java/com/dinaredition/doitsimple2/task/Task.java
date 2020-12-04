package com.dinaredition.doitsimple2.task;

import com.dinaredition.doitsimple2.task_type_group.TaskGroup;

import java.util.Date;

//1) Необходимо задать формат вывода дат в нужном виде
//2) Создать recyclerview для групп
//3) Связать отображение групп с заданиями
//4) Хэширование паролей при регистрации
public class Task {
    private int mTaskID; //Id задания, позволяющее идентифицировать конкретное задание

    private int mTaskGroupId;
    private String mTaskText; //Сам текст задачи
    private Date mTaskDeadline;    //Переменная отвечающая за сроки выполнения задачи
    private int mTaskPriority;    //Приоритет задачи установленный в списке
    private boolean mIsCompleted;  //Статус выполнения задачи
    private TaskGroup mTaskGroup;


    public int getTaskID() {
        return mTaskID;
    }
    public void setTaskID(int mTaskID) {
        this.mTaskID = mTaskID;
    }
    public String getTaskText() {
        return mTaskText;
    }
    public void setTaskText(String mTaskText) {
        this.mTaskText = mTaskText;
    }
    public Date getTaskDeadline() {
        return mTaskDeadline;
    }
    public void setTaskDeadline(Date mTaskDeadline) {
        this.mTaskDeadline = mTaskDeadline;
    }
    public int getTaskPriority() {
        return mTaskPriority;
    }
    public void setTaskPriority(int mTaskPriority) {
        this.mTaskPriority = mTaskPriority;
    }
    public boolean isCompleted() {
        return mIsCompleted;
    }
    public void setCompleted(boolean mIsCompleted) {
        this.mIsCompleted = mIsCompleted;
    }
    public int getTaskGroupId() { return mTaskGroupId; }
    public void setTaskGroupId(int mTaskGroupId) { this.mTaskGroupId = mTaskGroupId; }
}
