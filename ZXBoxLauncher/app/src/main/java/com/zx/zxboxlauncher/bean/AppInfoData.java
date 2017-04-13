package com.zx.zxboxlauncher.bean;
import android.graphics.drawable.Drawable;

public class AppInfoData {
	private Drawable icon;
	private String apkName;
	private String pkgName;
	private int versionCode;
	private String versionName;
	public void setAppicon(Drawable ico){
		icon = ico;
	}
	public void setAppname(String name){
		apkName = name;
	}
	public void setApppackage(String apppackage){
		pkgName = apppackage;
	}
	public void setAppversion(String ver){
		versionName = ver;
	}
	public void setAppversionCode(int code){
		versionCode = code;
	}
	public Drawable getIcon(){
		return icon;
	}
	public String getApkName(){
		return apkName;
	}
	public String getPkgName(){
		return pkgName;
	}
	public String getVer(){
		return versionName;
	}
	public int getCode(){
		return versionCode;
	}
}
