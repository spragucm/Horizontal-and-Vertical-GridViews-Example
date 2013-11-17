package com.learningcurve.example_horzAndVertGridViews;

import java.util.List;

import com.jess.ui.TwoWayGridView;
import com.learningcurve.example_draganddropeverywhere.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class VertGridViewAdapter extends BaseAdapter{
	
	private List<DataObject> data;
	private Context mContext;
	
	//VertGridView stuff
	//TODO base these on the orientation by using the layout folders and values files
	private final int childLayoutResourceId = R.layout.vert_gridview_child_layout;
	private int columns;//Used to set childSize in TwoWayGridView
	private int rows;//used with TwoWayGridView
	private int columnWidth;
	private int rowHeight;
	private int itemPadding;
	
	public VertGridViewAdapter(Context context, List<DataObject> data){
		this.mContext = context;
		this.data = data;
		
		//Get dimensions from values folders; note that the value will change
		//based on the device size but the dimension name will remain the same
		Resources res = mContext.getResources();
		itemPadding = (int) res.getDimension(R.dimen.vert_item_padding);
		int[] rowsColumns = res.getIntArray(R.array.vert_gv_rows_columns);
		rows = rowsColumns[0];
		columns = rowsColumns[1];
		
		
		
		//Initialize the layout params
		MainActivity.vertGridView.setNumRows(rows);
		MainActivity.vertGridView.setNumColumns(columns);
		
		//HorzGridView size not established yet, so need to set it using a viewtreeobserver
		ViewTreeObserver vto = MainActivity.vertGridView.getViewTreeObserver();
		
		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {
			
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				//First use the gridview height and width to determine child values
				rowHeight = (int)((float)(MainActivity.vertGridView.getHeight()/rows)-2*itemPadding);
				columnWidth = (int)((float)(MainActivity.vertGridView.getWidth()/columns)-2*itemPadding);
				
				MainActivity.vertGridView.setRowHeight(rowHeight);
				MainActivity.vertGridView.setColumnWidth(columnWidth);
				
				//Then remove the listener
				ViewTreeObserver vto = MainActivity.vertGridView.getViewTreeObserver();
				
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					vto.removeOnGlobalLayoutListener(this);
				}else{
					vto.removeGlobalOnLayoutListener(this);
				}
				
				
				
			}
		};
		
		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Get the data for the given position in the array
		DataObject thisData = data.get(position);
		
		//Use a viewHandler to improve performance
		ViewHandler handler;
		
		//If reusing a view get the handler info; if view is null, create it
		if(convertView == null){
			
			//Only get the inflater when it's needed, then release it-which isn't frequently
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(childLayoutResourceId , parent, false);
			
			//User findViewById only when first creating the child view
			handler = new ViewHandler();
			handler.iv = (ImageView) convertView.findViewById(R.id.vert_gv_iv);
			handler.tv = (TextView) convertView.findViewById(R.id.vert_gv_tv);
			convertView.setTag(handler);
			
			//Set height of each row based on previous calculations
			//((DragDropHorzGridView)parent).setRowHeight(rowHeight);
			
		}else{
			handler = (ViewHandler) convertView.getTag();
		}
		
		//Set the data outside once the handler and view are instantiated
		handler.iv.setBackgroundColor(thisData.getColor());
		handler.tv.setText(thisData.getName());
		FrameLayout.LayoutParams lp 
			= new FrameLayout.LayoutParams(columnWidth, rowHeight);// convertView.getLayoutParams();
		handler.iv.setLayoutParams(lp);

		Log.d("VertGVAdapter","Position:"+position+",children:"+parent.getChildCount());
		return convertView;
	}
	
	private static class ViewHandler{
		ImageView iv;
		TextView tv;
	}

	@Override
	public int getCount() {
		
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
