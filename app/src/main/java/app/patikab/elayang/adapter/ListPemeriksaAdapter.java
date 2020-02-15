package app.patikab.elayang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import app.patikab.elayang.model.Konsep;
import app.patikab.elayang.util.Config;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListPemeriksaAdapter extends RecyclerView.Adapter<ListPemeriksaAdapter.ViewHolder> {

    Context context;
    IPemeriksaAdapter mIPemeriksaAdapter;
    List<Konsep> konseps = new ArrayList<>();

    public ListPemeriksaAdapter(Context context, IPemeriksaAdapter mIPemeriksaAdapter, List<Konsep> konseps) {
        this.context = context;
        this.mIPemeriksaAdapter = mIPemeriksaAdapter;
        this.konseps = konseps;
    }

    @NonNull
    @Override
    public ListPemeriksaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pemeriksa, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListPemeriksaAdapter.ViewHolder holder, int position) {
        final Konsep konsep = konseps.get(position);

        String index = position + 1 + ". ";
        String pemeriksa = index + konsep.getNama_pemeriksa();
        holder.tvPemeriksa.setText(pemeriksa);

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
        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIPemeriksaAdapter.onClick(konsep.getId(), data, holder.itemView);
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

    public interface IPemeriksaAdapter {
        void onClick(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewStatus)
        TextView tvStatus;
        @BindView(R.id.textViewPemeriksa)
        TextView tvPemeriksa;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
