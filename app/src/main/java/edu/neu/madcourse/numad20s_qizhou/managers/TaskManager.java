package edu.neu.madcourse.numad20s_qizhou.managers;

import android.content.Context;

import edu.neu.madcourse.numad20s_qizhou.KanbanApplication;
import edu.neu.madcourse.numad20s_qizhou.data.Storage;
import edu.neu.madcourse.numad20s_qizhou.model.Card;
import edu.neu.madcourse.numad20s_qizhou.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TaskManager {

    public List<Task> tasksList = new ArrayList<Task>();

    private static TaskManager uniqInstance;

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        if (uniqInstance == null) {
            synchronized (TaskManager.class) {
                // check again to avoid multi-thread access
                if (uniqInstance == null)
                    uniqInstance = new TaskManager();
            }
        }
        return uniqInstance;
    }
    // other useful methods here

    public void addTaskItem(Task item) {
        tasksList.add(item);
    }

    public void replaceExistingTaskItem(String id, Task newTask) {
        int i = 0;
        for(Task task : tasksList) {
            if(task.taskId.equals(id)) {
                //found it!
                tasksList.set(i, newTask);
                break;
            }
            i++;
        }
    }

    public Task getTaskWithId(String id) {
        Task task = null;
        //int i = 0;
        for(Task taskItem : tasksList) {
            if(taskItem.taskId.equals(id)) {
                //found it!
                task = taskItem;//tasksList.get(i);
                break;
            }
            //i++;
        }
        return task;
    }

    public List<Task> getItemsOfPriority(Card.CardPriority cardPriority) {
        List<Task> priorityList = new ArrayList<>();
        int i = 0;
        for(Task taskItem : tasksList) {
            if(taskItem.cardPriority.equals(cardPriority)) {
                //found it!
                priorityList.add(taskItem);
            }
            i++;
        }
        return priorityList;
    }
    public List<Task> getItemsOfStatus(Card.CardStatus cardStatus) {
        List<Task> statusList = new ArrayList<>();
        int i = 0;
        for(Task taskItem : tasksList) {
            if(taskItem.cardStatus.equals(cardStatus)) {
                //found it!
                statusList.add(taskItem);
            }
            i++;
        }
        return statusList;
    }

    public void removeTaskItem(Task item) {
        tasksList.remove(item);
    }

    public void removeTaskAtPosition(int position) {
        tasksList.remove(position);
    }

    public void removeTaskWithTaskId(int position) {
        tasksList.remove(position);
    }

    public void removeAllTasks() {
        tasksList.clear();
    }

    public int totalNoOfTasks() { return tasksList.size(); }

    public void saveTask(Context context) {
        Storage storage = new Storage(context);
        storage.store(tasksList, "TasksFile", Storage.Directory.Documents);
    }

    public void initialize() {
        Storage storage = new Storage(KanbanApplication.getInstance());
        List<Task> itemList = (List<Task>) storage.retrieve("TasksFile", Storage.Directory.Documents);
        if (itemList!=null) { // BUG of a BITCH
            tasksList = itemList;
        }
    }

    public int getPriorityNumber(Card.CardPriority cardPriority) {
        int itemSelected = 0;
        if(cardPriority.equals(Card.CardPriority.LOW))
            itemSelected = 0;
        else if(cardPriority.equals(Card.CardPriority.MEDIUM))
            itemSelected = 1;
        else if(cardPriority.equals(Card.CardPriority.HIGH))
            itemSelected = 2;
        return itemSelected;
    }
    public int getStatusNumber(Card.CardStatus cardStatus) {
        int itemSelected = 0;
        if(cardStatus.equals(Card.CardStatus.TODO))
            itemSelected = 0;
        else if(cardStatus.equals(Card.CardStatus.INPROGRESS))
            itemSelected = 1;
        else if(cardStatus.equals(Card.CardStatus.COMPLETED))
            itemSelected = 2;
        return itemSelected;
    }
}
