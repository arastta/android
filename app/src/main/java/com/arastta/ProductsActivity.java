package com.arastta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ProductsActivity extends MasterActivity
{
    static Context context;
    static String ScreenName = "";

    static boolean Working = false;

    static TextView ProductsTotalValue;

    static ProductsAdapter adapter;
    static ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    static ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 3;

        super.onCreate(savedInstanceState);

        MenuTitle.setText(getResources().getString(R.string.products));

        setContentView(R.layout.activity_products);

        context = ProductsActivity.this;
        ScreenName = "ProductsActivity";

        //unUsed
        TextView TextViewProducts = (TextView)findViewById(R.id.TextViewProducts);
        TextViewProducts.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        ProductsTotalValue = (TextView)findViewById(R.id.ProductsTotalValue);
        ProductsTotalValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        listView = (ListView)findViewById(R.id.ProductsLV);
        listView.setSmoothScrollbarEnabled(true);
        listView.setDrawingCacheEnabled(true);
        listView.setWillNotCacheDrawing(true);
        listView.setHeaderDividersEnabled(false);

        xProducts("");
    }

    public static void xProducts(String text)
    {
        new getProducts().execute(text);
    }

    public static class getProducts extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute()
        {
            Working = true;
            loading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"products"+params[0]);

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Working = false;
            loading.dismiss();

            Log.e(ScreenName+":"+String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()),
                    ResultText);

            if(ResultText.equals("error"))
            {
                Toast.makeText(context, context.getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONArray jsonArray = (JSONArray) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    if (jsonArray.length() > 0)
                    {
                        arrayList.clear();
                        arrayList = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            arrayList.add(jsonArray.getJSONObject(i));
                        }
                        adapter = new ProductsAdapter(context,arrayList,0);
                        listView.setAdapter(adapter);

                        ProductsTotalValue.setText(String.valueOf(jsonArray.length()));
                    }
                    else
                    {
                        ProductsTotalValue.setText("0");

                        arrayList.clear();
                        arrayList = new ArrayList<JSONObject>();
                        adapter = new ProductsAdapter(context,arrayList,0);
                        listView.setAdapter(adapter);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
