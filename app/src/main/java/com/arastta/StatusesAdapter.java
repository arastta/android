package com.arastta;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusesAdapter extends ArrayAdapter<JSONObject>
{
	Context context;
	String ScreenName = "";

	View rowView = null;
	ArrayList<JSONObject> arrayList = null;

	static CheckBox[] StatusCheckBox;

	public StatusesAdapter(Context context, ArrayList<JSONObject> arrayList)
	{
		super(context, R.layout.item_statuses, arrayList);
		ScreenName = "StatusesAdapter";
		this.context = context;
		this.arrayList = arrayList;
		this.StatusCheckBox = new CheckBox[arrayList.size()];

	}

	@SuppressLint("ViewHolder") 
	public View getView(final int position, final View convertView, final ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rowView = inflater.inflate(R.layout.item_statuses, parent, false);
		
		try 
		{
			TextView StatusName = (TextView)rowView.findViewById(R.id.StatusName);
			StatusName.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
			StatusName.setText(arrayList.get(position).getString("name"));
			StatusName.setTextColor(ConstantsAndFunctions.getStatusColor(context,arrayList.get(position).getInt("order_status_id")));

			StatusCheckBox[position] = (CheckBox)rowView.findViewById(R.id.StatusCheckBox);
			StatusCheckBox[position].setChecked(AppWidgetSettings.trues[position]);
			StatusCheckBox[position].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(!AppWidgetSettings.selectAll)AppWidgetSettings.trues[position] = isChecked;
					setTextBox();
				}
			});

			RelativeLayout StatusesItem = (RelativeLayout)rowView.findViewById(R.id.StatusesItem);
			StatusesItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					StatusCheckBox[position].setChecked(!StatusCheckBox[position].isChecked());
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

	void setTextBox()
	{
		String texts = "";
		for(int tk=0;tk<AppWidgetSettings.trues.length;tk++)
		{
			if(AppWidgetSettings.trues[tk])
			{
				try
				{
					texts = texts + "," + arrayList.get(tk).getString("name");
				}
				catch (JSONException e){
					e.printStackTrace();
				}
			}
		}
		if(texts.length() > 0)texts = texts.substring(1,texts.length());

		AppWidgetSettings.WidgetSettingsTextStatus.setText(texts);
	}

}