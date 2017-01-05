package com.arastta;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;

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

    static ProgressDialog loading = null;

    boolean searchIsOpen = false;
    RelativeLayout SearchBar;
    EditText SearchText;
    ImageButton SearchClose;

    boolean typeOfDate = false;
    boolean filterIsOpen = false;
    RelativeLayout FilterArea;
    TextView FilterStartDate;
    TextView FilterFinishDate;

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

        //Search
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        searchIsOpen = false;
        SearchBar = (RelativeLayout) findViewById(R.id.SearchBar);

        SearchText = (EditText) findViewById(R.id.SearchText);
        SearchText.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        SearchText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String text = SearchText.getText().toString().trim();

                    imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);

                    if(activePage == 1)
                    {
                        //Orders
                        OrdersActivity.xOrders("&search="+text,"","");
                    }
                    if(activePage == 2)
                    {
                        //Customers
                        CustomersActivity.xCustomers("&search="+text,"","");
                    }
                    if(activePage == 3)
                    {
                        //Products
                        ProductsActivity.xProducts("?search="+text);
                    }
                    return true;
                }
                return false;
            }
        });

        SearchClose = (ImageButton) findViewById(R.id.SearchClose);
        SearchClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchIsOpen = false;
                SearchBar.setVisibility(RelativeLayout.GONE);
                SearchText.setText("");

                imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);

                if(activePage == 1)
                {
                    //Orders
                    OrdersActivity.xOrders("","","");
                }
                if(activePage == 2)
                {
                    //Customers
                    CustomersActivity.xCustomers("","","");
                }
                if(activePage == 3)
                {
                    //Products
                    ProductsActivity.xProducts("");
                }
            }
        });

        ImageButton SearchButton = (ImageButton) findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(searchIsOpen)
                {
                    String text = SearchText.getText().toString().trim();

                    imm.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);

                    if(activePage == 1)
                    {
                        //Orders
                        OrdersActivity.xOrders("&search="+text,"","");
                    }
                    if(activePage == 2)
                    {
                        //Customers
                        CustomersActivity.xCustomers("&search="+text,"","");
                    }
                    if(activePage == 3)
                    {
                        //Products
                        ProductsActivity.xProducts("?search="+text);
                    }
                }
                else
                {
                    searchIsOpen = true;
                    SearchBar.setVisibility(RelativeLayout.VISIBLE);
                }
            }
        });

        FilterArea = (RelativeLayout) findViewById(R.id.FilterArea);
        FilterArea.setVisibility(RelativeLayout.GONE);
        filterIsOpen = false;

        TextView FilterTitle = (TextView)findViewById(R.id.FilterTitle);
        FilterTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        TextView FilterStartDateX = (TextView)findViewById(R.id.FilterStartDateX);
        FilterStartDateX.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        TextView FilterFinishDateX = (TextView)findViewById(R.id.FilterFinishDateX);
        FilterFinishDateX.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        FilterStartDate = (TextView)findViewById(R.id.FilterStartDate);
        FilterStartDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        FilterStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                typeOfDate = false;

                Calendar calendar = Calendar.getInstance();

                String[] datePrms = FilterStartDate.getText().toString().trim().split("-");
                try
                {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datePrms[2]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(datePrms[1])-1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(datePrms[0]));
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                Dialog mDialog = new DatePickerDialog(context,
                        mDatesetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });

        FilterFinishDate = (TextView)findViewById(R.id.FilterFinishDate);
        FilterFinishDate.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        FilterFinishDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                typeOfDate = true;

                Calendar calendar = Calendar.getInstance();

                String[] datePrms = FilterFinishDate.getText().toString().trim().split("-");
                try
                {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(datePrms[2]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(datePrms[1])-1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(datePrms[0]));
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                Dialog mDialog = new DatePickerDialog(context,
                        mDatesetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });

        ImageButton FilterButton = (ImageButton) findViewById(R.id.FilterButton);
        FilterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FilterArea.setVisibility(RelativeLayout.VISIBLE);
                filterIsOpen = true;

                String sd = "";
                String fd = "";
                if(activePage == 0)
                {
                    //Orders
                    sd = ConstantsAndFunctions.getFromDate(DashboardActivity.day);
                }
                if(activePage == 1)
                {
                    //Orders
                    sd = ConstantsAndFunctions.getFromDate(OrdersActivity.day);
                }
                if(activePage == 2)
                {
                    //Customers
                    sd = ConstantsAndFunctions.getFromDate(CustomersActivity.day);
                }

                fd = ConstantsAndFunctions.getTodayDate();

                FilterStartDate.setText(sd);
                FilterFinishDate.setText(fd);
            }
        });

        ImageButton FilterClose = (ImageButton)findViewById(R.id.FilterClose);
        FilterClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FilterArea.setVisibility(RelativeLayout.GONE);
                filterIsOpen = false;
            }
        });

        TextView FilterDone = (TextView)findViewById(R.id.FilterDone);
        FilterDone.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        FilterDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FilterArea.setVisibility(RelativeLayout.GONE);
                filterIsOpen = false;

                String sd = FilterStartDate.getText().toString().trim();
                String fd = FilterFinishDate.getText().toString().trim();

                if(activePage == 0)
                {
                    //Orders
                    DashboardActivity.xDashboard(sd,fd);
                    DashboardActivity.resetTabs(context);
                }
                if(activePage == 1)
                {
                    //Orders
                    OrdersActivity.xOrders("",sd,fd);
                    OrdersActivity.resetTabs(context);
                }
                if(activePage == 2)
                {
                    //Customers
                    CustomersActivity.xCustomers("",sd,fd);
                    CustomersActivity.resetTabs(context);
                }
            }
        });

        SearchButton.setVisibility(ImageButton.VISIBLE);
        FilterButton.setVisibility(ImageButton.VISIBLE);

        if(activePage == 0)
        {
            SearchButton.setVisibility(ImageButton.GONE);
        }

        if(activePage == 3)
        {
            FilterButton.setVisibility(ImageButton.GONE);
        }

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
        if(searchIsOpen)
        {
            searchIsOpen = false;
            SearchBar.setVisibility(RelativeLayout.GONE);
            if(!SearchText.getText().toString().trim().equals(""))
            {
                SearchText.setText("");
                if(activePage == 1)OrdersActivity.xOrders("","","");
                if(activePage == 2)CustomersActivity.xCustomers("","","");
                if(activePage == 3)ProductsActivity.xProducts("");
            }
        }
        else if(filterIsOpen)
        {
            filterIsOpen = false;
            FilterArea.setVisibility(RelativeLayout.GONE);
        }
        else if(!SideDrawer.isClosed())
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

            if(!url.startsWith("http"))url = "http://" + url;//TODO XXX

            String avatarUrl = url +"/"+ activeStore.getString("config_image");

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

    DatePickerDialog.OnDateSetListener mDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker dp, int yil, int ay, int gun) {
            ay = ay + 1;

            String day = String.valueOf(gun);
            String month = String.valueOf(ay);
            String year = String.valueOf(yil);

            if (day.length() == 1) day = "0"+day;
            if (month.length() == 1) month = "0"+month;

            String date = year + "-" + month + "-" + day;

            if(!typeOfDate)
                FilterStartDate.setText(date);
            else
                FilterFinishDate.setText(date);
        }
    };
}
