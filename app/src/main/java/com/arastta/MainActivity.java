package com.arastta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity
{
    Context context;
    String ScreenName = "";

    String savePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = MainActivity.this;
        ScreenName = "MainActivity";

        savePath = getFilesDir().toString();

        //ConstantsAndFunctions.deleteToFile(savePath,0);

        if(ConstantsAndFunctions.checkToFile(savePath,0))
        {
            startActivity(new Intent(context, DashboardActivity.class));
            finish();
        }
        else
        {
            startActivity(new Intent(context, StoreActivity.class));
            finish();
        }
    }
}
