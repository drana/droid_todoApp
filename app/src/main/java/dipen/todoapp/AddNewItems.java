package dipen.todoapp;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNewItems extends AppCompatActivity {
    ImageButton btnBack;
    Button btnDone;
    EditText etNewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_items);

        btnBack = (ImageButton)findViewById(R.id.btnGoBack);
        btnDone =(Button) findViewById(R.id.btnDoneNewItem);

        //onclick listener
        setupButtonOnClickListener();

    }

    private void setupButtonOnClickListener() {

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.btnDoneNewItem:
                        OnDoneAddNewItem(v);
                        break;
                    case R.id.btnGoBack:
                        OnGoBackToMain(v);
                        break;
                    default:
                        break;
                }
            }
        };

        btnDone.setOnClickListener(btnClickListener);
        btnBack.setOnClickListener(btnClickListener);
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
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
//        Calendar c = Calendar.getInstance();
//        String currentDate = sdf.format(new Date());
//        TodoItem newItem = new TodoItem(newItemText,currentDate);
//        TodoItem newItem = new TodoItem(newItemText,currentDate);
//        itemsAdapter.add(newItem);
//        etNewItem.setText("");
//        writeItems();
    }

    //go back to main page
    private void OnGoBackToMain(View v) {

        Intent intent = new Intent(AddNewItems.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
