package com.example.app.documentmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout imageLayout;
    private LinearLayout audioLayout;
    private LinearLayout videoLayout;
    private LinearLayout documentLayout;
    private LinearLayout downloadLayout;
    private LinearLayout apkLayout;
    private LinearLayout phonestorage;
    private Toolbar toolbar;




    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        init();
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("文件管理器");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_serch){
                    Toast.makeText(MainActivity.this , "serch" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);

                }else if (menuItemId == R.id.action_settings){
                    Toast.makeText(MainActivity.this , "setting" , Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }




    private  void  init(){
        imageLayout = (LinearLayout)findViewById(R.id.image);
        audioLayout = (LinearLayout)findViewById(R.id.audio);
        videoLayout = (LinearLayout)findViewById(R.id.video);
        documentLayout = (LinearLayout)findViewById(R.id.document);
        downloadLayout = (LinearLayout)findViewById(R.id.download);
        apkLayout = (LinearLayout)findViewById(R.id.apk);
        phonestorage = (LinearLayout)findViewById(R.id.phonestorage);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imageLayout.setOnClickListener(this);
        audioLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        documentLayout.setOnClickListener(this);
        downloadLayout.setOnClickListener(this);
        apkLayout.setOnClickListener(this);
        phonestorage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                Toast.makeText(v.getContext(),"image",Toast.LENGTH_LONG).show();
                break;
            case R.id.audio:
                Toast.makeText(v.getContext(),"audio",Toast.LENGTH_LONG).show();
                break;
            case R.id.video:
                Toast.makeText(v.getContext(),"video",Toast.LENGTH_LONG).show();
                break;
            case R.id.document:
                Toast.makeText(v.getContext(),"document",Toast.LENGTH_LONG).show();
                break;
            case R.id.download:
                Toast.makeText(v.getContext(),"download",Toast.LENGTH_LONG).show();
                break;
            case R.id.apk:
                Toast.makeText(v.getContext(),"apk",Toast.LENGTH_LONG).show();
                break;
            case R.id.phonestorage:
                Intent intent = new Intent(MainActivity.this,FileListActivity.class);
                startActivity(intent);
                Toast.makeText(v.getContext(),"phonestorage",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(v.getContext(),"default",Toast.LENGTH_LONG).show();
            break;


        }
    }
}
