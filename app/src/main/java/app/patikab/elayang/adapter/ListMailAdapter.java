package app.patikab.elayang.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import app.patikab.elayang.model.Mail;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Pref;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListMailAdapter extends RecyclerView.Adapter<ListMailAdapter.ViewHolder> {

    Context context;
    IMailAdapter mIMailAdapter;
    List<Mail> mails = new ArrayList<>();
    SharedPreferences preferences;

    public ListMailAdapter(Context context, IMailAdapter mIMailAdapter, List<Mail> mails) {
        this.context = context;
        this.mIMailAdapter = mIMailAdapter;
        this.mails = mails;

        preferences = context.getSharedPreferences(Pref.DATA_USER, 0);
    }

    @NonNull
    @Override
    public ListMailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        final Mail mail = mails.get(position);
        if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_PIMPINAN)) {
            if (mail.getTgl_baca_pimpinan().equals("null")) {
                return 1;
            }
        } else if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_TATA_USAHA)) {
            if (mail.getTgl_baca_tata_usaha().equals("null")) {
                return 1;
            }
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMailAdapter.ViewHolder holder, int position) {
        final Mail mail = mails.get(position);

        holder.tvNo.setText(mail.getNomor_surat());

        holder.tvTgl.setText(Config.defTanggal(mail.getTgl_surat()));


//        if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_PIMPINAN)) {
//            if (mail.getTgl_baca_pimpinan().equals("null")) {
//                setNotif(holder.cardHa, holder.tvBaca);
//            }
//        } else if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_TATA_USAHA)) {
//            if (mail.getTgl_baca_tata_usaha().equals("null")) {
//                setNotif(holder.cardHa, holder.tvBaca);
//            }
//        }

//        if (position % 3 == 0){
//            setNotif(holder.cardHa, holder.tvBaca);
//        }


        holder.tvPengirim.setText(mail.getNama_pengirim());
        holder.tvInstansi.setText(mail.getNama_instansi());
        holder.tvPerihal.setText(("Perihal : " + mail.getPerihal()));

        final String data = new Gson().toJson(mail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIMailAdapter.onClick(mail.getId(), data, holder.itemView);
            }
        });

//        Glide.with(context).load(Config.BASE_URL_STORAGE + mail.)
//                .apply(RequestOptions.circleCropTransform()).into(ivProfile);
    }

    private void setNotif(CardView view, TextView tvBaca) {
        view.setCardBackgroundColor(Color.parseColor("#eeeeee"));
        tvBaca.setText("Belum Dibaca");
        tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_red));

    }

    @Override
    public int getItemCount() {
        return mails.size();
    }

    public interface IMailAdapter {
        void onClick(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardNha)
        CardView cardHa;
        @BindView(R.id.textViewNo)
        TextView tvNo;
        @BindView(R.id.textViewStatusBaca)
        TextView tvBaca;

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

        @BindView(R.id.view)
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
