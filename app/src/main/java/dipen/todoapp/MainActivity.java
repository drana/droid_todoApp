package dipen.todoapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

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
    Button btnaddNewItem;
    EditText etNewItem;
    ArrayList<TodoItem> arrayofItems = new ArrayList<TodoItem>();
    ItemsAdapter itemsAdapter;
    ListView lvItems;
    private int textPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnaddNewItem = (Button) findViewById(R.id.btn_AddItem);
        //btnBlank = (Button) findViewById(R.id.button_Blank);
        etNewItem = (EditText) findViewById(R.id.et_NewItem);
        lvItems = (ListView) findViewById(R.id.lv_ListofItems);
        //check for existing items and read them
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
        etNewItem.addTextChangedListener(newItemTextWatcher);
        setupButtonOnClickListener();
        setupListViewListener();
    }

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
        //itemsAdapter.add(newItem);
        //arrayofItems.get(position).task.toString();
        arrayofItems.set(textPosition,newItem);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    private void setupButtonOnClickListener() {

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btn_AddItem:
                        OnAddNewItem(v);
                        break;
                    default:
                        break;
                }
            }
        };

        btnaddNewItem.setOnClickListener(btnClickListener);
    }

    private void OnAddNewItem(View view) {
        etNewItem = (EditText) findViewById(R.id.et_NewItem);
        String newItemText = "";
        if(etNewItem != null) {
            newItemText = etNewItem.getText().toString();
        }
        if(newItemText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Calendar c = Calendar.getInstance();
        String currentDate = sdf.format(new Date());
        TodoItem newItem = new TodoItem(newItemText,currentDate);
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() throws IOException, ClassNotFoundException{
        try {
            String filename = "todo.srl";
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator+filename)));

            //ArrayList<TodoItem> itemsObject = (ArrayList<TodoItem>) input.readObject();
            arrayofItems = (ArrayList<TodoItem>) input.readObject();
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

    //  create a textWatcher member to enable and disable AddNewItem button with respect to etNewItem
    private TextWatcher newItemTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {

            // check etNewItem for empty values
            checkNewItemForEmptyValues();
        }
        void checkNewItemForEmptyValues(){
            Button btnAddItem = (Button)findViewById(R.id.btn_AddItem);

            String newItem = etNewItem.getText().toString();

            if(newItem.equals("")){
                btnAddItem.setEnabled(false);
            } else {
                btnAddItem.setEnabled(true);
            }
        }
    };

    //callback from dialog fragment
    @Override
    public void onFinishEditDialog(String inputText) {

        updateEditedText(inputText);
    }


}

