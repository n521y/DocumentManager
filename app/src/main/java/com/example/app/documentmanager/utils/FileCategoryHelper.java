package com.example.app.documentmanager.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.app.documentmanager.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

public class FileCategoryHelper {
	
	private List<String> mAudioList;
	private List<String> mVideoList;
	private List<String> mImageList;
	private List<String> mDocumentList;
	private List<String> mCompressionList;
	private List<String> mApkList;
	private List<String> mDocumentNameList;
	private List<String> mCompressionNameList;
	private List<String> mApkNameList;

	public FileCategoryHelper() {
		mAudioList=new ArrayList<String>();
		mVideoList=new ArrayList<String>();
		mImageList=new ArrayList<String>();
		mDocumentList=new ArrayList<String>();
		mCompressionList=new ArrayList<String>();
		mApkList=new ArrayList<String>();

	    mDocumentNameList =new ArrayList<String>() ;
		mCompressionNameList=new ArrayList<String>();
		mApkNameList=new ArrayList<String>();

	}

	public List<String> getmDocumentNameList() {
		return mDocumentNameList;
	}

	public void setmDocumentNameList(List<String> mDocumentNameList) {
		this.mDocumentNameList = mDocumentNameList;
	}

	public List<String> getmCompressionNameList() {
		return mCompressionNameList;
	}

	public void setmCompressionNameList(List<String> mCompressionNameList) {
		this.mCompressionNameList = mCompressionNameList;
	}

	public List<String> getmApkNameList() {
		return mApkNameList;
	}

	public void setmApkNameList(List<String> mApkNameList) {
		this.mApkNameList = mApkNameList;
	}

	public static List<String> getSystemAudio(Context context)
	{
		List<String> fileList=new ArrayList<String>();
		Cursor cursor= context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
				null, null);
		
		while(cursor.moveToNext())
		{
			String filePath=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
//			String fileName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			fileList.add(filePath);
		}
		cursor.close();
		return fileList;
	}
	
	public static List<String> getSystemVideo(Context context)
	{
		List<String> fileList=new ArrayList<String>();
		Cursor cursor= context.getContentResolver().query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
				null, null);
		
		while(cursor.moveToNext())
		{
			String filePath=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
//			String fileName=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
//			long fileTime=cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
//			int fileSize=cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
			fileList.add(filePath);
		}
		cursor.close();
		return fileList;
	}
	
	public static List<String> getSystemImage(Context context)
	{
		List<String> fileList=new ArrayList<String>();
		Cursor cursor= context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
				null, null);
		
		while(cursor.moveToNext())
		{
			String filePath=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//			String fileName=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
			long fileTime=cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
//			int fileSize=cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
			Log.d("fileTime",String.valueOf(fileTime));
			SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			java.util.Date data = new Date(fileTime* 1000);
			String sDateTime = sdf.format(data);

			Log.d("fileTime",sDateTime);
			fileList.add(filePath);
		}
		cursor.close();
		return fileList;
	}
	
	public  void getSystemFileCategory(String path, Context context)
	{
        Log.d("path",path);
		File file=new File(path);
		if(file.listFiles()!=null)
		{
			for(File files:file.listFiles())
			{
				if(files.isHidden())
				{
					continue;
				}
				else if(files.getAbsolutePath().startsWith(context.getExternalFilesDir(null).getParentFile().getParent()))
				{
					continue;
				}
				else if(files.isDirectory())
				{
					getSystemFileCategory(files.getAbsolutePath(),context);
				}
				else
				{
					String type=MimeTypeUtils.getCategoryMimeType(files.getAbsolutePath());
					if(type!=null)
					{
						if(type.equals("apk"))
						{
							Log.d("apk",files.getAbsolutePath());
							mApkNameList.add(files.getName());
							mApkList.add(files.getAbsolutePath());
						}
						else if(type.equals("compression"))
						{
							Log.d("compression",files.getAbsolutePath());
							mCompressionNameList.add(file.getName());
							mCompressionList.add(files.getAbsolutePath());
						}
						else if(type.equals("document"))
						{
							Log.d("document",files.getAbsolutePath());
							mDocumentNameList.add(file.getName());
							mDocumentList.add(files.getAbsolutePath());
						}
					}
				}
			}
		}
	}
	
	public List<String> getSystemAudio()
	{
		
		return mAudioList;
	}
	
	public List<String> getSystemVideo()
	{
		return mVideoList;
	}
	
	public List<String> getSystemImage()
	{
		return mImageList;
	}
	
	public List<String> getSystemDocument()
	{
		return mDocumentList;
	}
	
	public List<String> getSystemCompression()
	{
		return mCompressionList;
	}
	
	public List<String> getSystemApk()
	{
		return mApkList;
	}
			
	public static Bitmap getFileBitmap(Context context,String filePath,final boolean isDefault)
	{
		Bitmap bitmap=null;
		File file=new File(filePath);
		if(file.isDirectory())
		{
			bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.folder_blue);
		}
		else
		{
			String type=MimeTypeUtils.getCategoryMimeType(filePath);
			if(type!=null)
			{
				if(type.equals(MimeTypeUtils.MIMETYPE_AUDIO))
				{
					bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
				}
				else if(type.equals(MimeTypeUtils.MIMETYPE_VIDEO))
				{
					bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.video);
				}
				else if(type.equals(MimeTypeUtils.MIMETYPE_IMAGE))
				{
					if(isDefault)
					{
						bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
					}
					else
					{
						bitmap=getImageThumbnail(context,filePath);
					}
								
				}
				else if(type.equals(MimeTypeUtils.MIMETYPE_DOCUMENT))
				{
					if(isDefault)
					{
						bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.text);
					}
					else
					{
						bitmap=getDocumentIcon(context,filePath);
					}									
				}
				else if(type.equals(MimeTypeUtils.MIMETYPE_COMPRESSION))
				{
					bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.archive_yellow);
				}
				else if(type.equals(MimeTypeUtils.MIMETYPE_APK))
				{
					if(isDefault)
					{
						bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.apk);
					}
					else
					{
						bitmap=getApkIcon(context,filePath);
					}					
				}
			}			
			else
			{
				bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
			}			
		}
		return bitmap;
	}

	private static Bitmap getDocumentIcon(Context context,String filePath) {
		String mimeType = MimeTypeUtils.getMimeType(filePath);
		Bitmap bitmap=null;
		//�ı��ĵ�
		if(mimeType==null)
		{
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.unknown);
		}
		else if (mimeType.equals("text/plain")) {
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.text);
		} 
		//�ĵ�
		else if (mimeType.startsWith("text")) {
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.text);
		} 
		//word�ĵ�
		else if (mimeType.equals("application/msword")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.template")) {
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.word);
		}
		//excel����ļ�
		else if (mimeType.equals("application/vnd.ms-excel")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.template")) {
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.excel);
		} 
		//ppt�ļ�
		else if (mimeType.equals("application/vnd.ms-powerpoint")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.presentationml.template")
				|| mimeType
						.equals("application/vnd.openxmlformats-officedocument.presentationml.slideshow")) {
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.powerpoint);
		} 
		//pdf�ļ�
		else if(mimeType.equals("application/pdf"))
		{
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.pdf);
		}
		else
		{
			bitmap= BitmapFactory.decodeResource(context.getResources(),
					R.drawable.unknown);
		}
		return bitmap;
	}
	
	private static Bitmap getApkIcon(Context context,String filePath) {
		Drawable drawable=null;
		PackageManager pm = context.getPackageManager();
	    PackageInfo info = pm.getPackageArchiveInfo(filePath,
	        PackageManager.GET_ACTIVITIES);
	    if(info!=null)
	    {
	    	ApplicationInfo appInfo = info.applicationInfo;
	    	appInfo.sourceDir = filePath;
	        appInfo.publicSourceDir = filePath;
	        
	        try {
	        	drawable= appInfo.loadIcon(pm);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		BitmapDrawable bitmapDrawable=(BitmapDrawable) drawable;
		Bitmap bitmap=bitmapDrawable.getBitmap();
		if(bitmap==null)
		{
			bitmap=BitmapFactory.decodeResource(context.getResources(),
					R.drawable.apk);
		}
		return bitmap;
	}
	
	private static Bitmap getImageThumbnail(Context context,String filePath) {
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID },
				MediaStore.Images.Media.DATA + "=?",
				new String[] { filePath }, null);
		int id = 0;
		while (cursor.moveToNext()) {
			id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Images.Media._ID));
		}
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
				context.getContentResolver(), id,
				MediaStore.Images.Thumbnails.MINI_KIND, options);
		if (bitmap != null) {
			return bitmap;
		}
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		int height = options.outHeight/ 8;
		int width = options.outWidth/ 8;
		options.inSampleSize = 8;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		if(bitmap!=null)
		{
			return bitmap;
		}
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
	}
	
}
