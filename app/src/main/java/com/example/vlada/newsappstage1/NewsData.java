package com.example.vlada.newsappstage1;


// An object containing information related to a single news
public class NewsData {

    // Category of the news
    private String mCategory;

    // Title of the news
    private String mTitle;

    // Author of the news
    private String mAuthor;

    // Date of the news
    private String mDate;

    // Url of the news
    private String mUrl;

    // Constructs a new object
    public NewsData(String category, String title, String author, String date, String url){
        mCategory = category;
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mUrl = url;
    }

    // Returns the category of the news
    public String getCategory() {
        return mCategory;
    }

    // Returns the title of the news
    public String getTitle() {
        return mTitle;
    }

    // Returns the author of the news
    public String getAuthor() {
        return mAuthor;
    }

    // Returns the date of the news
    public String getDate() {
        return mDate;
    }

    // Returns the url of the news
    public String getUrl() {
        return mUrl;
    }
}
