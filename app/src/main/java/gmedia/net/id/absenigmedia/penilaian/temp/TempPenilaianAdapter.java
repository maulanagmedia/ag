package gmedia.net.id.absenigmedia.penilaian.temp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.penilaian.HistoryPenilaianAdapter;

public class TempPenilaianAdapter extends RecyclerView.Adapter<TempPenilaianAdapter.ViewHolder> {
    Context context;
    List<TempKaryawanModel> model;

    public TempPenilaianAdapter(Context context, List<TempKaryawanModel> model){
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public TempPenilaianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_temp_karyawan, parent, false);
        return new TempPenilaianAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempPenilaianAdapter.ViewHolder holder, int position) {
        TempKaryawanModel karyawanModel = model.get(position);
        holder.tvNama.setText(karyawanModel.getNama());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
        }
    }
}
