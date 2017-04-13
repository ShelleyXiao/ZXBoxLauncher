package com.zx.zxboxlauncher.adpter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	private final SparseArray<View> mViews;
	private View mConvertView;
	private int mPosition;

	public int getmPosition() {
		return mPosition;
	}

	public View getConvertView() {
		return mConvertView;
	}

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		mPosition = position;
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}

		ViewHolder existViewHolder = (ViewHolder) convertView.getTag();
		existViewHolder.mPosition = position;
		return existViewHolder;
	}

	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}

	public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
		// TODO Auto-generated method stub
		ImageView view = getView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}

	public ViewHolder setText(int viewId, CharSequence cs) {
		// TODO Auto-generated method stub
		TextView view = getView(viewId);
		view.setText(cs);
		return this;
	}
	public ViewHolder setText(int viewId, int cs) {
		// TODO Auto-generated method stub
		TextView view = getView(viewId);
		view.setText(cs);
		return this;
	}
	
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
}
