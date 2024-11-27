/**
 * ID: 116687560
 * Name: Simon Kuang
 * Recitation: R4
 */


package com.example.playfaircypher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    boolean encryptMode = true;
    private KeyTable keytable;

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

        TextView keyTextView = findViewById(R.id.key);
        keyTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        keytable = KeyTable.buildFromString(keyTextView.getText().toString());
                        hideKeyboard(keyTextView);
                    } catch (IllegalArgumentException e) {
                        keytable = null;
                        Toast.makeText(MainActivity.this, "Invalid key!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Button enterButton = findViewById(R.id.ready);
        EditText input = findViewById(R.id.input);
        TextView result = findViewById(R.id.result);
        result.setTextIsSelectable(true);
        attachFocusListener(input);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keytable == null) {
                    Toast.makeText(MainActivity.this, "Key table is not initialized!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Phrase temp;
                    if (encryptMode) {
                        temp = Phrase.buildPhraseFromStringforEnc(input.getText().toString());
                        result.setText(temp.encrypt(keytable).toString());
                    } else {
                        temp = Phrase.buildPhraseFromStringforDec(input.getText().toString());
                        result.setText(temp.decrypt(keytable).toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "The letter " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toggleEncrypt(View view)
    {
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch toggleSwitch = findViewById(R.id.encryptSwitch);
        Button readyBotton = findViewById(R.id.ready);
        TextView enter = findViewById(R.id.enter);
        TextView input = findViewById(R.id.askForInputTextview);
        if(encryptMode) {
            toggleSwitch.setText("Decrypt Mode");
            readyBotton.setText("Decrypt!");
            enter.setText("Enter the key for decryption:");
            input.setText("Word to be decrypted:");
            encryptMode = false;
        }
        else {
            toggleSwitch.setText("Encrypt Mode");
            readyBotton.setText("Encrypt!");
            enter.setText("Enter the key for encryption:");
            input.setText("Word to be encrypted:");
            encryptMode = true;
        }
    }

    private void attachFocusListener(View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}