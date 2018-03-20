package com.example.gaurav.pre69;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    long initialTime = 0 , endTime = 0;
    EditText editText;
    ImageView micImageView;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText) findViewById(R.id.main_input);
        takeInput();

        micImageView = (ImageView) findViewById(R.id.mic_button);
        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }


    private void takeInput() {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(initialTime == 0) {
                    initialTime = System.currentTimeMillis();
                } else {
                    endTime = System.currentTimeMillis();
                    if(endTime - initialTime <=500) {
                        changeKeyboardStatus();
                    }
                    initialTime = endTime;
                }
            }
        });
    }


    private void changeKeyboardStatus() {
                editText.setFocusable(true);
                InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    insertCode(result.get(0));
                }
                break;
            }
        }
    }

    private void insertCode(String input) {
        int cursorPosition = editText.getSelectionStart();
        String codeBeforeCursor = editText.getText().toString().substring(0,cursorPosition);
        String codeAfterCursor = editText.getText().toString().substring(cursorPosition,editText.getText().toString().length());
        String codeToBeInserted = "";
        if(input.startsWith("declare integer")) {
            codeToBeInserted = "int" + input.substring(15) + ";"; //int,float,char, double, long int , long long int
            editText.setText(codeBeforeCursor + codeToBeInserted + codeAfterCursor);
            editText.setSelection(codeBeforeCursor.length() + 5);
        } else if(input.startsWith("declare long integer")) {
            codeToBeInserted = "long int" + input.substring(20) + ";";
        } else if(input.startsWith("declare long long integer")) {
            codeToBeInserted = "long long int" + input.substring(25) + ";";
        } else if(input.startsWith("declare float")) {
            codeToBeInserted = "float" + input.substring(13) + ";";
        } else if(input.startsWith("declare character")) {
            codeToBeInserted = "char" + input.substring(17) + ";";
        } else if(input.startsWith("declare double")) {
            codeToBeInserted = "double" + input.substring(14) + ";";
        } else if(input.equals("for loop")) {
            codeToBeInserted = "for(  ;  ;  ) {\n\n}";
        } else {
            Toast.makeText(getBaseContext(),"Try Again",Toast.LENGTH_SHORT);
        }

    }
}

