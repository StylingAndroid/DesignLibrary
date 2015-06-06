package com.stylingandroid.designlibrary;

import com.stylingandroid.designlibrary.rss.model.Articles;

public interface ArticlesConsumer {
    void setArticles(Articles articles);

    void handleError(String message);
}
