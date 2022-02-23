package com.example.socialx.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.socialx.Model.newsModel;
import com.example.socialx.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.ViewHolder> {

    private Context cont;
    private ArrayList<newsModel> model;

    public newsAdapter(Context cont, ArrayList<newsModel> model) {
        this.cont = cont;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v= LayoutInflater.from(cont).inflate(R.layout.news_rc_layout,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        newsModel mod=model.get(position);
        holder.name.setText(mod.getName());
        holder.title.setText(mod.getTitle());
        holder.description.setText(mod.getDescription());

        String time=mod.getTime().toString();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date  date = inputFormat.parse(time);
            String dateTime = (String) DateUtils.getRelativeTimeSpanString(date.getTime() ,
                    Calendar.getInstance().getTimeInMillis(),
                    DateUtils.MINUTE_IN_MILLIS);
            holder.hours_ago.setText(dateTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(mod.getUrlToImage()).into(holder.urlToImage);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,name,hours_ago;
        ImageView urlToImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.news_about);
            description=itemView.findViewById(R.id.news_description);
            name=itemView.findViewById(R.id.source_name);
            urlToImage=itemView.findViewById(R.id.news_image);
            hours_ago=itemView.findViewById(R.id.hours_ago);

        }
    }
}
