package com.sayan.sample.sampletexteditor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadWriteFileUtil.readFile(this, "fileName.txt", new ReadWriteFileUtil.ReadCallback() {
            @Override
            public void onSuccess(String s) {
                EditText editedText = findViewById(R.id.editedText);
                editedText.setText(s);
            }
        });
    }

    public void onClickSave(View view) {
        EditText editedText = findViewById(R.id.editedText);
        ReadWriteFileUtil.writeFile(this, editedText.getText().toString(), "fileName.txt", new ReadWriteFileUtil.WriteCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "File saved successfully.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
