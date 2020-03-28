package edu.neu.madcourse.numad20s_qizhou.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.numad20s_qizhou.R;
import edu.neu.madcourse.numad20s_qizhou.activities.MainActivity;
import edu.neu.madcourse.numad20s_qizhou.activities.TaskDetailActivity;
import edu.neu.madcourse.numad20s_qizhou.managers.TaskManager;
import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.model.Task;
import edu.neu.madcourse.numad20s_qizhou.utils.BitmapTools;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment implements MainActivity.OnMainViewsClickListener {


    //
    // region properties
    //

    private RecyclerView tasksRecyclerView;
    private Context taskFragmentContext;
    private List<Task> itemList = new ArrayList<>();
    private Card.CardPriority prioritySelected = null;
    private Card.CardStatus statusSelected = null;
    public static final String EXISTING_ID = "existingTaskId";
    public static final String IS_EXISTING_TASK = "isUserClickedExistingTask";
    private boolean isKanbanPage = false;


    public static TasksFragment newInstance(boolean isKanbanPage) {
        TasksFragment myFragment = new TasksFragment();

        Bundle args = new Bundle();
        args.putBoolean("KANBAN", isKanbanPage);
        myFragment.setArguments(args);

        return myFragment;
    }

    private void updateItemsListAndNotifyAdapter(Boolean notify) {
        if(prioritySelected != null && !isKanbanPage)
            itemList = TaskManager.getInstance().getItemsOfPriority(prioritySelected);
        else if(statusSelected!= null && isKanbanPage)
            itemList = TaskManager.getInstance().getItemsOfStatus(statusSelected);
        else
            itemList = TaskManager.getInstance().tasksList;

        if(notify)
            tasksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    //  endregion


    //
    // region overrides
    //


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        taskFragmentContext = context;
        ((MainActivity)getActivity()).fragmentInterfaceListener = this;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isKanbanPage = getArguments() != null && getArguments().getBoolean("KANBAN", false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tasks_fragment, container, false);
        tasksRecyclerView = view.findViewById(R.id.tasksList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //itemList = TaskManager.getInstance().tasksList;
        updateItemsListAndNotifyAdapter(false);
        initializeRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateItemsListAndNotifyAdapter(true);
    }

    @Override
    public void onDeleteClicked() {
        updateItemsListAndNotifyAdapter(true);
    }


    @Override
    public void onPriorityButtonClicked(Card.CardPriority cardPriority) {
        prioritySelected = cardPriority;
        if(isKanbanPage){
            isKanbanPage = false;
            initializeRecyclerView();
        }

        updateItemsListAndNotifyAdapter(true);
    }
    @Override
    public void onStatusButtonClicked(Card.CardStatus cardStatus) {
        statusSelected = cardStatus;
        if(!isKanbanPage){
            isKanbanPage = true;
            initializeRecyclerView();
        }
        updateItemsListAndNotifyAdapter(true);
    }

    //  endregion


    //
    // region initializations
    //

    private void initializeRecyclerView() {

        // Create an instance of SectionedRecyclerViewAdapter
        final SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

        // Add your Sections
        sectionAdapter.addSection(new MySection());
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(taskFragmentContext, LinearLayoutManager.VERTICAL, false));
        tasksRecyclerView.setAdapter(sectionAdapter);
    }

    //  endregion


    //
    // region inner structure
    //

    class MySection extends StatelessSection {

        public MySection() {
            // call constructor with layout resources for this Section header and items
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.tasks_item)
                    .build());
        }

        @Override
        public int getContentItemsTotal() {
            return itemList.size(); // number of items of this section
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            // return a custom instance of ViewHolder for the items of this section
            return new MyItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

            // bind your view here
            itemHolder.hydrate(itemList.get(position));
        }
    }


    class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitleLabel;
        private final TextView taskDescLabel;
        private final TextView taskDateLabel;
        private final TextView taskTimeLabel;
        private final ImageView taskImage;

        private View view = null;

        public MyItemViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            taskTitleLabel = itemView.findViewById(R.id.taskTitle);
            taskDescLabel = itemView.findViewById(R.id.taskDescription);
            taskDateLabel = (TextView) itemView.findViewById(R.id.task_item_date);
            taskTimeLabel = (TextView) itemView.findViewById(R.id.task_item_time);
            taskImage = (ImageView) itemView.findViewById(R.id.taskImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(taskFragmentContext, TaskDetailActivity.class);
                    intent.putExtra(EXISTING_ID, itemList.get(getAdapterPosition()).taskId);
                    intent.putExtra(IS_EXISTING_TASK, true);
                    startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteWarningDialog(getAdapterPosition());
                    return false;
                }
            });

        }

        public void hydrate(Task task) {
            if (task.taskImagePath != null) {
                taskImage.setVisibility(View.VISIBLE);
                taskImage.setImageBitmap(BitmapTools.decodeSampledBitmapFromPath(task.taskImagePath, 100, 100));
            }
            else {
                taskImage.setVisibility(View.GONE);
            }

            taskTitleLabel.setText(task.taskTitle);
            taskDescLabel.setText(task.taskDescription);;
            taskDateLabel.setText(task.taskDate);
            taskTimeLabel.setText(task.taskTime);
            if(view!=null && isKanbanPage){
                switch (task.cardStatus) {
                    case TODO: view.setBackgroundColor(getResources().getColor(R.color.todoColor));
                        break;
                    case INPROGRESS: view.setBackgroundColor(getResources().getColor(R.color.progressColor));
                        break;
                    case COMPLETED: view.setBackgroundColor(getResources().getColor(R.color.completedColor));
                        break;
                }

            }
        }
    }

    private void showDeleteWarningDialog(final int adapterPosition) {
        if ( isVisible() ) {

            AlertDialog.Builder build= new AlertDialog.Builder(taskFragmentContext);
            build.setTitle("Are you sure you want to remove this task?");
            build.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TaskManager.getInstance().removeTaskItem(itemList.get(adapterPosition));
                    updateItemsListAndNotifyAdapter(true);
                }
            });
            build.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog= build.create();
            dialog.show();

        }
    }

    // endregion

}
