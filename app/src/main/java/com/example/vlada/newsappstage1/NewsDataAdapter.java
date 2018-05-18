package com.example.vlada.newsappstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsDataAdapter extends ArrayAdapter<NewsData> {

    public NewsDataAdapter(Context context, List<NewsData> data){
        super(context,0, data);
    }

    // Returns a list item view that displays information about the news at the given position in the list of news
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_details, parent, false);
        }

        // Find the news at the given position in the list of news
        NewsData actualNews = getItem(position);

        // Find the TextView with the desired id
        TextView newsCategory = listItemView.findViewById(R.id.news_category);
        // Display the current news in the TextView
        newsCategory.setText(actualNews.getCategory());

        // Find the TextView with the desired id
        TextView newsTitle = listItemView.findViewById(R.id.news_title);
        // Display the current news in the TextView
        newsTitle.setText(actualNews.getTitle());

        // Find the TextView with the desired id
        TextView newsAuthor = listItemView.findViewById(R.id.news_author);
        // Display the current news in the TextView
        newsAuthor.setText(actualNews.getAuthor());

        // Find the TextView with the desired id
        TextView newsDate = listItemView.findViewById(R.id.news_date);
        // Extract, format and display the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        SimpleDateFormat personalDateFormat = new SimpleDateFormat("dd MM,yyyy", Locale.US);

        try {
            Date newsWebDate = dateFormat.parse(actualNews.getDate());
            String date = personalDateFormat.format(newsWebDate);
            newsDate.setText(date);
        } catch (ParseException e){
            Log.e("Parsing Error","Error parsing date" + e.getMessage());
            e.printStackTrace();
        }

        return listItemView;
    }
}
