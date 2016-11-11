package com.arastta;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class OrderDetailsActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    ImageView OrderStatusIcon;
    TextView OrderStatus;

    String OrderID = "";
    ScrollView OrderInfo;

    ListView listView;
    ProductsAdapter adapter;
    static ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    ListView listView2;
    HistoryAdapter adapter2;
    static ArrayList<JSONObject> arrayList2 = new ArrayList<JSONObject>();

    boolean OpenChanges = false;
    ScrollView StatusChangeArea;

    ArrayList<JSONObject> Statuses = new ArrayList<JSONObject>();
    List<String> StatusList = new ArrayList<String>();

    Spinner StatusSpinner;
    ArrayList<String> statuses;

    RelativeLayout TabLine1;
    RelativeLayout TabLine2;
    RelativeLayout TabLine3;

    TextView TabText1;
    TextView TabText2;
    TextView TabText3;

    void resetAct()
    {
        OrderInfo.setVisibility(LinearLayout.GONE);
        listView.setVisibility(ListView.GONE);
        listView2.setVisibility(ListView.GONE);
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
        super.setContentView(R.layout.activity_order_details);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        context = OrderDetailsActivity.this;
        ScreenName = "OrderDetailsActivity";

        //unUsed
        TextView OrderTotal = (TextView)findViewById(R.id.OrderTotal);
        OrderTotal.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView OrderInfoID = (TextView)findViewById(R.id.OrderInfoID);
        OrderInfoID.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoInvoicePrefix = (TextView)findViewById(R.id.OrderInfoInvoicePrefix);
        OrderInfoInvoicePrefix.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoCompany = (TextView)findViewById(R.id.OrderInfoCompany);
        OrderInfoCompany.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoAddress = (TextView)findViewById(R.id.OrderInfoAddress);
        OrderInfoAddress.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoCity = (TextView)findViewById(R.id.OrderInfoCity);
        OrderInfoCity.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoCountry = (TextView)findViewById(R.id.OrderInfoCountry);
        OrderInfoCountry.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoEmail = (TextView)findViewById(R.id.OrderInfoEmail);
        OrderInfoEmail.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoPhone = (TextView)findViewById(R.id.OrderInfoPhone);
        OrderInfoPhone.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoFax = (TextView)findViewById(R.id.OrderInfoFax);
        OrderInfoFax.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView OrderInfoPaymentMethod = (TextView)findViewById(R.id.OrderInfoPaymentMethod);
        OrderInfoPaymentMethod.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

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
                listView.setVisibility(ListView.VISIBLE);
            }
        });

        TabText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabs();
                TabLine2.setVisibility(RelativeLayout.VISIBLE);
                TabText2.setTextColor(getResources().getColor(R.color.colorPrimary));
                TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                OrderInfo.setVisibility(LinearLayout.VISIBLE);
            }
        });

        TabText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabs();
                TabLine3.setVisibility(RelativeLayout.VISIBLE);
                TabText3.setTextColor(getResources().getColor(R.color.colorPrimary));
                TabText3.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                listView2.setVisibility(ListView.VISIBLE);
            }
        });

        OrderInfo = (ScrollView)findViewById(R.id.OrderInfo);

        listView = (ListView) findViewById(R.id.OrderProductsLV);
        listView.setSmoothScrollbarEnabled(true);
        listView.setDrawingCacheEnabled(true);
        listView.setWillNotCacheDrawing(true);
        listView.setHeaderDividersEnabled(false);

        listView2 = (ListView) findViewById(R.id.OrderHistoriesLV);
        listView2.setSmoothScrollbarEnabled(true);
        listView2.setDrawingCacheEnabled(true);
        listView2.setWillNotCacheDrawing(true);
        listView2.setHeaderDividersEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText(extras.getString("object"));

            try
            {
                JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(extras.getString("object"))).nextValue();

                OrderID = jsonObject.getString("order_id");

                MenuTitle.setText(jsonObject.getString("firstname") +" "+ jsonObject.getString("lastname"));

                TextView OrderTotalValue = (TextView)findViewById(R.id.OrderTotalValue);
                OrderTotalValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                OrderTotalValue.setText(jsonObject.getString("nice_total"));

                TextView OrderDate = (TextView)findViewById(R.id.OrderDate);
                OrderDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderDate.setText(jsonObject.getString("date_added"));

                OrderStatus = (TextView)findViewById(R.id.OrderStatus);
                OrderStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderStatus.setText(jsonObject.getString("order_status"));

                OrderStatusIcon = (ImageView)findViewById(R.id.OrderStatusIcon);
                int status_color = ConstantsAndFunctions.getStatusColor(context,jsonObject.getInt("order_status_id"));
                OrderStatusIcon.getBackground().mutate().setColorFilter(status_color, PorterDuff.Mode.MULTIPLY);

                TextView OrderInfoIDValue = (TextView)findViewById(R.id.OrderInfoIDValue);
                OrderInfoIDValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoIDValue.setText(jsonObject.getString("order_id"));

                TextView OrderInfoInvoicePrefixValue = (TextView)findViewById(R.id.OrderInfoInvoicePrefixValue);
                OrderInfoInvoicePrefixValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoInvoicePrefixValue.setText(jsonObject.getString("invoice_prefix"));

                TextView OrderInfoCompanyValue = (TextView)findViewById(R.id.OrderInfoCompanyValue);
                OrderInfoCompanyValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoCompanyValue.setText(jsonObject.getString("payment_company"));

                TextView OrderInfoAddressValue = (TextView)findViewById(R.id.OrderInfoAddressValue);
                OrderInfoAddressValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoAddressValue.setText(jsonObject.getString("payment_address_1") +" "+ jsonObject.getString("payment_address_2") +" "+ jsonObject.getString("payment_postcode"));

                TextView OrderInfoCityValue = (TextView)findViewById(R.id.OrderInfoCityValue);
                OrderInfoCityValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoCityValue.setText(jsonObject.getString("payment_city"));

                TextView OrderInfoCountryValue = (TextView)findViewById(R.id.OrderInfoCountryValue);
                OrderInfoCountryValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoCountryValue.setText(jsonObject.getString("payment_country"));

                TextView OrderInfoEmailValue = (TextView)findViewById(R.id.OrderInfoEmailValue);
                OrderInfoEmailValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoEmailValue.setText(jsonObject.getString("email"));

                TextView OrderInfoPhoneValue = (TextView)findViewById(R.id.OrderInfoPhoneValue);
                OrderInfoPhoneValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoPhoneValue.setText(jsonObject.getString("telephone"));

                TextView OrderInfoFaxValue = (TextView)findViewById(R.id.OrderInfoFaxValue);
                OrderInfoFaxValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoFaxValue.setText(jsonObject.getString("fax"));

                TextView OrderInfoPaymentMethodValue = (TextView)findViewById(R.id.OrderInfoPaymentMethodValue);
                OrderInfoPaymentMethodValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                OrderInfoPaymentMethodValue.setText(jsonObject.getString("payment_method"));

                RelativeLayout StatusButton = (RelativeLayout)findViewById(R.id.StatusButton);
                StatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        StatusChangeArea.setVisibility(ScrollView.VISIBLE);
                        OpenChanges = true;
                    }
                });

                new getOrderProducts().execute();
                new getOrderStatuses().execute();
                new getOrderHistories().execute();

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }


            //StatusChange
            StatusChangeArea = (ScrollView)findViewById(R.id.StatusChangeArea);

            StatusSpinner = (Spinner)findViewById(R.id.StatusSpinner);
            //setStatusSpinner();

            final CheckBox NotifyCheckBox = (CheckBox)findViewById(R.id.NotifyCheckBox);

            RelativeLayout NotifyCheckBoxArea = (RelativeLayout)findViewById(R.id.NotifyCheckBoxArea);
            NotifyCheckBoxArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotifyCheckBox.setChecked(!NotifyCheckBox.isChecked());
                }
            });

            TextView NotifyTextView = (TextView)findViewById(R.id.NotifyTextView);
            NotifyTextView.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

            TextView CommentTextView = (TextView)findViewById(R.id.CommentTextView);
            CommentTextView.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

            final EditText CommentEditText = (EditText)findViewById(R.id.CommentEditText);
            CommentEditText.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

            TextView CancelButton = (TextView)findViewById(R.id.CancelButton);
            CancelButton.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

            TextView SaveButton = (TextView)findViewById(R.id.SaveButton);
            SaveButton.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

            CancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    StatusChangeArea.setVisibility(ScrollView.GONE);
                    OpenChanges = false;
                }
            });

            SaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        String status = Statuses.get(StatusSpinner.getSelectedItemPosition()).getString("order_status_id");
                        Log.e("status",status);

                        String notify = NotifyCheckBox.isChecked() ? "1" : "0";
                        Log.e("notify",notify);

                        String comment = CommentEditText.getText().toString().trim();
                        Log.e("comment",comment);

                        new setOrderStatus().execute(status,notify,comment);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    StatusChangeArea.setVisibility(ScrollView.GONE);
                    OpenChanges = false;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(OpenChanges)
        {
            StatusChangeArea.setVisibility(ScrollView.GONE);
            OpenChanges = false;
        }
        else
        {
            super.onBackPressed();
        }
    }

    private class getOrderProducts extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(MasterActivity.username,MasterActivity.password,MasterActivity.url,"orders/"+OrderID+"/products");

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                    JSONArray jsonArray = (JSONArray) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    if (jsonArray.length() > 0)
                    {
                        arrayList.clear();
                        arrayList = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            arrayList.add(jsonArray.getJSONObject(i));
                        }
                        adapter = new ProductsAdapter(context,arrayList,1);
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

    private class getOrderStatuses extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(MasterActivity.username,MasterActivity.password,MasterActivity.url,"orders/statuses");

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                    JSONArray jsonArray = (JSONArray) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    if (jsonArray.length() > 0)
                    {
                        Statuses.clear();
                        StatusList.clear();

                        Statuses = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            Statuses.add(jsonArray.getJSONObject(i));

                            StatusList.add(jsonArray.getJSONObject(i).getString("name"));
                        }

                        /*
                        arrayList2.clear();
                        arrayList2 = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            arrayList2.add(jsonArray.getJSONObject(i));
                        }
                        adapter2 = new HistoryAdapter(context,arrayList2);
                        listView2.setAdapter(adapter2);
                        */

                        /*Statuses.clear();
                        StatusList.clear();

                        String[] rl=context.getResources().getStringArray(R.array.StatusesLists);
                        for(int i=0;i<rl.length;i++)
                        {
                            String[] g=rl[i].split(",");
                            try
                            {
                                JSONObject cc = new JSONObject();

                                cc.put("status_name", g[0]);
                                cc.put("status_id", g[1]);
                                cc.put("status_colour", g[2]);

                                Statuses.add(cc);

                                StatusList.add(g[0]);
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }*/

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, StatusList)
                        {
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View v = super.getView(position, convertView, parent);
                                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                                ((TextView) v).setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8),0,0,0);
                                //((TextView) v).setGravity(Gravity.CENTER);
                                try
                                {
                                    ((TextView) v).setTextColor(ConstantsAndFunctions.getStatusColor(context,Statuses.get(position).getInt("order_status_id")));
                                    //((TextView) v).setTextColor(Color.parseColor(Statuses.get(position).getString("status_colour")));
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }

                                return v;
                            }
                            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                                View v =super.getDropDownView(position, convertView, parent);
                                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                ((TextView) v).setGravity(Gravity.CENTER);
                                try
                                {
                                    ((TextView) v).setTextColor(ConstantsAndFunctions.getStatusColor(context,Statuses.get(position).getInt("order_status_id")));
                                    //((TextView) v).setTextColor(Color.parseColor(Statuses.get(position).getString("status_colour")));
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }
                                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBg));
                                return v;
                            }
                        };
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        StatusSpinner.setAdapter(dataAdapter);
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

    private class getOrderHistories extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(MasterActivity.username,MasterActivity.password,MasterActivity.url,"orders/"+OrderID+"/histories");

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                    JSONArray jsonArray = (JSONArray) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    if (jsonArray.length() > 0)
                    {
                        arrayList2.clear();
                        arrayList2 = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            arrayList2.add(jsonArray.getJSONObject(i));
                        }
                        adapter2 = new HistoryAdapter(context,arrayList2);
                        listView2.setAdapter(adapter2);
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

    private class setOrderStatus extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params)
        {
            String result = "";

            try
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url = new URL(MasterActivity.url + "/api/"+ "orders/"+OrderID+"/histories");

                if(MasterActivity.url.startsWith("https"))
                {
                    //TLS

                    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

                    String encoded = Base64.encodeToString((MasterActivity.username+":"+MasterActivity.password).getBytes("UTF-8"), Base64.NO_WRAP);
                    con.setRequestProperty("Authorization", "Basic "+encoded);
                    con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty( "charset", "utf-8");
                    con.setRequestMethod("POST");
                    con.setReadTimeout(10000);
                    con.setConnectTimeout(10000);
                    con.setUseCaches( false );
                    //con.setDoInput(true);
                    //con.setDoOutput(true);

                    String status = URLEncoder.encode(params[0], "UTF-8");
                    String notify = URLEncoder.encode(params[1], "UTF-8");
                    String comment = URLEncoder.encode(params[2], "UTF-8");

                    String urlParameters = "status="+status+"&notify="+notify+"&comment="+comment;

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();

                    result = response.toString();
                }
                else
                {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    String encoded = Base64.encodeToString((MasterActivity.username+":"+MasterActivity.password).getBytes("UTF-8"), Base64.NO_WRAP);
                    con.setRequestProperty("Authorization", "Basic "+encoded);
                    con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    con.setRequestProperty( "charset", "utf-8");
                    con.setRequestMethod("POST");
                    con.setReadTimeout(10000);
                    con.setConnectTimeout(10000);
                    con.setUseCaches( false );
                    //con.setDoInput(true);
                    //con.setDoOutput(true);

                    String status = URLEncoder.encode(params[0], "UTF-8");
                    String notify = URLEncoder.encode(params[1], "UTF-8");
                    String comment = URLEncoder.encode(params[2], "UTF-8");

                    String urlParameters = "status="+status+"&notify="+notify+"&comment="+comment;

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuffer response = new StringBuffer();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();

                    result = response.toString();
                }

            }
            catch (IOException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.e(ScreenName+":"+String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()),
                    result);

            Toast.makeText(context, getResources().getString(R.string.order_status_complete), Toast.LENGTH_SHORT).show();

            try
            {
                int id = Statuses.get(StatusSpinner.getSelectedItemPosition()).getInt("order_status_id");
                String status = Statuses.get(StatusSpinner.getSelectedItemPosition()).getString("name");
                //String color = Statuses.get(StatusSpinner.getSelectedItemPosition()).getString("status_colour");

                OrderStatusIcon.getBackground().mutate().setColorFilter(ConstantsAndFunctions.getStatusColor(context, id), PorterDuff.Mode.MULTIPLY);

                OrderStatus.setText(status);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            new getOrderHistories().execute();
        }
    }

}
