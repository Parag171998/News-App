package com.example.appyhightask.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appyhightask.models.Article;
import com.example.appyhightask.activities.NewsFeed;
import com.example.appyhightask.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    LayoutInflater layoutInflater;
    List<Article> articleList;
    ItemCallBack itemCallBack;

    public NewsAdapter(Context context, List<Article> articleList, ItemCallBack itemCallBack) {
        this.layoutInflater = layoutInflater.from(context);
        this.articleList = articleList;
        this.itemCallBack = itemCallBack;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_news_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articleList.get(position);
        Glide.with(layoutInflater.getContext()).load(article.getUrlToImage()).into(holder.newsImg);
        holder.newsTitle.setText(article.getTitle());
        if(itemCallBack == null){
            holder.btnSave.setVisibility(View.GONE);
        }
        else {
            holder.btnSave.setOnClickListener(view -> {
                itemCallBack.onItemClick(position);
                holder.btnSave.setClickable(false);
                holder.btnSave.setText(R.string.saved);
            });
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateList(List<Article> list){
        articleList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.newsImg)
        ImageView newsImg;
        @BindView(R.id.newsTitle)
        TextView newsTitle;
        @BindView(R.id.btnSave)
        Button btnSave;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(layoutInflater.getContext(), NewsFeed.class);
                intent.putExtra("newsUrl",articleList.get(getAdapterPosition()).getUrl());
                layoutInflater.getContext().startActivity(intent);
            });
        }
    }

    public interface ItemCallBack{
        public void onItemClick(int position);
    }
}
