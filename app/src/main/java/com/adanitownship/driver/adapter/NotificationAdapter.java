package com.adanitownship.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adanitownship.driver.R;
import com.adanitownship.driver.networkResponse.NotificationResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyDataViewHolder> {
    List<NotificationResponse.Notification> notifications;
    Context context;
      ItemSingleNotifyClickListener itemSingleNotifyClickListener;

    public NotificationAdapter(List<NotificationResponse.Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    public interface ItemSingleNotifyClickListener {
        void onItemClickListener(NotificationResponse.Notification notification);
        void onDeleteItemClickListener(NotificationResponse.Notification notification);
    }
    public void setOnItemClickListener(ItemSingleNotifyClickListener itemClickListener){
        itemSingleNotifyClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_notification, parent, false);
        return new MyDataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDataViewHolder holder, int position) {

        holder.txt_Status.setText(notifications.get(position).getNotificationTile());
        holder.txt_content.setText(notifications.get(position).getNotificationDescription());
        holder.tv_time_date.setText(notifications.get(position).getNotifiaction_date());
        holder.img_del_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSingleNotifyClickListener.onDeleteItemClickListener(notifications.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSingleNotifyClickListener.onItemClickListener(notifications.get(position));
            }
        });
        Glide.with(context)
                .load(notifications.get(position).getNotificationLogo())
                .placeholder(R.drawable.ic_driver_logo)
                .load(holder.iv_notify);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyDataViewHolder extends RecyclerView.ViewHolder {

        TextView  txt_Status , txt_content , tv_time_date;
        ImageView iv_notify , img_del_notify;

        public MyDataViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_Status = itemView.findViewById(R.id.txt_Status);
            txt_content = itemView.findViewById(R.id.txt_content);
            tv_time_date = itemView.findViewById(R.id.tv_time_date);
            iv_notify = itemView.findViewById(R.id.iv_notify);
            img_del_notify = itemView.findViewById(R.id.img_del_notify);
        }
    }
}
