package com.example.brent.v_cars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.brent.v_cars.Adapters.GeschiedenisAdapter;
import com.example.brent.v_cars.DB.GeredenRitDao;
import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.Model.GeredenRit;

import java.util.ArrayList;

public class GeschiedenisActivity extends AppCompatActivity {

    ArrayList<GeredenRit> items;
    ListView lstGeschiedenis;
    GeschiedenisAdapter adapter;

    GeredenRitDao geredenRitDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geschiedenis);

        geredenRitDao = VCarsDb.getDatabase(getApplicationContext()).geredenRitDao();

        initViews();
        handleEvents();
    }

    private void initViews() {
        lstGeschiedenis = (ListView) findViewById(R.id.lstGeschiedenis);
        items = new ArrayList<GeredenRit>();

        for (GeredenRit gr : geredenRitDao.getAllOrderByDate()){
            items.add(gr);
        }

        adapter = new GeschiedenisAdapter(items, this);
        lstGeschiedenis.setAdapter(adapter);
    }

    private void handleEvents() {
        lstGeschiedenis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GeredenRit rit = items.get(position);
                if (rit.getBestaandeRit_id() == 0){
                    Intent intent = new Intent(GeschiedenisActivity.this, BerekenRouteActivity.class);
                    intent.putExtra("origin", rit.getStartVolledig());
                    intent.putExtra("dest", rit.getEindVolledig());
                    startActivityForResult(intent, 0);
                }else{
                    Intent result = new Intent();
                    result.putExtra("rit_id", rit.getBestaandeRit_id());
                    setResult(1, result);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0: //BerekenRoute
                switch (resultCode){
                    case 0: //Geslaagd
                        if(data == null){ return; }

                        setResult(0, data);
                        finish();
                        break;
                    case 1: //Fail
                        break;
                }
                break;
        }
    }
}
