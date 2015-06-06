package com.stylingandroid.designlibrary.rss;

import com.stylingandroid.designlibrary.rss.model.Articles;
import com.stylingandroid.designlibrary.rss.model.Feed;
import com.stylingandroid.designlibrary.rss.model.Item;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class SaRssParser {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String CHANNEL = "channel";
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String PUB_DATE = "pubDate";
    private static final String DESCRIPTION = "description";
    private static final String CONTENT = "content:encoded";

    private final DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ", Locale.getDefault());
    private final XmlPullParser parser;
    private ParseState state = ParseState.NONE;

    private Feed feed = Feed.NONE;
    private Item currentItem = Item.NONE;

    private SaRssParser(XmlPullParser parser) {
        this.parser = parser;
    }

    public static SaRssParser newInstance(InputStream inputStream) {
        XmlPullParserFactory factory;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(inputStream, DEFAULT_CHARSET);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return new SaRssParser(parser);
    }

    public Articles parse() throws Exception {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            parseEvent(eventType);
            eventType = parser.next();
        }
        return Articles.transform(feed);
    }

    private void parseEvent(int eventType) {
        switch (eventType) {
            case XmlPullParser.START_TAG:
                startTag(parser.getName());
                break;
            case XmlPullParser.END_TAG:
                endTag(parser.getName());
                break;
            case XmlPullParser.TEXT:
                text(parser.getText());
                break;
            default:
        }
    }

    private void startTag(String name) {
        if (CHANNEL.equals(name)) {
            feed = new Feed();
        } else if (ITEM.equals(name)) {
            currentItem = new Item();
        } else if (TITLE.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_TITLE;
            }
        } else if (DESCRIPTION.equals(name)) {
            if (currentItem != Item.NONE) {
                state = ParseState.ITEM_DESCRIPTION;
            }
        } else if (CONTENT.equals(name)) {
            state = ParseState.ITEM_CONTENT;
        } else if (PUB_DATE.equals(name)) {
            state = ParseState.ITEM_PUB_DATE;
        }
    }

    private void text(String text) {
        switch (state) {
            case ITEM_TITLE:
                currentItem.setTitle(text);
                break;
            case ITEM_DESCRIPTION:
                currentItem.setDescription(text);
                break;
            case ITEM_CONTENT:
                currentItem.setContent(text);
                break;
            case ITEM_PUB_DATE:
                Date date;
                try {
                    date = dateFormat.parse(text);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                currentItem.setPubDate(date.getTime());
                break;
            default:
                break;
        }
    }

    private void endTag(String name) {
        if (ITEM.equals(name)) {
            feed.addItem(currentItem);
            currentItem = Item.NONE;
        }
        state = ParseState.NONE;
    }

    private enum ParseState {
        NONE,
        ITEM_TITLE,
        ITEM_DESCRIPTION,
        ITEM_CONTENT,
        ITEM_PUB_DATE
    }
}
