package gmedia.net.id.absenigmedia.penilaian;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import gmedia.net.id.absenigmedia.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPenilaianFragment extends Fragment {

    View view;
    TextView tvStartData, tvEndDate;
    String start_date="",end_date="";
    int start =0, count=20;
    Button btnSearch, btnTambahPenilaian;
    RecyclerView rvHistoryPenilaian;

    public HistoryPenilaianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history_penilaian, container, false);
        initUi();
        return view;
    }

    private void initUi(){
        tvStartData = view.findViewById(R.id.tv_startdate);
        tvEndDate = view.findViewById(R.id.tv_enddate);
        btnSearch = view.findViewById(R.id.btn_search);
        btnTambahPenilaian = view.findViewById(R.id.btn_penilaian);
        rvHistoryPenilaian = view.findViewById(R.id.rv_history_penilaian);
    }

}
