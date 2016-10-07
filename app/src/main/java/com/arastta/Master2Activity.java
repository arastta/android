package com.arastta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Master2Activity extends Activity
{
    Context context;
    String ScreenName = "";

    static int activePage = 0;

    static TextView MenuTitle;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_master2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = Master2Activity.this;
        ScreenName = "Master2Activity";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //statusBarColour
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarColor));
            //navigationBarColour
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorNavigationBarColor));
        }

        MenuTitle = (TextView)findViewById(R.id.MenuTitle);
        MenuTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        ImageButton BackButton = (ImageButton)findViewById(R.id.BackButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton DetailButton = (ImageButton)findViewById(R.id.DetailButton);
        DetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (activePage)
                {
                    case 0:
                        Log.e("DetailButton","BrowserActivity");
                        break;
                    case 1:
                        Log.e("DetailButton","OrderDetailsActivity");
                        break;
                    case 2:
                        Log.e("DetailButton","CustomerDetailsActivity");
                        break;
                    case 3:
                        Log.e("DetailButton","ProductDetailsActivity");
                        break;
                    case 4:
                        Log.e("DetailButton","AppSettingsActivity");
                        break;
                    case 5:
                        Log.e("DetailButton","ManageStoresActivity");
                        break;
                    default:
                        Log.e("DetailButton","Default");
                        break;
                }
            }
        });

        TextView GetArasttaCloud = (TextView)findViewById(R.id.GetArasttaCloud);
        GetArasttaCloud.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        GetArasttaCloud.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(context, BrowserActivity.class));
            }
        });

        if(activePage == 0)
            GetArasttaCloud.setVisibility(TextView.GONE);
        else
            GetArasttaCloud.setVisibility(TextView.VISIBLE);
    }

    @Override
    public void setContentView(int id)
    {
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ContentArea = (LinearLayout) findViewById(R.id.ContentArea);
        inflater.inflate(id, ContentArea);
    }
}
