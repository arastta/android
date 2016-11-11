package com.arastta;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
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

        MenuTitle.setText(getResources().getString(R.string.app_settings));

        //unUsed
        TextView TextViewNotification = (TextView)findViewById(R.id.TextViewNotification);
        TextViewNotification.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        final CheckBox CheckBoxNotification = (CheckBox)findViewById(R.id.CheckBoxNotification);

        RelativeLayout CheckBoxNotificationArea = (RelativeLayout)findViewById(R.id.CheckBoxNotificationArea);
        CheckBoxNotificationArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBoxNotification.setChecked(!CheckBoxNotification.isChecked());
            }
        });

    }
}
