package com.stylingandroid.designlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.stylingandroid.designlibrary.rss.model.Item;

public class ItemFragment extends Fragment {
    private static final String KEY_ITEM = "ARG_ITEM";
    public static final String NEWLINE = "\\n";
    public static final String BR = "<br />";
    public static final String HTML_MIME_TYPE = "text/html";

    public static Fragment newInstance(Context context, Item item) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_ITEM, item);
        return Fragment.instantiate(context, ItemFragment.class.getName(), args);
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Item item = (Item) getArguments().getSerializable(KEY_ITEM);
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        if (item != null) {
            WebView webView = (WebView) view.findViewById(R.id.web_view);
            String html = item.getContent();
            html = html.replaceAll(NEWLINE, BR);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(html, HTML_MIME_TYPE, null);
        }
        return view;
    }
}
