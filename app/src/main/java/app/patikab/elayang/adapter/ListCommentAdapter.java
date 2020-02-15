package app.patikab.elayang.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import app.patikab.elayang.model.Comment;
import app.patikab.elayang.util.Config;
import app.patikab.elayang.util.Pref;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListCommentAdapter extends RecyclerView.Adapter<ListCommentAdapter.ViewHolder> {

    Context context;
    ICommentAdapter mICommentAdapter;
    List<Comment> comments = new ArrayList<>();

    public ListCommentAdapter(Context context, ICommentAdapter mICommentAdapter, List<Comment> comments) {
        this.context = context;
        this.mICommentAdapter = mICommentAdapter;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ListCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);


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
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
//        final Comment comment = comments.get(position);
//        Log.i("nip.xml model", comment.getNip());

        final Comment comment = comments.get(position);
        SharedPreferences pref = context.getSharedPreferences(Pref.DATA_USER, 0);
        if (comment.getNip().equals(pref.getString(Pref.NIP, null))) {
            return 0;
        } else {
            return 1;
        }
//        return position % 2;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListCommentAdapter.ViewHolder holder, int position) {
        final Comment comment = comments.get(position);

        String tgl = comment.getTgl().substring(0, 16);
        tgl = tgl.replace("T", " ");
        tgl = tgl.replace("-", "/");

        holder.tvNama.setText(comment.getNama_lengkap());
        holder.tvKomen.setText(comment.getIsi_komentar());
        holder.tvTgl.setText(Config.defTanggalWaktu(comment.getTgl()));


        final String data = new Gson().toJson(comment);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mICommentAdapter.onClick(comment.getId(), data, holder.itemView);
//            }
//        });

//        Glide.with(context).load(Config.BASE_URL_STORAGE + Comment.)
//                .apply(RequestOptions.circleCropTransform()).into(ivProfile);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public interface ICommentAdapter {
        void onClick(String id, String data, View view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewNama)
        TextView tvNama;
        @BindView(R.id.textViewKomen)
        TextView tvKomen;
        @BindView(R.id.textViewTanggal)
        TextView tvTgl;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
