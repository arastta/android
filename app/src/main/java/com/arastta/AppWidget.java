package com.arastta;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppWidget extends AppWidgetProvider
{
    Context ctx;
    int wid = 0;
    boolean Working = false;

    static String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    static String SETTING = "Setting";
    static String REFRESH = "Refresh";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        Log.e("AppWidget","updateAppWidget:" + String.valueOf(appWidgetId));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        remoteViews.setTextViewText(R.id.WidgetDate, getRefreshDate(context));
        remoteViews.setTextViewText(R.id.WidgetStoreName, AppWidgetSettings.loadTitlePref(context, "title", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetFilterName, AppWidgetSettings.loadTitlePref(context, "periodText", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetOrderCount, AppWidgetSettings.loadTitlePref(context, "order_count", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetUserCount, AppWidgetSettings.loadTitlePref(context, "user_count", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetTotal, AppWidgetSettings.loadTitlePref(context, "total", appWidgetId));

        Intent setting = new Intent(context, AppWidgetSettings.class);
        setting.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setting.setAction(String.valueOf(appWidgetId));
        PendingIntent setting_ = PendingIntent.getActivity(context, 0, setting, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.WidgetSettings, setting_);

        Intent refresh = new Intent(context, AppWidget.class);
        refresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        refresh.setAction(REFRESH+String.valueOf(appWidgetId));
        PendingIntent refresh_ = PendingIntent.getBroadcast(context, 0, refresh, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.WidgetRefresh, refresh_);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.e("AppWidget","onUpdate");

        for (int i=0; i<appWidgetIds.length; i++)
        {
            int appWidgetId = appWidgetIds[i];
            Log.e("onUpdate","appWidgetId:"+String.valueOf(appWidgetId));
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("onReceive",intent.getAction());
        Log.e("onReceive","onReceive");

        if(intent.getAction().equals(APPWIDGET_UPDATE))
        {
            Log.e("onReceive",APPWIDGET_UPDATE);
            if(intent.getExtras() != null) {

                ComponentName componentName = new ComponentName(context, AppWidget.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

                //AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                //ComponentName thisAppWidget = new ComponentName(context.getPackageName(), AppWidget.class.getName());
                //int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }

        if(intent.getAction().startsWith(REFRESH))
        {
            Log.e("onReceive",REFRESH);
            if (intent.getExtras() != null)
            {
                ctx = context;
                //wid = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                Log.e("mAppWidgetIdXX",intent.getAction());
                wid = Integer.parseInt(intent.getAction().substring(REFRESH.length()));
                Log.e("mAppWidgetIdX",String.valueOf(wid));
                Log.e("mAppWidgetId2",String.valueOf(intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)));
                if(!Working)new getDashboard().execute();
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        Log.e("AppWidget","onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context)
    {
        Log.e("AppWidget","onEnabled");
    }

    @Override
    public void onDisabled(Context context)
    {
        Log.e("AppWidget","onDisabled");
    }

    public static String getRefreshDate(Context context)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm aa");
        if(DateFormat.is24HourFormat(context))sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String today = sdf.format(new Date());
        Log.e("getRefreshDate",today);
        return today;
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
            ResultText = ConstantsAndFunctions.getHtml(AppWidgetSettings.loadTitlePref(ctx, "username", wid),AppWidgetSettings.loadTitlePref(ctx, "password", wid),AppWidgetSettings.loadTitlePref(ctx, "url", wid),"stats"+
                    "?status=" +  AppWidgetSettings.loadTitlePref(ctx, "statusIDs", wid) +
                    "&date_from=" + ConstantsAndFunctions.getFromDate(Integer.parseInt(AppWidgetSettings.loadTitlePref(ctx, "period", wid))) +
                    "&date_to=" + ConstantsAndFunctions.getTodayDate());

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Working = false;

            if(ResultText.equals("error"))
            {
                Toast.makeText(ctx, ctx.getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(ResultText)).nextValue();
                    JSONObject orders = jsonObject.getJSONObject("orders");
                    JSONObject customers = jsonObject.getJSONObject("customers");

                    AppWidgetSettings.saveTitlePref(ctx, "order_count", wid, orders.getString("number"));
                    AppWidgetSettings.saveTitlePref(ctx, "user_count", wid, customers.getString("number"));
                    AppWidgetSettings.saveTitlePref(ctx, "total", wid, orders.getString("nice_price"));

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
                    AppWidget.updateAppWidget(ctx, appWidgetManager, wid);
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

