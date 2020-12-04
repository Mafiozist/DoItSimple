package com.dinaredition.doitsimple2.task_type_group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinaredition.doitsimple2.R;
import com.dinaredition.doitsimple2.TaskActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//
public class TaskGroupListFragment extends Fragment {
    private RecyclerView mTaskGroupRecycleView;
    private TaskGroupAdapter mAdapter;
    private int mSortState = 1; //1- сортировка по возростанию, 2- по убыванию
    public static String TASK_GROUP_ID_TAG = "task_group_id";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_list_fragment,container,false);
        mTaskGroupRecycleView = view.findViewById(R.id.task_recycler_view);
        //Linear layout необходим для работы RecyclerView и отвечает за позицианирование объектов и поведение прокрутки
        mTaskGroupRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }
    public void updateUI(){
        TaskGroupLab taskGroupLab = TaskGroupLab.get(getActivity());
        List<TaskGroup> taskGroups = taskGroupLab.getTasks();

        if(mAdapter == null){
            mAdapter = new TaskGroupAdapter(taskGroups);
            mTaskGroupRecycleView.setAdapter(mAdapter);
        }else {

            mAdapter.setTasks(TaskGroupLab.get(getActivity()).getTasks());
            //Обновление всех данных
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        updateUI();
        super.onPause();
    }

    //Адаптер необходимый для хранения и дальнейшего использования заданий
    // - RecycleView
    class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupHolder>{
        //private Map<Integer, Task> mTasks;
        private List<TaskGroup> mTaskGroups;
        public TaskGroupAdapter(List<TaskGroup> taskGroups){ mTaskGroups = taskGroups; }

        @NonNull
        @Override
        public TaskGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskGroupHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskGroupHolder holder, int position) {
            //Предпологаю, что потом здесь должна быть проверка на TTGroup
            TaskGroup taskGroup = mTaskGroups.get(position);
            holder.bind(taskGroup);
        }

        @Override
        public int getItemCount() {
            return mTaskGroups.size();
        }

        public void setTasks(List<TaskGroup> taskGroups) {
            this.mTaskGroups = taskGroups;
        }

        public TaskGroup getTask(int position){
            return mTaskGroups.get(position);
        }
    }

    class TaskGroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TaskGroup mTaskGroup;
        private TextView mTaskGroupTextView;
        private TextView mPriority;

        public TaskGroupHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.task_type_group_list_item_fragment,parent,false));
            mTaskGroupTextView = itemView.findViewById(R.id.task_group_text);
            mPriority = itemView.findViewById(R.id.create_task_group_priority_text);
            itemView.setOnClickListener(this);
        }

        public void bind(TaskGroup taskGroup){
            mTaskGroup = taskGroup;
            mTaskGroupTextView.setText(taskGroup.getTaskGroupText());
            mPriority.setText(Integer.toString(taskGroup.getTaskGroupPriority()));

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Вы нажали на " + mTaskGroup.getTaskGroupText() +" Id = " + mTaskGroup.getTaskGroupId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), TaskActivity.class);
            intent.putExtra(TASK_GROUP_ID_TAG, mTaskGroup.getTaskGroupId());
            Log.i(TASK_GROUP_ID_TAG, Integer.toString(mTaskGroup.getTaskGroupId()));
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slidein, R.anim.slideout);
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_type_group_list_item_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:

                if(mSortState == 1) {
                    TaskGroupLab.get(getActivity()).getTasks().sort(new TaskGroupSortType1());
                    mSortState =2;
                }
                else if (mSortState == 2) {
                    TaskGroupLab.get(getActivity()).getTasks().sort(new TaskGroupSortType2());
                    mSortState = 1;
                }
                updateUI();
                return true;

            case R.id.add:
                FragmentManager fm = getFragmentManager();
                TaskGroupFragment dialog = new TaskGroupFragment();

                dialog.show(fm, "");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void putTaskBack(TaskGroup taskGroup){
        TaskGroupLab.get(getActivity()).addTask(taskGroup);

        if(mSortState == 2) TaskGroupLab.get(getActivity()).getTasks().sort(new TaskGroupSortType1());
        if(mSortState == 1) TaskGroupLab.get(getActivity()).getTasks().sort(new TaskGroupSortType2());

        mAdapter.setTasks(TaskGroupLab.get(getActivity()).getTasks());
        mAdapter.notifyItemInserted(TaskGroupLab.get(getActivity()).findTask(taskGroup));
    }


    //Сортировка по возрастанию
    class TaskGroupSortType1 implements Comparator<TaskGroup> {

        @Override
        public int compare(TaskGroup t1, TaskGroup t2) {
            return t1.getTaskGroupPriority() - t2.getTaskGroupPriority();
        }
    }

    //Сортировка по убыванию
    class TaskGroupSortType2 implements Comparator<TaskGroup> {

        @Override
        public int compare(TaskGroup t1, TaskGroup t2) {
            return t2.getTaskGroupPriority() - t1.getTaskGroupPriority();
        }
    }

}
