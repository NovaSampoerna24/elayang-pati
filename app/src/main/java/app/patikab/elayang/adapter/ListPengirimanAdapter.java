package app.patikab.elayang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.R;
import app.patikab.elayang.model.Pengiriman;
import app.patikab.elayang.util.Config;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListPengirimanAdapter extends RecyclerView.Adapter<ListPengirimanAdapter.ViewHolder> {

    Context context;
    IPengirimanAdapter mIPengirimanAdapter;
    List<Pengiriman> pengirimans = new ArrayList<>();

    public ListPengirimanAdapter(Context context, IPengirimanAdapter mIPengirimanAdapter, List<Pengiriman> pengirimans) {
        this.context = context;
        this.mIPengirimanAdapter = mIPengirimanAdapter;
        this.pengirimans = pengirimans;
    }

    @NonNull
    @Override
    public ListPengirimanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pengiriman, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListPengirimanAdapter.ViewHolder holder, final int position) {
        final Pengiriman pengiriman = pengirimans.get(position);

        holder.tvTanggal.setText(Config.defTanggal(pengiriman.getTgl_baca()));
        holder.tvStatus.setText("Sudah Dibaca");
        if (pengiriman.getTgl_baca().equals("null"))
            holder.tvStatus.setText("Belum Dibaca");

        if (!pengiriman.getTgl_terima().equals("null"))
            holder.tvStatus.setText("Terkirim");

        holder.tvInstansi.setText(pengiriman.getNama_instansi());


        holder.tvKurir.setText(pengiriman.getKurir().toUpperCase());
        if (pengiriman.getKurir().equals("null"))
            holder.tvKurir.setText("-");

        holder.tvResi.setText(pengiriman.getResi());
        if (pengiriman.getResi().equals("null"))
            holder.tvResi.setText("-");

        holder.btnLacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIPengirimanAdapter.onLacak(pengiriman.id);
            }
        });

        holder.btnIsiResi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIPengirimanAdapter.onIsiResi(pengiriman.id, pengiriman.kurir, pengiriman.resi);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pengirimans.size();
    }

    public interface IPengirimanAdapter {
        void onLacak(String id);

        void onIsiResi(String id, String kurir, String resi);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTanggal)
        TextView tvTanggal;
        @BindView(R.id.textViewStatus)
        TextView tvStatus;
        @BindView(R.id.textViewInstansi)
        TextView tvInstansi;
        @BindView(R.id.textViewResi)
        TextView tvResi;
        @BindView(R.id.textViewKurir)
        TextView tvKurir;
        @BindView(R.id.btnLacak)
        Button btnLacak;
        @BindView(R.id.btnIsiResi)
        Button btnIsiResi;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
