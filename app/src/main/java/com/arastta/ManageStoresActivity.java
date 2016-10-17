package com.arastta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManageStoresActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    static String savePath = "";

    ListView ManageStoresLV;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 5;

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_manage_stores);

        //SetUp();

        //loadStores();
    }

    @Override
    protected void onResume() {
        activePage = 5;

        SetUp();

        loadStores();

        super.onResume();
    }

    public void SetUp()
    {
        context = ManageStoresActivity.this;
        ScreenName = "ManageStoresActivity";

        savePath = getFilesDir().toString();

        MenuTitle.setText(getResources().getString(R.string.manage_stores));

        ManageStoresLV = (ListView)findViewById(R.id.ManageStoresLV);
        ManageStoresLV.setSmoothScrollbarEnabled(true);
        ManageStoresLV.setDrawingCacheEnabled(true);
        ManageStoresLV.setWillNotCacheDrawing(true);
        ManageStoresLV.setHeaderDividersEnabled(false);

        View footerView = View.inflate(context, R.layout.item_stores, null);
        if(ManageStoresLV.getFooterViewsCount() == 0)ManageStoresLV.addFooterView(footerView);

        TextView StoreTitle = (TextView)footerView.findViewById(R.id.StoreTitle);
        StoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        ImageButton StoreButton = (ImageButton)footerView.findViewById(R.id.StoreButton);
        StoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(context, StoreActivity.class));
            }
        });
    }

    public void loadStores()
    {
        try
        {
            JSONArray jsonArray = new JSONArray(ConstantsAndFunctions.readToFile(savePath,1));
            Log.e("loadStores","jsonArray.length:"+jsonArray.length());

            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
            for (int i = 0; i<jsonArray.length(); i++)
            {
                arrayList.add(jsonArray.getJSONObject(i));
            }
            StoresAdapter adapter = new StoresAdapter(context,arrayList,1);
            ManageStoresLV.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}
