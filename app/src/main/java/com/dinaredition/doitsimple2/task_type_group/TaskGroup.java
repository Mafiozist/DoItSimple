package com.dinaredition.doitsimple2.task_type_group;

//Класс, отвечающий за хранение всех
//Типов задач
public class TaskGroup {
    private int mTaskGroupId;
    private String mTaskGroupText;
    private int mTaskGroupPriority;

    public void setTaskGroup(int id, String text, int priority){
        mTaskGroupId = id;
        mTaskGroupText = text;
        mTaskGroupPriority = priority;
    }

    public int getTaskGroupId() {return mTaskGroupId; }

    public void setTaskGroupId(int mTaskGroupId) { this.mTaskGroupId = mTaskGroupId; }

    public String getTaskGroupText() { return mTaskGroupText; }

    public void setTaskGroupText(String mTaskGroupText) { this.mTaskGroupText = mTaskGroupText; }

    public int getTaskGroupPriority() { return mTaskGroupPriority; }

    public void setTaskGroupPriority(int mTaskGroupPriority) { this.mTaskGroupPriority = mTaskGroupPriority; }

}
