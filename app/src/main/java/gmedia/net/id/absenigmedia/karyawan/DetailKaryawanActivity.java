package gmedia.net.id.absenigmedia.karyawan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import gmedia.net.id.absenigmedia.HistoryIjin;
import gmedia.net.id.absenigmedia.MainActivity;
import gmedia.net.id.absenigmedia.R;
import gmedia.net.id.absenigmedia.utils.CircleTransform;

public class DetailKaryawanActivity extends AppCompatActivity {
    Intent intent;
    KaryawanModel model;
    private Gson gson = new Gson();
    public static final String KARYAWAN_ITEM = "karyawan_item";
    TextView tvKaryawan, tvNama, tvAlamat, tvTelp, tvEmail, tvDivisi;
    ImageView imgKaryawan;
    FloatingActionButton fabContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_karyawan);
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
        final KaryawanModel item = gson.fromJson(kary_item, KaryawanModel.class);
        imgKaryawan = findViewById(R.id.img_karyawan);
        tvKaryawan = findViewById(R.id.tv_karyawan);
        tvNama = findViewById(R.id.tv_nama);
        tvTelp = findViewById(R.id.tv_notelp);
        tvAlamat = findViewById(R.id.tv_alamat);
        tvEmail = findViewById(R.id.tv_email);
        tvDivisi = findViewById(R.id.tv_divisi);

        tvNama.setText(item.getNama());
        tvAlamat.setText(item.getAlamat());
        tvTelp.setText(item.getTelp());
        tvEmail.setText(item.getEmail());
        tvDivisi.setText(item.getDivisi());

        if(item.getFoto_profil() != null && !item.getFoto_profil().equals("")){
            tvKaryawan.setVisibility(View.GONE);
            Picasso.get()
                    .load(item.getFoto_profil())
                    .transform(new CircleTransform())
                    .into(imgKaryawan);
        }else{
            tvKaryawan.setVisibility(View.VISIBLE);
            tvKaryawan.setText(item.getNama().substring(0,1).toUpperCase());
        }

        fabContact = findViewById(R.id.fab_contact);
        fabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, item.getNama())
                        .putExtra(ContactsContract.Intents.Insert.PHONE, item.getTelp())
                        .putExtra(ContactsContract.Intents.Insert.EMAIL, item.getEmail());

                startActivityForResult(contactIntent, 1);
            }
        });
        RelativeLayout back = findViewById(gmedia.net.id.absenigmedia.R.id.backHistoryIjin);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.isKaryawan = false;
        Intent intent = new Intent(DetailKaryawanActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }
}
