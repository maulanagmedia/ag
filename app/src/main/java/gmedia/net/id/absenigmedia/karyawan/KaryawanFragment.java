package gmedia.net.id.absenigmedia.karyawan;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.absenigmedia.HideKeyboard;
import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.utils.URL;
import gmedia.net.id.absenigmedia.Volley.ApiVolley;

/**
 * A simple {@link Fragment} subclass.
 */
public class KaryawanFragment extends Fragment {

    View view;
    EditText edtSearch;
    RecyclerView rvKaryawan;
    String keyword="";
    int start =0, count=10;
    List<KaryawanModel> karyawanModels = new ArrayList<>();
    KaryawanAdapter karyawanAdapter;

    public KaryawanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_karyawan, container, false);
        initUi();
        prepareDataKaryawan(keyword);
        return view;
    }

    private void initUi(){
        LinearLayout llKaryawan = view.findViewById(R.id.ll_karyawan);
        llKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideKeyboard.hideSoftKeyboard(getActivity());

            }
        });
        edtSearch = view.findViewById(R.id.edt_search);
        rvKaryawan = view.findViewById(R.id.rv_karyawan);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvKaryawan.setLayoutManager(layoutManager);
        setupListKaryawan();
        setupListScrollListenerKaryawan();
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    start =0;
                    count =10;
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
                    keyword = edtSearch.getText().toString();
                    prepareDataKaryawan("search");
                    return true;
                }
                return false;
            }
        });
    }

    private void prepareDataKaryawan(final String type){
        // search / scroll
        if(start == 0){
            karyawanModels.clear();
        }
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("keyword", keyword);
            jBody.put("start",start);
            jBody.put("count",count);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(getContext(), jBody, "POST", URL.urlKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {

                    JSONObject response = new JSONObject(result);
                    int status = response.getJSONObject("metadata").getInt("status");

                    if(status == 1){
                        JSONArray res = response.getJSONArray("response");
                        Log.d(">>>>>",String.valueOf(res));
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject isi = res.getJSONObject(i);
                            karyawanModels.add(new KaryawanModel(
                                    isi.getString("nip"),

                                    isi.getString("nama"),

                                    isi.getString("alamat"),

                                    isi.getString("telp"),

                                    isi.getString("email"),

                                    isi.getString("divisi"),

                                    isi.getString("posisi"),

                                    isi.getString("foto_profil")
                            ));
                        }
                        karyawanAdapter.notifyDataSetChanged();
                    }else{
                        if(type.equals("search")){
                            karyawanModels.clear();
                            karyawanAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Terjadi kesalahan saat mengambil data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getContext(), "Terjadi kesalahan",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListKaryawan() {
        karyawanAdapter = new KaryawanAdapter(getContext(), karyawanModels);
        rvKaryawan.setLayoutManager(new LinearLayoutManager(getContext()));
        rvKaryawan.setAdapter(karyawanAdapter);
    }

    private void setupListScrollListenerKaryawan() {
        rvKaryawan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (! recyclerView.canScrollVertically(1)) {
                    start += count;
                    prepareDataKaryawan("scroll");
                }
            }
        });
    }

}
