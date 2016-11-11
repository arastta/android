package com.arastta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arastta.GraphLib.Line;
import com.arastta.GraphLib.LineGraph;
import com.arastta.GraphLib.LinePoint;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class DashboardActivity extends MasterActivity
{
    Context context;
    String ScreenName = "";

    boolean Working = false;
    int day = 1;

    TextView ValueSales;
    TextView ValueOrders;
    TextView ValueCustomers;
    TextView ValueProducts;

    TextView DashboardDetailValue1;
    TextView DashboardDetailValue2;
    TextView DashboardDetailValue3;
    TextView DashboardDetailValue4;

    RelativeLayout TabLine1;
    RelativeLayout TabLine2;
    RelativeLayout TabLine3;

    TextView TabText1;
    TextView TabText2;
    TextView TabText3;

    LineGraph li;
    GraphView graphView;

    void resetAct()
    {
        ValueSales.setText("0");
        ValueOrders.setText("0");
        ValueCustomers.setText("0");
        ValueProducts.setText("0");

        DashboardDetailValue1.setText("0");
        DashboardDetailValue2.setText("0");
        DashboardDetailValue3.setText("0");
        DashboardDetailValue4.setText("0");
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
        activePage = 0;

        super.onCreate(savedInstanceState);

        MenuTitle.setText(getResources().getString(R.string.dashboard));

        setContentView(R.layout.activity_dashboard);

        context = DashboardActivity.this;
        ScreenName = "DashboardActivity";

        li = (LineGraph)findViewById(R.id.linegraph);
        graphView = (GraphView) findViewById(R.id.graphView);



        //unUsed
        TextView TextViewSales = (TextView)findViewById(R.id.TextViewSales);
        TextViewSales.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewOrders = (TextView)findViewById(R.id.TextViewOrders);
        TextViewOrders.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCustomers = (TextView)findViewById(R.id.TextViewCustomers);
        TextViewCustomers.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewProducts = (TextView)findViewById(R.id.TextViewProducts);
        TextViewProducts.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewDashboardDetail1 = (TextView)findViewById(R.id.TextViewDashboardDetail1);
        TextViewDashboardDetail1.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewDashboardDetail2 = (TextView)findViewById(R.id.TextViewDashboardDetail2);
        TextViewDashboardDetail2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewDashboardDetail3 = (TextView)findViewById(R.id.TextViewDashboardDetail3);
        TextViewDashboardDetail3.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewDashboardDetail4 = (TextView)findViewById(R.id.TextViewDashboardDetail4);
        TextViewDashboardDetail4.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

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
                new getDashboard().execute();
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
                new getDashboard().execute();
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
                new getDashboard().execute();
            }
        });

        ValueSales = (TextView)findViewById(R.id.ValueSales);
        ValueSales.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        ValueOrders = (TextView)findViewById(R.id.ValueOrders);
        ValueOrders.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        ValueCustomers = (TextView)findViewById(R.id.ValueCustomers);
        ValueCustomers.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        ValueProducts = (TextView)findViewById(R.id.ValueProducts);
        ValueProducts.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        DashboardDetailValue1 = (TextView)findViewById(R.id.DashboardDetailValue1);
        DashboardDetailValue1.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        DashboardDetailValue2 = (TextView)findViewById(R.id.DashboardDetailValue2);
        DashboardDetailValue2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        DashboardDetailValue3 = (TextView)findViewById(R.id.DashboardDetailValue3);
        DashboardDetailValue3.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        DashboardDetailValue4 = (TextView)findViewById(R.id.DashboardDetailValue4);
        DashboardDetailValue4.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        LinearLayout LayoutOrders = (LinearLayout)findViewById(R.id.LayoutOrders);
        LayoutOrders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, OrdersActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        LinearLayout LayoutCustomers = (LinearLayout)findViewById(R.id.LayoutCustomers);
        LayoutCustomers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, CustomersActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        LinearLayout LayoutProducts = (LinearLayout)findViewById(R.id.LayoutProducts);
        LayoutProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, ProductsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        day = 1;
        new getDashboard().execute();
    }

    private class getDashboard extends AsyncTask<String, Void, String>
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
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"stats"+"?date_from="+ConstantsAndFunctions.getFromDate(day)+"&date_to="+ConstantsAndFunctions.getTodayDate());//stats?date_from=2016-04-01&date_to=2016-09-07

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
                Toast.makeText(context, getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    JSONObject orders = jsonObject.getJSONObject("orders");
                    JSONObject customers = jsonObject.getJSONObject("customers");
                    JSONObject products = jsonObject.getJSONObject("products");

                    ValueSales.setText(orders.getString("nice_price"));
                    ValueOrders.setText(orders.getString("number"));
                    ValueCustomers.setText(customers.getString("number"));
                    ValueProducts.setText(products.getString("number"));

                    int totalValue = 0;
                    int maxValue = 0;
                    float orderValue = 0;
                    float orderNumber = 0;

                    ArrayList<Integer> number = new ArrayList<Integer>();
                    ArrayList<Integer> prices = new ArrayList<Integer>();
                    JSONArray daily = orders.getJSONArray("daily");

                    if(daily.length() > 0)
                    {
                        Log.i("daily.length",String.valueOf(daily.length()) + " day");
                        for(int i=0; i<daily.length();i++)
                        {
                            orderNumber += daily.getJSONObject(i).getInt("number");
                            number.add(daily.getJSONObject(i).getInt("number"));
                            try
                            {
                                orderValue += daily.getJSONObject(i).getInt("price");
                                prices.add(daily.getJSONObject(i).getInt("price"));

                                totalValue += daily.getJSONObject(i).getInt("price");

                                if(maxValue < daily.getJSONObject(i).getInt("price"))
                                    maxValue = daily.getJSONObject(i).getInt("price");
                            }
                            catch (JSONException e)
                            {
                                prices.add(0);
                            }
                        }
                        Log.i("orderValue",String.valueOf(orderValue));
                        Log.i("orderNumber",String.valueOf(orderNumber));

                        Log.i("totalValue",String.valueOf(totalValue));
                        Log.i("maxValue",String.valueOf(maxValue));


                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                        series.setColor(getResources().getColor(R.color.colorPrimary));
                        try
                        {
                            if (prices.size() > 0)
                            {
                                series.appendData(new DataPoint(0, 0), false, maxValue);
                                for (int j = 0; j < prices.size(); j++)
                                {
                                    series.appendData(new DataPoint(j + 1, prices.get(j)), false, maxValue);
                                }
                            }
                        }
                        catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }

                        graphView.getViewport().setXAxisBoundsManual(true);
                        graphView.getViewport().setMinX(0);
                        graphView.getViewport().setMaxX(daily.length());
                        graphView.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.colorAccent));
                        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.colorAccent));
                        graphView.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.colorAccent));
                        graphView.removeAllSeries();
                        graphView.addSeries(series);

/*
                        //https://github.com/Androguide/HoloGraphLibrary
                        Line l = new Line();
                        Log.i("prices",String.valueOf(prices.size()));

                        for (int j=0;j<prices.size();j++)
                        {
                            LinePoint p = new LinePoint();
                            p.setX(j);
                            p.setY(prices.get(j));
                            l.addPoint(p);
                        }
                        l.setColor(getResources().getColor(R.color.colorPrimary));

                        li.showHorizontalGrid(true);
                        li.removeAllLines();
                        li.addLine(l);
                        li.setRangeY(0, maxValue + ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8));
                        li.setLineToFill(0);//1 fill empty :S
*/
                        float customerNumber = customers.getInt("number");
                        if(orderValue != 0)
                            DashboardDetailValue1.setText(String.valueOf(Math.round(Float.valueOf(orderValue/day)*10.0)/10.0));//100
                        if(orderNumber != 0)
                            DashboardDetailValue2.setText(String.valueOf(Math.round(Float.valueOf(orderNumber/day)*10.0)/10.0));
                        if(customerNumber != 0)
                            DashboardDetailValue3.setText(String.valueOf(Math.round(Float.valueOf(customerNumber/day)*10.0)/10.0));
                        if(orderNumber != 0  && customerNumber != 0)
                            DashboardDetailValue4.setText(String.valueOf(Math.round(Float.valueOf(orderValue/customerNumber)*10.0)/10.0));
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
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
