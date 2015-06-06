package com.stylingandroid.designlibrary.rss.model;

import java.io.Serializable;

public class Item implements Serializable {
    public static final Item NONE = new Item();

    private String title;
    private String description;
    private String content;
    private long pubDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }
}
