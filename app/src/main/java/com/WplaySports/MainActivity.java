package com.WplaySports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ResourceBundle;

import im.delight.android.webview.AdvancedWebView;


public class MainActivity extends Activity implements AdvancedWebView.Listener {


    private AdvancedWebView mWebView;
    String loadUrl;

    ConstraintLayout ivError;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
        loadUrl = getString(R.string.url_inicio);
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new geturljason(this))
                .init();

        progressBar = (ProgressBar) findViewById(R.id.prg);

        ivError = findViewById(R.id.ivError);

        findViewById(R.id.btnGrupo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_grupo)));
                startActivity(i);
            }
        });

        findViewById(R.id.btnRegistro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_registro)));
                startActivity(i);
            }
        });

        findViewById(R.id.btnMarket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_whatsapp)));
                startActivity(i);
            }
        });

        findViewById(R.id.btnHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_juega)));
                startActivity(i);
            }
        });

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.url_telegran)));
                startActivity(i);
            }
        });

        mWebView = findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setInitialScale(1);

        mWebView.setListener(this, this);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {

            }
        });


        this.mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(view.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(view.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                handleURL(url);
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mWebView.loadUrl(loadUrl);
                    //progressBar = ProgressDialog.show(MainActivity.this, "", "Loading...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);


        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    OnEverySecond();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();

        }else {
            super.onBackPressed();
        }
    }

    public boolean IsNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            // boolean isWiFi = info.getType() == ConnectivityManager.TYPE_WIFI;
            return info != null && info.getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void OnEverySecond() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (IsNetworkAvailable()) {
                    ivError.setVisibility(View.GONE);
                } else {
                    ivError.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void handleURL(String url) {
        if (url.startsWith("tel:") || url.startsWith("geo:") || url.startsWith("mailto:")) {
            //if (url==null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            //new_notification();
            return;
        } else
            mWebView.loadUrl(url);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new geturljason(this))
                .init();
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
        mWebView.loadUrl(url);
    }


    public class geturljason implements  OneSignal.NotificationOpenedHandler{
        private MainActivity application;
        public geturljason(MainActivity application) {
            this.application = application;
        }
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            // Get custom datas from notification
            loadUrl=null;
            JSONObject data = result.notification.payload.additionalData;
            if (data != null) {
                try {
                    String  mycustomdata= data.getString("key");
                    loadUrl=mycustomdata;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mWebView.loadUrl(loadUrl);
            }
            // React to button pressed
            OSNotificationAction.ActionType actionType = result.action.type;
            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            // Launch new activity using Application object
            //  startApp();

        }

    }
    
}


