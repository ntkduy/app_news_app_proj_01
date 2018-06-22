package com.ntkduy1604.newsapp.model;

import android.graphics.drawable.Drawable;

/**
 * Created by NTKDUY on 6/20/2018
 * for PIGGY HOUSE
 * you can contact me at: ntkduy1604@gmail.com
 */
public class Article {
    private final String articleTitle;
    private final String articleAuthor;
    private final String publishedDate;

    private final String articleUrl;


    public Article(String articleTitle, String articleAuthor, String publishedDate, String articleUrl) {
        this.articleTitle = articleTitle;
        this.articleAuthor = articleAuthor;
        this.publishedDate = publishedDate;
        this.articleUrl = articleUrl;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

}
