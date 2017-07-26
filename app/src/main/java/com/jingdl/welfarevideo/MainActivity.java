package com.jingdl.welfarevideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.pnikosis.materialishprogress.ProgressWheel;

import net.youmi.android.AdManager;
import net.youmi.android.nm.bn.BannerManager;
import net.youmi.android.nm.cm.ErrorCode;
import net.youmi.android.nm.sp.SpotListener;
import net.youmi.android.nm.sp.SpotManager;
import net.youmi.android.nm.vdo.VideoAdManager;
import net.youmi.android.nm.vdo.VideoAdRequestListener;


public class MainActivity extends Activity {

    private Context mContext;
    private ProgressWheel progresswheel;
    private ScrollWebView webview;
    private long exitTime = 0;
    private LinearLayout layout_bottom;
    private String laodurl = "http://m.949d.cc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        AdManager.getInstance(mContext).init("996a721895b79307", "f1909fc31fa1f9d2", true);

        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        layout_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MallActivity.class);
                startActivity(intent);
            }
        });

        progresswheel = (ProgressWheel)findViewById(R.id.progresswheel);
        webview = (ScrollWebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webview.setOnScrollChangeListener(new com.jingdl.welfarevideo.ScrollWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                layout_bottom.setVisibility(View.GONE);
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
                layout_bottom.setVisibility(View.GONE);
            }

            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                layout_bottom.setVisibility(View.VISIBLE);
            }
        });


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
                if(url.contains("m.949d.cc/v")){
                    setupSpotAd();
                }
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


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                WebResourceResponse response = new WebResourceResponse("image/png", "UTF-8", null);

                String urltemp = request.getUrl().toString();

                if(urltemp.contains("ylfdy")){
                    return response;
                }

                if(urltemp.contains("13559")){
                    return response;
                }

                if(urltemp.contains("13665")){
                    return response;
                }

                return super.shouldInterceptRequest(view, request);
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.removeJavascriptInterface("searchBoxJavaBridge_");
            webview.removeJavascriptInterface("accessibility");
            webview.removeJavascriptInterface("accessibilityTraversal");
        }

        if (!TextUtils.isEmpty(laodurl)) {
            progresswheel.setVisibility(View.VISIBLE);
            webview.loadUrl(laodurl);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            // 点击后退关闭插屏广告
            if (SpotManager.getInstance(mContext).isSpotShowing()) {
                SpotManager.getInstance(mContext).hideSpot();
                return true;
            }

            if(webview.canGoBack()){
                webview.goBack();
                return true;
            }

            if((System.currentTimeMillis()-exitTime) > 1000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                SpotManager.getInstance(mContext).onDestroy();
                SpotManager.getInstance(mContext).onAppExit();
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 设置插屏广告
     */
    private void setupSpotAd() {
        SpotManager.getInstance(mContext).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);
        SpotManager.getInstance(mContext).setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);
        SpotManager.getInstance(mContext).showSpot(mContext, new SpotListener() {
            @Override
            public void onShowSuccess() {}

            @Override
            public void onShowFailed(int errorCode) {}

            @Override
            public void onSpotClosed() {}

            @Override
            public void onSpotClicked(boolean isWebPage) {}
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        SpotManager.getInstance(mContext).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotManager.getInstance(mContext).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpotManager.getInstance(mContext).onDestroy();
        SpotManager.getInstance(mContext).onAppExit();
    }
}
