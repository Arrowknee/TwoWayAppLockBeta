package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_layout_1);
        System.out.println("Attempt to pull layout");
//TODO: Add a usable clickable button

//        Button returnButton = findViewById(R.id.return_button);
//        returnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                finish();
//            }
//        });
    }

}