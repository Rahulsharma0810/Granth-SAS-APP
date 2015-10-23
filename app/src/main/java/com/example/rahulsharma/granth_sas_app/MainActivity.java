package com.example.rahulsharma.granth_sas_app;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;




@SuppressLint("JavascriptInterface") public class MainActivity extends Activity {
    /** Called when the activity is first created. */

    private WebView browser = null;



    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        browser.loadUrl(url);
        return true;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // connect to our browser so we can manipulate it
        browser = (WebView) findViewById(R.id.webView);

        // get settings so we can config our WebView instance
        WebSettings settings = browser.getSettings();

        // JavaScript?  Of course!
        settings.setJavaScriptEnabled(true);
        // clear cache
        browser.clearCache(true);

        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        browser.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                pd.show();
                browser.loadUrl(url);
                return true;
            }
        });
        // this is necessary for "alert()" to work
        browser.setWebChromeClient(new WebChromeClient());

        // add our custom functionality to the javascript environment
        browser.addJavascriptInterface(new MyCoolJSHandler(), "Cloud");

        // load a page to get things started
        pd.show();
        browser.loadUrl("http://schoolv2.inilabs.net/signin/index");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(browser.canGoBack() == true){
                        pd.show();
                        browser.goBack();
                    }else{
                        pd.show();
                        finish();
                    }
                    pd.dismiss();
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }



    final class MyCoolJSHandler
    {
        // write to LogCat (Info)
        public void Info(String str) {
            Log.i("GoingNative",str);
        }

        // write to LogCat (Error)
        public void Error(String str) {
            Log.e("GoingNative",str);
        }

        // Kill the app
        public void EndApp() {
            finish();
        }
    }

}