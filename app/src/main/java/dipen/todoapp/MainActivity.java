package dipen.todoapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageButton btnAddNewItems;
    ImageButton btnDeleteItems;
    Button btnEditItems;
    CheckBox checkboxItem;
    TextView textViewItem;
    TextView textViewDate;
    GridLayout gridItemView;
    TextView textViewItemsCount;
    ArrayList<TodoItem> arrayofItems = new ArrayList<TodoItem>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    private int textPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentAddItem = getIntent();


        btnAddNewItems = (ImageButton) findViewById(R.id.imgbtn_AddItem);
        btnDeleteItems = (ImageButton) findViewById(R.id.imgbtn_DeleteItem);
        btnEditItems = (Button) findViewById(R.id.btn_Edit);
        lvItems = (ListView) findViewById(R.id.lv_ListofItems);
        textViewItemsCount = (TextView) findViewById(R.id.txtview_NoOfItems);




        //check for existing items and read them
        try {
            readItems();}
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //attach adapter the listview
        itemsAdapter  = new ItemsAdapter(this,arrayofItems );
        lvItems.setAdapter(itemsAdapter);

        //click listener's
        setupButtonOnClickListener();
        setupListViewListener();


        //retrieve the data from add new item activity
        if(intentAddItem.hasExtra("Add_New_Item")){
            String newItemAdded = intentAddItem.getStringExtra("Add_New_Item");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy 'at' HH:mm:ss z");
            String currentDate = sdf.format(new Date());
            TodoItem newItem = new TodoItem(newItemAdded,currentDate);
            if(newItemAdded != null && !newItemAdded.isEmpty() && newItem !=null) {
                itemsAdapter.add(newItem);
                int count = arrayofItems.size();
                textViewItemsCount.setText(String.valueOf(count) + " Notes");
                writeItems();
            }
        }


        //retrieve data from updated text
        if(getIntent().hasExtra("Update_New_Item")){

            Bundle updatedbundle = getIntent().getExtras();
            //String updatedTextItem = intentAddItem.getStringExtra("Update_New_Item");
            String updatedTextItem = updatedbundle.getString("Update_New_Item");
            textPosition = updatedbundle.getInt("Position");
            if (updatedTextItem != null && !updatedTextItem.isEmpty()) {
            updateEditedText(updatedTextItem, textPosition);
            }

        }


        //disable delete
        btnDeleteItems.setEnabled(false);
    }

    //listener for item list changes
    private void setupListViewListener() {

        //listener to edit items
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textPosition = position;
                //launchEditItemActivity(items.get(position).toString());
                arrayofItems.get(position).task.toString();
                onShowEditItem(arrayofItems.get(position).task.toString(), position);
            }
        });
    }

    //listener for button clicks
    private void setupButtonOnClickListener() {

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.imgbtn_AddItem:
                        OnAddNewItem(v);
                        break;
                    case R.id.btn_Edit:
                        OnEditItems(v);
                        break;
                    case R.id.imgbtn_DeleteItem:
                        OnDeleteItems(v);
                    default:
                        break;
                }
            }
        };

        btnAddNewItems.setOnClickListener(btnClickListener);
        btnEditItems.setOnClickListener(btnClickListener);
        btnDeleteItems.setOnClickListener(btnClickListener);
    }

    //display updated todo item
    private void onShowEditItem(String editedItem, int position) {

        Intent intentExtra = new Intent(this, AddNewItems.class);
        Bundle editbundle = new Bundle();

        editbundle.putInt("Position",position);
        editbundle.putString("Edit_Item",editedItem);
        intentExtra.putExtras(editbundle);
        //intentExtra.putExtra("Edit_Item",editedItem);
        startActivity(intentExtra);
    }

    //send todo item for update
    private void updateEditedText(String updatedString,int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy 'at' HH:mm:ss z");
        Calendar c = Calendar.getInstance();
        String currentDate = sdf.format(new Date());
        TodoItem newItem = new TodoItem(updatedString,currentDate);

        arrayofItems.set(position,newItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    // delete all selected items
    private void OnDeleteItems(View v) {
        View child;
        for (int i = lvItems.getChildCount()-1;i >=0; i--){
            child = lvItems.getChildAt(i);
            checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
            if(checkboxItem.isChecked()){
                arrayofItems.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                checkboxItem.setChecked(false);
            }

        }

    }

    //edit button clicked
    private void OnEditItems(View v) {

        View child;
        btnDeleteItems.setEnabled(true);
        btnEditItems = (Button) findViewById(R.id.btn_Edit);
        String buttonText = btnEditItems.getText().toString();

        if(buttonText.equals("Edit")) {

            for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

                child = lvItems.getChildAt(i);
                //textViewItem = (TextView) child.findViewById(R.id.taskItem);
                //textViewDate = (TextView) child.findViewById(R.id.taskDate);
                checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
                gridItemView = (GridLayout) child.findViewById(R.id.itemView);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.checkboxItem);
                gridItemView.setLayoutParams(params);
                //textViewItem.setLayoutParams(params);
                //textViewDate.setLayoutParams(params);
                checkboxItem.setVisibility(View.VISIBLE);
            }
            btnEditItems.setText(R.string.done_button);
        }
        else if (buttonText.equals("Done"))
        {
            for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

                child = lvItems.getChildAt(i);
                gridItemView = (GridLayout) child.findViewById(R.id.itemView);
                //textViewItem = (TextView) child.findViewById(R.id.taskItem);
                //textViewDate = (TextView) child.findViewById(R.id.taskDate);
                checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_LEFT);
                gridItemView.setLayoutParams(params);
                //textViewItem.setLayoutParams(params);
                //textViewDate.setLayoutParams(params);
                checkboxItem.setVisibility(View.INVISIBLE);
            }
            btnEditItems.setText("Edit");
        }


    }

    //add new item button clicked
    private void OnAddNewItem(View view) {

        Intent intent = new Intent(MainActivity.this, AddNewItems.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    //read items from db
    private void readItems() throws IOException, ClassNotFoundException{
        try {
            String filename = "todo.srl";
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator+filename)));

            if(input !=null) {
                arrayofItems = (ArrayList<TodoItem>) input.readObject();
            }
            input.close();


        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //write items to db
    private void writeItems() {
        String filename = "todo.srl";
        try {
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), "") + File.separator + filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayofItems);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

