package com.arastta;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    boolean closePage = false;

    WebView Webview;
    ProgressDialog AsyncDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 0;

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_browser);

        context = BrowserActivity.this;
        ScreenName = "BrowserActivity";

        MenuTitle.setText(getResources().getString(R.string.get_arastta_cloud));

        AsyncDialog = new ProgressDialog(context);
        AsyncDialog.setMessage(getResources().getString(R.string.please_wait));
        AsyncDialog.setCancelable(true);
        AsyncDialog.setIndeterminate(true);
        AsyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Webview = (WebView)findViewById(R.id.Webview);
        Webview.setWebChromeClient(new WebChromeClient());

        //Webview.getSettings().setBuiltInZoomControls(true);
        //Webview.getSettings().setSupportZoom(true);
        //Webview.getSettings().setLoadWithOverviewMode(true);
        //Webview.getSettings().setUseWideViewPort(true);
        //Webview.getSettings().setAllowFileAccess(true);

        Webview.getSettings().setJavaScriptEnabled(true);
        Webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        Webview.getSettings().setDomStorageEnabled(true);
        Webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        Webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        Webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                AsyncDialog.show();
            }
            public void onPageFinished(WebView view, String url)
            {
                AsyncDialog.dismiss();
            }
        });
        Webview.loadUrl(" https://arastta.com/?utm_source=App&utm_medium=Android&utm_campaign=BottomBanner");

        /*
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            Webview.loadUrl(extras.getString("url"));
        }
        else
        {
            onBackPressed();
        }
        */
    }
    /*
    public void onBackPressed()
    {
        if(closePage)
        {
            super.onBackPressed();
        }
        else if (Webview.isFocused() && Webview.canGoBack())
        {
            Webview.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
    */
}
