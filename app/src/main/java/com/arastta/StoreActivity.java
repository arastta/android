package com.arastta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StoreActivity extends Activity
{
    Context context;
    String ScreenName = "";

    String savePath = "";

    boolean Working = false;

    EditText EditTextStoreTitle;
    EditText EditTextStoreURL;
    EditText EditTextUsername;
    EditText EditTextPassword;

    CheckBox CheckBoxTitle;
    CheckBox CheckBoxUrl;

    ProgressDialog loading = null;

    boolean editStore = false;
    String editStoreUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_store);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = StoreActivity.this;
        ScreenName = "StoreActivity";

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

        loading = new ProgressDialog(context);
        loading.setCancelable(true);
        loading.setIndeterminate(true);
        loading.setMessage(getResources().getString(R.string.please_wait));
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //unUsed
        TextView TextViewStoreTitle = (TextView)findViewById(R.id.TextViewStoreTitle);
        TextViewStoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewStoreUrl = (TextView)findViewById(R.id.TextViewStoreUrl);
        TextViewStoreUrl.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewUsername = (TextView)findViewById(R.id.TextViewUsername);
        TextViewUsername.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewPassword = (TextView)findViewById(R.id.TextViewPassword);
        TextViewPassword.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewNofitication = (TextView)findViewById(R.id.TextViewNofitication);
        TextViewNofitication.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewListSettings = (TextView)findViewById(R.id.TextViewListSettings);
        TextViewListSettings.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB1 = (TextView)findViewById(R.id.TextViewCB1);
        TextViewCB1.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB2 = (TextView)findViewById(R.id.TextViewCB2);
        TextViewCB2.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB3 = (TextView)findViewById(R.id.TextViewCB3);
        TextViewCB3.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB4 = (TextView)findViewById(R.id.TextViewCB4);
        TextViewCB4.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB5 = (TextView)findViewById(R.id.TextViewCB5);
        TextViewCB5.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB6 = (TextView)findViewById(R.id.TextViewCB6);
        TextViewCB6.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        TextView TextViewCB7 = (TextView)findViewById(R.id.TextViewCB7);
        TextViewCB7.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        //

        ImageButton TopBack = (ImageButton)findViewById(R.id.TopBack);
        TopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        ImageButton TrashButton = (ImageButton)findViewById(R.id.TrashButton);
        TrashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(context.getResources().getString(R.string.warning))
                    .setMessage(context.getResources().getString(R.string.delete_store))
                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            try
                            {
                                JSONArray allStores = new JSONArray();

                                JSONArray allStoresX = new JSONArray(ConstantsAndFunctions.readToFile(savePath, 1));
                                for (int i=0;i<allStoresX.length();i++)
                                {
                                    if(!allStoresX.getJSONObject(i).getString("store_url").equals(editStoreUrl))
                                    {
                                        allStores.put(allStoresX.getJSONObject(i));
                                    }
                                }

                                boolean back = true;

                                if(new JSONObject(ConstantsAndFunctions.readToFile(savePath, 0)).getString("store_url").equals(editStoreUrl))
                                {
                                    ConstantsAndFunctions.deleteToFile(savePath, 0);
                                    if(allStores.length() > 0)
                                    {
                                        ConstantsAndFunctions.writeToFile(savePath, 0, allStores.getJSONObject(0).toString());
                                    }
                                    back = false;
                                }

                                if(allStores.length() == 0)
                                {
                                    ConstantsAndFunctions.deleteToFile(savePath, 1);
                                }
                                else
                                {
                                    ConstantsAndFunctions.writeToFile(savePath, 1, allStores.toString());
                                }

                                if(back)
                                {
                                    onBackPressed();
                                }
                                else
                                {
                                    Intent i = new Intent(context, DashboardActivity.class);

                                    if(allStores.length() == 0)
                                        i = new Intent(context, MainActivity.class);

                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.no), null)
                    .show();
            }
        });

        TextView CancelButton = (TextView)findViewById(R.id.CancelButton);
        CancelButton.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        TextView TopTitle = (TextView)findViewById(R.id.TopTitle);
        TopTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        EditTextStoreTitle = (EditText)findViewById(R.id.EditTextStoreTitle);
        EditTextStoreTitle.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        EditTextStoreURL = (EditText)findViewById(R.id.EditTextStoreURL);
        EditTextStoreURL.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        EditTextUsername = (EditText)findViewById(R.id.EditTextUsername);
        EditTextUsername.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        EditTextPassword = (EditText)findViewById(R.id.EditTextPassword);
        EditTextPassword.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));

        CheckBoxTitle = (CheckBox)findViewById(R.id.CheckBoxTitle);
        CheckBoxUrl = (CheckBox)findViewById(R.id.CheckBoxUrl);

        TextView SaveButton = (TextView)findViewById(R.id.SaveButton);
        SaveButton.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(EditTextStoreURL.length() == 0 || EditTextUsername.length() == 0 || EditTextPassword.length() == 0)
                {
                    Toast.makeText(context, getResources().getString(R.string.store_create_error), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!CheckBoxTitle.isChecked() && EditTextStoreTitle.length() == 0)
                    {
                        Toast.makeText(context, getResources().getString(R.string.store_create_error), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(!Working)
                        {
                            Working = true;

                            String username = EditTextUsername.getText().toString().trim();
                            String password = EditTextPassword.getText().toString().trim();
                            String title = EditTextStoreTitle.getText().toString().trim();
                            String xUrl = EditTextStoreURL.getText().toString().trim();

                            if(ConstantsAndFunctions.NetworkIsAvailable(context))
                                new UserLogin().execute(xUrl,username,password,title);
                            else
                                Toast.makeText(context, getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //TODO
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.i("object",extras.getString("object"));
            try
            {
                JSONObject jsonObject = new JSONObject(extras.getString("object"));

                editStore = true;
                editStoreUrl = jsonObject.getString("store_url");

                TopTitle.setText(jsonObject.getString("store_name"));

                EditTextStoreTitle.setText(jsonObject.getString("store_name"));
                EditTextStoreURL.setText(jsonObject.getString("store_url"));
                EditTextUsername.setText(jsonObject.getString("username"));
                EditTextPassword.setText(jsonObject.getString("password"));
                if(jsonObject.getString("connection_type").equals("true")){
                    CheckBoxUrl.setChecked(true);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            TopTitle.setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,12),0,0,0);

            TopBack.setVisibility(ImageButton.GONE);
            CancelButton.setVisibility(TextView.GONE);
            TrashButton.setVisibility(ImageButton.GONE);
        }

    }

    private class UserLogin extends AsyncTask<String, Void, String>
    {
        String ResultText = "";
        String xUrl = "";
        String title = "";
        String username = "";
        String password = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute()
        {
            Working = true;
            loading.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            xUrl = params[0];
            title = params[3];
            username = params[1];
            password = params[2];

            ResultText = ConstantsAndFunctions.getHtml(username,password,xUrl,"settings");
            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Working = false;
            loading.dismiss();

            Log.e(ScreenName+":"+String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()),
                    ResultText);

            if(ResultText.equals("error"))
            {
                Toast.makeText(context, getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    Log.i("Store Adı", jsonObject.getString("config_name"));

                    String config_name = jsonObject.getString("config_name");
                    String config_image = jsonObject.getString("config_image");

                    if(CheckBoxTitle.isChecked())
                    {
                        title = jsonObject.getString("config_name");
                    }

                    JSONObject activeStore = new JSONObject();
                    activeStore.put("username",username);
                    activeStore.put("password",password);
                    activeStore.put("store_name",title);
                    activeStore.put("store_url",xUrl);
                    activeStore.put("config_name",config_name);
                    activeStore.put("config_image",config_image);
                    activeStore.put("config_name",config_name);
                    activeStore.put("connection_type",String.valueOf(CheckBoxUrl.isChecked()));
                    Log.i("activeStore",activeStore.toString());

                    ConstantsAndFunctions.writeToFile(savePath, 0, activeStore.toString());

                    Log.e("allStores",String.valueOf(ConstantsAndFunctions.checkToFile(savePath, 1)));
                    JSONArray allStores = new JSONArray();
                    allStores.put(activeStore);

                    if(ConstantsAndFunctions.checkToFile(savePath, 1))
                    {
                        try
                        {
                            JSONArray allStoresX = new JSONArray(ConstantsAndFunctions.readToFile(savePath, 1));
                            for (int i=0;i<allStoresX.length();i++)
                            {
                                if(editStore)
                                {
                                    if(!allStoresX.getJSONObject(i).getString("store_url").equals(editStoreUrl))
                                    {
                                        allStores.put(allStoresX.getJSONObject(i));
                                    }
                                }
                                else
                                {
                                    allStores.put(allStoresX.getJSONObject(i));
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    Log.e("allStores",allStores.toString());
                    ConstantsAndFunctions.writeToFile(savePath, 1, allStores.toString());

                    Intent i = new Intent(context, DashboardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
