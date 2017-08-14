package dipen.todoapp;

import java.io.Serializable;

/**
 * Created by dipenrana on 6/22/16.
 */
public class TodoItem implements Serializable{
    public String task;
    public String taskDate;
    public String taskPriority;

    public TodoItem(String task, String taskDate, String taskPriority)
    {
        this.task = task;
        this.taskDate = taskDate;
        this.taskPriority = taskPriority;
    }
}
