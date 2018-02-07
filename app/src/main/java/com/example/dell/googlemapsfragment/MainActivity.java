package com.example.dell.googlemapsfragment;

import android.app.Fragment;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    GoogleMap mapa;
    SupportMapFragment fragmentMapa;
    TextView tvLatitud, tvLongitud, tvDistancia;
    double latitud;
    double longitud;

    LatLng alcala;
    MarkerOptions marcador1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentMapa= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        fragmentMapa.getMapAsync(this);

        tvLatitud=(TextView)findViewById(R.id.tvLatitud);
        tvLongitud=(TextView)findViewById(R.id.tvLongitud);
        tvDistancia=(TextView)findViewById(R.id.tvDistancia);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa=googleMap;
        mapa.setMapType(googleMap.MAP_TYPE_NORMAL); //Establecemos tipo de mapa. Podemos poner tb MAP_TYPE_SATELLITE, o MAP_TYPE_HYBRID
        mapa.getUiSettings().setZoomControlsEnabled(true); //Para botones de zoom el el mapa

        alcala=new LatLng(40.4819791, -3.363542100000018);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(alcala, 15));

        //Agragamos un marcador al mapa situandolo en las coordenadas de la variable alcala y con el marcardor rotado 45º y para la imagen del marcador usamos una incorporada en nuestro R.drawable
        marcador1=new MarkerOptions().title("Plaza Cervantes de Alcalá de Henares").position(alcala).rotation(45.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador1));
        marcador1.draggable(true);
        mapa.addMarker(marcador1);

        //Cargamos inicialmente las coordenadas en los TextView
        latitud=alcala.latitude;
        longitud=alcala.longitude;
        tvLatitud.setText("Latitud: " + Double.toString(latitud));
        tvLongitud.setText("Longitud: " + Double.toString(longitud));

        mapa.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getApplicationContext(), "Cambiando coordenadas...", Toast.LENGTH_SHORT ).show();
        MarkerOptions marcadorOriginal=marcador1;
        marcadorOriginal.icon(BitmapDescriptorFactory.fromResource(R.drawable.origen));
        marcadorOriginal.title("Origen");
        mapa.addMarker(marcadorOriginal);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        latitud=marker.getPosition().latitude;
        longitud=marker.getPosition().longitude;

        tvLatitud.setText("Latitud: " + Double.toString(latitud));
        tvLongitud.setText("Longitud: " + Double.toString(longitud));

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(getApplicationContext(), "Cambio de coordenadas finalizado", Toast.LENGTH_SHORT ).show();

        Location localizacionOriginal=new Location("Plaza Cervantes (Alcala de Henares)");
        localizacionOriginal.setLatitude(alcala.latitude);
        localizacionOriginal.setLongitude(alcala.longitude);

        Location localizacionFinal=new Location("Localización final");
        localizacionFinal.setLatitude(marker.getPosition().latitude);
        localizacionFinal.setLongitude(marker.getPosition().longitude);

        float distancia=localizacionOriginal.distanceTo(localizacionFinal);
        tvDistancia.setText("Distancia al origen: " + distancia+" m");

    }
}
