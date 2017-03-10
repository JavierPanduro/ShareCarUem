package com.aja.proyectointegrado;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Fragmento con un diálogo de elección de fechas
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DateDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        EditText etFecha = (EditText) getActivity().findViewById(R.id.etFecha);
        if(view.getDayOfMonth()<=9 && view.getMonth()<=9){
            etFecha.setText("0"+view.getDayOfMonth()+"/0"+(view.getMonth()+1)+"/"+view.getYear());
        }else if(view.getMonth()<=9) {
            etFecha.setText(view.getDayOfMonth()+"/0"+(view.getMonth()+1)+"/"+view.getYear());
        }else if(view.getDayOfMonth()<=9) {
            etFecha.setText("0"+view.getDayOfMonth() + "/" + (view.getMonth() + 1) + "/" + view.getYear());
        }else {
            etFecha.setText(view.getDayOfMonth() + "/" + (view.getMonth() + 1) + "/" + view.getYear());
        }
    }
}
