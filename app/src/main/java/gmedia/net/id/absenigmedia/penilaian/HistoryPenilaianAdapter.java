package gmedia.net.id.absenigmedia.penilaian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import gmedia.net.id.absenigmedia.MainActivity;
import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.karyawan.DetailKaryawanActivity;
import gmedia.net.id.absenigmedia.karyawan.KaryawanAdapter;
import gmedia.net.id.absenigmedia.karyawan.KaryawanModel;

public class HistoryPenilaianAdapter extends RecyclerView.Adapter<HistoryPenilaianAdapter.ViewHolder> {
    Context context;
    List<HistoryPenilaianModel> penilaianModels;
    public HistoryPenilaianAdapter(Context context, List<HistoryPenilaianModel> penilaianModels){
        this.penilaianModels =penilaianModels;
        this.context =context;
    }
    @NonNull
    @Override
    public HistoryPenilaianAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_penilaian, parent, false);
        return new HistoryPenilaianAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryPenilaianModel item = penilaianModels.get(position);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d L yyyy", Locale.getDefault());
//        try {
//            Date dateHistory = dateFormat.parse(item.getTanggal());
//            String date = dateFormat.format(dateHistory);
//            holder.tvTanggal.setText(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        holder.tvTanggal.setText(item.getTanggal());
        holder.tvTotal.setText(item.getTotal()+" karyawan yang dinilai");
    }


    @Override
    public int getItemCount() {
        return penilaianModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTanggal, tvTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }
}
