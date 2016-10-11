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

import java.util.ArrayList;

public class CustomersActivity extends MasterActivity
{

    static Context context;
    static String ScreenName = "";

    static boolean Working = false;
    static int day = 1;

    static CustomersAdapter adapter;
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
        adapter = new CustomersAdapter(context,arrayList);
        listView.setAdapter(adapter);
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
        activePage = 2;

        super.onCreate(savedInstanceState);

        MenuTitle.setText(getResources().getString(R.string.customers));

        setContentView(R.layout.activity_customers);

        context = CustomersActivity.this;
        ScreenName = "CustomersActivity";

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
                xCustomers("");
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
                xCustomers("");
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
                xCustomers("");
            }
        });


        listView = (ListView)findViewById(R.id.CustomersLV);
        listView.setSmoothScrollbarEnabled(true);
        listView.setDrawingCacheEnabled(true);
        listView.setWillNotCacheDrawing(true);
        listView.setHeaderDividersEnabled(false);

        day = 1;
        xCustomers("");
    }

    public static void xCustomers(String text)
    {
        new getCustomers().execute(text);
    }

    public static class getCustomers extends AsyncTask<String, Void, String>
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
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"customers"+"?date_from="+ConstantsAndFunctions.getFromDate(day)+"&date_to="+ConstantsAndFunctions.getTodayDate()+params[0]);//stats?date_from=2016-04-01&date_to=2016-09-07

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
                        adapter = new CustomersAdapter(context,arrayList);
                        listView.setAdapter(adapter);
                    }
                    else
                    {
                        arrayList.clear();
                        arrayList = new ArrayList<JSONObject>();
                        adapter = new CustomersAdapter(context,arrayList);
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
