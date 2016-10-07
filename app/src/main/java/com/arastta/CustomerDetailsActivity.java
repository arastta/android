package com.arastta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class CustomerDetailsActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    String CustomerID = "";
    LinearLayout CustomerInfo;

    ListView listView;
    OrdersAdapter adapter;
    static ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    RelativeLayout TabLine1;
    RelativeLayout TabLine2;

    TextView TabText1;
    TextView TabText2;

    void resetAct()
    {
        CustomerInfo.setVisibility(LinearLayout.GONE);
        listView.setVisibility(ListView.GONE);
    }

    void resetTabs()
    {
        TabLine1.setVisibility(RelativeLayout.INVISIBLE);
        TabLine2.setVisibility(RelativeLayout.INVISIBLE);

        TabText1.setTextColor(getResources().getColor(R.color.colorHint));
        TabText2.setTextColor(getResources().getColor(R.color.colorHint));

        TabText1.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        resetAct();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 2;

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_customer_details);

        context = CustomerDetailsActivity.this;
        ScreenName = "CustomerDetailsActivity";

        //unUsed
        TextView CustomerOrders = (TextView)findViewById(R.id.CustomerOrders);
        CustomerOrders.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView CustomerInfoID = (TextView)findViewById(R.id.CustomerInfoID);
        CustomerInfoID.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView CustomerInfoFullname = (TextView)findViewById(R.id.CustomerInfoFullname);
        CustomerInfoFullname.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView CustomerInfoIP = (TextView)findViewById(R.id.CustomerInfoIP);
        CustomerInfoIP.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView CustomerInfoDescription = (TextView)findViewById(R.id.CustomerInfoDescription);
        CustomerInfoDescription.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView CustomerInfoOrderCount = (TextView)findViewById(R.id.CustomerInfoOrderCount);
        CustomerInfoOrderCount.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView CustomerInfoRegisterDate = (TextView)findViewById(R.id.CustomerInfoRegisterDate);
        CustomerInfoRegisterDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        //Tabs
        TabLine1 = (RelativeLayout)findViewById(R.id.TabLine1);
        TabLine2 = (RelativeLayout)findViewById(R.id.TabLine2);

        TabText1 = (TextView)findViewById(R.id.TabText1);
        TabText1.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TabText2 = (TextView)findViewById(R.id.TabText2);
        TabText2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

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
                CustomerInfo.setVisibility(LinearLayout.VISIBLE);
            }
        });


        CustomerInfo = (LinearLayout)findViewById(R.id.CustomerInfo);

        listView = (ListView) findViewById(R.id.CustomerOrdersLV);
        listView.setSmoothScrollbarEnabled(true);
        listView.setDrawingCacheEnabled(true);
        listView.setWillNotCacheDrawing(true);
        listView.setHeaderDividersEnabled(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText(extras.getString("object"));

            try
            {
                JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(extras.getString("object"))).nextValue();

                CustomerID = jsonObject.getString("customer_id");

                MenuTitle.setText(jsonObject.getString("firstname") +" "+ jsonObject.getString("lastname"));

                TextView CustomerOrdersValue = (TextView)findViewById(R.id.CustomerOrdersValue);
                CustomerOrdersValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
                CustomerOrdersValue.setText(jsonObject.getString("order_nice_total"));

                TextView CustomerEmail = (TextView)findViewById(R.id.CustomerEmail);
                CustomerEmail.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerEmail.setText(jsonObject.getString("email"));

                TextView CustomerPhone = (TextView)findViewById(R.id.CustomerPhone);
                CustomerPhone.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerPhone.setText(jsonObject.getString("telephone"));

                //Info
                TextView CustomerInfoIDValue = (TextView)findViewById(R.id.CustomerInfoIDValue);
                CustomerInfoIDValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoIDValue.setText(jsonObject.getString("customer_id"));

                TextView CustomerInfoFullnameValue = (TextView)findViewById(R.id.CustomerInfoFullnameValue);
                CustomerInfoFullnameValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoFullnameValue.setText(jsonObject.getString("firstname") +" "+ jsonObject.getString("lastname"));

                TextView CustomerInfoIPValue = (TextView)findViewById(R.id.CustomerInfoIPValue);
                CustomerInfoIPValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoIPValue.setText(jsonObject.getString("ip"));

                TextView CustomerInfoDescriptionValue = (TextView)findViewById(R.id.CustomerInfoDescriptionValue);
                CustomerInfoDescriptionValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoDescriptionValue.setText(jsonObject.getString("description"));

                TextView CustomerInfoOrderCountValue = (TextView)findViewById(R.id.CustomerInfoOrderCountValue);
                CustomerInfoOrderCountValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoOrderCountValue.setText(jsonObject.getString("order_number"));

                TextView CustomerInfoRegisterDateValue = (TextView)findViewById(R.id.CustomerInfoRegisterDateValue);
                CustomerInfoRegisterDateValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                CustomerInfoRegisterDateValue.setText(jsonObject.getString("date_added"));

                new getCustomerOrders().execute();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class getCustomerOrders extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(MasterActivity.username,MasterActivity.password,MasterActivity.url,"customers/"+CustomerID+"/orders");

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
        }
    }

}
