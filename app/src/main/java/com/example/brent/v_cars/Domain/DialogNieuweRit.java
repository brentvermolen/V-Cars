package com.example.brent.v_cars.Domain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brent.v_cars.DB.RitDao;
import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.MainActivity;
import com.example.brent.v_cars.Model.Rit;
import com.example.brent.v_cars.R;

import java.util.zip.Inflater;

public class DialogNieuweRit extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_nieuwe_rit, null);

        if (getArguments() == null){
            setArguments(new Bundle());
        }

        final Rit rit;
        final boolean isEdit = getArguments().getBoolean("isEdit", false);

        final RitDao ritDao = VCarsDb.getDatabase(requireContext()).rittenDao();

        final EditText txtNaam = (EditText) view.findViewById(R.id.txtNaam);
        final EditText txtKms = (EditText) view.findViewById(R.id.txtAantalKm);
        final EditText txtPrijs = (EditText) view.findViewById(R.id.txtTotPrijs);

        if (isEdit){
            rit = ritDao.getById(getArguments().getInt("ritId"));

            txtNaam.setText(rit.getNaam());
            txtKms.setText(String.valueOf(rit.getAfstandHeenInKm()));
            txtPrijs.setText(String.valueOf(rit.getPrijs()));
        }else{
            rit = new Rit();
        }

        builder.setView(view)
                .setPositiveButton("Opslaan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            rit.setNaam(txtNaam.getText().toString());
                            rit.setAfstandHeenInKm(Double.parseDouble(txtKms.getText().toString()));
                            rit.setPrijs(Double.parseDouble(txtPrijs.getText().toString()));

                            if (isEdit){
                                ritDao.update(rit);
                            }else{
                                ritDao.insert(rit);
                            }
                            ((MainActivity)getActivity()).insertRitten();
                        }catch (Exception e){
                            Toast.makeText(requireContext(), "Sommige velden zijn niet correct ingevuld", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                })
                .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogNieuweRit.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
