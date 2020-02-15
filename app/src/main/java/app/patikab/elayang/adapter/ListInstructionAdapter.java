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

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.R;
import app.patikab.elayang.model.Instruction;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListInstructionAdapter extends RecyclerView.Adapter<ListInstructionAdapter.ViewHolder> {

    Context context;
    IInstructionAdapter mIInstructionAdapter;
    List<Instruction> instructions = new ArrayList<>();

    int lastSelected;

    public ListInstructionAdapter(Context context, IInstructionAdapter mIInstructionAdapter, List<Instruction> instructions) {
        this.context = context;
        this.mIInstructionAdapter = mIInstructionAdapter;
        this.instructions = instructions;
    }

    @NonNull
    @Override
    public ListInstructionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_instruksi, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListInstructionAdapter.ViewHolder holder, final int position) {
        final Instruction instruction = instructions.get(position);

        holder.tvInstruksi.setText(instruction.getNama());

        if (lastSelected == holder.getAdapterPosition()) {
            holder.tvInstruksi.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.tvInstruksi.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }

        final String data = new Gson().toJson(instruction);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelected = holder.getAdapterPosition();
                notifyDataSetChanged();
                mIInstructionAdapter.onClickInstruksi(instruction.getId(), data, holder.itemView);
                if(instruction.getNama().equals("Lain - lain")){
                    Intent intent = new Intent("type-instruksi");
                        intent.putExtra("intruksi","Lain - lain");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public interface IInstructionAdapter {
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
