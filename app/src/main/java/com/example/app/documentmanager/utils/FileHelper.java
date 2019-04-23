package com.example.app.documentmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.widget.Toast;

public class FileHelper {
	
	public FileHelper() {

	}
	
	public static List<String> getFilePathLists(String filePath, boolean isShowHidden) {
		List<String> ResultPathLists = new ArrayList<String>();
		List<String> folderPathLists = new ArrayList<String>();
		List<String> hiddenFilePathLists = new ArrayList<String>();
		List<String> noHiddenFilePathLists = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isHidden()) {
					if (!isShowHidden) {
						continue;
					} else {
						if (file.isDirectory()) {
							ResultPathLists.add(file.getAbsolutePath());
						} else {
							hiddenFilePathLists.add(file.getAbsolutePath());
						}
					}
				} else {
					if (file.isDirectory()) {
						folderPathLists.add(file.getAbsolutePath());
					} else {
						noHiddenFilePathLists.add(file.getAbsolutePath());
					}
				}

			}
		}
		Collections.sort(ResultPathLists);
		Collections.sort(folderPathLists);
		Collections.sort(hiddenFilePathLists);
		Collections.sort(noHiddenFilePathLists);
		if (hiddenFilePathLists != null) {
			for (String path : hiddenFilePathLists) {
				ResultPathLists.add(path);
			}
		}
		if (folderPathLists != null) {
			for (String path : folderPathLists) {
				ResultPathLists.add(path);
			}
		}
		if (noHiddenFilePathLists != null) {
			for (String path : noHiddenFilePathLists) {
				ResultPathLists.add(path);
			}
		}
		return ResultPathLists;
	}

	public static boolean fileOpenWay(String filePath,Context context) {
		File file = new File(filePath);
		if (file.isFile()) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.fromFile(file);
				String type = MimeTypeUtils.getMimeType(filePath);
				if (type != null) {
					intent.setDataAndType(uri, type);
					context.startActivity(Intent.createChooser(intent, "打开方式"));
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(context, "打开文件失败",
						Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}

	public static List<String> deleteFileList(List<String> filePathLists) {
		List<String> filePathList = new ArrayList<String>();
		for (String filePath : filePathLists) {
			File file = new File(filePath);
			if (file.exists()) {
				try {
					if (file.isFile()) {
						file.delete();
					} else {
						File[] filesarr = file.listFiles();
						List<String> tempFilePathLists = new ArrayList<String>();
						for (File tempFile : filesarr) {
							tempFilePathLists.add(tempFile.getAbsolutePath());
						}
						deleteFileList(tempFilePathLists);
						file.delete();
					}
					filePathList.add(filePath);
				} catch (Exception e) {
				}
			} else {
			}
		}
		return filePathList;
	}

	public static boolean deleteFile(String filePath) {
			File file = new File(filePath);
			if (file.exists()) {
					if (file.isFile()) {
						file.delete();
						return true;
					} else {
						return false;
					}
			}
			return false;
	}

	public static boolean reNameFile(final String filePath, String newName) {
		boolean isSucceed = false;
		if ("".equals(newName)) {
			return false;
		} else {
			String newPath = filePath.substring(0,
					filePath.lastIndexOf("/") + 1);
			File file = new File(newPath + newName);

			if (!new File(filePath).renameTo(file)) {
				isSucceed = false;
			} else {
				isSucceed = true;
			}
		}
		return isSucceed;
	}

	public static List<String> copyFile(List<String> filePathLists, String newPath,
			boolean isCopy) {
		File newFile = new File(newPath);
		File[] files = newFile.listFiles();
		List<String> resultFilePathList = new ArrayList<String>();
		for (File file : files) {
			for (int i = 0; i < filePathLists.size(); i++) {
				if (file.getName().equals(
						new File(filePathLists.get(i)).getName())) {
					filePathLists.remove(i);
					i--;
					break;
				}
			}
		}
		if (filePathLists.size() > 0) {
			for (String filePath : filePathLists) {
				File file = new File(filePath);
				try {
					String path = newPath + "/" + file.getName();
					File tempFile = new File(path);
					if (file.isFile()) {
						InputStream inStream = new FileInputStream(file); // 读入原文件
						FileOutputStream fs = new FileOutputStream(tempFile);
						byte[] buffer = new byte[1024 * 4];
						int length;
						while ((length = inStream.read(buffer)) != -1) {
							fs.write(buffer, 0, length);
						}
						fs.close();
						inStream.close();
					} else {
						tempFile.mkdir();
						File[] oldFiles = file.listFiles();
						List<String> tempFilePathList = new ArrayList<String>();
						for (File oldFile : oldFiles) {
							tempFilePathList.add(oldFile.getAbsolutePath());

						}
						copyFile(tempFilePathList, tempFile.getAbsolutePath(),
								isCopy);
					}
					if (!isCopy) {
						file.delete();
					}
					resultFilePathList.add(path);
				} catch (Exception e) {
				}
			}
		}
		return resultFilePathList;
	}

	public static long getFileTime(String filePath) {
		long time = 0;
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			try {
				if (file.lastModified() > time) {
					time = file.lastModified();
				}
				for (File tempFile : files) {
					long tempTime = getFileTime(tempFile.getAbsolutePath());
					if (tempTime > time) {
						time = tempTime;
					}
				}
			} catch (Exception e) {
			}
		} else {
			try {
				time = file.lastModified();
			} catch (Exception e) {
			}
		}
		return time;
	}

	public static long getFileSize(String filePath) {
		long size = 0;
		File file = new File(filePath);
		if (file.isDirectory()) {
			try {
				size += file.length();
				File[] files = file.listFiles();
				for (File tempFile : files) {
					size = size+getFileSize(tempFile.getAbsolutePath());
				}
			} catch (Exception e) {
			}
		} else {
			try {
				size = file.length();
			} catch (Exception e) {
			}
		}
		return size;
	}

	public static List<String> getFileAttribute(String filePath) {
		List<String> resultList = new ArrayList<String>();
		File file = new File(filePath);
		String name = file.getName();
		String path = file.getAbsolutePath();
		float fSize = getFileSize(filePath);
		if(fSize!=0)
		{
			fSize =fSize/ 1024;
		}
		long time = getFileTime(filePath);
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DecimalFormat df = new DecimalFormat("##0.00");
		String dateTime = sdf.format(date);
		resultList.add(name);
		resultList.add(path);
		if (fSize < 1000) {
			String size = df.format(fSize);
			resultList.add(size + "KB");
		} else {
			fSize = fSize / 1024;
			if (fSize < 1000) {
				String size = df.format(fSize);
				resultList.add(size + "MB");
			} else {
				fSize = fSize / 1024;
				String size = df.format(fSize);
				resultList.add(size + "GB");
			}
		}
		resultList.add(dateTime + "");
		return resultList;
	}
	
	public static boolean newFile(String parentPath, String fileName, boolean isFile) {
		try {
			File file = new File(parentPath + "/" + fileName);
			if (!file.exists()) {
				if (isFile) {
					file.createNewFile();
				} else {
					file.mkdir();
				}
				return true;
			} 
		} catch (Exception e) {
			
		}
		return false;
	}

	public static List<String> searchFile(List<String> filePathLists,
			String searchPath, String keyWord) {
		keyWord = keyWord.replaceAll(" ", "").toLowerCase();
		File[] files = new File(searchPath).listFiles();
		for (File file : files) {
			String fileName = file.getName().replaceAll(" ", "").toLowerCase();
			if (fileName.indexOf(keyWord) != -1) {
				filePathLists.add(file.getAbsolutePath());
			}
			if (file.isDirectory()) {
				searchFile(filePathLists, file.getAbsolutePath(), keyWord);
			}
		}
		return filePathLists;
	}

	public static void shareOrSend(List<String> filePathLists,Context context) {		
		boolean countIsOne=!(filePathLists.size()>1);
		Intent intent = new Intent(countIsOne? Intent.ACTION_SEND:Intent.ACTION_SEND_MULTIPLE);
		if(countIsOne)
		{
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(filePathLists.get(0))));
		}
		else
		{
			List<Uri> uris=new ArrayList<Uri>();
			for (String filePath : filePathLists) {
				uris.add(Uri.fromFile(new File(filePath)));
			}
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>) uris);
		}
		intent.setType("*/*");
		context.startActivity(Intent.createChooser(intent, "分享/发送"));
	}
	
	public static void showFileAttribute(List<String> list,Context context) {
		if (list != null) {
			String name = list.get(0);
			String path = list.get(1);
			String size = list.get(2);
			String time = list.get(3);
			new AlertDialog.Builder(context)
					.setTitle("文件属性")
					.setMessage(
							"名称" + name + "\r\n" + "路径" + path + "\r\n"
									+ "大小" + size + "\r\n" + "时间" + time)
					.setPositiveButton("确定", null).show();
		}
		else
		{
			Toast.makeText(context, "属性获取失败", Toast.LENGTH_SHORT).show();
		}
	}

}
