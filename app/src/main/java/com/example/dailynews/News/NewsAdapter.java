package com.example.dailynews.News;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dailynews.FullNews;
import com.example.dailynews.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    Activity context;
    ArrayList<NewsModel> newsModel;
    public NewsAdapter(Activity context, ArrayList<NewsModel> newsModel) {
        this.context = context;
        this.newsModel = newsModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //String headline = newsModel.get(position).getHeadLine().substring(0,newsModel.get(position).getHeadLine().lastIndexOf("-"));
        holder.headline.setText(newsModel.get(position).getHeadLine());
        holder.author.setText(newsModel.get(position).getSource().getName());
        holder.published.setText(newsModel.get(position).getPublished());
        Glide.with(context).load(newsModel.get(position).getImageUrl()).into(holder.urlImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullNews.class);
                intent.putExtra("link",newsModel.get(position).getNewsUrl());
                context.startActivity(intent);
            }
        });



        try {
            URL url = new URL(newsModel.get(position).getNewsUrl());
            String baseUrl = url.getProtocol() + "://" + url.getHost();

            String finalUrl = "https://logo.clearbit.com/" +baseUrl+ "?size=" + 500 + "&format=png";
            Glide.with(context).load(finalUrl).into(holder.newsLogo);
        } catch (MalformedURLException e) {
            // do something
        }


    }

    @Override
    public int getItemCount() {
        return newsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView headline, author, published;
        ImageView newsLogo, urlImage;
        CardView root;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headline = itemView.findViewById(R.id.headline);
            author = itemView.findViewById(R.id.author);
            published = itemView.findViewById(R.id.published);
            urlImage = itemView.findViewById(R.id.urlImage);
            newsLogo = itemView.findViewById(R.id.newsLogo);
            root = itemView.findViewById(R.id.root);
        }
    }
}
