package dipen.todoapp;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
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

public class MainActivity extends AppCompatActivity implements EditItemFragment.OnEditItemListener {
    ImageButton btnAddNewItems;
    ImageButton btnDeleteItems;
    ImageButton btnEditItems;
    ImageButton btnCancelEditItem;
    CheckBox checkboxItem;
    TextView textViewItem;
    TextView textViewDate;
    GridLayout gridItemView;
    TextView textViewItemsCount;
    ArrayList<TodoItem> arrayofItems = new ArrayList<TodoItem>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    private int textPosition = 0;
    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentAddItem = getIntent();


        btnAddNewItems = (ImageButton) findViewById(R.id.imgbtn_AddItem);
        btnDeleteItems = (ImageButton) findViewById(R.id.imgbtn_DeleteItem);
        btnEditItems = (ImageButton) findViewById(R.id.btn_Edit);
        btnCancelEditItem = (ImageButton) findViewById(R.id.btnCancelEditItems);
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
        count = arrayofItems.size();
        textViewItemsCount.setText(String.valueOf(count) + " Notes");

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
                count = arrayofItems.size();
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

                String taskEdit = arrayofItems.get(position).task;
                onEditItemSelected(taskEdit, position);
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
                        break;
                    case R.id.btnCancelEditItems:
                        OnCancelEditItems(v);
                        break;
                    default:
                        break;
                }
            }
        };

        btnAddNewItems.setOnClickListener(btnClickListener);
        btnEditItems.setOnClickListener(btnClickListener);
        btnDeleteItems.setOnClickListener(btnClickListener);
        btnCancelEditItem.setOnClickListener(btnClickListener);
    }

    private void OnCancelEditItems(View v) {
        OnCancelItems(v);
    }

    //add new item button clicked
    private void OnAddNewItem(View view) {

        Intent intent = new Intent(MainActivity.this, AddNewItems.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    //display updated todo item
    private void onEditItemSelected(String editedItem, int position) {

//        FrameLayout frag = (FrameLayout)findViewById(R.id.fragEditItems);
//        frag.setVisibility(View.VISIBLE);
//        EditItemFragment editItemFragment = new EditItemFragment();
//        Bundle args = new Bundle();
//        args.putString("Edit_Item",editedItem);
//        args.putInt("Position",position);
//        editItemFragment.setArguments(args);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        //fragmentTransaction.add(R.id.fragEditItems,editItemFragment);
//        fragmentTransaction.replace(R.id.fragEditItems,editItemFragment);
//        fragmentTransaction.commit();

        Intent intentExtra = new Intent(this, AddNewItems.class);
        Bundle editbundle = new Bundle();

        editbundle.putInt("Position",position);
        editbundle.putString("Edit_Item",editedItem);
        intentExtra.putExtras(editbundle);
        //intentExtra.putExtra("Edit_Item",editedItem);
        startActivity(intentExtra);
    }

    //edit button clicked
    private void OnEditItems(View v) {

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btnEditItems.getLayoutParams();
        params.rightMargin = 140;
        btnEditItems.setLayoutParams(params);

        btnDeleteItems.setEnabled(true);
        btnCancelEditItem.setVisibility(View.VISIBLE);
        OnButtonClickEdit(v);
    }

    private void OnCancelItems(View v){

        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btnEditItems.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        params.rightMargin = 10;
        btnEditItems.setLayoutParams(params);
        btnCancelEditItem.setVisibility(View.INVISIBLE);
        OnButtonClickDone(v);

    }

    private void OnButtonClickEdit(View v) {
        View child;
        for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

            child = lvItems.getChildAt(i);
            checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
            gridItemView = (GridLayout) child.findViewById(R.id.itemView);

            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.checkboxItem);
            gridItemView.setLayoutParams(params);
            checkboxItem.setVisibility(View.VISIBLE);
        }
        btnAddNewItems.setVisibility(View.INVISIBLE);
        btnDeleteItems.setVisibility(View.VISIBLE);
    }

    private void OnButtonClickDone(View v) {
        View child;
        for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

            child = lvItems.getChildAt(i);
            gridItemView = (GridLayout) child.findViewById(R.id.itemView);
            checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
            checkboxItem.setChecked(false);

            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_LEFT);
            gridItemView.setLayoutParams(params);
            checkboxItem.setVisibility(View.INVISIBLE);
        }
        btnAddNewItems.setVisibility(View.VISIBLE);
        btnDeleteItems.setVisibility(View.INVISIBLE);
        //btnEditItems.setText("Edit");
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

        OnCancelItems(v);
        count = arrayofItems.size();
        textViewItemsCount.setText(String.valueOf(count) + " Notes");

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

    @Override
    public void OnEditItemUpdateCompleted(String updatedItem) {

    }
}

