package sdm.ifspsaocarlos.edu.br.gmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa;

    private final double LATITUDE = -21.970306;
    private final double LONGITUDE = -47.878733;

    private final int DELAY = 10000;
    private final float INCLINACAO = 50;
    private final float ROTACAO = 90;
    private final float ZOOM = 17.5f;

    private final int CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_mapa);
        fragmentoMapa.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setIndoorEnabled(true);
        mapa.setBuildingsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED    ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO);
            }
        } else {
            mapa.setMyLocationEnabled(true);
        }

        LatLng novaLatLng = new LatLng(LATITUDE, LONGITUDE);
        CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
        cameraPositionBuilder.bearing(ROTACAO);
        cameraPositionBuilder.zoom(ZOOM);
        cameraPositionBuilder.tilt(INCLINACAO);
        cameraPositionBuilder.target(novaLatLng);
        CameraPosition cameraPosition = cameraPositionBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mapa.animateCamera(cameraUpdate, DELAY, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "Centralizado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_LONG).show();

            }
        });

        MarkerOptions optionsMarker = new MarkerOptions();
        optionsMarker.position(novaLatLng);
        mapa.addMarker(optionsMarker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED    ) {
                mapa.setMyLocationEnabled(true);
            }
        }
    }
}
