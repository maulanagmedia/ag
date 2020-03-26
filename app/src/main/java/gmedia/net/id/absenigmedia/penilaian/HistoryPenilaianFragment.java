package gmedia.net.id.absenigmedia.penilaian;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.Volley.ApiVolley;
import gmedia.net.id.absenigmedia.utils.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPenilaianFragment extends Fragment {

    View view;
    TextView tvStartDate, tvEndDate;
    String start_date="",end_date="";
    int start =0, count=20;
    Button btnSearch, btnTambahPenilaian;
    RecyclerView rvHistoryPenilaian;
    List<HistoryPenilaianModel> historyPenilaianModel = new ArrayList<>();
    HistoryPenilaianAdapter adapter;
    private Calendar calendar;
    private int mYear, mMonth, mDay;

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
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        start_date= date;
        end_date= date;
        tvStartDate = view.findViewById(R.id.tv_startdate);
        tvStartDate.setText(start_date);
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int m = monthOfYear+1;
                                String bulan = String.valueOf(m);
                                if(String.valueOf(monthOfYear).length() == 1){
                                    bulan = "0"+m;
                                }

                                int d = dayOfMonth;
                                String tanggal = String.valueOf(d);
                                if(tanggal.length() == 1){
                                    tanggal = "0"+d;
                                }
                                start_date = year+"-"+bulan+"-"+tanggal;
                                tvStartDate.setText(start_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        tvEndDate = view.findViewById(R.id.tv_enddate);
        tvEndDate.setText(end_date);
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int m = monthOfYear+1;
                                String bulan = String.valueOf(m);
                                if(String.valueOf(monthOfYear).length() == 1){
                                    bulan = "0"+m;
                                }

                                int d = dayOfMonth;
                                String tanggal = String.valueOf(d);
                                if(tanggal.length() == 1){
                                    tanggal = "0"+d;
                                }
                                end_date = year+"-"+bulan+"-"+tanggal;
                                tvEndDate.setText(end_date);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnSearch = view.findViewById(R.id.btn_search);
        btnTambahPenilaian = view.findViewById(R.id.btn_penilaian);
        rvHistoryPenilaian = view.findViewById(R.id.rv_history_penilaian);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHistoryPenilaian.setLayoutManager(layoutManager);
        setupListHistoryPenilaian();
        setupListScrollListenerHistoryPenilaian();
        prepareDataHistoryPenilaian("");
    }

    @Override
    public void onResume() {
        super.onResume();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start =0;
                count =10;
                adapter.notifyDataSetChanged();
                prepareDataHistoryPenilaian("search");
            }
        });
    }
    private void prepareDataHistoryPenilaian(final String type) {
        if(start == 0){
            historyPenilaianModel.clear();
        }
        JSONObject params = new JSONObject();
        try {
            params.put("start_date",start_date);
            params.put("end_date",end_date);
            params.put("start",start);
            params.put("count",count);
        }catch (Exception e){
            e.printStackTrace();
        }

        new ApiVolley(getContext(), params, "POST", URL.urlHistoryPenilaian, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(">>>>>", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        JSONArray array = object.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject isi = array.getJSONObject(i);
                            historyPenilaianModel.add(new HistoryPenilaianModel(
                                    isi.getString("tgl"),
                                    isi.getString("total_kary")
                            ));
                        }

                        adapter.notifyDataSetChanged();
                    }else{
                        if(type.equals("search")){
                            historyPenilaianModel.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Terjadi kesalahan dalam memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String result) {
                Log.d(">>>>>", result);
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupListHistoryPenilaian() {
        adapter = new HistoryPenilaianAdapter(getContext(), historyPenilaianModel);
        rvHistoryPenilaian.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistoryPenilaian.setAdapter(adapter);
    }

    private void setupListScrollListenerHistoryPenilaian() {
        rvHistoryPenilaian.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (! recyclerView.canScrollVertically(1)) {
                    start += count;
                    prepareDataHistoryPenilaian("");
                }
            }
        });
    }
}
