package com.example.gaurav.pre69;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    int count=0;
    EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittext=(EditText) findViewById(R.id.main_input);
        takeInput();

    }
    private void takeInput() {
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkTheNumberOfTap();
            }
        });
    }
    private void checkTheNumberOfTap(){
            count++;
            if(count==2){
                edittext.setFocusable(true);
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                count=0;
            }
            /* TODO: Another feature to be implemented is to make the keyboard appear only at the time of two QUICK double clicks and not just two clicks made at any intervals*/
    }
}

