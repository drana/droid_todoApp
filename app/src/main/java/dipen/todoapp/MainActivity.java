package dipen.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnaddNewItem;
    Button btnBlank;
    EditText etNewItem;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    private int textPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnaddNewItem = (Button) findViewById(R.id.btn_AddItem);
        btnBlank = (Button) findViewById(R.id.button_Blank);
        etNewItem = (EditText) findViewById(R.id.et_NewItem);
        lvItems = (ListView) findViewById(R.id.lv_ListofItems);
        //check for existing items and read them
        readItems();
        //attach adapter the listview
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        //click listener's
        setupButtonOnClickListener();
        setupListViewListener();
    }

    private void setupListViewListener() {

        //listener to edit items
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textPosition = position;
                launchEditItemActivity(items.get(position).toString());
            }
        });

        //listener to remove items from list
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                });
    }

    private void launchEditItemActivity(String editItemText) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("editText", editItemText);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String temp = data.getExtras().getString("saveEditedText");
            updateEditedText(temp);
        }
    }

    private void updateEditedText(String temp) {
        items.set(textPosition,temp);
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
                    case R.id.button_Blank:
                        OnBlankBtnPress(v);
                        break;
                    default:
                        break;
                }
            }
        };

        btnaddNewItem.setOnClickListener(btnClickListener);
    }

    private void OnBlankBtnPress(View v) {
       /* Intent intetBlank = new Intent(this,BlankActivity.class);
        startActivity(intetBlank);*/
    }

    private void OnAddNewItem(View view) {
        etNewItem = (EditText) findViewById(R.id.et_NewItem);
        String newItemText = etNewItem.getText().toString();
        itemsAdapter.add(newItemText);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir,"todo.txt");
        try{
                items = new ArrayList<String>(FileUtils.readLines(todoFile));

        }catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile,items);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
