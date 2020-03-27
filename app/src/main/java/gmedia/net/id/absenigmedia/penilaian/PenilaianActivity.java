package gmedia.net.id.absenigmedia.penilaian;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.Volley.ApiVolley;
import gmedia.net.id.absenigmedia.karyawan.KaryawanModel;
import gmedia.net.id.absenigmedia.penilaian.temp.TempPenilaianActivity;
import gmedia.net.id.absenigmedia.utils.URL;

public class PenilaianActivity extends AppCompatActivity {

    TextView tvNama;
    EditText edtQuality, edtSkill, edtRelasi, edtKelebihan, edtKekurangan, edtSaran;
    public static  final String KARYAWAN_ITEM ="karyawan_item";
    private Gson gson = new Gson();
    Button btnSimpan;
    KaryawanModel item;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penilaian);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(gmedia.net.id.absenigmedia.R.color.statusbar));
        }
        initUi();
    }

    private void initUi(){
        String kary_item = getIntent().getStringExtra(KARYAWAN_ITEM);
        item = gson.fromJson(kary_item, KaryawanModel.class);
        tvNama = findViewById(R.id.tv_nama_karyawan);
        tvNama.setText(item.getNama());
        edtQuality = findViewById(R.id.edt_quality);
        edtSkill = findViewById(R.id.edt_skill);
        edtRelasi = findViewById(R.id.edt_relasi);
        edtKelebihan = findViewById(R.id.edt_kelebihan);
        edtKekurangan = findViewById(R.id.edt_kekurangan);
        edtSaran = findViewById(R.id.edt_saran);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasiForm();
                if(validasiForm()){
                    dialog = new Dialog(PenilaianActivity.this);
                    dialog.setContentView(R.layout.popup_sure);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    Button btnYa = dialog.findViewById(R.id.btnYa);
                    Button btnTidak = dialog.findViewById(R.id.btnTidak);
                    RelativeLayout rlCancel = dialog.findViewById(R.id.cancelPopupApproval);

                    btnYa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveTempPenilaian();
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
            }
        });
        RelativeLayout rlBack = findViewById(R.id.rl_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validasiForm(){
        if (edtQuality.length() == 0) {
            edtQuality.setError(Html
                    .fromHtml("<font color='red'>Quality tidak boleh kosong</font>"));
            edtQuality.requestFocus();
            return false;
        }

        if(edtSkill.length() == 0){
            edtSkill.setError(Html
                    .fromHtml("<font color='red'>Skill tidak boleh kosong</font>"));
            edtSkill.requestFocus();
            return false;
        }

        if(edtRelasi.length() == 0){
            edtRelasi.setError(Html
                    .fromHtml("<font color='red'>Relationship tidak boleh kosong</font>"));
            edtRelasi.requestFocus();
            return false;
        }

        if(edtKelebihan.length()==0){
            edtKelebihan.setError(Html
                    .fromHtml("<font color='red'>Kelebihan tidak boleh kosong</font>"));
            edtKelebihan.requestFocus();
            return false;
        }
        if(edtKekurangan.length()==0){
            edtKekurangan.setError(Html
                    .fromHtml("<font color='red'>Kekurangan tidak boleh kosong</font>"));
            edtKekurangan.requestFocus();
            return false;
        }
        if(edtSaran.length()==0){
            edtSaran.setError(Html
                    .fromHtml("<font color='red'>Saran tidak boleh kosong</font>"));
            edtSaran.requestFocus();
            return false;
        }
        return true;
    }

    private void saveTempPenilaian(){

        JSONObject params = new JSONObject();
        try {
            params.put("nip_kary",item.getNip());
            params.put("quality",edtQuality.getText().toString());
            params.put("skill",edtSkill.getText().toString());
            params.put("relasi",edtRelasi.getText().toString());
            params.put("kelebihan",edtKelebihan.getText().toString());
            params.put("kekurangan",edtKekurangan.getText().toString());
            params.put("saran",edtSaran.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(">>>>", String.valueOf(params));
        new ApiVolley(PenilaianActivity.this, params, "POST", URL.urlPenilaianKaryawan, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(">>>>",result);
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d(">>>>>",result);
                    String status = object.getJSONObject("metadata").getString("status");
                    String message = object.getJSONObject("metadata").getString("message");
                    if (status.equals("1")) {
                        Toast.makeText(PenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(PenilaianActivity.this, message, Toast.LENGTH_SHORT).show();
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
}