package com.arastta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;

	public HistoryAdapter(Context context, ArrayList<JSONObject> arrayList)
	{
		super(context, R.layout.item_history, arrayList);
		ScreenName = "HistoryAdapter";
		this.context = context;
		this.arrayList = arrayList;
	}
	/*
	"order_history_id": "31",
	"order_status_id": "1",
	"order_status_name": "Pending",
	"comment": "",
	"notify": "0",
	"date_added": "2016-08-11 11:48:43"
	 */
	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_history, parent, false);
		
		try 
		{
			TextView HistoryDate = (TextView)rowView.findViewById(R.id.HistoryDate);
			HistoryDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			HistoryDate.setText(arrayList.get(position).getString("date_added"));

			TextView HistoryStatus = (TextView)rowView.findViewById(R.id.HistoryStatus);
			HistoryStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			HistoryStatus.setText(arrayList.get(position).getString("order_status_name"));
			HistoryStatus.setTextColor(ConstantsAndFunctions.getStatusColor(context,arrayList.get(position).getInt("order_status_id")));
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		catch (ClassCastException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e) 
		{
			e.printStackTrace();
		}
		
		return rowView;
	}
	
}