package gmedia.net.id.absenigmedia.penilaian.temp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.absenigmedia.MainActivity;
import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.SessionManager;
import gmedia.net.id.absenigmedia.Volley.ApiVolley;
import gmedia.net.id.absenigmedia.karyawan.DetailKaryawanActivity;
import gmedia.net.id.absenigmedia.karyawan.KaryawanAdapter;
import gmedia.net.id.absenigmedia.karyawan.KaryawanModel;
import gmedia.net.id.absenigmedia.penilaian.PenilaianActivity;
import gmedia.net.id.absenigmedia.utils.URL;

public class TempPenilaianActivity extends AppCompatActivity {

    RelativeLayout rvBack;
    RecyclerView rvTempKaryawan;
    Button btnTambaPenilaian, btnBatal, btnSimpan;
    Spinner spKompeten, spTidakKompeten;
    EditText edtAlasanKompeten, edtAlasanTidakKompeten, edtSuka, edtDuka;
    private List<TempKaryawanModel> tempKaryawanModel = new ArrayList<>();
    private List<KaryawanModel> karyawanModels = new ArrayList<>();
    private List<TempKaryawanModel> tempKaryawanModelKompeten = new ArrayList<>();
    private List<TempKaryawanModel> tempKaryawanModelTidakKompeten = new ArrayList<>();
    private List<String> names_karyawan_kompeten = new ArrayList<>();
    private List<String> names_karyawan_tidak_kompeten = new ArrayList<>();
    private List<Integer> idx = new ArrayList<>();

    String nip_kary_kompeten, nip_kary_tidak_kompeten, keyword_karyawan;
    private int start = 0,count=20;
    public static Dialog dgKaryawan;
    RecyclerView rvTempKaryawanDialog;
    TempPenilaianAdapter tempPenilaianAdapter;
    TempPenilaianAdapter adapter;
    KaryawanAdapter karyawanAdapter;
    Dialog dialog;
    LinearLayout llNotFound;
    Dialog dialogKonfirmasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_penilaian);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(gmedia.net.id.absenigmedia.R.color.statusbar));
        }
        initUi();
        llNotFound.setVisibility(View.GONE);
        rvTempKaryawan.setVisibility(View.GONE);
    }

    private void initUi(){
        rvBack = findViewById(R.id.back);
        rvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rvTempKaryawan = findViewById(R.id.rv_temp_karyawan);
        btnTambaPenilaian = findViewById(R.id.btn_tambah_penilaian);
        btnTambaPenilaian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKaryawan();
            }
        });

        spKompeten = (Spinner) findViewById(R.id.sp_paling_kompeten);
        spKompeten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TempKaryawanModel twoItemModel = tempKaryawanModelKompeten.get(position);
                nip_kary_kompeten = twoItemModel.getNip();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtAlasanKompeten = findViewById(R.id.edt_alasan_kompeten);
        spTidakKompeten = findViewById(R.id.sp_tidak_kompeten);
//        listKaryawanTemp(spTidakKompeten);
        spTidakKompeten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TempKaryawanModel twoItemModel = tempKaryawanModelTidakKompeten.get(position);
                nip_kary_tidak_kompeten = twoItemModel.getNip();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtAlasanTidakKompeten = findViewById(R.id.edt_alasan_tidak_kompeten);
        edtSuka = findViewById(R.id.edt_suka);
        edtDuka = findViewById(R.id.edt_duka);
        btnBatal = findViewById(R.id.btn_batal);
        btnSimpan = findViewById(R.id.btn_simpan);
        llNotFound = findViewById(R.id.ll_not_found);
//        llNotFound.setVisibility(View.GONE);
//        rvTempKaryawan.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTempKaryawan();
        setupListTempKaryawan();
        listKaryawanTempKompeten();
        listKaryawanTempTidakKompeten();
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiSave();
                if(validasiSave()){
                    dialogKonfirmasi = new Dialog(TempPenilaianActivity.this);
                    dialogKonfirmasi.setContentView(R.layout.popup_sure);
                    dialogKonfirmasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogKonfirmasi.setCanceledOnTouchOutside(false);
                    Button btnYa = dialogKonfirmasi.findViewById(R.id.btnYa);
                    Button btnTidak = dialogKonfirmasi.findViewById(R.id.btnTidak);
                    RelativeLayout rlCancel = dialogKonfirmasi.findViewById(R.id.cancelPopupApproval);

                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            savePenilaian();
                        }
                    });

                    btnTidak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogKonfirmasi.dismiss();
                        }
                    });

                    rlCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogKonfirmasi.dismiss();
                        }
                    });
                    dialogKonfirmasi.show();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(TempPenilaianActivity.this);
                dialog.setContentView(R.layout.popup_batal);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                Button btnYa = dialog.findViewById(R.id.btnYa);
                Button btnTidak = dialog.findViewById(R.id.btnTidak);
                RelativeLayout rlCancel = dialog.findViewById(R.id.cancelPopupApproval);

                btnYa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        batalPenilaian();
                    }
                });

                btnTidak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                rlCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private boolean validasiSave(){
        if(spKompeten == null && spKompeten.getSelectedItem() == null){
            TextView errorText = (TextView)spKompeten.getSelectedView();
            errorText.setError(Html
                    .fromHtml("<font color='red'>Pilih karyawan yang kompeten</font>"));
            spKompeten.requestFocus();
            return false;
        }
        if(edtAlasanKompeten.length() == 0){
            edtAlasanKompeten.setError(Html
                    .fromHtml("<font color='red'>Alasan karyawan kompeten tidak boleh kosong</font>"));
            edtAlasanKompeten.requestFocus();
            return false;
        }
        if(spTidakKompeten == null && spTidakKompeten.getSelectedItem() == null){
            TextView errorText = (TextView)spTidakKompeten.getSelectedView();
            errorText.setError(Html
                    .fromHtml("<font color='red'>Pilih karyawan yang tidak kompeten</font>"));
            spTidakKompeten.requestFocus();
            return false;
        }

        if(edtAlasanTidakKompeten.length() == 0){
            edtAlasanTidakKompeten.setError(Html
                    .fromHtml("<font color='red'>Alasan karyawan tidak kompeten tidak boleh kosong</font>"));
            edtAlasanTidakKompeten.requestFocus();
            return false;

        }

        if(edtSuka.length() == 0){
            edtSuka.setError(Html
                    .fromHtml("<font color='red'>Suka tidak boleh kosong</font>"));
            edtSuka.requestFocus();
            return false;

        }

        if(edtDuka.length() == 0){
            edtDuka.setError(Html
                    .fromHtml("<font color='red'>Duka tidak boleh kosong</font>"));
            edtDuka.requestFocus();
            return false;

        }
        return true;
    }

    private void savePenilaian(){
        JSONObject params = new JSONObject();
        try {
            params.put("id",new JSONArray(idx));
            params.put("kompeten",nip_kary_kompeten);
            params.put("alasan_kompeten",edtAlasanKompeten.getText().toString());
            params.put("tidak_kompeten",nip_kary_tidak_kompeten);
            params.put("alasan_tidak_kompeten",edtAlasanTidakKompeten.getText().toString());
            params.put("suka",edtSuka.getText().toString());
            params.put("duka",edtDuka.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(">>>>", String.valueOf(params));
        new ApiVolley(TempPenilaianActivity.this, params, "POST", URL.urlSavePenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(">>>>",result);
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d(">>>>>",result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(TempPenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else{
                        Toast.makeText(TempPenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void listKaryawanTempKompeten() {

        tempKaryawanModelKompeten = new ArrayList<>();
        names_karyawan_kompeten = new ArrayList<>();
        new ApiVolley(TempPenilaianActivity.this, new JSONObject(), "GET", URL.urlTempPenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        JSONArray array = object.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            TempKaryawanModel model =new TempKaryawanModel();
                            JSONObject isi = array.getJSONObject(i);
                            model.setId(isi.getString("id"));
                            model.setNama(isi.getString("nama"));
                            model.setNip(isi.getString("nip_kary"));

                            tempKaryawanModelKompeten.add(model);
                        }
                        for (int i = 0; i < tempKaryawanModelKompeten.size(); i++){
                            names_karyawan_kompeten.add(tempKaryawanModelKompeten.get(i).getNama().toString());
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TempPenilaianActivity.this, R.layout.layout_simple_list, names_karyawan_kompeten);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.layout_simple_list); // The drop down view
                        spKompeten.setAdapter(spinnerArrayAdapter);
//                        spKompeten.setAdapter(new ArrayAdapter<>(TempPenilaianActivity.this, R.layout.layout_simple_list, tempKaryawanModel));
                    } else {
                        spTidakKompeten.setAdapter(null);
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void listKaryawanTempTidakKompeten() {
        names_karyawan_tidak_kompeten= new ArrayList<>();
        tempKaryawanModelTidakKompeten = new ArrayList<>();
        new ApiVolley(TempPenilaianActivity.this, new JSONObject(), "GET", URL.urlTempPenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        JSONArray array = object.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            TempKaryawanModel model =new TempKaryawanModel();
                            JSONObject isi = array.getJSONObject(i);
                            model.setId(isi.getString("id"));
                            model.setNama(isi.getString("nama"));
                            model.setNip(isi.getString("nip_kary"));

                            tempKaryawanModelTidakKompeten.add(model);
                        }
                        for (int i = 0; i < tempKaryawanModelTidakKompeten.size(); i++){
                            names_karyawan_tidak_kompeten.add(tempKaryawanModelTidakKompeten.get(i).getNama().toString());
                        }

                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TempPenilaianActivity.this, R.layout.layout_simple_list, names_karyawan_tidak_kompeten);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.layout_simple_list); // The drop down view
                        spTidakKompeten.setAdapter(spinnerArrayAdapter);
                    } else {
                        spTidakKompeten.setAdapter(null);
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTempKaryawan(){

        tempKaryawanModel = new ArrayList<>();
        JSONObject params = new JSONObject();

        new ApiVolley(TempPenilaianActivity.this, params, "GET", URL.urlTempPenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d(">>>>>",result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        rvTempKaryawan.setVisibility(View.VISIBLE);
                        llNotFound.setVisibility(View.GONE);
                        JSONArray arr = object.getJSONArray("response");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject isi = arr.getJSONObject(i);
                            tempKaryawanModel.add(new TempKaryawanModel(
                                    isi.getString("id"),
                                    isi.getString("nip_kary"),
                                    isi.getString("nama")
                            ));

                            idx.add(isi.getInt("id"));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        llNotFound.setVisibility(View.VISIBLE);
                        rvTempKaryawan.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setupListTempKaryawan() {
        adapter = new TempPenilaianAdapter(TempPenilaianActivity.this, tempKaryawanModel,"temp");
        rvTempKaryawan.setLayoutManager(new LinearLayoutManager(TempPenilaianActivity.this));
        rvTempKaryawan.setAdapter(adapter);
    }

    private void showDialogKaryawan(){
        karyawanModels.clear();
        keyword_karyawan="";
        dgKaryawan= new Dialog(TempPenilaianActivity.this);
        dgKaryawan.setContentView(R.layout.popup_karyawan);
        dgKaryawan.setCancelable(false);
        RelativeLayout rvCancel = dgKaryawan.findViewById(R.id.rv_cancel);
        rvTempKaryawanDialog = dgKaryawan.findViewById(R.id.rv_karyawan);
        setupListKaryawan();
        setupListScrollListenerTempKaryawan();
        start =0;
        count =20;
        getKaryawan();

        final EditText edtSearchMerchant = dgKaryawan.findViewById(R.id.edt_search);
        edtSearchMerchant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(edtSearchMerchant.getWindowToken(), 0);
                    keyword_karyawan = edtSearchMerchant.getText().toString().trim();
                    start =0;
                    count =20;
                    karyawanModels.clear();
                    getKaryawan();
                    karyawanAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        edtSearchMerchant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edtSearchMerchant.getText().toString().length() == 0) {
                    keyword_karyawan="";
                    start=0;
                    count=20;
                    getKaryawan();
                }
            }
        });

        rvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dgKaryawan.dismiss();
            }
        });
        dgKaryawan.show();
    }

    private void getKaryawan(){
        SessionManager sessionManager = new SessionManager(TempPenilaianActivity.this);
        JSONObject params = new JSONObject();
        try {
            params.put("divisi",sessionManager.getDivisi());
            params.put("keyword",keyword_karyawan);
            params.put("start",start);
            params.put("count",count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiVolley(TempPenilaianActivity.this, params, "POST", URL.urlKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(">>>>>",result);
                try {
                    JSONObject object = new JSONObject(result);

                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        JSONArray arr = object.getJSONArray("response");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject isi = arr.getJSONObject(i);
                            karyawanModels.add(new KaryawanModel(
                                    isi.getString("nip"),
                                    isi.getString("nama")
                            ));
                        }
                        karyawanAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListKaryawan() {
        karyawanAdapter = new KaryawanAdapter(TempPenilaianActivity.this, karyawanModels,"penilaian");
        rvTempKaryawanDialog.setLayoutManager(new LinearLayoutManager(TempPenilaianActivity.this));
        rvTempKaryawanDialog.setAdapter(karyawanAdapter);
    }

    private void setupListScrollListenerTempKaryawan() {
        rvTempKaryawanDialog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (! recyclerView.canScrollVertically(1)) {
                    start += count;
                    getKaryawan();
                }
            }
        });
    }

    private void batalPenilaian(){

        JSONObject params = new JSONObject();
        try {
            params.put("id",new JSONArray(idx));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(">>>>", String.valueOf(params));
        new ApiVolley(TempPenilaianActivity.this, params, "POST", URL.urlBatalPenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(">>>>",result);
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d(">>>>>",result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(TempPenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else{
                        Toast.makeText(TempPenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.isPenilaian = false;
        Intent intent = new Intent(TempPenilaianActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

}
