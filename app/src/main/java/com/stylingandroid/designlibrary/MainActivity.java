package com.stylingandroid.designlibrary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.stylingandroid.designlibrary.rss.model.Article;
import com.stylingandroid.designlibrary.rss.model.Articles;

public class MainActivity extends AppCompatActivity implements ArticlesConsumer {
    private static final String DATA_FRAGMENT_TAG = DataFragment.class.getCanonicalName();
    private static final int MENU_GROUP = 0;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;

    private Articles articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setupToolbar();
        setupNavigationView();
        setupDataFragment();
        setupFab();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        selectArticle(menuItem.getTitle());
                        return true;
                    }
                });
    }

    private void setupDataFragment() {
        DataFragment dataFragment = (DataFragment) getSupportFragmentManager().findFragmentByTag(DATA_FRAGMENT_TAG);
        if (dataFragment == null) {
            dataFragment = (DataFragment) Fragment.instantiate(this, DataFragment.class.getName());
            dataFragment.setRetainInstance(true);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(dataFragment, DATA_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    private void setupFab() {
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, R.string.fab_press, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                //NO-OP
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setArticles(Articles articles) {
        Menu menu = navigationView.getMenu();
        menu.clear();
        this.articles = articles;
        int item = 0;
        for (String articleTitle : articles) {
            menu.add(MENU_GROUP, item, item, articleTitle);
            if (item == 0) {
                menu.getItem(0).setChecked(true);
                setCurrentArticle(articles.getArticle(articleTitle));
            }
            item++;
        }
        menu.setGroupCheckable(MENU_GROUP, true, true);
    }

    @Override
    public void handleError(String message) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_LONG).show();
    }

    private void selectArticle(CharSequence title) {
        Article article = articles.getArticle(title.toString());
        setCurrentArticle(article);
    }

    private void setCurrentArticle(Article article) {
        setTitle(article.getTitle());
        ArticleViewPagerAdapter adapter = ArticleViewPagerAdapter.newInstance(getSupportFragmentManager(), this, article);
        viewPager.setAdapter(adapter);
        if (article.getPartsCount() <= 1) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
        tabLayout.setupWithViewPager(viewPager);
    }
}
