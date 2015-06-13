package com.stylingandroid.designlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.stylingandroid.designlibrary.rss.model.Article;
import com.stylingandroid.designlibrary.rss.model.Item;

public final class ArticleViewPagerAdapter extends FragmentPagerAdapter {
    private final Article article;
    private final Context context;
    private final Resources resources;

    public static ArticleViewPagerAdapter newInstance(FragmentManager fragmentManager, Context context, Article article) {
        Resources resources = context.getResources();
        return new ArticleViewPagerAdapter(fragmentManager, context, resources, article);
    }

    private ArticleViewPagerAdapter(FragmentManager fragmentManager, Context context, Resources resources, Article article) {
        super(fragmentManager);
        this.context = context;
        this.resources = resources;
        this.article = article;
    }

    @Override
    public Fragment getItem(int position) {
        Item item = article.getPartAtPosition(position);
        return ItemFragment.newInstance(context, item);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return article.getPartsCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return resources.getString(R.string.part_title, article.getPartNumber(position));
    }
}
