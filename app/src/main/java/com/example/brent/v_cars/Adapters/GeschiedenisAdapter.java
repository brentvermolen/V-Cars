package com.example.brent.v_cars.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.brent.v_cars.DB.RitDao;
import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.Model.GeredenRit;
import com.example.brent.v_cars.Model.Rit;
import com.example.brent.v_cars.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GeschiedenisAdapter extends ArrayAdapter<GeredenRit> {
    private ArrayList<GeredenRit> items;
    Context context;

    RitDao rittenDao;

    public GeschiedenisAdapter(ArrayList<GeredenRit> items, Context context){
        super(context, R.layout.lst_item_history, items);

        rittenDao = VCarsDb.getDatabase(context).rittenDao();

        this.items = items;
        this.context = context;
    }

    private static class ViewHolder{
        TextView lblTitel;
        TextView lblDatum;
        TextView lblKm;
        TextView lblPrijsPer;
        TextView lblPrijsTot;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GeredenRit geredenRit = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lst_item_history, parent, false);

            viewHolder.lblTitel = (TextView) convertView.findViewById(R.id.lblTitel);
            viewHolder.lblDatum = (TextView) convertView.findViewById(R.id.lblDatum);
            viewHolder.lblKm = (TextView) convertView.findViewById(R.id.lblAantalKm);
            viewHolder.lblPrijsPer = (TextView) convertView.findViewById(R.id.lblPrijsPerKm);
            viewHolder.lblPrijsTot = (TextView) convertView.findViewById(R.id.lblTotPrijs);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String naam;
        String aantalKm;
        String prijsPer;
        String totPrijs;

        if (geredenRit.getBestaandeRit_id() == 0){
            naam = geredenRit.getStart() + " - " + geredenRit.getEind();
            aantalKm = String.format("%.2f", geredenRit.getAantalKm()) + "km";
            prijsPer = "€ " + String.format("%.2f", geredenRit.getPrijsPerKmOpMomentVanRit());
            totPrijs = "€ " + String.format("%.2f", (geredenRit.getPrijsPerKmOpMomentVanRit() * geredenRit.getAantalKm() * 2));
        }else{
            Rit rit = rittenDao.getById(geredenRit.getBestaandeRit_id());

            naam = rit.getNaam();
            aantalKm = String.format("%.2f", rit.getAfstandHeenInKm() * 2) + "km";
            prijsPer = "n.v.t.";
            totPrijs = "€ " + String.format("%.2f", rit.getPrijs());
        }

        viewHolder.lblTitel.setText(naam);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        viewHolder.lblDatum.setText(format.format(geredenRit.getDatum()));
        viewHolder.lblKm.setText(aantalKm);
        viewHolder.lblPrijsPer.setText(prijsPer);
        viewHolder.lblPrijsTot.setText(totPrijs);

        return convertView;
    }
}
