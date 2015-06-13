package com.stylingandroid.designlibrary.rss.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Articles implements Iterable<String> {
    private static final String TITLE_REGEX_STRING = "(.*)\\s+\\u2013\\s+Part\\s+([0-9]+)";
    private static final Pattern TITLE_PATTERN = Pattern.compile(TITLE_REGEX_STRING);
    private static final int TITLE_GROUP = 1;
    private static final int PART_NUMBER_GROUP = 2;

    private final Map<String, Article> articles;
    private final List<String> titles;

    public static Articles transform(Feed feed) {
        Map<String, Article> articles = new HashMap<>();
        List<String> titles = new ArrayList<>();
        for (Item item : feed.getItems()) {
            String title = item.getTitle();
            Matcher matcher = TITLE_PATTERN.matcher(title);
            if (matcher.find()) {
                String baseTitle = matcher.group(TITLE_GROUP);
                int part = Integer.parseInt(matcher.group(PART_NUMBER_GROUP));
                Article article = articles.get(baseTitle);
                if (article == null) {
                    article = Article.newInstance(baseTitle);
                    articles.put(baseTitle, article);
                    titles.add(baseTitle);
                }
                article.addPart(part, item);
            } else {
                Article article = Article.newInstance(title);
                articles.put(title, article);
                titles.add(title);
                article.addPart(0, item);
            }
        }
        Collections.sort(titles);
        return new Articles(titles, articles);
    }

    Articles(List<String> titles, Map<String, Article> articles) {
        this.titles = titles;
        this.articles = articles;
    }

    @Override
    public Iterator<String> iterator() {
        return titles.iterator();
    }

    public Article getArticle(String title) {
        return articles.get(title);
    }
}
