package app.patikab.elayang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.patikab.elayang.R;
import app.patikab.elayang.model.Plt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ListPltAdapter extends RecyclerView.Adapter<ListPltAdapter.ViewHolder> {

    Context context;
    IpltAdapter mIInstructionAdapter;
    List<Plt> pltList = new ArrayList<>();

    int lastSelected;

    public ListPltAdapter(Context context, IpltAdapter mIInstructionAdapter, List<Plt> instructions) {
        this.context = context;
        this.mIInstructionAdapter = mIInstructionAdapter;
        this.pltList = instructions;
    }

    @NonNull
    @Override
    public ListPltAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_instruksi, parent, false);
        ListPltAdapter.ViewHolder vh = new ListPltAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListPltAdapter.ViewHolder holder, final int position) {
        final Plt plts = pltList.get(position);

        holder.tvInstruksi.setText(plts.getNmjabatan());

        if (lastSelected == holder.getAdapterPosition()) {
            holder.tvInstruksi.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.tvInstruksi.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }

        final String data = new Gson().toJson(plts);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelected = holder.getAdapterPosition();
                notifyDataSetChanged();
                mIInstructionAdapter.onClickInstruksi(plts.getTblpltjabatan_nip(), data, holder.itemView);

            }
        });

    }

    @Override
    public int getItemCount() {
        return pltList.size();
    }

    public interface IpltAdapter {
        void onClickInstruksi(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewInstruksi)
        TextView tvInstruksi;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
