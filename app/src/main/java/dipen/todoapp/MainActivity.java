package dipen.todoapp;


import android.content.Intent;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

public class MainActivity extends AppCompatActivity implements  ModalFragment.OnFragmentInteractionListener{
    ImageButton btnAddNewItems;
    ImageButton btnDeleteItems;
    ImageButton btnEditItems;
    ImageButton btnCancelEditItem;
    CheckBox checkboxItem;
    TextView textViewItem;
    DatePicker datePick;
    RelativeLayout gridItemView;
    TextView textViewItemsCount;
    ArrayList<TodoItem> arrayofItems = new ArrayList<TodoItem>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    private int textPosition = 0;
    private int count =0;

    private String selectedItem;
    private String selectedDate;
    private String selectedPriority;

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
        //set no of to do items
        count = arrayofItems.size();
        textViewItemsCount.setText(String.valueOf(count) + " Notes");

        //click listener's
        setupButtonOnClickListener();
        setupListViewListener();

        //retrieve the data from add new item activity
        onSaveItemIntent();

        //retrieve data from updated text
        onUpdateItemIntent();

        btnDeleteItems.setEnabled(false);

    }

    //listener for item list changes
    private void setupListViewListener() {

        //listener to edit items
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textPosition = position;

                selectedItem = arrayofItems.get(position).task;
                selectedDate = arrayofItems.get(position).taskDate;
                selectedPriority = arrayofItems.get(position).taskPriority;
                onEditItemSelected(selectedItem, selectedDate,selectedPriority);
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
                        onShowModalBox(v);
                        //OnDeleteItems(v);
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

    //add new item button clicked
    private void OnAddNewItem(View view) {

        Intent intent = new Intent(MainActivity.this, AddNewItems.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    //save new item
    private void onSaveItemIntent() {
        if(getIntent().hasExtra("SAVE_NEW_ITEM")){
            Bundle saveBundle = getIntent().getExtras();

            String item = saveBundle.getString("SAVE_NEW_ITEM");
            String itemPriority = saveBundle.getString("SAVE_ITEM_PRIORITY");
            String formatedDate = saveBundle.getString("SAVE_ITEM_DUE_DATE");
            TodoItem newItem = new TodoItem(item,formatedDate,itemPriority);

            if (newItem != null) {
                itemsAdapter.add(newItem);
                count = arrayofItems.size();
                textViewItemsCount.setText(String.valueOf(count) + " Notes");
                writeItems();
            }
        }
    }

    //display updated todo item
    private void onEditItemSelected(String itemSelected, String dateSelected, String prioritySelected) {

        Intent intentExtra = new Intent(this, AddNewItems.class);
        Bundle selectedbundle = new Bundle();

        selectedbundle.putString("EDIT_SELECTED_ITEM",itemSelected);
        selectedbundle.putString("EDIT_SELECTED_DATE",dateSelected);
        selectedbundle.putString("EDIT_SELECTED_PRIORITY",prioritySelected);
        selectedbundle.putInt("EDIT_SELECTED_POSITION",textPosition);
        intentExtra.putExtras(selectedbundle);

        startActivity(intentExtra);
    }

    //update the selected item
    private void onUpdateItemIntent() {
        //retrieve data from updated text

        if(getIntent().hasExtra("UPDATED_NEW_ITEM")){
            Bundle updatedbundle = getIntent().getExtras();

            String updatedItem = updatedbundle.getString("UPDATED_NEW_ITEM");
            String updatedPriority = updatedbundle.getString("UPDATED_ITEM_DUE_DATE");
            String updatedDate = updatedbundle.getString("UPDATED_ITEM_PRIORITY");
            int updatedPosition = updatedbundle.getInt("UPDATED_ITEM_POSITION");
            TodoItem updateItems = new TodoItem(updatedItem,updatedDate,updatedPriority);

            if (updateItems != null) {
                arrayofItems.set(updatedPosition,updateItems);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }

        }
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

    //on new item cancel
    private void OnCancelItems(View v){
        Boolean result = checkItemUpdated(v);
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btnEditItems.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        params.rightMargin = 10;
        btnEditItems.setLayoutParams(params);
        btnCancelEditItem.setVisibility(View.INVISIBLE);
        OnButtonClickDone(v);

    }

    private void OnCancelEditItems(View v) {
        OnCancelItems(v);
    }

    private Boolean checkItemUpdated(View v) {
        return  false;
    }

    //click edit button
    private void OnButtonClickEdit(View v) {
        View child;
        for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

            child = lvItems.getChildAt(i);
            checkboxItem = (CheckBox) child.findViewById(R.id.checkboxItem);
            gridItemView = (RelativeLayout) child.findViewById(R.id.itemView);

            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, R.id.checkboxItem);
            gridItemView.setLayoutParams(params);
            checkboxItem.setVisibility(View.VISIBLE);
        }
        btnAddNewItems.setVisibility(View.INVISIBLE);
        btnDeleteItems.setVisibility(View.VISIBLE);
    }

    //click save button
    private void OnButtonClickDone(View v) {
        View child;
        for (int i = lvItems.getChildCount() - 1; i >= 0; i--) {

            child = lvItems.getChildAt(i);
            gridItemView = (RelativeLayout) child.findViewById(R.id.itemView);
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

    // delete all selected items
    private void OnDeleteItems() {
        View currentFocus = getWindow().getCurrentFocus();

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

        OnCancelItems(currentFocus.getRootView());
        count = arrayofItems.size();
        textViewItemsCount.setText(String.valueOf(count) + " Notes");

    }

    private void onShowModalBox(View v) {
        DialogFragment newFragment = ModalFragment.newInstance("MSG_DELETE");
        newFragment.show(getFragmentManager(),"fragment_modal");
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
    public void onFragmentInteraction(int Value) {
        int temp;
        if(Value == 1){
            temp =1;
        }
        else if(Value == 0){
            temp=0;
        }
    }

    public void onOkSelected() {
        OnDeleteItems();
        
    }

    public void onCancelSelected() {

    }
}

