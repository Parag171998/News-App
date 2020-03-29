package com.example.appyhightask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Article> articleList;

    public NewsAdapter(Context context, List<Article> articleList) {
        this.layoutInflater = layoutInflater.from(context);
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_news_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(layoutInflater.getContext()).load(articleList.get(position).getUrlToImage()).into(holder.newsImg);
        holder.newsTitle.setText(articleList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView newsImg;
        TextView newsTitle;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            newsImg = itemView.findViewById(R.id.newsImg);
            newsTitle = itemView.findViewById(R.id.newsTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(layoutInflater.getContext(),NewsFeed.class);
                    intent.putExtra("newsUrl",articleList.get(getAdapterPosition()).getUrl());
                    layoutInflater.getContext().startActivity(intent);
                }
            });
        }
    }
}
