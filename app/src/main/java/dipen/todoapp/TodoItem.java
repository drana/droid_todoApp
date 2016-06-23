package dipen.todoapp;

import java.io.Serializable;

/**
 * Created by dipenrana on 6/22/16.
 */
public class TodoItem implements Serializable{
    public String task;
    public String taskDate;

    public TodoItem(String task, String taskDate)
    {
        this.task = task;
        this.taskDate = taskDate;
    }
}
