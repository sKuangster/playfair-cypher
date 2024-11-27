package com.example.playfaircypher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    boolean encryptMode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }

    public void toggleEncrypt(View view)
    {
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch toggleSwitch = findViewById(R.id.encryptSwitch);
        TextView enter = findViewById(R.id.enter);
        if(encryptMode) {
            toggleSwitch.setText("Decrypt Mode");
            enter.setText("Enter the key for decryption:");
            encryptMode = false;
        }
        else {
            toggleSwitch.setText("Encrypt Mode");
            enter.setText("Enter the key for encryption:");
            encryptMode = true;
        }
    }
}