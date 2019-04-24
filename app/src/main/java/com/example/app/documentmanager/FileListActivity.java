package com.example.app.documentmanager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.PhoneFileAdapter;
import com.example.app.documentmanager.bean.FileEntity;
import com.example.app.documentmanager.utils.FileHelper;
import com.example.app.documentmanager.utils.FileOpen;


/**
 * 文件列表 界面
 *
 *
 */
public class FileListActivity extends Activity implements View.OnClickListener {

    private RecyclerView.LayoutManager mLayoutManager;
    private String niupath;
    private Toolbar mToolbar;
    private TextView mPathTextView;
    private RecyclerView mListView;
    private PhoneFileAdapter mAdapter;
    private static Context mContext;
    private File currentFile;
    String sdRootPath;
    private boolean mFlag;
    private FileOpen mFileOpen;
    private List<String> mCopyFileList;
    private Button pasteButton;
    private Button cancelButton;
    private LinearLayout listBottomBarLinearLayout;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private ArrayList<FileEntity> mList;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist);
        mFlag=false;
        mContext = this;
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        init();
                        break;
                    case 2:
                        init();
                        break;
                    case 3:
                        init();
                        break;
                    default:
                        break;
                }
            }
        };
        verifyStoragePermissions(this);
        mList = new ArrayList<>();
        sdRootPath =Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d("niuniu",sdRootPath);
        currentFile = new File(sdRootPath);
        System.out.println(sdRootPath);
        initView();
        init();
        niupath=currentFile.getAbsolutePath().substring(19);
        mPathTextView.setText("我的文件"+niupath);
        getData(sdRootPath);
    }

    //返回的回掉
    @Override
    public void onBackPressed() {
//		super.onBackPressed();
        System.out.println("onBackPressed...");
        if(sdRootPath.equals(currentFile.getAbsolutePath())){
            System.out.println("已经到了根目录...");
            finish();
            return ;
        }

        String parentPath = currentFile.getParent();
        currentFile = new File(parentPath);
        niupath=currentFile.getAbsolutePath().substring(19);
        mPathTextView.setText("我的文件"+niupath);
        getData(parentPath);
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        pasteButton = (Button)findViewById(R.id.id_mainBottomBarPasteButton);
        cancelButton = (Button)findViewById(R.id.id_mainBottomBarCancelButton);
        mToolbar = (Toolbar)findViewById(R.id.activity_filelidt_toolbar);
        mPathTextView = (TextView) findViewById(R.id.activity_filelidt__path);
        listBottomBarLinearLayout =(LinearLayout)findViewById(R.id.id_mainBottomBar);
        mFileOpen = new FileOpen(mContext);
        pasteButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        //初始化菜单
        mToolbar.inflateMenu(R.menu.menu_common_activity);
        //设置菜单点击事件
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_serch){
                    Toast.makeText(FileListActivity.this , "serch" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FileListActivity.this,SearchActivity.class);
                    startActivity(intent);

                }else if (menuItemId == R.id.action_settings){
                    Intent intent = new Intent(FileListActivity.this,CommonActivity.class);
                    startActivity(intent);
                    Toast.makeText(FileListActivity.this , "setting" , Toast.LENGTH_SHORT).show();
                }else if(menuItemId == R.id.action_addNewFolder){
                    Toast.makeText(FileListActivity.this , "addNewFolder" , Toast.LENGTH_SHORT).show();
                    creatNewFolder(currentFile.getAbsolutePath(),false);

                } else if(menuItemId == R.id.action_addNewFile){
                    Toast.makeText(FileListActivity.this , "addNewFile"+currentFile.getAbsolutePath() , Toast.LENGTH_SHORT).show();
                    creatNewFolder(currentFile.getAbsolutePath(),true);

                } else if(menuItemId == R.id.action_grid){
                    if(mFlag == false){
                        mFlag=true;
                        item.setIcon(R.drawable.ic_grid);
                        //网格排列文件
                        Message msg = new Message();
                        msg.what=2;
                        mHandler.sendMessage(msg);

                    }else {
                        mFlag = false;
                        item.setIcon(R.drawable.ic_vertical);
                        //水平排列文件
                        Message msg = new Message();
                        msg.what=3;
                        mHandler.sendMessage(msg);
                    }

                }
                return false;
            }
        });

        //返回图片的点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sdRootPath.equals(currentFile.getAbsolutePath())){
                    System.out.println("已经到了根目录...");
                    finish();
                    return ;
                }

                String parentPath = currentFile.getParent();
                currentFile = new File(parentPath);
                niupath=currentFile.getAbsolutePath().substring(19);
                mPathTextView.setText("我的文件"+niupath);
                getData(parentPath);

            }
        });


        mListView = (RecyclerView) findViewById(R.id.listView1);
    }

    private  void init(){
        if (mFlag == false){
            mLayoutManager = new LinearLayoutManager(this);
            mListView.setLayoutManager(mLayoutManager);
        }else {
            mLayoutManager = new
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mListView.setLayoutManager(mLayoutManager);
        }
        if(mAdapter == null) {
            mAdapter = new PhoneFileAdapter(FileListActivity.this, mList, mFlag);
            mListView.setAdapter(mAdapter);

        }else {
            mAdapter = new PhoneFileAdapter(FileListActivity.this, mList, mFlag);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setOnItemClickListener(new PhoneFileAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                Toast.makeText(FileListActivity.this,"item",Toast.LENGTH_LONG).show();
                final FileEntity entity = mList.get(data);
                if(entity.getFileType() == FileEntity.Type.FLODER){
                    currentFile = new File(entity.getFilePath());
                    niupath=currentFile.getAbsolutePath().substring(19);
                    mPathTextView.setText("我的文件"+niupath);
                    getData(entity.getFilePath());
                }else if(entity.getFileType() == FileEntity.Type.FILE){
                    final File file = new File(entity.getFilePath());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent =mFileOpen.openFile(entity.getFilePath());
                            startActivity(intent);
                            Toast.makeText(mContext, entity.getFilePath()+"  "+entity.getFileName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        mAdapter.setOnItemLongClickListener(new PhoneFileAdapter.OnRecyclerItemLongListener() {
            String[] fileOpItemStr = {"重命名","删除","移动","查看文件属性"};
            List<String> stringList = new ArrayList<>();
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(FileListActivity.this,"itemlong"+mList.get(position).getFilePath(),Toast.LENGTH_LONG).show();
                File file = new File(mList.get(position).getFilePath());
                Toast.makeText(FileListActivity.this,"itemlong"+file.getParent(),Toast.LENGTH_LONG).show();
                final String filePath = mList.get(position).getFilePath();
                stringList.add(filePath);
                final DialogInterface.OnClickListener fileOpDialogOnClickListener = new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                rename(filePath);
                                break;
                            case 1:
                                FileHelper.deleteFileList(stringList);
                                Toast.makeText(FileListActivity.this, "删除成功",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                listBottomBarLinearLayout.setVisibility(View.VISIBLE);
                                mCopyFileList = new ArrayList<String>();
                                mCopyFileList.add(filePath);

                                break;
                            case 3:
                                List<String> resultList = new ArrayList<String>();
                                resultList=FileHelper.getFileAttribute(filePath);
                                FileHelper.showFileAttribute(resultList,mContext);
                                break;
                            default:

                                break;
                        }
                    }
                };

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                };
                new AlertDialog.Builder(FileListActivity.this)
                        .setTitle("文件操作")
                        .setItems(fileOpItemStr, fileOpDialogOnClickListener).show();
            }
        });
    }

    //重命名
    private void rename(final String filePath) {

        final AlertDialog alertDialog = new AlertDialog.Builder(FileListActivity.this).create();
        View renameDialog = View.inflate(FileListActivity.this, R.layout.dialog_commonactivity_rename,null);
        File file = new File(filePath);
        final String name = file.getName();
        alertDialog.setView(renameDialog);
        alertDialog.show();
        final EditText newEditText = alertDialog.findViewById(R.id.edittext_dialog_commonactivity_newname);
        Button confimButton = alertDialog.findViewById(R.id.button_commonactivity_rename_dialog_confirm);
        Button cancelButton = alertDialog.findViewById(R.id.button_commonactivity_rename_dialog_cancel);
        //显示文件原名称
        newEditText.setText(name);
        //重命名弹出框确定按钮
        confimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = newEditText.getText().toString().trim();
                boolean success=FileHelper.reNameFile(filePath,newName);
                if (success){
                    Toast.makeText(FileListActivity.this, "重命名成功", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(FileListActivity.this, "重命名失败", Toast.LENGTH_LONG).show();

                }
                alertDialog.dismiss();
            }
        });
        //重命名弹出框取消按钮
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void getData(final String path) {
        new Thread(){
            @Override
            public void run() {
                super.run();

                findAllFiles(path);
            }
        }.start();

    }

    /**
     * 查找path地址下所有文件
     * @param path
     */
    public void findAllFiles(String path) {
        mList.clear();

        if(path ==null ||path.equals("")){
            return;
        }
        File fatherFile = new File(path);
        File[] files = fatherFile.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                FileEntity entity = new FileEntity();
                boolean isDirectory = files[i].isDirectory();
                if(isDirectory ==true){
                    entity.setFileType(FileEntity.Type.FLODER);
//					entity.setFileName(files[i].getPath());
                }else{
                    entity.setFileType(FileEntity.Type.FILE);
                }
                entity.setFileName(files[i].getName().toString());
                entity.setFilePath(files[i].getAbsolutePath());
                entity.setFileSize(files[i].length()+"");
                mList.add(entity);
            }
            Log.d("niuniu",mList.toString());
        }
        mHandler.sendEmptyMessage(1);

    }




    private void creatNewFolder(final String parentPath, final boolean isFile){
        final EditText editTextView = new EditText(mContext);
        new AlertDialog.Builder(mContext).setTitle("新文件名")
                .setView(editTextView).setNegativeButton("取消", null)
                .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName=editTextView.getText().toString();
                        if(fileName==""||fileName.replaceAll(" ", "").toLowerCase()=="")
                        {
                            Toast.makeText(mContext, "未输入有效文件名，无法创建文件",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!FileHelper.newFile(parentPath, fileName, isFile)) {
                            Toast.makeText(mContext, "创建文件失败",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "创建文件成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_mainBottomBarPasteButton:
                Toast.makeText(mContext, "id_mainBottomBarPasteButton",
                        Toast.LENGTH_SHORT).show();
                Thread pasteThread = new Thread() {
                    @Override
                    public void run() {

                        FileHelper.copyFile(mCopyFileList,currentFile.getAbsolutePath(), false);
                    }
                };
                pasteThread.start();
                listBottomBarLinearLayout.setVisibility(View.GONE);
                break;
            case R.id.id_mainBottomBarCancelButton:
                listBottomBarLinearLayout.setVisibility(View.GONE);
                break;
        }
    }
}
