package com.arastta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrdersActivity extends MasterActivity
{
    static Context context;
    static String ScreenName = "";

    static boolean Working = false;
    static int day = 1;

    static TextView OrdersTotalValue;

    static OrdersAdapter adapter;
    static ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    static ListView listView;

    RelativeLayout TabLine1;
    RelativeLayout TabLine2;
    RelativeLayout TabLine3;

    TextView TabText1;
    TextView TabText2;
    TextView TabText3;

    void resetAct()
    {
        arrayList.clear();
        arrayList = new ArrayList<JSONObject>();
        adapter = new OrdersAdapter(context,arrayList);
        listView.setAdapter(adapter);
        OrdersTotalValue.setText("$0.00");
    }

    void resetTabs()
    {
        TabLine1.setVisibility(RelativeLayout.INVISIBLE);
        TabLine2.setVisibility(RelativeLayout.INVISIBLE);
        TabLine3.setVisibility(RelativeLayout.INVISIBLE);

        TabText1.setTextColor(getResources().getColor(R.color.colorHint));
        TabText2.setTextColor(getResources().getColor(R.color.colorHint));
        TabText3.setTextColor(getResources().getColor(R.color.colorHint));

        TabText1.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TabText3.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        resetAct();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 1;

        super.onCreate(savedInstanceState);

        MenuTitle.setText(getResources().getString(R.string.orders));

        setContentView(R.layout.activity_orders);

        context = OrdersActivity.this;
        ScreenName = "OrdersActivity";

        //unUsed
        TextView TextViewTotal = (TextView)findViewById(R.id.TextViewTotal);
        TextViewTotal.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        //Tabs
        TabLine1 = (RelativeLayout)findViewById(R.id.TabLine1);
        TabLine2 = (RelativeLayout)findViewById(R.id.TabLine2);
        TabLine3 = (RelativeLayout)findViewById(R.id.TabLine3);

        TabText1 = (TextView)findViewById(R.id.TabText1);
        TabText1.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TabText2 = (TextView)findViewById(R.id.TabText2);
        TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TabText3 = (TextView)findViewById(R.id.TabText3);
        TabText3.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        TabText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabs();
                TabLine1.setVisibility(RelativeLayout.VISIBLE);
                TabText1.setTextColor(getResources().getColor(R.color.colorPrimary));
                TabText1.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                day = 1;
                xOrders("");
            }
        });

        TabText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabs();
                TabLine2.setVisibility(RelativeLayout.VISIBLE);
                TabText2.setTextColor(getResources().getColor(R.color.colorPrimary));
                TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                day = 30;
                xOrders("");
            }
        });

        TabText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabs();
                TabLine3.setVisibility(RelativeLayout.VISIBLE);
                TabText3.setTextColor(getResources().getColor(R.color.colorPrimary));
                TabText3.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                day = 365;
                xOrders("");
            }
        });


        OrdersTotalValue = (TextView)findViewById(R.id.OrdersTotalValue);
        OrdersTotalValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        listView = (ListView)findViewById(R.id.OrdersLV);
        listView.setSmoothScrollbarEnabled(true);
        listView.setDrawingCacheEnabled(true);
        listView.setWillNotCacheDrawing(true);
        listView.setHeaderDividersEnabled(false);

        day = 1;
        xOrders("");
    }

    public static void xOrders(String text)
    {
        new getOrders().execute(text);
    }

    public static class getOrders extends AsyncTask<String, Void, String>
    {
        String ResultText = "";
        String text = "";

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
            text = params[0];
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"orders?order=DESC"+"&date_from="+ConstantsAndFunctions.getFromDate(day)+"&date_to="+ConstantsAndFunctions.getTodayDate()+text);//stats?date_from=2016-04-01&date_to=2016-09-07

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
                        adapter = new OrdersAdapter(context,arrayList);
                        listView.setAdapter(adapter);
                    }
                    else
                    {
                        arrayList.clear();
                        arrayList = new ArrayList<JSONObject>();
                        adapter = new OrdersAdapter(context,arrayList);
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

            new getOrdersTotal().execute(text);
        }
    }

    public static class getOrdersTotal extends AsyncTask<String, Void, String>
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
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"orders/totals"+"?date_from="+ConstantsAndFunctions.getFromDate(day)+"&date_to="+ConstantsAndFunctions.getTodayDate()+params[0]);//stats?date_from=2016-04-01&date_to=2016-09-07

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
                    JSONObject jsonObject = (JSONObject)new JSONTokener(String.valueOf(ResultText)).nextValue();
                    OrdersTotalValue.setText(jsonObject.getString("nice_price"));
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
