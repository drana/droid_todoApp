package dipen.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dipenrana on 6/22/16.
 */
public class ItemsAdapter extends ArrayAdapter<TodoItem>{

    public ItemsAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context,0,todoItems);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // Get the data item for this position
        TodoItem todoItem = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todoitems, parent, false);
        }
        // Lookup view for data population
        TextView taskName = (TextView) convertView.findViewById(R.id.taskItem);
        TextView taskDate = (TextView) convertView.findViewById(R.id.taskDate);
        TextView taskPriority = (TextView) convertView.findViewById(R.id.taskPriority);

        // Populate the data into the template view using the data object

        taskName.setText(todoItem.task);
        taskDate.setText(todoItem.taskDate);
        taskPriority.setText(todoItem.taskPriority);


        // Return the completed view to render on screen
        return convertView;
    }
}
