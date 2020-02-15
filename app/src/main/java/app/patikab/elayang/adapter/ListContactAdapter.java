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
import app.patikab.elayang.model.User;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ViewHolder> {

    Context context;
    IUserAdapter mIUserAdapter;
    List<User> users = new ArrayList<>();

    int lastSelected;
    List<Integer> selectedIdx = new ArrayList<>();

    public ListContactAdapter(Context context, IUserAdapter mIUserAdapter, List<User> users) {
        this.context = context;
        this.mIUserAdapter = mIUserAdapter;
        this.users = users;
    }

    @NonNull
    @Override
    public ListContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListContactAdapter.ViewHolder holder, final int position) {
        final User user = users.get(position);

        holder.tvNama.setText(user.getNama_lengkap());

        boolean isSame = false;
        for (int i = 0; i < selectedIdx.size(); i++) {
            if (selectedIdx.get(i) == holder.getAdapterPosition()) {
                isSame = true;
                break;
            }
        }
        if (isSame) {
            holder.tvNama.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.tvNama.setTextColor(context.getResources().getColor(R.color.colorGrey));
            holder.ivCheck.setVisibility(View.GONE);
        }

//        if (lastSelected == holder.getAdapterPosition()) {
//            holder.tvNama.setTextColor(context.getResources().getColor(R.color.colorAccent));
//        } else {
//            holder.tvNama.setTextColor(context.getResources().getColor(R.color.colorGrey));
//        }

//        Glide.with(context)
//                .load(Config.BASE_URL_STORAGE + user.getFoto())
//                .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person))
//                .into(holder.ivProfile);

        final String data = new Gson().toJson(user);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelected = holder.getAdapterPosition();
                boolean isSame = false;
                int idx = 0;
                for (int i = 0; i < selectedIdx.size(); i++) {
                    if (selectedIdx.get(i) == holder.getAdapterPosition()) {
                        isSame = true;
                        idx = i;
                    }
                }
                if (isSame)
                    selectedIdx.remove(idx);
                else
                    selectedIdx.add(holder.getAdapterPosition());

                notifyDataSetChanged();
                mIUserAdapter.onClickContact(user.getNip(), data, holder.itemView);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface IUserAdapter {
        void onClickContact(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView ivProfile;
        @BindView(R.id.textViewNama)
        TextView tvNama;
        @BindView(R.id.imageViewCheck)
        ImageView ivCheck;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
