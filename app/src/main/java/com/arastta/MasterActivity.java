package com.arastta;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.navdrawer.SimpleSideDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class MasterActivity extends Activity
{
    Context context;
    String ScreenName = "";

    static String savePath = "";

    Toast ExitToast;
    ExitCount ExitCounter;
    boolean ExitEnable = false;
    int ExitTime = 2;

    ListView MenuStoreLv;

    static int activePage = 0;

    static String url = "";
    static String username = "";
    static String password = "";

    SimpleSideDrawer SideDrawer;
    RelativeLayout MenuLayout;
    ImageView Avatar;
    TextView MenuStoreTitle;
    TextView MenuStoreURL;
    boolean MenuStoreLvIsOpen = false;

    TextView MenuTitle;

    ProgressDialog loading = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.activity_master);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = MasterActivity.this;
        ScreenName = "MasterActivity";

        savePath = getFilesDir().toString();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //statusBarColour
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarColor));
            //navigationBarColour
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorNavigationBarColor));
        }

        ExitToast = Toast.makeText(context, getResources().getString(R.string.exit_toast), Toast.LENGTH_SHORT);

        loading = new ProgressDialog(context);
        loading.setCancelable(true);
        loading.setIndeterminate(true);
        loading.setMessage(getResources().getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //TopBar
        MenuTitle = (TextView)findViewById(R.id.MenuTitle);
        MenuTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        ImageButton MenuButton = (ImageButton)findViewById(R.id.MenuButton);
        MenuButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideDrawer.toggleLeftDrawer();
            }
        });

        //MENU
        SideDrawer = new SimpleSideDrawer(this);
        SideDrawer.setLeftBehindContentView(R.layout.menu_side);//SideDrawer.toggleLeftDrawer();
        //SideDrawer.setRightBehindContentView(R.layout.menu_side);//SideDrawer.toggleRightDrawer();
        MenuLayout = (RelativeLayout)findViewById(R.id.MenuLayout);
        try
        {
            int space = (int) ConstantsAndFunctions.convertDpToPixel_PixelToDp(context, true, 25);
            MenuLayout.getLayoutParams().height = ConstantsAndFunctions.getDisplayHeight(context) - space;
            MenuLayout.getLayoutParams().width = (int) ConstantsAndFunctions.convertDpToPixel_PixelToDp(context, true, 240);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        MenuStoreLv = (ListView)findViewById(R.id.MenuStoreLv);
        MenuStoreLv.setSmoothScrollbarEnabled(true);
        MenuStoreLv.setDrawingCacheEnabled(true);
        MenuStoreLv.setWillNotCacheDrawing(true);
        MenuStoreLv.setHeaderDividersEnabled(false);
        /*
        View footerView = View.inflate(context, R.layout.item_stores, null);
        MenuStoreLv.addFooterView(footerView);

        TextView StoreTitle = (TextView)footerView.findViewById(R.id.StoreTitle);
        StoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        ImageButton StoreButton = (ImageButton)footerView.findViewById(R.id.StoreButton);
        StoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("StoreButton","OnClickListener");
            }
        });
        */

        final TextView MenuButtonAppSettings = (TextView)findViewById(R.id.MenuButtonAppSettings);
        MenuButtonAppSettings.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        MenuButtonAppSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideDrawer.toggleLeftDrawer();

                startActivity(new Intent(context, AppSettingsActivity.class));
            }
        });

        final TextView MenuButtonManageStores = (TextView)findViewById(R.id.MenuButtonManageStores);
        MenuButtonManageStores.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        MenuButtonManageStores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SideDrawer.toggleLeftDrawer();

                startActivity(new Intent(context, ManageStoresActivity.class));
            }
        });

        RelativeLayout MenuStoreButton = (RelativeLayout)findViewById(R.id.MenuStoreButton);
        MenuStoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(MenuStoreLvIsOpen)
                {
                    MenuStoreLvIsOpen = false;

                    MenuStoreLv.setVisibility(ListView.GONE);
                    MenuButtonManageStores.setVisibility(TextView.GONE);
                    MenuButtonAppSettings.setVisibility(TextView.VISIBLE);
                }
                else
                {
                    MenuStoreLvIsOpen = true;

                    MenuButtonAppSettings.setVisibility(TextView.GONE);
                    MenuButtonManageStores.setVisibility(TextView.VISIBLE);
                    MenuStoreLv.setVisibility(ListView.VISIBLE);
                }
            }
        });

        TextView MenuButtonDashboard = (TextView)findViewById(R.id.MenuButtonDashboard);
        MenuButtonDashboard.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        MenuButtonDashboard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activePage != 0)
                {
                    SideDrawer.toggleLeftDrawer();

                    Intent i = new Intent(context, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        TextView MenuButtonOrders = (TextView)findViewById(R.id.MenuButtonOrders);
        MenuButtonOrders.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        MenuButtonOrders.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activePage != 1)
                {
                    SideDrawer.toggleLeftDrawer();

                    Intent i = new Intent(context, OrdersActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        TextView MenuButtonCustomers = (TextView)findViewById(R.id.MenuButtonCustomers);
        MenuButtonCustomers.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        MenuButtonCustomers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activePage != 2)
                {
                    SideDrawer.toggleLeftDrawer();

                    Intent i = new Intent(context, CustomersActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        TextView MenuButtonProducts = (TextView)findViewById(R.id.MenuButtonProducts);
        MenuButtonProducts.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        MenuButtonProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activePage != 3)
                {
                    SideDrawer.toggleLeftDrawer();

                    Intent i = new Intent(context, ProductsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        Avatar = (ImageView) findViewById(R.id.Avatar);
        MenuStoreTitle = (TextView)findViewById(R.id.MenuStoreTitle);
        MenuStoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        MenuStoreURL = (TextView)findViewById(R.id.MenuStoreURL);
        MenuStoreURL.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

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

        loadStore();

    }

    @Override
    public void setContentView(int id)
    {
        LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ContentArea = (LinearLayout) findViewById(R.id.ContentArea);
        inflater.inflate(id, ContentArea);
    }

    @Override
    public void onBackPressed()
    {
        Log.e("SideDrawerIsClosed",String.valueOf(SideDrawer.isClosed()));
        if(!SideDrawer.isClosed())
        {
            SideDrawer.closeLeftSide();
        }
        else if(!ExitEnable)
        {
            ExitToast.show();
            ExitEnable = true;
            ExitCounter = new ExitCount(ExitTime*1000,1000);
            ExitCounter.start();
        }
        else
        {
            ExitEnable = false;
            ExitToast.cancel();

            android.os.Process.killProcess(android.os.Process.myPid());
            //super.onBackPressed();
        }
    }

    public class ExitCount extends CountDownTimer
    {
        public ExitCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished){}

        @Override
        public void onFinish()
        {
            ExitEnable = false;
        }
    }

    public void loadStore()
    {
        try
        {
            JSONObject activeStore = (JSONObject) new JSONTokener(ConstantsAndFunctions.readToFile(savePath,0)).nextValue();
            Log.i("activeStore",activeStore.toString());
//{"username":"mobile","password":"app","store_name":"test","store_url":"mobile.arastta.com","config_name":"Mobile","config_image":"image\/placeholder.png"}

            url = activeStore.getString("store_url");
            username = activeStore.getString("username");
            password = activeStore.getString("password");

            String avatarUrl = ConstantsAndFunctions.getHttpOrHttps() + activeStore.getString("store_url") +"/"+ activeStore.getString("config_image");

            ImageOptions avatarOptions = new ImageOptions();
            //avatarOptions.round = 999;
            avatarOptions.fileCache = true;
            avatarOptions.memCache = true;
            AQuery AvatarAQ = new AQuery(context);
            AvatarAQ.id(Avatar).image(avatarUrl, avatarOptions);

            MenuStoreTitle.setText(activeStore.getString("store_name"));
            MenuStoreURL.setText(activeStore.getString("store_url"));

            loadStores();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void loadStores()
    {
        try
        {
            JSONArray jsonArray = new JSONArray(ConstantsAndFunctions.readToFile(savePath,1));
            Log.e("loadStores","jsonArray.length:"+jsonArray.length());

            ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
            for (int i = 0; i<jsonArray.length(); i++)
            {
                arrayList.add(jsonArray.getJSONObject(i));
            }
            StoresAdapter adapter = new StoresAdapter(context,arrayList,0);
            MenuStoreLv.setAdapter(adapter);

            int height = (int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,53) * jsonArray.length();
            ViewGroup.LayoutParams paramsWhisp = MenuStoreLv.getLayoutParams();
            if (paramsWhisp == null) {
                paramsWhisp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            } else {
                paramsWhisp.height = height;
            }
            MenuStoreLv.setLayoutParams(paramsWhisp);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
