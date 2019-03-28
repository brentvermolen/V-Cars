package com.example.brent.v_cars;

import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.v_cars.DB.*;
import com.example.brent.v_cars.Domain.DialogNieuweRit;
import com.example.brent.v_cars.Domain.DialogPrePaidToevoegen;
import com.example.brent.v_cars.Domain.DialogSettings;
import com.example.brent.v_cars.Model.GeredenRit;
import com.example.brent.v_cars.Model.Rit;
import com.example.brent.v_cars.Model.Settings;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    VCarsDb database;
    RitDao rittenDao;
    Settings settings;
    GeredenRitDao geredenRitDao;

    TextView lblPrepaidTegoed;
    LinearLayout lstOpgeslagenRitten;

    LinearLayout llNieuweRit;
    LinearLayout llBestaandeRit;

    TextView lblRitnaam;
    TextView lblRitPrijs;
    Button btnBevestig;
    Button btnAnulleerRit;

    EditText txtVan;
    EditText txtNaar;
    Button btnBereken;
    ImageButton btnSetAddressHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        database = VCarsDb.getDatabase(getApplicationContext());
        rittenDao = database.rittenDao();
        settings = database.settingsDao().getSettings();
        geredenRitDao = database.geredenRitDao();

        initViews();
        handleEvents();
    }

    private void initViews() {
        lblPrepaidTegoed = (TextView) findViewById(R.id.lblPrepaidTegoed);
        updatePrePaidTegoed();

        lstOpgeslagenRitten = (LinearLayout) findViewById(R.id.lstBestaandeRitten);
        insertRitten();

        llNieuweRit = (LinearLayout) findViewById(R.id.llNieuweRit);
        llBestaandeRit = (LinearLayout) findViewById(R.id.llBestaandeRit);

        lblRitnaam = (TextView) findViewById(R.id.lblRitnaam);
        lblRitPrijs = (TextView) findViewById(R.id.lblRitPrijs);
        btnBevestig = (Button) findViewById(R.id.btnBevestigRit);
        btnAnulleerRit = (Button) findViewById(R.id.btnAnulleerRit);

        txtVan = (EditText) findViewById(R.id.txtVan);
        txtNaar = (EditText) findViewById(R.id.txtNaar);
        btnBereken = (Button) findViewById(R.id.btnBereken);
        btnSetAddressHome = (ImageButton) findViewById(R.id.btnSetAddressHome);
    }

    private void handleEvents() {
        btnBevestig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnBevestig.getTag() != null){
                    GeredenRit geredenRit;
                    double prijs;

                    if (btnBevestig.getTag() instanceof Rit){
                        Rit rit = (Rit)btnBevestig.getTag();
                        geredenRit = new GeredenRit();
                        geredenRit.setBestaandeRit_id(rit.getId());
                        prijs = rit.getPrijs();
                    }else{
                        geredenRit = (GeredenRit)btnBevestig.getTag();
                        geredenRit.setPrijsPerKmOpMomentVanRit(settings.getPrijsPerKilometer());
                        prijs = geredenRit.getPrijsPerKmOpMomentVanRit() * geredenRit.getAantalKm() * 2;
                    }

                    double tegoed = settings.getPrePaidTegoed() - prijs;
                    if (tegoed < 0){
                        Toast.makeText(MainActivity.this, "Niet genoeg tegoed!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    settings.setPrePaidTegoed(tegoed);
                    geredenRitDao.insert(geredenRit);
                    database.settingsDao().update(settings);
                    updatePrePaidTegoed();

                    llBestaandeRit.setVisibility(View.GONE);
                    llNieuweRit.setVisibility(View.VISIBLE);

                    txtNaar.setText("");
                    txtVan.setText("");
                }
            }
        });

        btnAnulleerRit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBestaandeRit.setVisibility(View.GONE);
                llNieuweRit.setVisibility(View.VISIBLE);
            }
        });

        btnBereken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BerekenRouteActivity.class);
                intent.putExtra("origin", txtVan.getText().toString());
                intent.putExtra("dest", txtNaar.getText().toString());
                startActivityForResult(intent, 0);
            }
        });

        btnSetAddressHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtVan.setText(settings.getHomeAddress());
            }
        });
    }

    public void insertRitten() {
        lstOpgeslagenRitten.removeAllViews();
        for(final Rit r : rittenDao.getAllOrderByNaam()){
            Button b = new Button(this);
            b.setText(r.toString());
            b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llNieuweRit.setVisibility(View.GONE);
                    llBestaandeRit.setVisibility(View.VISIBLE);

                    btnBevestig.setTag(r);
                    lblRitnaam.setText(r.getNaam());
                    lblRitPrijs.setText("€ " + r.getPrijs());
                }
            });
            b.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogNieuweRit dialogNieuweRit = new DialogNieuweRit();
                    dialogNieuweRit.setArguments(new Bundle());
                    dialogNieuweRit.getArguments().putBoolean("isEdit", true);
                    dialogNieuweRit.getArguments().putInt("ritId", r.getId());
                    dialogNieuweRit.show(getSupportFragmentManager(), "DialogEditRit");

                    return false;
                }
            });
            lstOpgeslagenRitten.addView(b);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_history:
                Intent intent = new Intent(this, GeschiedenisActivity.class);
                startActivityForResult(intent, 0);
                return true;
            case R.id.action_settings:
                DialogSettings dialogSettings = new DialogSettings();
                dialogSettings.show(getSupportFragmentManager(), "DialogSettings");
                return true;
            case R.id.action_add_rit:
                DialogNieuweRit dialogNieuweRit = new DialogNieuweRit();
                dialogNieuweRit.show(getSupportFragmentManager(), "DialogNieuweRit");
                return true;
            case R.id.action_add_prepaid:
                DialogPrePaidToevoegen dialogPrePaidToevoegen = new DialogPrePaidToevoegen();
                dialogPrePaidToevoegen.show(getSupportFragmentManager(), "DialogPrePaid");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updatePrePaidTegoed() {
        settings = database.settingsDao().getSettings();
        lblPrepaidTegoed.setText("€ " + String.format("%.2f", settings.getPrePaidTegoed()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0: //BerekenRoute
                switch (resultCode){
                    case 0: //Geslaagd - Nieuwe rit
                        if(data == null){ return; }
                        GeredenRit rit = (GeredenRit)data.getSerializableExtra("rit");

                        lblRitnaam.setText(rit.getStart() + " - " + rit.getEind() + "\n(" + rit.getAantalKm() + "km)");
                        lblRitPrijs.setText("€ " + String.format("%.2f", settings.getPrijsPerKilometer() * rit.getAantalKm() * 2));
                        btnBevestig.setTag(rit);

                        llNieuweRit.setVisibility(View.GONE);
                        llBestaandeRit.setVisibility(View.VISIBLE);
                        break;
                    case 1: //Geslaagd - Bestaande rit
                        if (data != null){
                            Rit r = rittenDao.getById(data.getIntExtra("rit_id", 0));
                            if (r.getId() == 0){
                                return;
                            }

                            btnBevestig.setTag(r);
                            lblRitnaam.setText(r.getNaam());
                            lblRitPrijs.setText("€ " + r.getPrijs());

                            llNieuweRit.setVisibility(View.GONE);
                            llBestaandeRit.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;
        }
    }

    public void updateSettings() {
        settings = VCarsDb.getDatabase(this).settingsDao().getSettings();
    }
}
