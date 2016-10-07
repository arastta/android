package com.arastta;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class AppSettingsActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 4;

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_app_settings);

        context = AppSettingsActivity.this;
        ScreenName = "AppSettingsActivity";

        //unUsed
        TextView TextViewNotification = (TextView)findViewById(R.id.TextViewNotification);
        TextViewNotification.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        MenuTitle.setText(getResources().getString(R.string.app_settings));
    }
}
