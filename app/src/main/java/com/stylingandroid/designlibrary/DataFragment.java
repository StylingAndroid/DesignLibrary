package com.stylingandroid.designlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.stylingandroid.designlibrary.net.VolleySingleton;
import com.stylingandroid.designlibrary.rss.RssRequest;
import com.stylingandroid.designlibrary.rss.model.Articles;

public class DataFragment extends Fragment implements Response.Listener<Articles>, Response.ErrorListener {
    private Articles articles;

    private VolleySingleton volley;

    private ArticlesConsumer articlesConsumer;
    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = inflater.getContext();

        initVolley(context);
        update();
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        volley.stop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ArticlesConsumer) {
            articlesConsumer = (ArticlesConsumer) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        articlesConsumer = null;
    }

    @Override
    public void onResponse(Articles newArticles) {
        this.articles = newArticles;
        if (articlesConsumer != null) {
            articlesConsumer.setArticles(articles);
        }
        isLoading = false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (articlesConsumer != null) {
            articlesConsumer.handleError(error.getLocalizedMessage());
        }
        isLoading = false;
    }

    private void initVolley(Context context) {
        if (volley == null) {
            volley = VolleySingleton.getInstance(context);
        }
    }

    private void update() {
        if (articles == null && !isLoading()) {
            String url = getString(R.string.feed_url);
            volley.addToRequestQueue(new RssRequest(Request.Method.GET, url, this, this));
            isLoading = true;
        } else {
            if (articlesConsumer != null) {
                articlesConsumer.setArticles(articles);
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}
