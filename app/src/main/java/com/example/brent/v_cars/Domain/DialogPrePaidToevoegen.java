package com.example.brent.v_cars.Domain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.brent.v_cars.DB.SettingsDao;
import com.example.brent.v_cars.DB.VCarsDb;
import com.example.brent.v_cars.MainActivity;
import com.example.brent.v_cars.Model.Settings;
import com.example.brent.v_cars.R;

import java.awt.font.NumericShaper;

public class DialogPrePaidToevoegen extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_prepaid_toevoegen, null);

        final NumberPicker npGrootGetal = (NumberPicker) view.findViewById(R.id.npGrootGetal);
        final NumberPicker npDecimaal = (NumberPicker) view.findViewById(R.id.npDecimaal);

        npGrootGetal.setMinValue(0);
        npGrootGetal.setMaxValue(100);

        npDecimaal.setMinValue(1);
        npDecimaal.setMaxValue(4);
        npDecimaal.setDisplayedValues(new String[] { ".00", ".25", ".50", ".75" });

        final SettingsDao settingsDao = VCarsDb.getDatabase(requireContext()).settingsDao();

        builder.setView(view)
                .setPositiveButton("Toevoegen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String decimaal = npDecimaal.getDisplayedValues()[npDecimaal.getValue() - 1];
                        double value = Double.parseDouble(npGrootGetal.getValue() + decimaal);

                        Settings settings = settingsDao.getSettings();
                        settings.setPrePaidTegoed(settings.getPrePaidTegoed() + value);
                        settingsDao.update(settings);
                        ((MainActivity)getActivity()).updatePrePaidTegoed();
                    }
                })
                .setNegativeButton("Anulleren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogPrePaidToevoegen.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
