package dipen.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditItemActivity extends AppCompatActivity {

    EditText editedTextVal;
    ImageButton backBtn;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        backBtn = (ImageButton) findViewById(R.id.backButton);
        saveBtn = (Button) findViewById(R.id.btn_Save);
        String textToEdit = getIntent().getStringExtra("editText");
        editedTextVal = (EditText)findViewById(R.id.editedText);
        editedTextVal.setText(textToEdit);
        editedTextVal.setSelection(textToEdit.length());


        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.backButton:
                        finish();
                        break;

                    case R.id.btn_Save:
                        Intent data = new Intent();
                        data.putExtra("saveEditedText",editedTextVal.getText().toString());
                        data.putExtra("code", 20);
                        setResult(RESULT_OK, data);
                        finish();
                        break;
                    default:
                        break;
                }

            }
        };

        backBtn.setOnClickListener(btnClickListener);
        saveBtn.setOnClickListener(btnClickListener);


    }
}
