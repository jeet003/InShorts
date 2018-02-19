package jeet.com.inshorts.Activities;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import jeet.com.inshorts.R;

/**
 * Created by jeet on 11/9/17.
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        toolbar=(Toolbar) findViewById(R.id.toolbar_webview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbar_title=(TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.detailed_news));

        String url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
    private class MyWebViewClient extends WebViewClient {

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
        }

        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {

        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }
}
