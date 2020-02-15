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
import app.patikab.elayang.model.Konsep;
import app.patikab.elayang.util.Config;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListKonsepAdapter extends RecyclerView.Adapter<ListKonsepAdapter.ViewHolder> {

    Context context;
    IKonsepAdapter mIKonsepAdapter;
    List<Konsep> konseps = new ArrayList<>();

    public ListKonsepAdapter(Context context, IKonsepAdapter mIKonsepAdapter, List<Konsep> konseps) {
        this.context = context;
        this.mIKonsepAdapter = mIKonsepAdapter;
        this.konseps = konseps;
    }

    @NonNull
    @Override
    public ListKonsepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_konsep, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListKonsepAdapter.ViewHolder holder, int position) {
        final Konsep konsep = konseps.get(position);

        holder.tvNo.setText(konsep.getNomor_surat());
        holder.tvTgl.setText(Config.defTanggal(konsep.getTgl_surat()));

//        SharedPreferences preferences = context.getSharedPreferences(Pref.DATA_USER, 0);
//        if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_PIMPINAN)) {
//            if (konsep.getTgl_baca_pimpinan().equals("null")) {
//                setNotif(holder.cardHa, holder.tvBaca);
//            }
//        } else if (preferences.getString(Pref.LEVEL, "").equals(Pref.LEVEL_TATA_USAHA)) {
//            if (konsep.getTgl_baca_tata_usaha().equals("null")) {
//                setNotif(holder.cardHa, holder.tvBaca);
//            }
//        }

        holder.tvPembuat.setText(konsep.getNama_pembuat());
        holder.tvInstansi.setText(konsep.getJabatan_pembuat());
        holder.tvPerihal.setText(("Perihal : " + konsep.getPerihal()));

        holder.tvStatus.setText(Config.defStatusKonsep(konsep.getStatus_surat()));
        if (konsep.getStatus_surat().equals("0")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_chip_red);
        } else if (konsep.getStatus_surat().equals("1")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_chip_yellow);
        } else if (konsep.getStatus_surat().equals("2")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_chip_blue);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_chip_green);
        }

        final String data = new Gson().toJson(konsep);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIKonsepAdapter.onClick(konsep.getId(), data, holder.itemView);
            }
        });

//        Glide.with(context).load(Config.BASE_URL_STORAGE + konsep.)
//                .apply(RequestOptions.circleCropTransform()).into(ivProfile);
    }

    private void setNotif(CardView view, TextView tvBaca) {
        view.setCardBackgroundColor(Color.parseColor("#eeeeee"));
        tvBaca.setText("Belum Dibaca");
        tvBaca.setBackground(context.getResources().getDrawable(R.drawable.shape_chip_red));

    }

    @Override
    public int getItemCount() {
        return konseps.size();
    }

    public interface IKonsepAdapter {
        void onClick(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardNha)
        CardView cardHa;
        @BindView(R.id.textViewNo)
        TextView tvNo;
        @BindView(R.id.textViewStatus)
        TextView tvStatus;

        @BindView(R.id.textViewTgl)
        TextView tvTgl;
        @BindView(R.id.textViewPembuat)
        TextView tvPembuat;
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
