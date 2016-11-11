package com.arastta;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
//
    boolean Working = false;

    static String SETTINGS_CLICKED = "SettingButton";
    static String REFRESH_CLICKED = "RefreshButton";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        Log.e("AppWidget","updateAppWidget:" + String.valueOf(appWidgetId));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        remoteViews.setTextViewText(R.id.WidgetStoreName, AppWidgetSettings.loadTitlePref(context, "title", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetDate, getRefreshDate(context));

        remoteViews.setTextViewText(R.id.WidgetOrderCount, AppWidgetSettings.loadTitlePref(context, "order_count", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetUserCount, AppWidgetSettings.loadTitlePref(context, "user_count", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetTotal, AppWidgetSettings.loadTitlePref(context, "total", appWidgetId));
        remoteViews.setTextViewText(R.id.WidgetFilterName, AppWidgetSettings.loadTitlePref(context, "periodText", appWidgetId));

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.e("AppWidget","onUpdate");
        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds)
        {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            remoteViews.setTextViewText(R.id.WidgetStoreName, AppWidgetSettings.loadTitlePref(context, "title", widgetId));
            remoteViews.setTextViewText(R.id.WidgetDate, getRefreshDate(context));

            remoteViews.setTextViewText(R.id.WidgetOrderCount, AppWidgetSettings.loadTitlePref(context, "order_count", widgetId));
            remoteViews.setTextViewText(R.id.WidgetUserCount, AppWidgetSettings.loadTitlePref(context, "user_count", widgetId));
            remoteViews.setTextViewText(R.id.WidgetTotal, AppWidgetSettings.loadTitlePref(context, "total", widgetId));
            remoteViews.setTextViewText(R.id.WidgetFilterName, AppWidgetSettings.loadTitlePref(context, "periodText", widgetId));

            remoteViews.setOnClickPendingIntent(R.id.WidgetSettings, getPendingSelfIntent(context, SETTINGS_CLICKED, widgetId, appWidgetIds));
            remoteViews.setOnClickPendingIntent(R.id.WidgetRefresh, getPendingSelfIntent(context, REFRESH_CLICKED, widgetId, appWidgetIds));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("onReceive","onReceive");

        int thisWidgetId = intent.getIntExtra("ID", -1);

        ComponentName thisWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager appWidgetManager= AppWidgetManager.getInstance(context);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds)
        {
            if (widgetId==thisWidgetId)
            {
                Log.e("onReceive",Integer.toString(widgetId));
                //RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
                //remoteViews.setTextViewText(R.id.WidgetStoreName, AppWidgetSettings.loadTitlePref(context, thisWidgetId));
                //appWidgetManager.updateAppWidget(thisWidgetId, remoteViews);

                //if(intent.getExtras().getString("click").equals(SETTINGS_CLICKED))
                if(intent.getAction().equals(SETTINGS_CLICKED))
                {
                    Log.e("onReceiveXXX",SETTINGS_CLICKED);

                    Intent SettingIntent = new Intent(context, AppWidgetSettings.class);
                    SettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SettingIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, thisWidgetId);
                    context.startActivity(SettingIntent);
                }
                if(intent.getAction().equals(REFRESH_CLICKED))
                {
                    Log.e("onReceiveXXX",REFRESH_CLICKED);

                    ctx = context;
                    wid = thisWidgetId;

                    if(!Working)new getDashboard().execute();
                }
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        Log.e("AppWidget","onDeleted");
        for (int appWidgetId : appWidgetIds)
        {
            AppWidgetSettings.deleteTitlePref(context, "title", appWidgetId);
            AppWidgetSettings.deleteTitlePref(context, "order_count", appWidgetId);
            AppWidgetSettings.deleteTitlePref(context, "user_count", appWidgetId);
            AppWidgetSettings.deleteTitlePref(context, "total", appWidgetId);
        }
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
        if(DateFormat.is24HourFormat(context))
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        String today = sdf.format(new Date());
        Log.e("getRefreshDate",today);
        return today;
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId, int[] appWidgetIds) {
        Intent intent = new Intent(context, getClass());
        intent.setData(Uri.withAppendedPath(Uri.parse("abc" + "://widget/id/"), String.valueOf(appWidgetId)));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        intent.putExtra("ID",appWidgetId);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
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

