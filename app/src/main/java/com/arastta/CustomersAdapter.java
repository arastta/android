package com.arastta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CustomersAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;

	//ImageView[] FortunesItemImage;

	public CustomersAdapter(Context context, ArrayList<JSONObject> arrayList)
	{
		super(context, R.layout.item_customers, arrayList);
		ScreenName = "CustomersAdapter";
		this.context = context;
		this.arrayList = arrayList;

		//FortunesItemImage = new ImageView[arrayList.size()];
	}
	/*
	"customer_id": "2",
"customer_group_id": "1",
"store_id": "0",
"firstname": "Ahmet",
"lastname": "Mehmet",
"email": "ahmet@arastta.com",
"telephone": "51654",
"fax": "",
"password": "b6617f32e90c46d76efed6d4b187ea515f600fa0",
"salt": "b859619f2",
"cart": "a:0:{}",
"wishlist": "",
"newsletter": "0",
"address_id": "2",
"custom_field": "a:0:{}",
"ip": "78.180.150.58",
"status": "1",
"approved": "1",
"safe": "0",
"token": "",
"date_added": "2016-06-01 11:12:35",
"language_id": "1",
"name": "Ahmet Mehmet",
"description": "Default",
"customer_group": "Default",
"order_number": 0,
"order_total": "$0.00"
	 */
	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_customers, parent, false);
		
		try 
		{
			TextView CustomerFullname = (TextView)rowView.findViewById(R.id.CustomerFullname);
			CustomerFullname.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			CustomerFullname.setText(arrayList.get(position).getString("firstname") +" "+ arrayList.get(position).getString("lastname"));

			TextView CustomerMail = (TextView)rowView.findViewById(R.id.CustomerMail);
			CustomerMail.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			CustomerMail.setText(arrayList.get(position).getString("email"));

			TextView CustomerID = (TextView)rowView.findViewById(R.id.CustomerID);
			CustomerID.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			CustomerID.setText(context.getResources().getString(R.string.customer_id)+" "+arrayList.get(position).getString("customer_id"));

			TextView CustomerOrders = (TextView)rowView.findViewById(R.id.CustomerOrders);
			CustomerOrders.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			CustomerOrders.setText(context.getResources().getString(R.string.order_number)+" "+arrayList.get(position).getString("order_number"));

			TextView CustomerRegistered = (TextView)rowView.findViewById(R.id.CustomerRegistered);
			CustomerRegistered.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			CustomerRegistered.setText(arrayList.get(position).getString("date_added"));

			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, CustomerDetailsActivity.class);
					i.putExtra("object",arrayList.get(position).toString());
					context.startActivity(i);
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