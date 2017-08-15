package dipen.todoapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.ParseException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNewItems extends AppCompatActivity implements  ModalFragment.OnFragmentInteractionListener{

    ImageButton btnSave;
    ImageButton btnCancel;
    EditText etNewItem;
    Spinner spinPriority;
    DatePicker dueDate;

    String isUpdateText = "0";
    private String itemText;
    private String itemDueDate;
    private String itemPriority;

    private String selectedItem;
    private String selectedDate;
    private String selectedPriority;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_items);



        btnSave =(ImageButton) findViewById(R.id.btnDoneNewItem);
        btnCancel = (ImageButton) findViewById(R.id.btnCancelNewItem);
        etNewItem = (EditText)findViewById(R.id.editTextNewItems);
        spinPriority = (Spinner)findViewById(R.id.itemPriority);
        dueDate = (DatePicker) findViewById(R.id.pickerDueDate);

        //onclick listener
        setupButtonOnClickListener();


        //edit text
        SelectedItemIntent();

        //create a list of items for the spinner.
        String[] items = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinPriority.setAdapter(adapter);


    }

    private void SelectedItemIntent() {
        if(getIntent().hasExtra("EDIT_SELECTED_ITEM")) {
            Bundle editbundle = getIntent().getExtras();

            selectedItem = editbundle.getString("EDIT_SELECTED_ITEM");
            selectedDate = editbundle.getString("EDIT_SELECTED_DATE");
            selectedPriority = editbundle.getString("EDIT_SELECTED_PRIORITY");
            position = editbundle.getInt("EDIT_SELECTED_POSITION");


            if(selectedItem !=null && !selectedItem.isEmpty()) {
                isUpdateText = "1";
                etNewItem.setText(selectedItem);
                //set priority
                SetSelectedPriority();

                //set selected date
                SetSelectedDate();

                etNewItem.requestFocus();
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        }

    }

    private void SetSelectedDate() {

        SimpleDateFormat sdfFormat = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date parsed = null;
        Calendar calSelected = Calendar.getInstance();
        int month, day,year;

        try {
            parsed = sdfFormat.parse(selectedDate);
            calSelected.setTime(parsed);
            month =   calSelected.get(Calendar.MONTH);
            day = calSelected.get(calSelected.DAY_OF_MONTH);
            year = calSelected.get(calSelected.YEAR);

            dueDate.updateDate(year,month,day);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private void SetSelectedPriority() {
        spinPriority.post(new Runnable() {
            public void run() {
                switch (selectedPriority){
                    case "Low":
                        spinPriority.setSelection(0);
                        break;
                    case "Medium":
                        spinPriority.setSelection(1);
                        break;
                    case "High":
                        spinPriority.setSelection(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // listeners for button clicks
    private void setupButtonOnClickListener() {

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btnDoneNewItem:
                        if(isUpdateText.equals("1")) {
                            // Return input text back to activity through the implemented listener
                           OnUpdateText(v);
                            break;
                        }
                        else if(isUpdateText.equals("0")) {
                            OnSaveNewItem(v);
                            break;
                        }
                        break;
                    case R.id.btnCancelNewItem:
                        OnCancelItem(v);
                        break;
                    default:
                        break;
                }
            }
        };

        btnSave.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);
    }

    private void OnCancelItem(View v) {

        itemText = "";

        if(etNewItem !=null) {
            itemText = etNewItem.getText().toString();
            if(!itemText.isEmpty()) {
                itemDueDate = getDueDate();
                itemPriority = spinPriority.getSelectedItem().toString();

                if(!selectedItem.equals(itemText)) {
                    DialogFragment newFragment = ModalFragment.newInstance("MSG_CLOSE");
                    newFragment.show(getFragmentManager(),"fragment_modal");
                }
                else {
                    Intent intent = new Intent(AddNewItems.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
            else{
                Intent intent = new Intent(AddNewItems.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }



    }

    // send updated todo item
    private void OnUpdateText(View v) {
        itemText = "";

        if(etNewItem !=null) {
            itemText = etNewItem.getText().toString();
            itemDueDate = getDueDate();
            itemPriority = spinPriority.getSelectedItem().toString();
        }
        if(itemText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intentExtra = new Intent(this, MainActivity.class);
        Bundle updatebundle = new Bundle();

        updatebundle.putString("UPDATED_NEW_ITEM",itemText);
        updatebundle.putString("UPDATED_ITEM_DUE_DATE",itemDueDate);
        updatebundle.putString("UPDATED_ITEM_PRIORITY",itemPriority);
        updatebundle.putInt("UPDATED_ITEM_POSITION",position);

        intentExtra.putExtras(updatebundle);
        intentExtra.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentExtra);
        finish();
    }

    //done adding new todo items
    private void OnSaveNewItem(View v){
        itemText = "";

        if(etNewItem !=null) {
            itemText = etNewItem.getText().toString();
            itemDueDate = getDueDate();
            itemPriority = spinPriority.getSelectedItem().toString();
        }
        if(itemText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT).show();
        }

        Intent saveIntent = new Intent(this, MainActivity.class);
        Bundle saveBundle = new Bundle();

        saveBundle.putString("SAVE_NEW_ITEM",itemText);
        saveBundle.putString("SAVE_ITEM_DUE_DATE",itemDueDate);
        saveBundle.putString("SAVE_ITEM_PRIORITY",itemPriority);

        saveIntent.putExtras(saveBundle);
        saveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(saveIntent);

    }

    private String getDueDate(){
        int month = dueDate.getMonth();
        int day = dueDate.getDayOfMonth();
        int year = dueDate.getYear();


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat sdfformat = new SimpleDateFormat("MM-dd-yyyy");
        String dueDateSelected = sdfformat.format(calendar.getTime());

        return  dueDateSelected;

    }


    public void onOkSelected() {

        Intent intent = new Intent(AddNewItems.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void onCancelSelected() {

    }

    @Override
    public void onFragmentInteraction(int Value) {

    }
}
