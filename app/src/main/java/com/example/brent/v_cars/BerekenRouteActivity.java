package com.example.brent.v_cars;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.Domain.Directions;
import com.example.brent.v_cars.Domain.DirectionsJSONParser;
import com.example.brent.v_cars.Model.GeredenRit;
import com.example.brent.v_cars.Model.Rit;
import com.example.brent.v_cars.Model.Settings;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BerekenRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();

    public String origin;
    public String dest;
    public String originLocality;
    public String destLocality;

    List<Address> addresses;

    public boolean addressLoaded;

    LinearLayout llInfo;
    TextView lblAantalKm;
    TextView lblPrijs;
    Button btnGa;

    Settings settings;

    GeredenRit rit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bereken_route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.origin = getIntent().getStringExtra("origin");
        this.dest = getIntent().getStringExtra("dest");

        addressLoaded = false;

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids) {
                Geocoder geocoder;
                geocoder = new Geocoder(BerekenRouteActivity.this, Locale.getDefault());

                //addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                try {
                    addresses = geocoder.getFromLocationName(origin, 1);
                    addresses.addAll(geocoder.getFromLocationName(dest, 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadAddress();
            }
        }.execute();

        initViews();
        handleEvents();
    }

    private void initViews() {
        llInfo = (LinearLayout) findViewById(R.id.llInfoBerekening);
        lblAantalKm = (TextView) findViewById(R.id.lblAantalKm);
        lblPrijs = (TextView) findViewById(R.id.lblPrijs);
        btnGa = (Button) findViewById(R.id.btnGa);

        settings = VCarsDb.getDatabase(this).settingsDao().getSettings();
    }

    private void handleEvents() {
        btnGa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rit == null){
                    Toast.makeText(BerekenRouteActivity.this, "Geen rit gevonden", Toast.LENGTH_SHORT).show();
                }else {
                    Intent result = new Intent();
                    result.putExtra("rit", rit);
                    setResult(0, result);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng merksplas = new LatLng(51.3582, 4.8635);
        //mMap.addMarker(new MarkerOptions().position(merksplas).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(merksplas, 12));

        loadAddress();
        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                markerPoints.add(latLng);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(latLng);

                if (markerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (markerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = (LatLng) markerPoints.get(0);
                    LatLng dest = (LatLng) markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }

            }
        });*/

    }

    private void loadAddress() {
        if (!addressLoaded && mMap != null && addresses!= null){
            if (addresses.size() == 2){
                addressLoaded = true;

                Address origin = addresses.get(0);
                Address dest = addresses.get(1);

                this.originLocality = origin.getLocality();
                this.destLocality = dest.getLocality();

                LatLng originLatLng = new LatLng(origin.getLatitude(), origin.getLongitude());
                LatLng destLatLng = new LatLng(dest.getLatitude(), dest.getLongitude());

                // Adding new item to the ArrayList
                markerPoints.add(originLatLng);
                markerPoints.add(destLatLng);

                // Creating MarkerOptions
                MarkerOptions optionsOrigin = new MarkerOptions();
                MarkerOptions optionsDest = new MarkerOptions();

                // Setting the position of the marker
                optionsOrigin.position(originLatLng);
                optionsDest.position(destLatLng);

                optionsOrigin.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                optionsDest.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(optionsOrigin);
                mMap.addMarker(optionsDest);

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(originLatLng, destLatLng);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, Directions> {

        // Parsing the data in non-ui thread
        @Override
        protected Directions doInBackground(String... jsonData) {

            JSONObject jObject;
            Directions directions = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                directions = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return directions;
        }

        public LatLng midPoint(double lat1,double lon1,double lat2,double lon2){

            double dLon = Math.toRadians(lon2 - lon1);

            //convert to radians
            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);
            lon1 = Math.toRadians(lon1);

            double Bx = Math.cos(lat2) * Math.cos(dLon);
            double By = Math.cos(lat2) * Math.sin(dLon);
            double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
            double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

            //print out in degrees
            return new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
        }


        @Override
        protected void onPostExecute(Directions directions) {
            List<List<HashMap<String,String>>> result = directions.getDirections();
            //Toast.makeText(BerekenRouteActivity.this, directions.getAantalMeter() + "m", Toast.LENGTH_SHORT).show();

            double latStart = Double.parseDouble(result.get(0).get(0).get("lat"));
            double lngStart = Double.parseDouble(result.get(0).get(0).get("lng"));
            double latEnd = Double.parseDouble(result.get(0).get(result.get(0).size() - 1).get("lat"));
            double lngEnd = Double.parseDouble(result.get(0).get(result.get(0).size() - 1).get("lng"));
            LatLng halfwayPoint = midPoint(latStart, lngStart, latEnd, lngEnd);

            Circle circle = mMap.addCircle(new CircleOptions().center(halfwayPoint).radius(directions.getAantalMeter() / 2 * 1.20));
            circle.setVisible(false);

            String strAantalKm = String.format("%.2f", (double)directions.getAantalMeter() / 1000);
            double aantalKm = Double.parseDouble(strAantalKm.replace(',', '.'));
            lblAantalKm.setText(strAantalKm + "km");
            String strPrijs = String.format("%.2f", aantalKm * 2 * settings.getPrijsPerKilometer());
            lblPrijs.setText("€ " + strPrijs);
            rit = new GeredenRit();
            rit.setAantalKm(aantalKm);
            rit.setStart(originLocality);
            rit.setEind(destLocality);
            rit.setStartVolledig(origin);
            rit.setEindVolledig(dest);

            double radius = circle.getRadius();
            double scale = radius / 500;
            int zoomLevel =(int) (16 - Math.log(scale) / Math.log(2));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(halfwayPoint, zoomLevel));

            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);
            }

// Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
            llInfo.setVisibility(View.VISIBLE);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + getString(R.string.google_maps_key) + "&region=be";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}