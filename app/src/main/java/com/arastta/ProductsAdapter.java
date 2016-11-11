package com.arastta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;
	int type = 0;

	public ProductsAdapter(Context context, ArrayList<JSONObject> arrayList, int type)
	{
		super(context, R.layout.item_products, arrayList);
		ScreenName = "ProductsAdapter";
		this.context = context;
		this.arrayList = arrayList;
		this.type = type;
	}
	/*
"product_id": "44",
"name": "Fitness Slim",
"description": "<p>Curabitur posuere ullamcorper dignissim. Sed ultrices tempor velit, et tristique diam consequat vitae.</p><p>Fusce nec dolor eu lectus fermentum rutrum quis ut nibh. Nullam luctus semper diam, ut mollis libero pellentesque a.</p><p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.</p><p>Nam urna dolor, consequat euismod neque ac, cursus posuere lectus. Aliquam aliquet porta consequat.</p><p>Donec faucibus nibh in odio pretium aliquet ac nec ipsum. Cras dui nisi, ullamcorper vel massa nec, accumsan porta quam. Nunc tortor nibh, viverra a ultricies id, ullamcorper cursus sapien. Phasellus vel sodales velit, a porttitor mauris. <br><br></p>",
"meta_title": "Fitness Slim",
"meta_description": "",
"meta_keyword": "",
"tag": "",
"model": "Product 17",
"sku": "",
"upc": "",
"ean": "",
"jan": "",
"isbn": "",
"mpn": "",
"location": "",
"quantity": "1500",
"stock_status": "Out Of Stock",
"stock_color": "#FF0000",
"manufacturer_id": "14",
"manufacturer": "Spiral",
"price": "15.0000",
"special": null,
"reward": "700",
"points": "0",
"tax_class_id": "9",
"date_available": "2015-05-17",
"weight": "0.00000000",
"weight_class_id": "1",
"length": "0.00000000",
"width": "0.00000000",
"height": "0.00000000",
"length_class_id": "2",
"subtract": "1",
"rating": 0,
"reviews": 0,
"minimum": "1",
"sort_order": "0",
"status": "1",
"date_added": "2015-05-18 00:00:00",
"date_modified": "2016-04-18 15:32:53",
"viewed": "0",
"nice_price": "$15.00",
"images": [
"image/cache/catalog/demo/product/striped-2-1-228x228.jpg",
"image/cache/catalog/demo/product/striped-2-2-228x228.jpg",
"image/cache/catalog/demo/product/striped-2-3-228x228.jpg",
"image/cache/catalog/demo/product/striped-2-4-228x228.jpg",
"image/cache/catalog/demo/product/striped-2-5-228x228.jpg"
]
	 */
	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_products, parent, false);
		
		try 
		{
			TextView ProductName = (TextView)rowView.findViewById(R.id.ProductName);
			ProductName.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			ProductName.setText(arrayList.get(position).getString("name"));

			TextView ProductID = (TextView)rowView.findViewById(R.id.ProductID);
			ProductID.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			ProductID.setText(context.getResources().getString(R.string.id)+" "+arrayList.get(position).getString("product_id"));

			TextView ProductModel = (TextView)rowView.findViewById(R.id.ProductModel);
			ProductModel.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			ProductModel.setText(context.getResources().getString(R.string.model)+" "+arrayList.get(position).getString("model"));

			TextView ProductValue = (TextView)rowView.findViewById(R.id.ProductValue);
			ProductValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			ProductValue.setText(arrayList.get(position).getString("nice_price"));

			TextView ProductQuantity = (TextView)rowView.findViewById(R.id.ProductQuantity);
			ProductQuantity.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			ProductQuantity.setText(context.getResources().getString(R.string.quantity)+" "+arrayList.get(position).getString("quantity"));

			TextView ProductStatus = (TextView)rowView.findViewById(R.id.ProductStatus);
			ProductStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

			if(arrayList.get(position).getString("status").equals("0")){
				ProductStatus.setText(context.getResources().getString(R.string.disable));
				ProductStatus.setTextColor(context.getResources().getColor(R.color.colorStatus07));
				ProductValue.setTextColor(context.getResources().getColor(R.color.colorStatus07));
			}

			/*JSONArray imagesArray = new JSONArray(arrayList.get(position).getJSONArray("images").toString());
			for (int i = 0; i < imagesArray.length(); i++) {
				String value=imagesArray.getString(i);
				Log.e("json", i+"="+value);
			}*/

			String avatarUrl = "";
			if(type == 0)
				avatarUrl = MasterActivity.url +"/"+ arrayList.get(position).getJSONArray("images").getString(0);
			else
				avatarUrl = MasterActivity.url +"/"+ arrayList.get(position).getString("image");

			ImageView ProductImage = (ImageView)rowView.findViewById(R.id.ProductImage);
			ImageOptions avatarOptions = new ImageOptions();
			//avatarOptions.round = 999;
			avatarOptions.fileCache = true;
			avatarOptions.memCache = true;
			AQuery AvatarAQ = new AQuery(context);
			AvatarAQ.id(ProductImage).image(avatarUrl, avatarOptions);

			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, ProductDetailsActivity.class);
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