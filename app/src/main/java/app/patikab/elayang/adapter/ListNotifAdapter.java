package app.patikab.elayang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.R;
import app.patikab.elayang.model.Notification;
import app.patikab.elayang.util.Config;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListNotifAdapter extends RecyclerView.Adapter<ListNotifAdapter.ViewHolder> {

    Context context;
    INotificationAdapter mINotificationAdapter;
    List<Notification> notifications = new ArrayList<>();

    public ListNotifAdapter(Context context, INotificationAdapter mINotificationAdapter, List<Notification> notifications) {
        this.context = context;
        this.mINotificationAdapter = mINotificationAdapter;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ListNotifAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_notif, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListNotifAdapter.ViewHolder holder, int position) {
        final Notification notification = notifications.get(position);

        holder.tvIsi.setText(notification.getIsi());
        holder.tvTgl.setText(Config.defTanggalWaktu(notification.getTanggal()));

        if (notification.getStatus().equals("0"))
            holder.ivNotif.setImageResource(R.drawable.ic_notifications_active_blue_24dp);
        else
            holder.ivNotif.setImageResource(R.drawable.ic_notifications_none_black_24dp);

        final String data = new Gson().toJson(notification);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mINotificationAdapter.onClick(notification.getId(), data, holder.itemView);
            }
        });

    }

    private void setNotif(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface INotificationAdapter {
        void onClick(String id, String data, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewNotif)
        ImageView ivNotif;
        @BindView(R.id.textViewIsi)
        TextView tvIsi;
        @BindView(R.id.textViewTanggal)
        TextView tvTgl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
