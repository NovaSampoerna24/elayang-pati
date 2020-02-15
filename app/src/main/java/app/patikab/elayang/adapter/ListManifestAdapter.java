package app.patikab.elayang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import app.patikab.elayang.R;
import app.patikab.elayang.model.Manifest;

/**
 * Created by paymin on 23/08/2018.
 */

public class ListManifestAdapter extends RecyclerView.Adapter<ListManifestAdapter.ViewHolder> {

    Context context;
    List<Manifest> manifests = new ArrayList<>();

    public ListManifestAdapter(Context context, List<Manifest> manifests) {
        this.context = context;
        this.manifests = manifests;
    }

    @NonNull
    @Override
    public ListManifestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_manifest, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListManifestAdapter.ViewHolder holder, int position) {
        final Manifest manifest = manifests.get(position);
        holder.tvDeskripsi.setText(manifest.getManifest_description());
        holder.tvTgl.setText(manifest.getManifest_date());
        holder.tvJam.setText(manifest.getManifest_time());
    }

    @Override
    public int getItemCount() {
        return manifests.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewDeskripsi)
        TextView tvDeskripsi;
        @BindView(R.id.textViewTgl)
        TextView tvTgl;
        @BindView(R.id.textViewJam)
        TextView tvJam;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
