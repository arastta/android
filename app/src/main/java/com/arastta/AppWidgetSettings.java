package com.arastta;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class AppWidgetSettings extends Activity
{
    private static final String PREFS_NAME = "com.arastta.AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    Context context;
    boolean Working = false;
    String savePath;

    String url = "";
    String username = "";
    String password = "";
    String status = "";
    int period = 1;
    getDashboard db;

    JSONObject orders;
    JSONObject customers;
    TextView WidgetOrderCount;
    TextView WidgetUserCount;
    TextView WidgetTotal;

    boolean StoreFirst = true;
    Spinner WidgetSettingsSpinnerStore;
    List<String> stores = new ArrayList<String>();
    ArrayList<JSONObject> storeList = new ArrayList<JSONObject>();

    boolean selectAllFirst = true;
    static boolean selectAll = false;
    static boolean[] trues = new boolean[0];
    boolean StatusesLayoutOpen = false;
    RelativeLayout StatusesLayout;
    RelativeLayout StatusesLayoutTop;
    TextView StatusesLayoutTitle;
    CheckBox StatusesLayoutCheckBox;
    TextView StatusesLayoutDone;
    static TextView WidgetSettingsTextStatus;
    ListView StatusesLV;
    StatusesAdapter statusesAdapter;
    ArrayList<JSONObject> Statuses = new ArrayList<JSONObject>();
    String listIDs = "";
    String statusIDs = "";

    boolean PeriodFirst = true;
    Spinner WidgetSettingsSpinnerPeriod;
    List<String> periods = new ArrayList<String>();

    View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            final Context ctx = AppWidgetSettings.this;

            saveTitlePref(context, "title", mAppWidgetId, WidgetSettingsSpinnerStore.getSelectedItem().toString());
            saveTitlePref(context, "periodText", mAppWidgetId, WidgetSettingsSpinnerPeriod.getSelectedItem().toString());
            saveTitlePref(context, "period", mAppWidgetId, String.valueOf(getPeriodCode(WidgetSettingsSpinnerPeriod.getSelectedItemPosition())));
            saveTitlePref(context, "listIDs", mAppWidgetId, listIDs);
            saveTitlePref(context, "statusIDs", mAppWidgetId, statusIDs);

            try
            {
                saveTitlePref(context, "order_count", mAppWidgetId, orders.getString("number"));
                saveTitlePref(context, "user_count", mAppWidgetId, customers.getString("number"));
                saveTitlePref(context, "total", mAppWidgetId, orders.getString("nice_price"));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
            AppWidget.updateAppWidget(ctx, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    @Override
    public void onBackPressed()
    {
        if(StatusesLayoutOpen)
        {
            StatusesLayoutOpen = false;
            StatusesLayout.setVisibility(RelativeLayout.GONE);
        }
        else
        {
            saveTitlePref(context, "title", mAppWidgetId, WidgetSettingsSpinnerStore.getSelectedItem().toString());
            saveTitlePref(context, "periodText", mAppWidgetId, WidgetSettingsSpinnerPeriod.getSelectedItem().toString());
            saveTitlePref(context, "period", mAppWidgetId, String.valueOf(getPeriodCode(WidgetSettingsSpinnerPeriod.getSelectedItemPosition())));
            saveTitlePref(context, "listIDs", mAppWidgetId, listIDs);
            saveTitlePref(context, "statusIDs", mAppWidgetId, statusIDs);

            try {
                saveTitlePref(context, "order_count", mAppWidgetId, orders.getString("number"));
                saveTitlePref(context, "user_count", mAppWidgetId, customers.getString("number"));
                saveTitlePref(context, "total", mAppWidgetId, orders.getString("nice_price"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            super.onBackPressed();
        }
    }

    public AppWidgetSettings()
    {
        super();
    }

    static void saveTitlePref(Context context, String value, int appWidgetId, String text)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + value + "_" + appWidgetId, text);
        prefs.apply();
    }

    static String loadTitlePref(Context context, String value, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + value + "_" + appWidgetId, null);
        if (titleValue != null)
        {
            return titleValue;
        }
        else
        {
            return context.getString(R.string.app_name);
        }
    }

    static void deleteTitlePref(Context context, String value, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + value + "_" + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.app_widget_settings);

        context = AppWidgetSettings.this;

        savePath = getFilesDir().toString();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Log.e("mAppWidgetId",String.valueOf(mAppWidgetId));
            Log.e("mAppWidgetId2",String.valueOf(extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)));
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish();
            return;
        }

        findViewById(R.id.WidgetSettingsButton).setOnClickListener(mOnClickListener);

        WidgetOrderCount = (TextView)findViewById(R.id.WidgetOrderCount);
        WidgetUserCount = (TextView)findViewById(R.id.WidgetUserCount);
        WidgetTotal = (TextView)findViewById(R.id.WidgetTotal);


        status = loadTitlePref(context, "statusIDs", mAppWidgetId);
        if(status.equals("Arastta"))status = "";

        String p = loadTitlePref(context, "period", mAppWidgetId);
        if(p.equals("Arastta"))period = 1;
        else period = Integer.parseInt(p);


        StatusesLayoutOpen = false;
        StatusesLayout = (RelativeLayout)findViewById(R.id.StatusesLayout) ;
        StatusesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusesLayoutOpen = false;
                StatusesLayout.setVisibility(RelativeLayout.GONE);
            }
        });

        StatusesLV = (ListView)findViewById(R.id.StatusesLV);

        WidgetSettingsTextStatus = (TextView) findViewById(R.id.WidgetSettingsTextStatus);
        WidgetSettingsTextStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        WidgetSettingsTextStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Statuses.size() > 0){
                    StatusesLayoutOpen = true;
                    StatusesLayout.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });

        StatusesLayoutDone = (TextView)findViewById(R.id.StatusesLayoutDone) ;
        StatusesLayoutDone.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        StatusesLayoutDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusesLayoutOpen = false;
                StatusesLayout.setVisibility(RelativeLayout.GONE);

                //for (int i = 0; i < Statuses.size(); i++) {
                   // AppWidgetSettings.trues[i] = isChecked;

                int trueCount = 0;
                listIDs = "";
                statusIDs = "";
                for(int i=0;i<trues.length;i++)
                {
                    try
                    {
                        if(trues[i])
                        {
                            trueCount += 1;
                            listIDs = listIDs + "," + String.valueOf(i);
                            statusIDs = statusIDs + "," + Statuses.get(i).getString("order_status_id");
                        }
                        Log.e("trues", Statuses.get(i).getString("name") + ":" + Statuses.get(i).getString("order_status_id") + " = " + String.valueOf(trues[i]));
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                if(trueCount > 0)
                {
                    listIDs = listIDs.substring(1,listIDs.length());
                    statusIDs = statusIDs.substring(1,statusIDs.length());
                }
                else
                {
                    for(int i=0;i<trues.length;i++)
                    {
                        trues[i] = true;
                        listIDs = listIDs + "," + String.valueOf(i);
                        try
                        {
                            statusIDs = statusIDs + "," + Statuses.get(i).getString("order_status_id");
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    listIDs = listIDs.substring(1,listIDs.length());
                    statusIDs = statusIDs.substring(1,statusIDs.length());
                }
                Log.e("listIDs", listIDs);
                Log.e("statusIDs", statusIDs);

                saveTitlePref(context, "listIDs", mAppWidgetId, listIDs);
                saveTitlePref(context, "statusIDs", mAppWidgetId, statusIDs);

                status = statusIDs;

                if(Working)db.cancel(true);
                db = new getDashboard();
                db.execute();
            }
        });

        StatusesLayoutTitle = (TextView)findViewById(R.id.StatusesLayoutTitle) ;
        StatusesLayoutTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        StatusesLayoutTop = (RelativeLayout) findViewById(R.id.StatusesLayoutTop);
        StatusesLayoutTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatusesLayoutCheckBox.setChecked(!StatusesLayoutCheckBox.isChecked());
            }
        });

        selectAllFirst = true;
        StatusesLayoutCheckBox = (CheckBox)findViewById(R.id.StatusesLayoutCheckBox) ;
        StatusesLayoutCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!selectAllFirst) {
                    for (int i = 0; i < Statuses.size(); i++) {
                        AppWidgetSettings.trues[i] = isChecked;
                        Log.e("zzz", String.valueOf(isChecked));
                    }
                    selectAll = true;
                    statusesAdapter.notifyDataSetChanged();
                    StatusesLV.invalidateViews();
                    StatusesLV.refreshDrawableState();
                    selectAll = false;
                }
                selectAllFirst = false;
                setTextBox();
            }
        });



        WidgetSettingsSpinnerPeriod = (Spinner)findViewById(R.id.WidgetSettingsSpinnerPeriod);
        setPeriodSpinner();
        PeriodFirst = true;
        WidgetSettingsSpinnerPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!PeriodFirst)
                {
                    Log.e("onItemSelected",WidgetSettingsSpinnerPeriod.getSelectedItem().toString());
                    saveTitlePref(context, "periodText", mAppWidgetId, WidgetSettingsSpinnerPeriod.getSelectedItem().toString());

                    period = getPeriodCode(position);

                    saveTitlePref(context, "period", mAppWidgetId, String.valueOf(period));

                    if(Working)db.cancel(true);
                    db = new getDashboard();
                    db.execute();
                }
                PeriodFirst = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        WidgetSettingsSpinnerStore = (Spinner)findViewById(R.id.WidgetSettingsSpinnerStore);
        loadStores();
        StoreFirst = true;
        WidgetSettingsSpinnerStore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!StoreFirst)
                {
                    Log.e("onItemSelected",WidgetSettingsSpinnerStore.getSelectedItem().toString());
                    saveTitlePref(context, "title", mAppWidgetId, WidgetSettingsSpinnerStore.getSelectedItem().toString());

                    Log.i("selectedStore",storeList.get(WidgetSettingsSpinnerStore.getSelectedItemPosition()).toString());
                    //{"username":"mobile","password":"app","store_name":"test","store_url":"mobile.arastta.com","config_name":"Mobile","config_image":"image\/placeholder.png"}

                    try
                    {
                        url = storeList.get(WidgetSettingsSpinnerStore.getSelectedItemPosition()).getString("store_url");
                        username = storeList.get(WidgetSettingsSpinnerStore.getSelectedItemPosition()).getString("username");
                        password = storeList.get(WidgetSettingsSpinnerStore.getSelectedItemPosition()).getString("password");
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    saveTitlePref(context, "url", mAppWidgetId, url);
                    saveTitlePref(context, "username", mAppWidgetId, username);
                    saveTitlePref(context, "password", mAppWidgetId, password);

                    if(Working)db.cancel(true);
                    db = new getDashboard();
                    db.execute();
                }
                StoreFirst = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }

    public int getPeriodCode(int position)
    {
        int period = 1;
        switch (position){
            case 0:
                period = 1;
                break;
            case 1:
                period = 7;
                break;
            case 2:
                period = 30;
                break;
            case 3:
                period = 90;
                break;
            case 4:
                period = 365;
                break;
        }
        return period;
    }

    public void loadStores()
    {
        try
        {
            JSONArray jsonArray = new JSONArray(ConstantsAndFunctions.readToFile(savePath,1));
            Log.e("loadStores","jsonArray.length:"+jsonArray.length());

            stores.clear();

            int store_id = 0;
            for (int i = 0; i<jsonArray.length(); i++)
            {
                storeList.add(jsonArray.getJSONObject(i));
                stores.add(jsonArray.getJSONObject(i).getString("store_name"));

                if(jsonArray.getJSONObject(i).getString("store_name").equals(loadTitlePref(AppWidgetSettings.this, "title", mAppWidgetId))){
                    store_id = i;

                    url = jsonArray.getJSONObject(i).getString("store_url");
                    username = jsonArray.getJSONObject(i).getString("username");
                    password = jsonArray.getJSONObject(i).getString("password");

                    saveTitlePref(context, "url", mAppWidgetId, url);
                    saveTitlePref(context, "username", mAppWidgetId, username);
                    saveTitlePref(context, "password", mAppWidgetId, password);
                }
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, stores)
            {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                    ((TextView) v).setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8),0,0,0);
                    //((TextView) v).setGravity(Gravity.CENTER);
                    return v;
                }
                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                    View v =super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                    ((TextView) v).setGravity(Gravity.CENTER);
                    ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBg));
                    ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));
                    return v;
                }
            };
            dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            WidgetSettingsSpinnerStore.setAdapter(dataAdapter);

            WidgetSettingsSpinnerStore.setSelection(store_id);


            if(store_id == 0)
            {
                url = jsonArray.getJSONObject(0).getString("store_url");
                username = jsonArray.getJSONObject(0).getString("username");
                password = jsonArray.getJSONObject(0).getString("password");

                saveTitlePref(context, "url", mAppWidgetId, url);
                saveTitlePref(context, "username", mAppWidgetId, username);
                saveTitlePref(context, "password", mAppWidgetId, password);
            }

            db = new getDashboard();
            db.execute();

            new getOrderStatuses().execute();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"orders/statuses");

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
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
                        Statuses = new ArrayList<JSONObject>();
                        for (int i = 0; i<jsonArray.length(); i++)
                        {
                            Statuses.add(jsonArray.getJSONObject(i));
                        }

                        try
                        {
                            String[] trs = loadTitlePref(context, "listIDs", mAppWidgetId).split(",");
                            trues = new boolean[Statuses.size()];
                            for (int i = 0; i < Statuses.size(); i++) {
                                trues[i] = false;
                                if(trs.length == 0){
                                    StatusesLayoutCheckBox.setChecked(true);
                                    trues[i] = true;
                                }
                                else{
                                    StatusesLayoutCheckBox.setChecked(false);
                                }
                            }
                            for (int j = 0; j < trs.length; j++) {
                                trues[Integer.parseInt(trs[j])] = true;
                            }
                        }
                        catch (NumberFormatException e){}

                        statusesAdapter = new StatusesAdapter(context,Statuses);
                        StatusesLV.setAdapter(statusesAdapter);
                    }

                    setTextBox();
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

    private class getDashboard extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute()
        {
            Working = true;
        }

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(username,password,url,"stats"+
                    "?status=" + status +
                    "&date_from=" + ConstantsAndFunctions.getFromDate(period) +
                    "&date_to=" + ConstantsAndFunctions.getTodayDate());

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Working = false;

            if(ResultText.equals("error"))
            {
                Toast.makeText(context, getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(ResultText)).nextValue();
                    orders = jsonObject.getJSONObject("orders");
                    customers = jsonObject.getJSONObject("customers");

                    WidgetOrderCount.setText(orders.getString("number"));
                    WidgetUserCount.setText(customers.getString("number"));
                    WidgetTotal.setText(orders.getString("nice_price"));

                    saveTitlePref(context, "order_count", mAppWidgetId, orders.getString("number"));
                    saveTitlePref(context, "user_count", mAppWidgetId, customers.getString("number"));
                    saveTitlePref(context, "total", mAppWidgetId, orders.getString("nice_price"));
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

    public void setPeriodSpinner()
    {
        periods.clear();

        String[] rl=this.getResources().getStringArray(R.array.Periods);
        for(int i=0;i<rl.length;i++)
        {
            periods.add(rl[i]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, periods)
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                ((TextView) v).setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8),0,0,0);
                //((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBg));
                ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));
                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        WidgetSettingsSpinnerPeriod.setAdapter(dataAdapter);

        Log.e("periodperiod",String.valueOf(period));
        switch (period){
            case 1:
                WidgetSettingsSpinnerPeriod.setSelection(0);
                break;
            case 7:
                WidgetSettingsSpinnerPeriod.setSelection(1);
                break;
            case 30:
                WidgetSettingsSpinnerPeriod.setSelection(2);
                break;
            case 90:
                WidgetSettingsSpinnerPeriod.setSelection(3);
                break;
            case 365:
                WidgetSettingsSpinnerPeriod.setSelection(4);
                break;
        }
    }

    void setTextBox()
    {
        String texts = "";
        for(int tk=0;tk<trues.length;tk++)
        {
            if(trues[tk])
            {
                try
                {
                    texts = texts + "," + Statuses.get(tk).getString("name");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
        if(texts.length() > 0)texts = texts.substring(1,texts.length());

        WidgetSettingsTextStatus.setText(texts);
    }

}

