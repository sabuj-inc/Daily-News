package com.example.dailynews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebSettingsCompat;

import com.example.dailynews.Splash.Theme;

public class FullNews extends AppCompatActivity implements View.OnClickListener {
    AlertDialog.Builder builder;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ProgressBar progressBar;
    LinearLayout noInternetLayout, wrongLayout;
    //control
    ImageView webBack, webRefresh, webMenu;
    Point screenPoint;
    private String domain = "https://google.com";
    String websiteURL;
    private WebView webview;


    @SuppressLint("RequiresFeature")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);
        domain = getIntent().getStringExtra("link");
        websiteURL = domain;
        //findViewById area
        webview = findViewById(R.id.webView);
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
        progressBar = findViewById(R.id.progressBar);
        noInternetLayout = findViewById(R.id.noInternetLayout);
        wrongLayout = findViewById(R.id.wrongLayout);
        webBack = findViewById(R.id.webBack);
        webRefresh = findViewById(R.id.webRefresh);
        webMenu = findViewById(R.id.webMenu);

        //click Listener
        webBack.setOnClickListener(this);
        webRefresh.setOnClickListener(this);
        webMenu.setOnClickListener(this);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);

        Theme theme = new Theme(FullNews.this);
        if (theme.setAppTheme()){
            WebSettingsCompat.setForceDark(webview.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
        }

        //if no internet connection VISIBLE "noInternetLayout" Layout
        if (!CheckNetwork.isInternetAvailable(this)) {
            noInternetLayout.setVisibility(View.VISIBLE);
        }
        //Check Internet Connection
        internet(false);

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        //Swipe to refresh functionality
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        internet(false);
                    }
                }
        );


        //handle downloading
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1);
                    }
                }
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file....");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int clickId = v.getId();
        if (clickId == R.id.webBack) { //back functionality
            onBackPressed();
        } else if (clickId == R.id.webRefresh) {
            internet(false);
        }else if (clickId == R.id.webMenu) {
            int[] location = new int[2];
            webMenu.getLocationOnScreen(location);
            screenPoint = new Point();
            screenPoint.x = location[0];
            screenPoint.y = location[1];
            screenPopup();
        }
    }

    public void screenPopup() {
        LinearLayout copyLink, shareLink;
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.screenAccess);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.screen_access, viewGroup);
        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setWidth(500);
        popup.setHeight(500);
        popup.setFocusable(true);
        int OFFSET_X = 0;
        int OFFSET_Y = 0;
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, screenPoint.x + OFFSET_X, screenPoint.y + OFFSET_Y);
        copyLink = layout.findViewById(R.id.copyLink);
        shareLink = layout.findViewById(R.id.shareLink);
        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("key", webview.getUrl());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_SHORT).show();
            }
        });
        shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
                startActivity(Intent.createChooser(intent, "Send to"));

            }
        });

    }

    private void internet(boolean home) {
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            mySwipeRefreshLayout.setRefreshing(false);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet connection available");
            builder.setMessage("Please Check you're Mobile data or Wifi network.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //.setNegativeButton("No", null)
            builder.setCancelable(false);
            builder.show();
        } else {
            webview.getSettings().setBuiltInZoomControls(true);
            webview.getSettings().setDisplayZoomControls(false);
            //Webview stuff
            noInternetLayout.setVisibility(View.GONE);
            websiteURL = webview.getUrl();
            if (webview.getUrl() == null || home) {
                websiteURL = domain;
            }
            webview.loadUrl(websiteURL);
            webview.setWebViewClient(new WebViewClientDemo());
        }
    }


    //set back button functionality
    @Override
    public void onBackPressed() { //if user presses the back button do this
        if (webview.isFocused() && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack(); //go back in webview
        }else {
            finish();
        }
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        //Keep webView in app when clicking links
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //phone intent
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
                //main
            } else if (url.contains("mailto:")) {
                view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else if (url.contains("intent:")) {

            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mySwipeRefreshLayout.setRefreshing(false);

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(FullNews.this, "Something wrong", Toast.LENGTH_SHORT).show();
            webview.loadUrl("about:blank");
        }
    }
}

class CheckNetwork {
    private static final String TAG = CheckNetwork.class.getSimpleName();
    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            Log.d(TAG, "no internet connection");
            return false;
        } else {
            if (info.isConnected()) {
                Log.d(TAG, " internet connection available...");
                return true;
            } else {
                Log.d(TAG, " internet connection");
                return true;
            }

        }
    }
}