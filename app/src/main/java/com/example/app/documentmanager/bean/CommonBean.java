package com.example.app.documentmanager.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonBean {

	private Bitmap icon;
	private int iconId;
	private String title;
	private String content;
	private boolean isSeleced;

	public CommonBean() {

	}

	public CommonBean(Bitmap icon, String title, String content) {
		super();
		this.icon = icon;
		this.title = title;
		this.content = content;
	}

	public CommonBean(Bitmap icon, String title, boolean isSeleced) {
		this.icon = icon;
		this.title = title;
		this.isSeleced = isSeleced;
	}

	public CommonBean(int iconId, String title, boolean isSeleced) {
		this.iconId = iconId;
		this.title = title;
		this.isSeleced = isSeleced;
	}

	public CommonBean(Bitmap icon, String title, String content,
			boolean isSeleced) {
		this.icon = icon;
		this.title = title;
		this.content = content;
		this.isSeleced = isSeleced;
	}

	public CommonBean(int iconId, String title, String content,
			boolean isSeleced) {
		this.iconId = iconId;
		this.title = title;
		this.content = content;
		this.isSeleced = isSeleced;
	}

	public Bitmap getIcon(Context context) {
		if(icon==null)
		{
			return BitmapFactory.decodeResource(context.getResources(), iconId);
		}
		return icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getIsSeleced() {
		return isSeleced;
	}

	public void setSeleced(boolean isSeleced) {
		this.isSeleced = isSeleced;
	}

	@Override
	public String toString() {
		return "CommonBean [icon=" + icon + ", iconId=" + iconId + ", title="
				+ title + ", content=" + content + ", isSeleced=" + isSeleced
				+ "]";
	}
	
}
