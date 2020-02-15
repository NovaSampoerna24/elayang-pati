package app.patikab.elayang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListDispositionAdapter extends RecyclerView.Adapter<ListDispositionAdapter.ViewHolder> {

    Context context;
    IDispositionAdapter mIDispositionAdapter;
    List<Disposition> dispositions = new ArrayList<>();

    int lastSelected;

    public ListDispositionAdapter(Context context, IDispositionAdapter mIDispositionAdapter, List<Disposition> dispositions) {
        this.context = context;
        this.mIDispositionAdapter = mIDispositionAdapter;
        this.dispositions = dispositions;
    }

    @NonNull
    @Override
    public ListDispositionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_disposisi, parent, false);
//        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mail_unread, parent, false);

        ViewHolder vh;
//        if (viewType == 0) {
        vh = new ViewHolder(v1);
//        } else {
//            vh = new ViewHolder(v2);
//        }

        return vh;
    }

//    @Override
//    public int getItemViewType(int position) {
//        final Disposition disposition = dispositions.get(position);
//        if (disposition.getTgl_baca().equals("null")) {
//            return 1;
//        }
//
//        return 0;
//    }

    @Override
    public void onBindViewHolder(@NonNull final ListDispositionAdapter.ViewHolder holder, final int position) {
        final Disposition disposition = dispositions.get(position);

        String tahun = disposition.getTgl_disposisi().substring(0, 4);
        String bulan = Config.convertMonth(disposition.getTgl_disposisi().substring(5, 7));
        String hari = disposition.getTgl_disposisi().substring(8, 10);
        String tgl = hari + " " + bulan + " " + tahun;

        holder.tvTanggal.setText(tgl);
        holder.tvStatus.setText("Sudah Dibaca");
        if (disposition.getTgl_baca().equals("null"))
            holder.tvStatus.setText("Belum Dibaca");

        holder.tvPenerima.setText(disposition.getNama_penerima());
        holder.tvPengirim.setText(disposition.getNama_pengirim());

        final String data = new Gson().toJson(disposition);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelected = holder.getAdapterPosition();
                notifyDataSetChanged();
                mIDispositionAdapter.onClickInstruksi(disposition.getId(), data, holder.itemView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dispositions.size();
    }

    public interface IDispositionAdapter {
        void onClickInstruksi(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTanggal)
        TextView tvTanggal;
        @BindView(R.id.textViewStatus)
        TextView tvStatus;
        @BindView(R.id.textViewPengirim)
        TextView tvPengirim;
        @BindView(R.id.textViewPenerima)
        TextView tvPenerima;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
