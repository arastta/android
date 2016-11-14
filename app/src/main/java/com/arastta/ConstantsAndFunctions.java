package com.arastta;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConstantsAndFunctions extends Service 
{
	static String[] filenames;

	@Override
	public void onCreate()
	{
		Log.i("ConstantsAnsFunctions","onCreate");

		CreateFilenames();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.i("ConstantsAnsFunctions","onBind");

		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startid) 
	{
		Log.i("ConstantsAnsFunctions","onStart");
	}
	
	@Override
	public void onDestroy() 
	{
		Log.i("ConstantsAnsFunctions","onDestroy");
	}

	public static void CreateFilenames()
	{
		filenames = new String[2];
		filenames[0] = "/activeStore.file";
		filenames[1] = "/allStores.file";
	}

	public static Boolean checkToFile(String savePath, int file)
	{
		CreateFilenames();

		File logFile = new File(savePath, filenames[file]);

		Log.i("checkToFile",savePath + filenames[file] + " = " + String.valueOf(logFile.exists()));

		return logFile.exists();
	}

	public static void deleteToFile(String savePath, int file)
	{
		CreateFilenames();

		File logFile = new File(savePath, filenames[file]);

		if(logFile.exists())logFile.delete();

		Log.i("deleteToFile",savePath + filenames[file]);
	}

	public static String readToFile(String savePath, int file)
	{
		String content = "";
		try 
	    {
			CreateFilenames();
			File xFile = new File(savePath, filenames[file]);
			
	    	StringBuilder noticeText = new StringBuilder();
        	BufferedReader br = new BufferedReader(new FileReader(xFile));
			String line;
		    while ((line = br.readLine()) != null) 
			{
		    	noticeText.append(line);
			}
			br.close();
			
			content = noticeText.toString();
		}
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		return content;
	}
	
	public static void writeToFile(String savePath, int file, String content)
	{
		try
		{
			CreateFilenames();
			File logFile = new File(savePath, filenames[file]);

			if (logFile.exists())logFile.delete();
			
			try
			{
				logFile.createNewFile();
			}
			catch (IOException e)
			{
				Log.i("IOException1","writeToFile");
				e.printStackTrace();
			}
			
			try
			{
				BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, false)); 
				buf.write(content);
				buf.newLine();
				buf.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static boolean NetworkIsAvailable(Context c)
    {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
        	Log.i("isConn","Online");
        	return true;
        }
        
        Log.i("isConn","Offline");
        return false;
    }
	
	public static String getHtml(String username, String password, String xUrl, String functions)
	{
		String response = "";
	    
	    try
	    {
            String link = xUrl+"/index.php/api/"+functions;
			Log.e("getHtml", link);

			if(!link.startsWith("http"))link = "http://" + link;//TODO XXX

			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);

		    URL url = new URL(link);
		    URLConnection urlConnection = url.openConnection();

			String encoded = Base64.encodeToString((username+":"+password).getBytes("UTF-8"), Base64.NO_WRAP);
			urlConnection.setRequestProperty("Authorization", "Basic "+encoded);
		    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		    try 
		    {
		    	ByteArrayOutputStream bo = new ByteArrayOutputStream();
		    	int i = in.read();
		    	while(i != -1) {
		    		bo.write(i);
		    		i = in.read();
		    	}
		    	response = bo.toString();
		    }
		    catch (IOException e) 
		    {
		    	response = "error";
		    }
		    finally
		    {
		    	in.close();
		    }
	    }
	    catch (IOException e) 
	    {
			e.printStackTrace();
	    	response = "error";
		}

        if(response.contains("<!DOCTYPE html>")) response = "error";
        
    	Log.i("getHtml()_Response",response);
	    
	    return response;
	}

	public static String getOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

	public static String getAppVersion(Context context) {
        try 
        {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } 
        catch (NameNotFoundException e) {
            return "";
        }
    }

	public static String getAndroidVersion()
    {
    	String apiLevel = "";
    	String versionName = "";
    	
    	switch (android.os.Build.VERSION.SDK_INT) {
		case 10000:
			apiLevel = "10000";
	    	versionName = "Development_Build";
			break;
		case 1:
			apiLevel = "1";
	    	versionName = "Android_1.0";
			break;
		case 2:
			apiLevel = "2";
	    	versionName = "Android_1.1_Petit_Four";
			break;
		case 3:
			apiLevel = "3";
	    	versionName = "Android_1.5_Cupcake";
			break;
		case 4:
			apiLevel = "4";
	    	versionName = "Android_1.6_Donut";
			break;
		case 5:
			apiLevel = "5";
	    	versionName = "Android_2.0_Eclair";
			break;
		case 6:
			apiLevel = "6";
	    	versionName = "Android_2.0.X_Eclair";
			break;
		case 7:
			apiLevel = "7";
	    	versionName = "Android_2.1_Eclair";
			break;
		case 8:
			apiLevel = "8";
	    	versionName = "Android_2.2_Froyo";
			break;
		case 9:
			apiLevel = "9";
	    	versionName = "Android_2.3_Gingerbread";
			break;
		case 10:
			apiLevel = "10";
	    	versionName = "Android_2.3.X_Gingerbread";
			break;
		case 11:
			apiLevel = "11";
	    	versionName = "3.0_Honeycomb";
			break;
		case 12:
			apiLevel = "12";
	    	versionName = "3.1_Honeycomb";
			break;
		case 13:
			apiLevel = "13";
	    	versionName = "3.2_Honeycomb";
			break;
		case 14:
			apiLevel = "14";
	    	versionName = "Android_4.0_Ice_Cream_Sandwich";
			break;
		case 15:
			apiLevel = "15";
	    	versionName = "Android_4.0.X_Ice_Cream_Sandwich";
			break;
		case 16:
			apiLevel = "16";
	    	versionName = "Android_4.1_Jellybean";
			break;
		case 17:
			apiLevel = "17";
	    	versionName = "Android_4.2_Jellybean";
			break;
		case 18:
			apiLevel = "18";
	    	versionName = "Android_4.3_Jellybean";
			break;
		case 19:
			apiLevel = "19";
	    	versionName = "Android_4.4_KitKat";
			break;
		case 20:
			apiLevel = "20";
	    	versionName = "Android_4.4_KitKat Watch";
			break;
		case 21:
			apiLevel = "21";
	    	versionName = "Android_5.0_Lollipop";
			break;
		case 22:
			apiLevel = "22";
	    	versionName = "Android_5.1_Lollipop";
			break;
        case 23:
            apiLevel = "23";
            versionName = "Android_6.0_Marshmallow";
            break;
        case 24:
            apiLevel = "24";
            versionName = "Android_7.0_Nougat";
            break;
        default: 
			apiLevel = "X";
	    	versionName = "X";
            break;
		}
    	
    	return apiLevel + "_" + versionName;
    }

	public static String getDeviceInfo(String s)
	{
		if (s == null || s.length() == 0) 
		{
		    return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first))
		{
		    return s;
		}
		else
		{
		    return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static int getDisplayWidth(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		return Math.min(dm.widthPixels, dm.heightPixels);
	}

	public static int getDisplayHeight(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();

		return Math.max(dm.widthPixels, dm.heightPixels);
	}

	public static float convertDpToPixel_PixelToDp(Context context, boolean type, float value)
	{
		/*
		type is true = DpToPixel
		type is false = PixelToDp
		 */
		float result = 0;
		if(type) result = value * (context.getResources().getDisplayMetrics().densityDpi / 160f);//DpToPixel
		if(!type) result = value / (context.getResources().getDisplayMetrics().densityDpi / 160f);//PixelToDp
		return result;
	}

	public static Typeface getTypeFace(Context context, boolean bold)
	{
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font));
		if(bold)
			typeFace = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_bold));
		return typeFace;
	}

	//TODO For Arastta

	public static int getStatusColor(Context context, int id)
	{
		int color = 0;
		switch(id)
		{
			case 0:
				color = context.getResources().getColor(R.color.colorAccent);
				break;
			case 1:
				color = context.getResources().getColor(R.color.colorStatus01);
				break;
			case 2:
				color = context.getResources().getColor(R.color.colorStatus02);
				break;
			case 3:
				color = context.getResources().getColor(R.color.colorStatus03);
				break;
			case 5:
				color = context.getResources().getColor(R.color.colorStatus05);
				break;
			case 7:
				color = context.getResources().getColor(R.color.colorStatus07);
				break;
			case 8:
				color = context.getResources().getColor(R.color.colorStatus08);
				break;
			case 9:
				color = context.getResources().getColor(R.color.colorStatus09);
				break;
			case 10:
				color = context.getResources().getColor(R.color.colorStatus10);
				break;
			case 11:
				color = context.getResources().getColor(R.color.colorStatus11);
				break;
			case 12:
				color = context.getResources().getColor(R.color.colorStatus12);
				break;
			case 13:
				color = context.getResources().getColor(R.color.colorStatus13);
				break;
			case 14:
				color = context.getResources().getColor(R.color.colorStatus14);
				break;
			case 15:
				color = context.getResources().getColor(R.color.colorStatus15);
				break;
			case 16:
				color = context.getResources().getColor(R.color.colorStatus16);
				break;
			default:
				color = context.getResources().getColor(R.color.colorText);
				break;
		}
		return color;
	}

	public static String addZero(int number)
	{
		String r = String.valueOf(number);
		if(number < 10)r = "0"+String.valueOf(number);
		return r;
	}

	public static String getTodayDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());
		Log.e("getTodayDate",today);
		return today;
	}

	public static String getFromDate(int day)
	{
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month =  ConstantsAndFunctions.addZero(calendar.get(Calendar.MONTH)+1);
		String day_of_month =  ConstantsAndFunctions.addZero(calendar.get(Calendar.DAY_OF_MONTH));
		switch (day)
		{
			case 1:
				calendar.add(Calendar.DAY_OF_YEAR, -day);
				day_of_month = ConstantsAndFunctions.addZero(calendar.get(Calendar.DAY_OF_MONTH));
				break;
			case 30:
				day_of_month = "01";
				month = ConstantsAndFunctions.addZero(calendar.get(Calendar.MONTH)+1);
				break;
			case 365:
				day_of_month = "01";
				month = "01";
				break;
			case 7:
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
				cal.clear(Calendar.MINUTE);
				cal.clear(Calendar.SECOND);
				cal.clear(Calendar.MILLISECOND);

				cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
				System.out.println("Start of this week:       " + cal.getTime());

				day_of_month = ConstantsAndFunctions.addZero(cal.get(Calendar.DAY_OF_MONTH));
				month = ConstantsAndFunctions.addZero(cal.get(Calendar.MONTH)+1);
				break;
			case 90:
				day_of_month = "01";
				switch (calendar.get(Calendar.MONTH)+1){
					case 1:
					case 2:
					case 3:
						month = "01";
						break;
					case 4:
					case 5:
					case 6:
						month = "04";
						break;
					case 7:
					case 8:
					case 9:
						month = "07";
						break;
					case 10:
					case 11:
					case 12:
						month = "10";
						break;

				}
				break;
		}
		String from = year +"-"+ month +"-"+ day_of_month;
		Log.e("getFromDate",from);
		if(day == 1)from = getTodayDate();
		return from;
	}

    public static String getFromDate2(int day)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -day);
        String from = String.valueOf(calendar.get(Calendar.YEAR)) +"-"+
                ConstantsAndFunctions.addZero(calendar.get(Calendar.MONTH)+1) +"-"+
                ConstantsAndFunctions.addZero(calendar.get(Calendar.DAY_OF_MONTH));
        Log.e("getFromDate",from);
        return from;
    }

}