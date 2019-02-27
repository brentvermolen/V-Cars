package com.example.brent.v_cars.Domain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brent.v_cars.DB.RitDao;
import com.example.brent.v_cars.DB.SettingsDao;
import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.MainActivity;
import com.example.brent.v_cars.Model.Rit;
import com.example.brent.v_cars.Model.Settings;
import com.example.brent.v_cars.R;

public class DialogSettings extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_settings, null);

        final EditText txtPrijsPerKm = (EditText) view.findViewById(R.id.txtPrijsPerKm);
        final EditText txtThuisAdres = (EditText) view.findViewById(R.id.txtThuisAdres);

        final Settings settings = VCarsDb.getDatabase(requireContext()).settingsDao().getSettings();
        txtPrijsPerKm.setText(String.valueOf(settings.getPrijsPerKilometer()));
        txtThuisAdres.setText(settings.getHomeAddress());

        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            double newPrijsPerKm = Double.parseDouble(txtPrijsPerKm.getText().toString());

                            settings.setPrijsPerKilometer(newPrijsPerKm);
                            settings.setHomeAddress(txtThuisAdres.getText().toString());

                            VCarsDb.getDatabase(requireContext()).settingsDao().update(settings);
                            ((MainActivity)getActivity()).updateSettings();
                        }catch (Exception e){
                            Toast.makeText(requireContext(), "Sommige velden zijn niet correct ingevuld", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }
}
