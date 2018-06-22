package com.ntkduy1604.newsapp.recycling;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ntkduy1604.newsapp.model.Article;
import com.ntkduy1604.newsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

/**
 * Provide a reference to the type of views that you are using (custom ViewHolder)
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.tv_article_author)
    TextView tvArticleAuthor;
    @BindView(R.id.tv_article_date)
    TextView tvArticleDate;

    private Uri articleUrl;



    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        // Define click listener for the ViewHolder's View.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUrl);
                context.startActivity(websiteIntent);
            }
        });
    }

    public void bindDataModel(Article article) {
        tvArticleTitle.setText(article.getArticleTitle());
        tvArticleAuthor.setText(article.getArticleAuthor());
        tvArticleDate.setText(article.getPublishedDate());
        articleUrl = Uri.parse(article.getArticleUrl());
    }
}
