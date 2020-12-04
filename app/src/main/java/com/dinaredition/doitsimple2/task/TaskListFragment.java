package com.dinaredition.doitsimple2.task;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinaredition.doitsimple2.R;
import com.dinaredition.doitsimple2.task_type_group.TaskGroupFragment;
import com.dinaredition.doitsimple2.task_type_group.TaskGroupListFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.dinaredition.doitsimple2.task_type_group.TaskGroupListFragment.TASK_GROUP_ID_TAG;

//Нужно подумать о сохранении некоторых данных файл, чтобы уменьшить кол-во операций пользователя
//Логика с отображением дней до дедлайна и если дней меньше 3 - красный цвет
//Юнит тесты блоков


//Отображение
public class TaskListFragment extends Fragment {
    private RecyclerView mTaskRecycleView;
    private TaskAdapter mAdapter;
    private int mSortState = 1; //1- сортировка по возростанию, 2- по убыванию
    private int mCurrentTaskGroupId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_list_fragment,container,false);
        mTaskRecycleView = view.findViewById(R.id.task_recycler_view);
        //Linear layout необходим для работы RecyclerView и отвечает за позицианирование объектов и поведение прокрутки
        mTaskRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mSimpleCallback);
        mItemTouchHelper.attachToRecyclerView(mTaskRecycleView);
        return view;
    }

    public void updateUI(){
        TaskLab taskLab = TaskLab.get(getActivity());
        //Map<Integer, Task> tasks = taskLab.getTasks();
        List<Task> tasks = taskLab.getTasks(mCurrentTaskGroupId);

        if(mAdapter == null){
            mAdapter = new TaskAdapter(tasks);
            mTaskRecycleView.setAdapter(mAdapter);
        }else {

            mAdapter.setTasks(TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId));
            //Обновление всех данных
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentTaskGroupId = getActivity().getIntent().getIntExtra(TASK_GROUP_ID_TAG, 0);
        Log.i(TASK_GROUP_ID_TAG, Integer.toString(mCurrentTaskGroupId));

    }

    @Override
    public void onPause() {
        updateUI();
        super.onPause();

    }

    //Сортировка по возрастанию
    class TaskSortType1 implements Comparator<Task> {

        @Override
        public int compare(Task t1, Task t2) {
            return t1.getTaskPriority() - t2.getTaskPriority();
        }
    }

    //Сортировка по убыванию
    class TaskSortType2 implements Comparator<Task> {

        @Override
        public int compare(Task t1, Task t2) {
            return t2.getTaskPriority() - t1.getTaskPriority();
        }
    }

    //Адаптер необходимый для хранения и дальнейшего использования заданий
    // - RecycleView
    class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        //private Map<Integer, Task> mTasks;
        private List<Task> mTasks = new ArrayList<>();
        public TaskAdapter(List<Task> tasks){
            for (int i = 0; i<tasks.size(); i++){
                if(tasks.get(i).getTaskGroupId() == mCurrentTaskGroupId) mTasks.add(tasks.get(i));
            }
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            //Предпологаю, что потом здесь должна быть проверка на TTGroup
            Task task = mTasks.get(position);
                holder.bind(task);

        }

        @Override
        public int getItemCount() {
            return mTasks.size();
        }

        public void setTasks(List<Task> mTasks) {
            this.mTasks = mTasks;
        }

        public Task getTask(int position){
            return mTasks.get(position);

        }
    }

     class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private Task mTask;
        private TextView mTaskTextView;
        private TextView mDeadlineTexView;
        private TextView mPriority;

        public TaskHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.task_list_item_fragment,parent,false));
            mTaskTextView = itemView.findViewById(R.id.task_text);
            mDeadlineTexView = itemView.findViewById(R.id.deadline_date_text);
            mPriority = itemView.findViewById(R.id.task_priority_text);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task){
                mTask = task;
                mTaskTextView.setText(task.getTaskText());
                mDeadlineTexView.setText(task.getTaskDeadline().toString());
                mPriority.setText(getString(R.string.priority, Integer.toString(task.getTaskPriority()) ));

        }

         @Override
         public void onClick(View view) {
             Toast.makeText(getActivity(), "Вы нажали на " + mTask.getTaskText(), Toast.LENGTH_SHORT).show();
         }

         @Override
         public boolean onLongClick(View view) {
             return false;
         }
     }
     
     //Удаление объектов и выполнение задачи
     ItemTouchHelper.SimpleCallback mSimpleCallback  = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
         @Override
         public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
             return false;
         }

         @Override
         public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             final int postion = viewHolder.getAdapterPosition();
             final Task task = TaskLab.get(getActivity()).getTask(postion);

            switch (direction){
                case ItemTouchHelper.LEFT:
                    //Задание удалено

                    TaskLab.get(getActivity()).removeTask(TaskLab.get(getActivity()).getTask(postion));
                    mAdapter.notifyItemRemoved(postion);
                    mAdapter.setTasks(TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId));

                    Snackbar.make(mTaskRecycleView, R.string.delete_task_question,Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo_action, new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    putTaskBack(task);
                                    Toast.makeText(getActivity(), "Задание успещно возвращено.", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    break;


                case ItemTouchHelper.RIGHT:
                    //Задание принято

                    Snackbar.make(mTaskRecycleView, R.string.apply_task_question,Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo_action, new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    // putTaskBack(task);
                                    Toast.makeText(getActivity(), "Задание успещно возвращено.", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    break;
            }


         }

         @Override
         public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //Сторонняя библиотека для рисования иконок
             new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                   .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorDelete))
                     .addActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                     .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorApply))
                     .addSwipeRightActionIcon(R.drawable.ic_baseline_check_24)
                     .create()
                     .decorate();
             super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
         }
     };


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.task_type_group_list_item_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        List<Task> tasks = new ArrayList<>();
        switch (item.getItemId()){
            case R.id.sort:

                if(mSortState == 1) {
                    tasks = TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId);
                    tasks.sort(new TaskSortType1());
                    mSortState =2;
                }
                else if (mSortState == 2) {
                    tasks = TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId);
                    tasks.sort(new TaskSortType2());
                    mSortState = 1;
                }

                mAdapter.setTasks(tasks);
                mAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void putTaskBack(Task task){
        TaskLab.get(getActivity()).addTask(task);

        if(mSortState == 2) TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId).sort(new TaskSortType1());
        if(mSortState == 1) TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId).sort(new TaskSortType2());

        mAdapter.setTasks(TaskLab.get(getActivity()).getTasks(mCurrentTaskGroupId));
        mAdapter.notifyItemInserted(TaskLab.get(getActivity()).findTask(task));
    }



}
