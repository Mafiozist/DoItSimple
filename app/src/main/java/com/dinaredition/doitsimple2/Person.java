package com.dinaredition.doitsimple2;

import android.content.Context;

// Класс предназначенный для хранения основной
// информации по текущему пользователю
public class Person {
    private static Person sPerson;
    private Context mContext;

    private int mPersonId = -1;
    private String mName;
    private String mSurname;
    private int mTaskCount;
    private int mTaskCompleteCount;
    private int mTaskFailedCount;


    private Person(Context context){
        //Это поможет этому классу существовать до уничтожения приложения
        mContext = context.getApplicationContext();
    }

    public static Person get(Context context){
        if(sPerson == null) sPerson = new Person(context);
        return sPerson;
    }

    public void setPerson(int id, String name, String surname, int task_count ,int task_complete_count, int task_failed_count){
        mPersonId = id;
        mName = name;
        mSurname = surname;
        mTaskCompleteCount = task_complete_count;
        mTaskFailedCount = task_failed_count;
        mTaskCount = task_count;
    }

    public int getPersonId() { return mPersonId; }

    public String getName() { return mName; }

    public String getSurname() { return mSurname; }

    public int getTaskCompleteCount() { return mTaskCompleteCount; }

    public int getTaskFailedCount() { return mTaskFailedCount; }



}
