package com.arastta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoresAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;

	int type;

	public StoresAdapter(Context context, ArrayList<JSONObject> arrayList, int type)
	{
		super(context, R.layout.item_stores, arrayList);
		ScreenName = "StoresAdapter";
		this.context = context;
		this.arrayList = arrayList;
		this.type = type;
	}

	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_stores, parent, false);
		
		try 
		{
			TextView StoreTitle = (TextView)rowView.findViewById(R.id.StoreTitle);
			StoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			StoreTitle.setText(arrayList.get(position).getString("store_name"));

			ImageView StoreButton = (ImageView)rowView.findViewById(R.id.StoreButton);
			switch (type)
			{
				case 0:
					StoreButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_select));
					break;
				case 1:
					StoreButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.menu_settings));
					break;
			}

			RelativeLayout StoreItem = (RelativeLayout)rowView.findViewById(R.id.StoreItem);
			StoreItem.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Log.e("StoreButton","OnClickListener");

					Intent i = null;

					switch (type)
					{
						case 0:
							Log.i("Obj",arrayList.get(position).toString());

							ConstantsAndFunctions.writeToFile(context.getFilesDir().toString(), 0, arrayList.get(position).toString());

							switch (MasterActivity.activePage)
							{
								case 0:
									i = new Intent(context, DashboardActivity.class);
									break;
								case 1:
									i = new Intent(context, OrdersActivity.class);
									break;
								case 2:
									i = new Intent(context, CustomersActivity.class);
									break;
								case 3:
									i = new Intent(context, ProductsActivity.class);
									break;
								default:
									i = new Intent(context, DashboardActivity.class);
									break;
							}
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(i);
							((Activity)context).finish();
							break;
						case 1:
							Log.i("Obj",arrayList.get(position).toString());

							i = new Intent(context, StoreActivity.class);
							i.putExtra("object",arrayList.get(position).toString());
							context.startActivity(i);
							break;
					}
				}
			});
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