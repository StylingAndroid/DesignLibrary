package com.stylingandroid.designlibrary.rss;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.stylingandroid.designlibrary.rss.model.Articles;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RssRequest extends Request<Articles> {
    private final Response.Listener<Articles> feedListener;

    public RssRequest(int method, String url, Response.Listener<Articles> feedListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.feedListener = feedListener;
    }

    @Override
    protected Response<Articles> parseNetworkResponse(NetworkResponse response) {
        InputStream inputStream = new ByteArrayInputStream(response.data);
        SaRssParser parser = SaRssParser.newInstance(inputStream);
        Articles articles;
        try {
            articles = parser.parse();
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(articles, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Articles response) {
        feedListener.onResponse(response);
    }
}
