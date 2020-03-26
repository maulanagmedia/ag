package gmedia.net.id.absenigmedia;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.ImageQuality;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import gmedia.net.id.absenigmedia.Adapter.ListAdapter;
import gmedia.net.id.absenigmedia.Model.CustomItem;
import gmedia.net.id.absenigmedia.Volley.ApiVolley;
import gmedia.net.id.absenigmedia.karyawan.KaryawanFragment;
import gmedia.net.id.absenigmedia.penilaian.HistoryPenilaianFragment;
import gmedia.net.id.absenigmedia.utils.CircleTransform;
import gmedia.net.id.absenigmedia.utils.ImageUtils;
import gmedia.net.id.absenigmedia.utils.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends RuntimePermissionsActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	private ArrayList<CustomItem> items;
	private ListView listView;
	private static ListAdapter adapter;
	public static boolean popup = true;
	public static boolean posisi = true;
	public static DrawerLayout drawer;
	public static TextView title;
	private EditText passLama, passBaru, rePassBaru;
	private EditText passLamaEditPin, passBaruEditPin, rePassBaruEditPin;
	private EditText inputPIN;
	private Boolean klikToVisiblePassLama = true;
	private Boolean klikToVisiblePassBaru = true;
	private Boolean klikToVisibleRePassBaru = true;
	private Boolean klikToVisibleReInputPIN = true;
	public static boolean isCuti = true;
	public static boolean isIjin = true;
	public static boolean isKaryawan =true;
	public static boolean isApproval = true;
	NavigationView navigationView;
	private int imageRequestCode = 100;
	private ImageUtils imageUtils = new ImageUtils();

	private Fragment fragment;
	public final Integer icon[] =
			{
					gmedia.net.id.absenigmedia.R.drawable.home, // 0
					R.drawable.icon_scan_log, // 1
					R.drawable.approved, // 2
					gmedia.net.id.absenigmedia.R.drawable.jadwal, // 3
					R.drawable.kary, // 4
					gmedia.net.id.absenigmedia.R.drawable.profile, // 5
					gmedia.net.id.absenigmedia.R.drawable.profile, // 6
					R.drawable.kary, // 7
					gmedia.net.id.absenigmedia.R.drawable.uang_makan, // 8
					gmedia.net.id.absenigmedia.R.drawable.uanglembur, // 9
					//R.drawable.icon_slip_gaji,
					gmedia.net.id.absenigmedia.R.drawable.potongan, // 10
					gmedia.net.id.absenigmedia.R.drawable.pengajuan_cuti, // 11
					gmedia.net.id.absenigmedia.R.drawable.ijin, // 12
					gmedia.net.id.absenigmedia.R.drawable.rekab, // 13
					gmedia.net.id.absenigmedia.R.drawable.rekab, // 14
					R.drawable.about, // 15
					gmedia.net.id.absenigmedia.R.drawable.logout // 16
			};
	public final String textIcon[] =
			{
					"Dashboard", // 0
					"Scan Log", // 1
					"Approval", // 2
					"Profile", // 3
					"Karyawan", // 4
					"Jadwal", // 5
					"Jadwal TS", // 6
					"Penilaian 360", // 7
					"Uang Makan", // 8
					"Uang Lembur", // 9
					//"Slip Gaji",
					"Potongan Gaji", // 10
					"Cuti", // 11
					"Ijin", // 12
					"Rekap Absensi", // 13
					"Rekapitulasi Absensi", // 14
					"About", // 15
					"Logout" // 16
			};
	private boolean doubleBackToExitPressedOnce = false;
	private WifiManager manager;
	private static final int REQUEST_PERMISSIONS = 20;
	private String version, latestVersion, link;
	public static int state = 0;
	CustomItem custom;
	public static boolean changelog = false;
	public static Boolean cuti = false;
	public static Boolean keluar_kantor = false;
	public static Boolean pulang_awal = false;
	public static Boolean onCuti = false;
	public static Boolean onKeluar_kantor = false;
	public static Boolean onPulang_awal = false;
	private String tipe="", onTipe="";

	private FusedLocationProviderClient mFusedLocationClient;
	private LocationCallback mLocationCallback;
	private LocationRequest mLocationRequest;
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
			UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	private LocationSettingsRequest mLocationSettingsRequest;
	private SettingsClient mSettingsClient;
	private static final int REQUEST_CHECK_SETTINGS = 0x1;
	private Boolean mRequestingLocationUpdates;
	private Location mCurrentLocation;
	private boolean refreshMode = false;
	private Location location;
	private double latitude, longitude;
	private boolean dialogActive = false;
	private SessionManager session;
	ImageView imgCamera;
	ImageView imgProfil;

	//    private CustomMapView mvMap;
//    private GoogleMap googleMap;
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialogActive = false;
		session = new SessionManager(getApplicationContext());

//		navigationView = findViewById(R.id.nav_view);
//		navigationView.setNavigationItemSelectedListener(this);
//		View view = navigationView.getHeaderView(0);
		imgCamera = findViewById(R.id.img_camera);
		imgProfil = findViewById(R.id.img_profile);

		imgCamera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Options options = Options.init()
						.setRequestCode(imageRequestCode)                                    //Request code for activity results
						.setCount(1)                                                         //Number of images to restict selection count
						.setFrontfacing(false)                                               //Front Facing camera on start
						.setImageQuality(ImageQuality.HIGH)                                  //Image Quality
						.setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
						.setPath("/absensigmedia/images");                                             //Custom Path For Image Storage

				Pix.start(MainActivity.this, options);
			}
		});

        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle.getString("About")!=null) {
//            String isi = (String) bundle.getString("About");
//            Toast.makeText(this,isi,Toast.LENGTH_LONG).show();
            fragment = new About();
            callFragment(fragment);
            title.setText("About");
            title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
            drawer.closeDrawer(GravityCompat.START);
            posisi = false;
        }*/
		manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_WIFI_STATE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
				!= PackageManager.PERMISSION_GRANTED) {

			super.requestAppPermissions(new
							String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE,
							Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS},
					gmedia.net.id.absenigmedia.R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
		}
		final NavigationView navigationView = (NavigationView) findViewById(gmedia.net.id.absenigmedia.R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(this.getResources().getColor(gmedia.net.id.absenigmedia.R.color.statusbar));
		}
		if (android.os.Build.VERSION.SDK_INT > 25) {
			statusCheck();
		}
		title = (TextView) findViewById(gmedia.net.id.absenigmedia.R.id.title);
		Toolbar toolbar = (Toolbar) findViewById(gmedia.net.id.absenigmedia.R.id.toolbar);
		RelativeLayout buttonDrawer = (RelativeLayout) findViewById(gmedia.net.id.absenigmedia.R.id.drawer_button);
		buttonDrawer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DrawerLayout drawer = (DrawerLayout) findViewById(gmedia.net.id.absenigmedia.R.id.drawer_layout);
				drawer.openDrawer(GravityCompat.START);
			}
		});

		final RelativeLayout buttonSettting = (RelativeLayout) findViewById(gmedia.net.id.absenigmedia.R.id.setting);
		buttonSettting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.popup_pilihan_setting);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

				LinearLayout layoutUtama = (LinearLayout) dialog.findViewById(R.id.layoutUtamaPopupPilihanSetting);
				layoutUtama.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				LinearLayout btnGantiPassword = (LinearLayout) dialog.findViewById(R.id.btnGantiPassword);
				btnGantiPassword.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						preparePopupGantiPassword();
					}
				});

				LinearLayout btnEditPin = (LinearLayout) dialog.findViewById(R.id.btnEditPin);
				btnEditPin.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						preparePopupEditPin();
//						preparePopupInputPin();
					}
				});

				dialog.show();
//                showPopupMenu(buttonSettting);
			}
		});
		listView = (ListView) findViewById(gmedia.net.id.absenigmedia.R.id.listmenu);
		ArrayList<CustomItem> items = isiArray();
		adapter = new ListAdapter(this, items);
		listView.setAdapter(adapter);
		drawer = (DrawerLayout) findViewById(gmedia.net.id.absenigmedia.R.id.drawer_layout);
		final DrawerLayout finalDrawer = drawer;
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						fragment = new DashboardBaru();
						callFragment(fragment);
						title.setText("Dashboard");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = true;
						break;
					case 1:
						fragment = new ScanLog();
						callFragment(fragment);
						title.setText("Scan Log");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 2:
						fragment = new Approval();
						callFragment(fragment);
						title.setText("Approval");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 3:
						fragment = new Profile();
						callFragment(fragment);
						title.setText("Profile");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 4:
						fragment = new KaryawanFragment();
						callFragment(fragment);
						title.setText("Karyawan");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 5:
						fragment = new Jadwal();
						callFragment(fragment);
						title.setText("Jadwal");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 6:
						fragment = new JadwalTS();
						callFragment(fragment);
						title.setText("Jadwal TS");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 7:
						fragment = new HistoryPenilaianFragment();
						callFragment(fragment);
						title.setText("Penilaian 360");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 8:
						fragment = new UangMakan();
						callFragment(fragment);
						title.setText("Uang Makan");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 9:
						fragment = new UangLembur();
						callFragment(fragment);
						title.setText("Uang Lembur");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					/*case 8:
						fragment = new SlipGaji();
						callFragment(fragment);
						title.setText("Slip Gaji");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;*/
					case 10:
						fragment = new Potongan();
						callFragment(fragment);
						title.setText("Potongan Gaji");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 11:
						fragment = new Cuti();
						callFragment(fragment);
						title.setText("Cuti");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 12:
						fragment = new Ijin();
						callFragment(fragment);
						title.setText("Ijin");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 13:
						fragment = new RekapAbsensi();
						callFragment(fragment);
						title.setText("Rekap Absensi");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 14:
						fragment = new RekapitulasiAbsensi();
						callFragment(fragment);
						title.setText("Rekapitulasi Absensi");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 15:
						fragment = new About();
						callFragment(fragment);
						title.setText("About");
						title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
						finalDrawer.closeDrawer(GravityCompat.START);
						posisi = false;
						break;
					case 16:
						SessionManager session = new SessionManager(getApplicationContext());
						session.logoutUser();
						break;
				}
                /*FrameLayout frameLayout = findViewById(R.id.mainLayout);
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View v = layoutInflater.inflate(R.layout.uang_makan,null,false);
                frameLayout.removeAllViews();
                frameLayout.addView(v);*/
			}
		});
//        listView.getItemAtPosition(2)
        /*if (savedInstanceState == null) {
            if (!isCuti) {
                fragment = new Cuti();
                callFragment(fragment);
                title.setText("Cuti");
                title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
                drawer.closeDrawer(GravityCompat.START);
                isCuti = true;
            } else {
                fragment = new DashboardBaru();
                callFragment(fragment);
                title.setText("Dashboard");
                title.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Helvetica-Bold.otf"));
            }
        }*/
		prepareBiodataDrawer();

//        checkVersion();
        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();*/
//        Toast.makeText(getApplicationContext(),imei,Toast.LENGTH_LONG).show();
		FirebaseApp.initializeApp(MainActivity.this);
		FirebaseMessaging.getInstance().subscribeToTopic("gmedia_news");
		String token = FirebaseInstanceId.getInstance().getToken();
		Log.d("token", token);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			Log.d("bundle11", bundle.getString("jenis", ""));
			Log.d("bundle12", bundle.getString("notif", ""));
			Log.d("bundle13", bundle.getString("tipe", ""));
			tipe = bundle.getString("tipe", "");
			onTipe = bundle.getString("approval", "");
//            isApproval = false;
			state = 1;
            /*if (approval != null) {
                if (approval.equals("isi")) {
                    fragment = new Approval();
                    callFragment(fragment);
                    title.setText("Approval");
                    title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
                    drawer.closeDrawer(GravityCompat.START);
                    posisi = false;
                }
            } else {
                fragment = new DashboardBaru();
                callFragment(fragment);
                title.setText("Dashboard");
                title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
                finalDrawer.closeDrawer(GravityCompat.START);
                posisi = true;
            }*/
		}
//		initState();

	}


	public void statusCheck() {
		final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			buildAlertMessageNoGps();
		}
	}

	private void buildAlertMessageNoGps() {
		if (!dialogActive) {
			dialogActive = true;
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Mohon Hidupkan Akses Lokasi (GPS) Anda.")
					.setCancelable(false)
					.setPositiveButton("Hidupkan", new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, final int id) {
							startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
					});
			final AlertDialog alert = builder.create();
			alert.show();

			alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialogInterface) {
					dialogActive = false;
				}
			});
		}
	}

	private void initState(){

		if (!isCuti) {
			fragment = new Cuti();
			callFragment(fragment);
			title.setText("Cuti");
			title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
			drawer.closeDrawer(GravityCompat.START);
			isCuti = true;
		} else if (!isIjin) {
			fragment = new Ijin();
			callFragment(fragment);
			title.setText("Ijin");
			title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
			drawer.closeDrawer(GravityCompat.START);
			isIjin = true;
		}else if(!isKaryawan){
			fragment = new KaryawanFragment();
			callFragment(fragment);
			title.setText("Karyawan");
			title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
			drawer.closeDrawer(GravityCompat.START);
			isKaryawan = true;
		} else if (changelog) {
			changelog = false;
			fragment = new About();
			callFragment(fragment);
			title.setText("About");
			title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
			drawer.closeDrawer(GravityCompat.START);
			posisi = false;
		} else if (state == 1) {
			state = 0;
			if (tipe.equals("cuti") || onTipe.equals("cuti")) {
				cuti = true;
				fragment = new Approval();
				callFragment(fragment);
				title.setText("Approval");
				title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
				drawer.closeDrawer(GravityCompat.START);
				posisi = false;
			} else if (tipe.equals("pulang_awal") || onTipe.equals("pulang_awal")) {
				pulang_awal = true;
				fragment = new Approval();
				callFragment(fragment);
				title.setText("Approval");
				title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
				drawer.closeDrawer(GravityCompat.START);
				posisi = false;
			} else if (tipe.equals("keluar_kantor") || onTipe.equals("keluar_kantor")) {
				keluar_kantor = true;
				fragment = new Approval();
				callFragment(fragment);
				title.setText("Approval");
				title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
				drawer.closeDrawer(GravityCompat.START);
				posisi = false;
			} else {
				fragment = new DashboardBaru();
				callFragment(fragment);
				title.setText("Dashboard");
				title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
				drawer.closeDrawer(GravityCompat.START);
				posisi = false;
			}
		}else {
			fragment = new DashboardBaru();
			callFragment(fragment);
			title.setText("Dashboard");
			title.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Helvetica-Bold.otf"));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		FirebaseApp.initializeApp(MainActivity.this);
		initState();
		prepareBiodataDrawer();
		checkVersion();

		if (android.os.Build.VERSION.SDK_INT > 25) {
			statusCheck();
		}

	}

	private void checkVersion() {

		PackageInfo pInfo = null;
		version = "";

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		version = pInfo.versionName;
//        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name) + " v "+ version);
		latestVersion = "";
		link = "";

		ApiVolley request = new ApiVolley(MainActivity.this, new JSONObject(), "GET", URL.urlUpVersion, "", "", 0, new ApiVolley.VolleyCallback() {

			@Override
			public void onSuccess(String result) {

				JSONObject responseAPI;
				try {
					responseAPI = new JSONObject(result);
					String status = responseAPI.getJSONObject("metadata").getString("status");

					if (status.equals("1")) {
						latestVersion = responseAPI.getJSONObject("response").getString("build_version");
						link = responseAPI.getJSONObject("response").getString("link_update");

						if (!version.trim().equals(latestVersion.trim()) && link.length() > 0) {

							final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setIcon(R.mipmap.ic_launcher)
									.setTitle("Update")
									.setMessage("Versi terbaru " + latestVersion + " telah tersedia, mohon download versi terbaru.")
									.setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
											startActivity(browserIntent);
										}
									})
									.setCancelable(false)
									.show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				Toast.makeText(getApplicationContext(), "Terjadi kesalahan check version", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void preparePopupEditPin() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.popup_edit_pin);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		final ImageView visiblePassLama = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visiblePassLama);
		final ImageView visiblePassBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visiblePassBaru);
		final ImageView visibleRePassBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visibleRePassBaru);
		passLamaEditPin = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.passLama);
		passBaruEditPin = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.passBaru);
		rePassBaruEditPin = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.reTypePassBaru);
		visiblePassLama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisiblePassLama) {
					visiblePassLama.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					passLamaEditPin.setInputType(InputType.TYPE_CLASS_NUMBER);
					klikToVisiblePassLama = false;
				} else {
					visiblePassLama.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					passLamaEditPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
					passLamaEditPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisiblePassLama = true;
				}
			}
		});
		visiblePassBaru.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisiblePassBaru) {
					visiblePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					passBaruEditPin.setInputType(InputType.TYPE_CLASS_NUMBER);
					klikToVisiblePassBaru = false;
				} else {
					visiblePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					passBaruEditPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
					passBaruEditPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisiblePassBaru = true;
				}
			}
		});
		visibleRePassBaru.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisibleRePassBaru) {
					visibleRePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					rePassBaruEditPin.setInputType(InputType.TYPE_CLASS_NUMBER);
					klikToVisibleRePassBaru = false;
				} else {
					visibleRePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					rePassBaruEditPin.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
					rePassBaruEditPin.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisibleRePassBaru = true;
				}
			}
		});
		RelativeLayout OK = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.tombolOKgantiPassword);
		OK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// validasi
				if (passLamaEditPin.getText().toString().isEmpty()) {
					passLamaEditPin.setError("Password lama harap diisi");
					passLamaEditPin.requestFocus();
					return;
				} else {
					passLamaEditPin.setError(null);
				}

				if (passBaruEditPin.getText().toString().isEmpty()) {
					passBaruEditPin.setError("Password baru harap diisi");
					passBaruEditPin.requestFocus();
					return;
				} else {
					passBaruEditPin.setError(null);
				}

				if (rePassBaruEditPin.getText().toString().isEmpty()) {
					rePassBaruEditPin.setError("Password baru ulang harap diisi");
					rePassBaruEditPin.requestFocus();
					return;
				} else {
					rePassBaruEditPin.setError(null);
				}
				if (!rePassBaruEditPin.getText().toString().equals(passBaruEditPin.getText().toString())) {
					rePassBaruEditPin.setError("Password ulang tidak sama");
					rePassBaruEditPin.requestFocus();
					return;
				} else {
					rePassBaruEditPin.setError(null);
				}
				prepareDataGantiPin();
				dialog.dismiss();
			}
		});
		RelativeLayout cancel = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.tombolcancelGantiPassword);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void prepareDataGantiPin() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(gmedia.net.id.absenigmedia.R.layout.loading);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final JSONObject jBody = new JSONObject();
		try {
			jBody.put("pin", passLamaEditPin.getText().toString());
			jBody.put("pin_baru", passBaruEditPin.getText().toString());
			jBody.put("confirm_pin", rePassBaruEditPin.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(MainActivity.this, jBody, "POST", URL.urlEditPIN, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String pesan = object.getJSONObject("metadata").getString("message");
					String status = object.getJSONObject("metadata").getString("status");
					if (status.equals("1")) {
//						session.createPIN(passLamaEditPin.getText().toString());
						Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void preparePopupGantiPassword() {
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(gmedia.net.id.absenigmedia.R.layout.popup_ganti_password);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		final ImageView visiblePassLama = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visiblePassLama);
		final ImageView visiblePassBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visiblePassBaru);
		final ImageView visibleRePassBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.visibleRePassBaru);
		passLama = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.passLama);
		passBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.passBaru);
		rePassBaru = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.reTypePassBaru);
		visiblePassLama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisiblePassLama) {
					visiblePassLama.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					passLama.setInputType(InputType.TYPE_CLASS_TEXT);
					klikToVisiblePassLama = false;
				} else {
					visiblePassLama.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					passLama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					passLama.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisiblePassLama = true;
				}
			}
		});
		visiblePassBaru.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisiblePassBaru) {
					visiblePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					passBaru.setInputType(InputType.TYPE_CLASS_TEXT);
					klikToVisiblePassBaru = false;
				} else {
					visiblePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					passBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					passBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisiblePassBaru = true;
				}
			}
		});
		visibleRePassBaru.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (klikToVisibleRePassBaru) {
					visibleRePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.visible));
					rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT);
					klikToVisibleRePassBaru = false;
				} else {
					visibleRePassBaru.setImageDrawable(getResources().getDrawable(gmedia.net.id.absenigmedia.R.drawable.invisible));
					rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					rePassBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
					klikToVisibleRePassBaru = true;
				}
			}
		});
		RelativeLayout OK = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.tombolOKgantiPassword);
		OK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// validasi
				if (passLama.getText().toString().isEmpty()) {
					passLama.setError("Password lama harap diisi");
					passLama.requestFocus();
					return;
				} else {
					passLama.setError(null);
				}

				if (passBaru.getText().toString().isEmpty()) {
					passBaru.setError("Password baru harap diisi");
					passBaru.requestFocus();
					return;
				} else {
					passBaru.setError(null);
				}

				if (rePassBaru.getText().toString().isEmpty()) {
					rePassBaru.setError("Password baru ulang harap diisi");
					rePassBaru.requestFocus();
					return;
				} else {
					rePassBaru.setError(null);
				}
				if (!rePassBaru.getText().toString().equals(passBaru.getText().toString())) {
					rePassBaru.setError("Password ulang tidak sama");
					rePassBaru.requestFocus();
					return;
				} else {
					rePassBaru.setError(null);
				}
				prepareDataGantiPassword();
				dialog.dismiss();
			}
		});
		RelativeLayout cancel = dialog.findViewById(gmedia.net.id.absenigmedia.R.id.tombolcancelGantiPassword);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
                /*if (popup) {
                    popupmenu.setVisibility(View.VISIBLE);
                    popup = false;
                } else {
                    popupmenu.setVisibility(View.GONE);
                    popup = true;
                }*/
	}

	private void prepareDataGantiPassword() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(gmedia.net.id.absenigmedia.R.layout.loading);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		final JSONObject jBody = new JSONObject();
		try {
			jBody.put("password_lama", passLama.getText());
			jBody.put("password_baru", passBaru.getText());
			jBody.put("re_password", rePassBaru.getText());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(this, jBody, "POST", URL.urlGantiPassword, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String pesan = object.getJSONObject("metadata").getString("message");
					String status = object.getJSONObject("metadata").getString("status");
					if (status.equals("1")) {
						Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});

	}

	private void prepareBiodataDrawer() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(gmedia.net.id.absenigmedia.R.layout.loading);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		ApiVolley request = new ApiVolley(this, new JSONObject(), "POST", URL.urlBiodata, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("1")) {
						JSONObject biodata = object.getJSONObject("response");
						Log.d(">>>>>", String.valueOf(biodata));
						TextView nama = findViewById(gmedia.net.id.absenigmedia.R.id.namaDrawer);
						nama.setText(biodata.getString("nama"));
						TextView nip = findViewById(gmedia.net.id.absenigmedia.R.id.nikDrawer);
						nip.setText(biodata.getString("nip"));
						String img_profil = biodata.getString("foto_profil");
						if(img_profil != null && !img_profil.equals("")){
							Picasso.get()
									.load(img_profil)
									.transform(new CircleTransform())
									.into(imgProfil);
						}
					} else {
						Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Toast.makeText(this, requestCode, Toast.LENGTH_SHORT).show();
		Log.d(">>>>>", String.valueOf(requestCode));
		if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
			ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

			if (returnValue.size() > 0) {

				File f = new File(returnValue.get(0));
				Bitmap b = new BitmapDrawable(MainActivity.this.getResources(), f.getAbsolutePath()).getBitmap();
				imageUtils.LoadCircleRealImage((ImageUtils.getImageUri(MainActivity.this, b)).toString(), imgProfil);

				saveFotoProfil(ImageUtils.convert(b));
			}
		}else if (resultCode == Activity.RESULT_OK && requestCode == 4) {
			tipe ="karyawan";
			fragment = new KaryawanFragment();
			callFragment(fragment);
			title.setText("Karyawan");
			title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
			posisi = false;
		}
	}

	private void saveFotoProfil(final String foto) {

		JSONObject jBody = new JSONObject();
		try {
			jBody.put("foto_profil", foto);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		new ApiVolley(this, jBody, "POST", URL.urlUpdateFotoProfil, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {

				try {

					JSONObject response = new JSONObject(result);
					Log.d(">>>>",result);
					int status = response.getJSONObject("metadata").getInt("status");
					String message = response.getJSONObject("metadata").getString("message");

					Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
					if(status == 1){
						prepareBiodataDrawer();
						Toast.makeText(MainActivity.this, "Update foto profil berhasil", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "Terjadi kesalahan saat mengambil data",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(String result) {
				Toast.makeText(MainActivity.this, "Terjadi kesalahan saat mengambil data",Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void callFragment(Fragment fragment) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(gmedia.net.id.absenigmedia.R.id.mainLayout, fragment, fragment.getClass().getSimpleName())
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
				.addToBackStack(null)
				.commit();
	}

	private ArrayList<CustomItem> isiArray() {
		ArrayList<CustomItem> rvData = new ArrayList<>();
		for (int i = 0; i < icon.length; i++) {
			CustomItem customItem = new CustomItem();
			customItem.setIcon(icon[i]);
			customItem.setTextIcon(textIcon[i]);
			rvData.add(customItem);
		}
		return rvData;
	}


	@Override
	public void onBackPressed() {
		DrawerLayout drawer = findViewById(gmedia.net.id.absenigmedia.R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if (!posisi) {
				fragment = new DashboardBaru();
				callFragment(fragment);
				title.setText("Dashboard");
				title.setTypeface(Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/Helvetica-Bold.otf"));
				drawer.closeDrawer(GravityCompat.START);
				posisi = true;
			} else {
				if (doubleBackToExitPressedOnce) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
				}
				doubleBackToExitPressedOnce = true;
				Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doubleBackToExitPressedOnce = false;
					}
				}, 2000);

			}
		}
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = MainActivity.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(MainActivity.this.getResources().getColor(gmedia.net.id.absenigmedia.R.color.statusbar));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(gmedia.net.id.absenigmedia.R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == gmedia.net.id.absenigmedia.R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();


		return true;
	}

	@Override
	public void onPermissionsGranted(int requestCode) {

		manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

		if (manager.isWifiEnabled() == false) {
			//Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            /*Snackbar.make(findViewById(android.R.id.content), "Wifi is disabled, please making it enabled",
                    Snackbar.LENGTH_SHORT).setAction("OK",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();*/
			manager.setWifiEnabled(true);
			WifiInfo info = manager.getConnectionInfo();
//			File vidDir = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
		}
	}
}
/*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;*/

        /*//setting tanggal

        RelativeLayout.LayoutParams layoutTanggal = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTanggal.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutTanggal.setMargins(0,80,0,0);
        dinotanggal.setLayoutParams(layoutTanggal);*/

       /* //setting waktu

        RelativeLayout.LayoutParams layoutWaktu = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutWaktu.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutWaktu.addRule(RelativeLayout.BELOW,dinotanggal.getId());
        waktu.setLayoutParams(layoutWaktu);*/

       /* //setting titik dua

        RelativeLayout.LayoutParams layoutTitik = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutTitik.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutTitik.addRule(RelativeLayout.BELOW,dinotanggal.getId());
        layoutTitik.setMargins(0,120,0,0);
        titikdua.setLayoutParams(layoutTitik);
        titikdua.setText("..");*/

//        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.listview_drawer,items);
//        ListView listView = (ListView)findViewById(R.id.listView);
//        listView.setAdapter(adapter);
/* private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_password:
                    Toast.makeText(MainActivity.this, "Add to Ganti Password", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_settings:
                    Toast.makeText(MainActivity.this, "Add to Setting", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }*/