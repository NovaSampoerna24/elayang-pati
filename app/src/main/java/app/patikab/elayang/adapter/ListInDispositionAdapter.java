package app.patikab.elayang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import app.patikab.elayang.model.Disposition;
import app.patikab.elayang.util.Config;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListInDispositionAdapter extends RecyclerView.Adapter<ListInDispositionAdapter.ViewHolder> {

    Context context;
    IDispositionAdapter mIDispositionAdapter;
    List<Disposition> dispositions = new ArrayList<>();

    public ListInDispositionAdapter(Context context, IDispositionAdapter mIDispositionAdapter, List<Disposition> dispositions) {
        this.context = context;
        this.mIDispositionAdapter = mIDispositionAdapter;
        this.dispositions = dispositions;
    }

    @NonNull
    @Override
    public ListInDispositionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mail, parent, false);
        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mail_unread, parent, false);

        ViewHolder vh;
        if (viewType == 0) {
            vh = new ViewHolder(v1);
        } else {
            vh = new ViewHolder(v2);
        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        final Disposition disposition = dispositions.get(position);
        if (disposition.getTgl_baca().equals("null")) {
            return 1;
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListInDispositionAdapter.ViewHolder holder, int position) {
        final Disposition disposition = dispositions.get(position);

        holder.tvNo.setText(disposition.mail.getNomor_surat());

        holder.tvTgl.setText(Config.defTanggal(disposition.mail.getTgl_surat()));
//        if (disposition.getTgl_baca().equals("null")) {
//            setNotif(holder.cardHa, holder.tvBaca);
//        }

        holder.tvBaca.setText(Config.defStatus(disposition.getStatus()));
        if (disposition.getStatus().equals("0")) {
            holder.tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_yellow));
        } else if (disposition.getStatus().equals("1")) {
            holder.tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_blue));
        } else {
            holder.tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_red));
        }

        holder.tvPengirim.setText(disposition.mail.getNama_pengirim());
        holder.tvInstansi.setText(disposition.mail.getNama_instansi());

        holder.tvPerihal.setText(("Perihal : " + disposition.mail.getPerihal()));


        final String data = new Gson().toJson(disposition);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIDispositionAdapter.onClick(disposition.getId(), data, holder.itemView);
            }
        });

//        Glide.with(context).load(Config.BASE_URL_STORAGE + disposition.)
//                .apply(RequestOptions.circleCropTransform()).into(ivProfile);
    }

    private void setNotif(CardView view, TextView tvBaca) {
        view.setCardBackgroundColor(Color.parseColor("#eeeeee"));
//        tvBaca.setText("Belum Dibaca");
//        tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_red));
    }

    @Override
    public int getItemCount() {
        return dispositions.size();
    }

    public interface IDispositionAdapter {
        void onClick(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardNha)
        CardView cardHa;

        @BindView(R.id.textViewNo)
        TextView tvNo;

        @BindView(R.id.textViewTgl)
        TextView tvTgl;

        @BindView(R.id.textViewPengirim)
        TextView tvPengirim;

        @BindView(R.id.textViewInstansi)
        TextView tvInstansi;

        @BindView(R.id.textViewPerihal)
        TextView tvPerihal;

        @BindView(R.id.imageViewProfile)
        ImageView ivProfile;

        @BindView(R.id.textViewStatusBaca)
        TextView tvBaca;

        @BindView(R.id.view)
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
