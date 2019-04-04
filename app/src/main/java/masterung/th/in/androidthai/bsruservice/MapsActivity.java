package masterung.th.in.androidthai.bsruservice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latBSRU = 13.733058, lngBSRU = 100.489428;
    private double myLat = 0, myLng = 0;
    private LocationManager locationManager;
    private Criteria criteria;
    private int[] plateInts = {R.drawable.ic_action_person, R.drawable.ic_action_school,
            R.drawable.ic_action_park};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Setup
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);


    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();

//        for NetWork
        Location netWorkLocation = findLocation(LocationManager.NETWORK_PROVIDER);
        if (netWorkLocation != null) {
            myLat = netWorkLocation.getLatitude();
            myLng = netWorkLocation.getLongitude();
        }

//        for GPS
        Location gpsLocation = findLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            myLat = gpsLocation.getLatitude();
            myLng = gpsLocation.getLongitude();
        }

        if (myLat == 0) {
            onResume();
        } else {
            Log.d("4AprilV1", "Lat ==> " + myLat);
            Log.d("4AprilV1", "Lng ==> " + myLng);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location findLocation(String providerString) {

        Location location = null;

        if (locationManager.isProviderEnabled(providerString)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(providerString, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(providerString);

        }


        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            myLat = location.getLatitude();
            myLng = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        Setup Center Map
        LatLng bsruLatLng = new LatLng(latBSRU, lngBSRU);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bsruLatLng, 16));

        if (myLat != 0) {
            createMarker(myLat, myLng, plateInts[0]);
        }


    }   // onMapReady

    private void createMarker(double lat, double lng, int iconPlate) {

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(iconPlate));
        mMap.addMarker(markerOptions);



    }
}
