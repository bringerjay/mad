package edu.neu.madcourse.numad20s_qizhou.model;
import java.io.Serializable;

public class Task implements Serializable {
    public String taskId;
    public String taskTitle;
    public String taskDescription;
    public StringBuilder taskDate;
    public String taskTime;
    public Card.CardPriority cardPriority = Card.CardPriority.LOW;
    public Card.CardStatus cardStatus = Card.CardStatus.TODO;
    public String taskImagePath = null;

    public Task(String taskId, String taskTitle, String taskDescription, StringBuilder taskDate, String taskTime,
                Card.CardPriority cardPriority, Card.CardStatus cardStatus, String taskImagePath)
    {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.cardPriority = cardPriority;
        this.cardStatus = cardStatus;
        this.taskImagePath = taskImagePath;
    }

}
