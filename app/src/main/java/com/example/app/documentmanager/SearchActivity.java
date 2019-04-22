package com.example.app.documentmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity  implements View.OnClickListener {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar)findViewById(R.id.serch_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final EditText serchText = (EditText)findViewById(R.id.serch_text);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.
                    permission.READ_EXTERNAL_STORAGE }, 1);
        } else {
            read();
        }
        serchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(SearchActivity.this, serchText.getText()+"", Toast.LENGTH_SHORT).show();
                //进行搜索功能实现

            }
        });
        serchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(SearchActivity.this, "点击了软键盘中的搜索按钮", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    private void read(){
        //Uri uri = Uri.parse("content://com.android.providers.downloads.documents/downloads");
        Cursor cursor= null;
        try {
            cursor = getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("cursor",cursor.toString());
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    read();
                } else {
                    Toast.makeText(SearchActivity.this, "requestCode", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }

    @Override
    public void onClick(View v) {

    }
}
