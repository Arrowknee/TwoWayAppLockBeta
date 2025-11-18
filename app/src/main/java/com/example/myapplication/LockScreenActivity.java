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

//        Button unlockButton = findViewById(R.id.unlock_button);
//        unlockButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                finish();
//            }
//        });
    }
}