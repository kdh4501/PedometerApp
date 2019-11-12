package com.example.pedometerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CommunityActivity extends AppCompatActivity {
    WebView web_view;

    private String communityUrl = "http://192.168.0.8:8080/step/home.do";
    private WebSettings mWebSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initUI();

        Log.d("WTF", "comunity : " + communityUrl);
        web_view.loadUrl(communityUrl);

    }

    private void initUI() {
        web_view = findViewById(R.id.web_view);

        web_view.getSettings().setJavaScriptEnabled(true);                              // javascript를 실행할 수 있도록 설정

        web_view.getSettings().setBlockNetworkImage(false);
        web_view.getSettings().setLoadsImagesAutomatically(true);
        web_view.getSettings().setAllowContentAccess(true);
        web_view.getSettings().setLoadWithOverviewMode(false);
        web_view.getSettings().setAppCacheEnabled(true);
        web_view.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebSettings = web_view.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    // 웹페이지 자바스크립트 허용
        mWebSettings.setSupportMultipleWindows(false);  // 새창 띄우기 허용 여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);   // 자바스크립트 새창 띄우기 허용 여부
        mWebSettings.setUseWideViewPort(false);  // 화면 사이즈 맞추기 허용 여부
        mWebSettings.setSupportZoom(false); // 화면 줌 허용 여부
        mWebSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //컨텐츠 사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);   // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true);    // 로컬 저장소 허용 여부

    }

}
