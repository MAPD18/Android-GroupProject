package com.android.mapd.myplaces.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.android.mapd.myplaces.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.mapd.myplaces.AppConstants.PLACE_WEBSITE_URI_KEY;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        webView.loadUrl(getIntent().getStringExtra(PLACE_WEBSITE_URI_KEY));
    }
}
