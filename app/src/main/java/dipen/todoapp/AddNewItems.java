package dipen.todoapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewItems extends AppCompatActivity {

    ImageButton btnDone;
    ImageButton btnCancel;
    EditText etNewItem;
    String isUpdateText = "0";
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_items);



        btnDone =(ImageButton) findViewById(R.id.btnDoneNewItem);
        btnCancel = (ImageButton) findViewById(R.id.btnCancelNewItem);
        etNewItem = (EditText)findViewById(R.id.editTextNewItems);


        //get focus and keyboard
        etNewItem.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //onclick listener
        setupButtonOnClickListener();

        //edit text
        //String updateText = intentEditItem.getStringExtra("Edit_Item");
        if(getIntent().hasExtra("Edit_Item")) {
            Bundle editbundle = getIntent().getExtras();

            String updateText = editbundle.getString("Edit_Item");
            position = editbundle.getInt("Position");
            if(updateText !=null && !updateText.isEmpty()) {
                isUpdateText = "1";
                etNewItem.setText(updateText);
                etNewItem.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        }
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
                            OnDoneAddNewItem(v);
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

        btnDone.setOnClickListener(btnClickListener);
        btnCancel.setOnClickListener(btnClickListener);
    }

    private void OnCancelItem(View v) {
        Intent intent = new Intent(AddNewItems.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    // send updated todo item
    private void OnUpdateText(View v) {
        String newItemText = "";
        //etNewItem = (EditText)findViewById(R.id.editTextNewItems);
        if(etNewItem !=null) {
            newItemText = etNewItem.getText().toString();
        }
        if(newItemText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intentExtra = new Intent(this, MainActivity.class);
        Bundle updatebundle = new Bundle();
        updatebundle.putInt("Position",position);
        updatebundle.putString("Update_New_Item",newItemText);
        intentExtra.putExtras(updatebundle);
        //intentExtra.putExtra("Update_New_Item",newItemText);
        startActivity(intentExtra);
    }

    //done adding new todo items
    private void OnDoneAddNewItem(View v){
        String newItemText = "";
        etNewItem = (EditText)findViewById(R.id.editTextNewItems);
        if(etNewItem !=null) {
            newItemText = etNewItem.getText().toString();
        }
        if(newItemText.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intentExtra = new Intent(this, MainActivity.class);
        intentExtra.putExtra("Add_New_Item",newItemText);
        startActivity(intentExtra);
    }

}
