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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;

	//ImageView[] FortunesItemImage;

	public OrdersAdapter(Context context, ArrayList<JSONObject> arrayList)
	{
		super(context, R.layout.item_orders, arrayList);
		ScreenName = "OrdersAdapter";
		this.context = context;
		this.arrayList = arrayList;

		//FortunesItemImage = new ImageView[arrayList.size()];
	}
	/*
"order_id": "18",
"invoice_no": "0",
"invoice_prefix": "INV-2015-00",
"store_id": "0",
"store_name": "Mobile",
"store_url": "http://mobile.arastta.com/",
"customer_id": "1",
"firstname": "Ali",
"lastname": "Veli",
"email": "ali@arastta.com",
"telephone": "14587",
"fax": "",
"custom_field": false,
"payment_firstname": "Ali",
"payment_lastname": "Veli",
"payment_company": "",
"payment_address_1": "Cumhuriyet Cd.",
"payment_address_2": "",
"payment_postcode": "34722",
"payment_city": "İstanbul",
"payment_zone_id": "3354",
"payment_zone": "İstanbul",
"payment_zone_code": "IST",
"payment_country_id": "215",
"payment_country": "Turkey",
"payment_iso_code_2": "TR",
"payment_iso_code_3": "TUR",
"payment_address_format": "",
"payment_custom_field": false,
"payment_method": "Cash On Delivery",
"payment_code": "cod",
"shipping_firstname": "Ali",
"shipping_lastname": "Veli",
"shipping_company": "",
"shipping_address_1": "Cumhuriyet Cd.",
"shipping_address_2": "",
"shipping_postcode": "34722",
"shipping_city": "İstanbul",
"shipping_zone_id": "3354",
"shipping_zone": "İstanbul",
"shipping_zone_code": "IST",
"shipping_country_id": "215",
"shipping_country": "Turkey",
"shipping_iso_code_2": "TR",
"shipping_iso_code_3": "TUR",
"shipping_address_format": "",
"shipping_custom_field": false,
"shipping_method": "Flat Shipping Rate",
"shipping_code": "flat.flat",
"comment": "",
"total": "18.0000",
"order_status_id": "2",
"order_status": "Processing",
"affiliate_id": "0",
"commission": "0.0000",
"language_id": "1",
"language_code": "en",
"language_directory": "en-GB",
"currency_id": "2",
"currency_code": "USD",
"currency_value": "1.00000000",
"ip": "78.180.150.58",
"forwarded_ip": "78.180.150.58",
"user_agent": "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 OPR/39.0.2256.71",
"accept_language": "en-US,en;q=0.8",
"date_modified": "2016-09-26 07:44:04",
"date_added": "2016-09-26 07:00:14",
"nice_total": "$18.00",
"products": [
{
"order_product_id": "29",
"order_id": "18",
"product_id": "30",
"name": "Mesh Breathable",
"model": "Product 3",
"quantity": "1",
"price": "13.0000",
"total": "13.0000",
"tax": "0.0000",
"reward": "200",
"nice_total": "$13.00"
}
]
	 */
	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_orders, parent, false);
		
		try 
		{
			TextView OrderFullname = (TextView)rowView.findViewById(R.id.OrderFullname);
			OrderFullname.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderFullname.setText(arrayList.get(position).getString("firstname") +" "+ arrayList.get(position).getString("lastname"));

			TextView OrderStatus = (TextView)rowView.findViewById(R.id.OrderStatus);
			OrderStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderStatus.setText(arrayList.get(position).getString("order_status"));
			OrderStatus.setTextColor(ConstantsAndFunctions.getStatusColor(context,arrayList.get(position).getInt("order_status_id")));

			TextView OrderCustomerID = (TextView)rowView.findViewById(R.id.OrderCustomerID);
			OrderCustomerID.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderCustomerID.setText(context.getResources().getString(R.string.order_id)+" "+arrayList.get(position).getString("order_id"));

			TextView OrderValue = (TextView)rowView.findViewById(R.id.OrderValue);
			OrderValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderValue.setText(arrayList.get(position).getString("nice_total"));
			OrderValue.setTextColor(ConstantsAndFunctions.getStatusColor(context,arrayList.get(position).getInt("order_status_id")));

			TextView OrderDate = (TextView)rowView.findViewById(R.id.OrderDate);
			OrderDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderDate.setText(arrayList.get(position).getString("date_added"));

			JSONArray productsArray = new JSONArray(arrayList.get(position).getJSONArray("products").toString());

			TextView OrderItemNumber = (TextView)rowView.findViewById(R.id.OrderItemNumber);
			OrderItemNumber.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			OrderItemNumber.setText(context.getResources().getString(R.string.product_number)+" "+String.valueOf(productsArray.length()));

			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, OrderDetailsActivity.class);
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