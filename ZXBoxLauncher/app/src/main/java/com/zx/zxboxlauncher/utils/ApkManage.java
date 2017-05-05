package com.zx.zxboxlauncher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.bean.AppInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.zx.zxboxlauncher.R.raw.app;
import static com.zx.zxboxlauncher.R.string.apps;

public class ApkManage {
	public static PackageInfo getAPKInfo(Context ctx, String apk, boolean type) {// 获取apk的信息
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pakinfo = null;
		if (type) {
			try {
				pakinfo = pm.getPackageInfo(apk, PackageManager.GET_ACTIVITIES);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
			}
		} else {
			pakinfo = pm.getPackageArchiveInfo(apk,
					PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		return pakinfo;
	}

	public static boolean openApk(Context context, String pkg) {
		try {
			PackageManager pm = context.getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage(pkg);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	/** 
	 * 查询手机内非系统应用 
	 * @param context 
	 * @return 
	 */  
	public static List<PackageInfo> getAllApps(Context context) {  
		List<PackageInfo> apps = new ArrayList<PackageInfo>();  
	    PackageManager pManager = context.getPackageManager();  
	    //获取手机内所有应用  
	    List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
	    for (int i = 0; i < paklist.size(); i++) {  
	        PackageInfo pak = (PackageInfo) paklist.get(i);  
	        //判断是否为非系统预装的应用程序  
	        if (((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0
	        		|| pak.applicationInfo.packageName.equals("com.voole.epg")) 
	        		&& !pak.applicationInfo.packageName.equals("com.android.rockchip")
	        		&& !pak.applicationInfo.packageName.equals("com.android.apkinstaller")) {  
	            // customs applications  
	            apps.add(pak);  
	        }  
	    }  
	    int n=0;
//	    for(int i = 0; i< Contanst.pkgs.length; i++){
//	    	for(int j=0; j<apps.size(); j++){
//	    		if(apps.get(j).applicationInfo.packageName.equals(Contanst.pkgs[i])){
//	    			PackageInfo info = apps.get(n);
//	    			apps.set(n, apps.get(j));
//	    			apps.set(j, info);
//	    			n++;
//	    			break;
//	    		}
//	    	}
//	    }
	    
	    return apps;  
	}

	public static List<String> getFavPackageName(Context ctx,String pkgs) {
		if(ctx == null || TextUtils.isEmpty(pkgs)) {
			return null;
		}
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		String pkg[] = pkgs.split(";");
		return Arrays.asList(pkg);
	}
	
	public static List<PackageInfo> getAllApps(Context ctx,String pkgs) {
		if(ctx == null || TextUtils.isEmpty(pkgs)) {
			return null;
		}
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		String pkg[] = pkgs.split(";");
		PackageManager pm = ctx.getPackageManager();
		for(int i=0; i<pkg.length; i++){
			PackageInfo pkgInfo = null;
			if(checkInstall(ctx, pkg[i])){
				try {
					pkgInfo = pm.getPackageInfo(pkg[i],
							PackageManager.GET_PERMISSIONS);
					apps.add(pkgInfo);
				} catch (NameNotFoundException e) {
				}
			}else{
				pkgInfo = new PackageInfo();
				pkgInfo.applicationInfo = new ApplicationInfo();
				pkgInfo.applicationInfo.packageName = "CustomApp";
				apps.add(pkgInfo);
			}
		}
		return apps;
	}
	
	public static boolean checkInstall(Context ctx, String apk) {// 检查是否安装
		// TODO Auto-generated method stub
		boolean install = false;
		PackageManager pm = ctx.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(apk, 1);
			if (info != null && info.activities.length > 0) {
				install = true;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return install;
	}

	public static void install(Context ctx, String filename) {// 安装应用
		Uri uri = Uri.fromFile(new File(filename));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		ctx.startActivity(intent);
	}

	public static void uninstall(Context ctx, String apk) {// 卸载应用
		Uri packageURI = Uri.parse("package:" + apk);// "package:com.demo.CanavaCancel"
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(uninstallIntent);
	}

	public static void execCommand(final String path) { // 静默安装
		new Thread() {
			public void run() {
				Process install = null;
				try {
					// 安装apk命令
					install = Runtime.getRuntime()
							.exec("pm install -r " + path);
					install.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (install != null) {
						install.destroy();
					}
				}
			}
		}.start();
	}

	public static int installSlient(Context paramContext, String path) {
		if (!new File(path).exists())
			return -1;
		StringBuilder localStringBuilder = new StringBuilder();
		Process localProcess = null;
		BufferedReader localBufferedReader = null;
		try {
			chmod("777", path);
			localProcess = Runtime.getRuntime().exec(
					"pm install -r   " + path);
			localBufferedReader = new BufferedReader(new InputStreamReader(
					localProcess.getInputStream()));
			String str = localBufferedReader.readLine();
			if (str != null)
				localStringBuilder.append(str);
			if (localStringBuilder.toString().contains("Success")) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception localException) {
		}
		try {
			localBufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		localProcess.destroy();
		return 0;
	}

	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
		}
	}
	


	public static List<ResolveInfo> getMore(Context context) {
		// TODO Auto-generated method stub
		List<ResolveInfo> infos = new ArrayList<ResolveInfo>();
		PackageManager pm = context.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent,0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(
				pm));
		for (int i = 0; i < apps.size(); i++) {
			ResolveInfo info = apps.get(i);
			if(info.activityInfo.packageName.equals("com.mylove.happyvideo")||
					info.activityInfo.packageName.equals("com.android.development")||
					info.activityInfo.packageName.equals("com.rockchip.wfd")||
					info.activityInfo.packageName.equals("com.rockchip.mediacenter")||
					info.activityInfo.packageName.equals("com.mylove.settting")||
//					info.activityInfo.packageName.equals("com.bsw.update")||
					info.activityInfo.packageName.equals("com.android.apkinstaller")){
				continue;
			}
			
			if(info.activityInfo.packageName.equals("com.voole.epg")){
				if(infos.size() > 0){
					infos.add(infos.get(0));
					infos.set(0, info);
				}
				continue;
			}
			
			infos.add(info);
		}
		
		return infos;
	}

	public static void selectApp(int position, String packageName) {
		// TODO Auto-generated method stub
		String favorite = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
		String pkg[] = favorite.split(";");
		if(pkg.length > position){
			pkg[position] = packageName;
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < pkg.length; i++) {
			sb.append(pkg[i]).append(";");
		}
		SharedPreferencesUtils.setParam(BaseApplication.getInstance(), Constant.FAVORITE, sb.toString());
	}

	public static  List<AppInfo> getMoreApps(Context context) {
		PackageManager mPackManager = context.getPackageManager();
		Intent mainiIntent = new Intent(Intent.ACTION_MAIN, null);
		mainiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = mPackManager.queryIntentActivities(mainiIntent, 0);
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackManager));
		List<AppInfo> data = new ArrayList<AppInfo>();
		data.clear();
		for (ResolveInfo app : resolveInfos) {
			data.add(getApp(context, app));
		}
		return data;
	}

	public static AppInfo getApp(Context context, ResolveInfo app) {
        PackageManager mPackManager = context.getPackageManager();
		AppInfo mAppInfo = new AppInfo();
		mAppInfo.setAppName((String) app.loadLabel(mPackManager));
		mAppInfo.setAppPackageName(app.activityInfo.packageName);
		mAppInfo.setAppIcon(app.loadIcon(mPackManager));

		if ((app.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
			mAppInfo.setIsSystemApps(true);
		} else {
			mAppInfo.setIsSystemApps(false);
		}

		Intent intent = new Intent();
		intent.setComponent(new ComponentName(app.activityInfo.packageName, app.activityInfo.name));
		mAppInfo.setAppIntent(intent);

		return mAppInfo;
	}

	public static ResolveInfo getAppReaolveInfo(Context context, String packageName) {
		PackageManager mPackManager = context.getPackageManager();
		Intent mainiIntent = new Intent(Intent.ACTION_MAIN, null);
		mainiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		mainiIntent.setPackage(packageName);
		List<ResolveInfo> resolveInfos = mPackManager.queryIntentActivities(mainiIntent, 0);
		if(resolveInfos != null && resolveInfos.size() > 0) {
			return resolveInfos.get(0);
		}

		return null;
    }
}
