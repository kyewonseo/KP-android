package net.bluehack.kiosk.store;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import net.bluehack.kiosk.api.ApiClient;
import net.bluehack.kiosk.order_pay.OrderPayActivity;
import net.bluehack.kiosk.R;
import net.bluehack.kiosk.util.GooglePlaceXMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static net.bluehack.kiosk.util.Logger.LOGD;
import static net.bluehack.kiosk.util.Logger.LOGE;
import static net.bluehack.kiosk.util.Logger.makeLogTag;


public class StoreActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback, PlacesListener {

    private static final String TAG = makeLogTag(StoreActivity.class);
    private Context context;
    private static GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest = null;
    private static GoogleMap googleMap = null;
    private String currentLocationAddress = null;
    private static LocationManager locationManager = null;
    private static MapFragment mapFragment = null;
    private boolean setGPS = false; //현재는 network check
    private final int REQUEST_CODE_LOCATION = 2000;
    private final int REQUEST_CODE_GPS = 2001;
    private static Location location = null;
    private final String radius = "1000";
    private final String types = "cafe";
    private static List<StoreItem> storeList = new ArrayList<StoreItem>();

    private StoreAdapter storeAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    LatLng SEOUL = new LatLng(37.56, 126.97);

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API) //place
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .build();

        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.bluehack.kiosk.R.layout.activity_store);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        recyclerView = (RecyclerView) findViewById(R.id.store_recommend_list);
        recyclerView.setHasFixedSize(true);

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storeAdapter = new StoreAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(storeAdapter);

        /*LinearLayout store_ll_btn = (LinearLayout) findViewById(R.id.store_ll_btn);
        store_ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, OrderPayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/
    }


    public boolean checkLocationPermission()
    {
        Log.d( TAG, "checkLocationPermission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //퍼미션 요청을 위해 UI를 보여줘야 하는지 검사
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    //Prompt the user once explanation has been shown;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

                } else
                    //UI보여줄 필요 없이 요청
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);

                return false;
            } else {

                Log.d( TAG, "checkLocationPermission"+"이미 퍼미션 획득한 경우");

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
                {
                    Log.d(TAG, "checkLocationPermission Version >= M");
                    showGPSDisabledAlertToUser();
                }

                if (googleApiClient == null) {
                    Log.d( TAG, "checkLocationPermission "+"mGoogleApiClient==NULL");
                    buildGoogleApiClient();
                }
                else  Log.d( TAG, "checkLocationPermission "+"mGoogleApiClient!=NULL");

                if ( googleApiClient.isConnected() ) Log.d( TAG, "checkLocationPermission"+"mGoogleApiClient 연결되 있음");
                else Log.d( TAG, "checkLocationPermission"+"mGoogleApiClient 끊어져 있음");


                googleApiClient.reconnect();//이미 연결되 있는 경우이므로 다시 연결

                googleMap.setMyLocationEnabled(true);
            }
        }
        else {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS)
            {
                Log.d(TAG, "checkLocationPermission Version < M");
                showGPSDisabledAlertToUser();
            }

            if (googleApiClient == null) {
                buildGoogleApiClient();
            }
            googleMap.setMyLocationEnabled(true);
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //퍼미션이 허가된 경우
                    if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && !setGPS ||
                                !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                            showGPSDisabledAlertToUser();
                        }

                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "퍼미션 취소", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        googleMap = map;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                } else {

                    if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && !setGPS ||
                            !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !setGPS) {
                        showGPSDisabledAlertToUser();
                    }

                    if (googleApiClient == null) {
                        buildGoogleApiClient();
                    }

                    googleMap.setMyLocationEnabled(true);
                }
            }
        });

        //구글 플레이 서비스 초기화
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
            else
            {
                /**앱을 삭제할 경우*/
                checkLocationPermission();
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        LOGD(TAG, "onConnected");

        /**TODO: @k
         * LocationManager
         * NETWORK_PROVIDER, GPS_PROVIDER 둘다 기기별 테스트 필요 */
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            setGPS = true;
            //location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            setGPS = true;
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }else {
            LOGD(TAG, "Can't get locationManager!");
        }

        locationRequest = new LocationRequest();
        //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);


        if ( setGPS && googleApiClient.isConnected() ) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            LOGD(TAG, "GPS_PROVIDER location:" + location.getLatitude() + "," + location.getLongitude());
            LOGD(TAG, "GPS_PROVIDER Address:" + findAddress(location));
            if (location != null) {

                new NRPlaces.Builder()
                        .listener(this)
                        .key("AIzaSyA7av6NBZ3U-CwHzYMPex2M96OWP1zQz4Y")
                        .latlng(location.getLatitude(), location.getLongitude())
                        .radius(1000)
                        .type(PlaceType.CAFE)
                        .build()
                        .execute();
            } else {
                return;
            }
        }

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if ( !googleApiClient.isConnected()) {
                googleApiClient.connect();
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        LOGD(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        LOGD(TAG, "Connection suspended");
        googleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onPause() {
        if ( googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (googleApiClient != null) {
            googleApiClient.unregisterConnectionCallbacks(this);
            googleApiClient.unregisterConnectionFailedListener(this);

            if (googleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }

            googleApiClient.disconnect();
            googleApiClient = null;
        }

        super.onDestroy();
    }


    @Override
    public void onLocationChanged(Location location) {

        LOGD(TAG, "location:" + location.getLatitude() + "," + location.getLongitude());
        LOGD(TAG, "Address:" + findAddress(location));
    }

    //GPS 활성화를 위한 다이얼로그 보여주기
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS가 비활성화 되어있습니다. 활성화 할까요?")
                .setCancelable(false)
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent, REQUEST_CODE_GPS);
                    }
                });

        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    //GPS 활성화를 위한 다이얼로그의 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_GPS:

                if (locationManager == null)
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    setGPS = true;
                    mapFragment.getMapAsync(this);
                }
                break;
        }
    }

    //좌표값 => 주소 변환
    private String findAddress(Location location) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> address;

        try {
            if (geocoder != null) {

                double lat = location.getLatitude();
                double lng = location.getLongitude();

                // 세번째 인수는 최대결과값
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소
                    currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress).append("#");
                    bf.append(lat).append("#");
                    bf.append(lng);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf.toString();
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
        LOGE(TAG,"onPlacesFailure()");
    }

    @Override
    public void onPlacesStart() {
        LOGD(TAG,"onPlacesStart()");
    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        LOGD(TAG,"onPlacesSuccess()");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (noman.googleplaces.Place place : places) {

                    if (places.size() != 0) {
                        StoreItem storeItem = new StoreItem();
                        int meter = distanceLocation(location.getLatitude(), location.getLongitude(), place.getLatitude(), place.getLongitude());
                        LOGE(TAG,"PALCE meter : " + meter);
                        LOGE(TAG,"PALCE getName : " + place.getName());
                        LOGE(TAG,"PALCE address : " + place.getVicinity());
                        LOGE(TAG,"PALCE getLatitude :" + place.getLatitude());
                        LOGE(TAG,"PALCE getLongitude :" + place.getLongitude());
                        storeItem.setName(place.getName());
                        storeItem.setAddress(place.getVicinity());
                        storeItem.setMeter(String.valueOf(meter)+"m");
                        storeList.add(storeItem);
                    } else {
                        LOGE(TAG, "places is null");
                    }
                }
            }
        });
    }

    @Override
    public void onPlacesFinished() {
        LOGD(TAG,"onPlacesFinished()");
        LOGE(TAG,"storeList = " + storeList.toString());

        storeAdapter.clean();
        storeAdapter.addItem(storeList);
        storeAdapter.notifyDataSetChanged();
    }


    public static int distanceLocation(double curLatitude, double curLongitude, double targetLatitude, double targetLngitude) {

        int meter;
        Location current = new Location("current");
        Location target = new Location("target");

        current.setLatitude(curLatitude);
        current.setLongitude(curLongitude);
        target.setLatitude(targetLatitude);
        target.setLongitude(targetLngitude);

        double distance = current.distanceTo(target);
        meter = (int) distance;

        return meter;
    }
}
