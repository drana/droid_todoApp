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

public class MainActivity extends AppCompatActivity implements EditItemFragment.EditItemDialogListener {
    ImageButton btnAddNewItems;
    ImageButton btnDeleteItems;
    Button btnEditItems;
    CheckBox checkboxItem;
    TextView textViewItem;
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
        //etNewItem.addTextChangedListener(newItemTextWatcher);
        setupButtonOnClickListener();
        setupListViewListener();


        //retrieve the data from add new item activity
        String newItemAdded = intentAddItem.getStringExtra("Add_New_Item");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        String currentDate = sdf.format(new Date());
        TodoItem newItem = new TodoItem(newItemAdded,currentDate);
        if(newItemAdded != null && !newItemAdded.isEmpty() && newItem !=null) {
            itemsAdapter.add(newItem);
            writeItems();
        }
    }

//    private void setupCheckBoxListner() {
//
//        checkboxItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox chkbx = (CheckBox)v;
//                if(chkbx.isChecked()){
//                    Boolean temp = true;
//                    int temp1 = 0;
//                }
//            }
//        });
//    }

    private void setupListViewListener() {

        //listener to edit items
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textPosition = position;
                //launchEditItemActivity(items.get(position).toString());
                arrayofItems.get(position).task.toString();

                onShowEditItem(arrayofItems.get(position).task.toString());
            }
        });

        //listener to remove items from list
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                        arrayofItems.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                });

    }

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

    private void onShowEditItem(String editedItem) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = EditItemFragment.newInstance(editedItem);
        editItemFragment.show(fm,"fragment_edit_item");

    }

    private void updateEditedText(String updatedString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Calendar c = Calendar.getInstance();
        String currentDate = sdf.format(new Date());
        TodoItem newItem = new TodoItem(updatedString,currentDate);

        arrayofItems.set(textPosition,newItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }



    private void OnDeleteItems(View v) {
        View child;
        for (int i = lvItems.getChildCount()-1;i >=0; i--){
            child = lvItems.getChildAt(i);
            checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
            Boolean temp = checkboxItem.isChecked();
            int test = 0;
            if(temp){
                arrayofItems.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                checkboxItem.setChecked(false);

            }

        }

    }

    private void OnEditItems(View v) {

        View child;
        btnEditItems = (Button) findViewById(R.id.btn_Edit);
        String buttonText = btnEditItems.getText().toString();

        if(buttonText.equals("Edit")) {

            for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

                child = lvItems.getChildAt(i);
                textViewItem = (TextView) child.findViewById(R.id.taskItem);
                checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.checkboxItem);
                textViewItem.setLayoutParams(params);

                checkboxItem.setVisibility(View.VISIBLE);
            }
            btnEditItems.setText("Cancel");
        }
        else if (buttonText.equals("Cancel"))
        {
            for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

                child = lvItems.getChildAt(i);
                textViewItem = (TextView) child.findViewById(R.id.taskItem);
                checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_LEFT);
                textViewItem.setLayoutParams(params);

                checkboxItem.setVisibility(View.INVISIBLE);
            }
            btnEditItems.setText("Edit");
        }


    }

    private void OnAddNewItem(View view) {

        Intent intent = new Intent(MainActivity.this, AddNewItems.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

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

    //callback from dialog fragment
    @Override
    public void onFinishEditDialog(String inputText) {

        updateEditedText(inputText);
    }




}

