package gmedia.net.id.absenigmedia.karyawan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import gmedia.net.id.absenigmedia.MainActivity;
import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.penilaian.PenilaianActivity;

public class KaryawanAdapter extends RecyclerView.Adapter<KaryawanAdapter.ViewHolder> {
    Context context;
    List<KaryawanModel> karyawanModels;
    String type="";
    public KaryawanAdapter(Context context, List<KaryawanModel> karyawanModels, String type){
        this.karyawanModels =karyawanModels;
        this.context =context;
        this.type =type;
    }
    @NonNull
    @Override
    public KaryawanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_karyawan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KaryawanAdapter.ViewHolder holder, int position) {
        final KaryawanModel model = karyawanModels.get(position);
        if(type.equals("penilaian")){
            holder.tvNama.setText(model.getNama());
            holder.tvTelp.setVisibility(View.GONE);
            holder.tvDivisi.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.itemView.getContext(), PenilaianActivity.class);
                    intent.putExtra(PenilaianActivity.KARYAWAN_ITEM, new Gson().toJson(model));
                    context.startActivity(intent);
                }
            });
        }else{
            holder.tvNama.setText(model.getNama());
            holder.tvTelp.setText(model.getTelp());
            holder.tvDivisi.setText(model.getDivisi());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.isKaryawan = false;
                    Intent intent = new Intent(holder.itemView.getContext(), DetailKaryawanActivity.class);
                    intent.putExtra(DetailKaryawanActivity.KARYAWAN_ITEM, new Gson().toJson(model));
//                ((MainActivity) context).startActivityForResult(intent,4);
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return karyawanModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvTelp, tvDivisi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTelp = itemView.findViewById(R.id.tv_notelp);
            tvDivisi = itemView.findViewById(R.id.tv_divisi);
        }
    }
}
