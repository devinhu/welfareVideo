package com.jingdl.welfarevideo;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

public class MallActivity extends Activity {

    private ProgressWheel progresswheel;
    private WebView webview;
    private String mall = "https://wap.youzan.com/v2/showcase/homepage?kdt_id=16383360";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);

        progresswheel = (ProgressWheel)findViewById(R.id.progresswheel);
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);



        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progresswheel.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progresswheel.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                progresswheel.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.removeJavascriptInterface("searchBoxJavaBridge_");
            webview.removeJavascriptInterface("accessibility");
            webview.removeJavascriptInterface("accessibilityTraversal");
        }

        if (!TextUtils.isEmpty(mall)) {
            progresswheel.setVisibility(View.VISIBLE);
            webview.loadUrl(mall);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(webview.canGoBack()){
                webview.goBack();
                return true;
            }

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
