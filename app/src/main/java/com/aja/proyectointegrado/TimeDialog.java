package com.aja.proyectointegrado;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Javier on 16/02/2017.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class TimeDialog extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText etHora = (EditText) getActivity().findViewById(R.id.etHora);
        //hourOfDay = view.getHour();
        //minute = view.getMinute();
        //etHora.setText(hourOfDay+":"+minute);
        int hour;
        hour = hourOfDay;
        if(hourOfDay<=9 && minute==0){
            etHora.setText("0"+hour + ":" + minute+"0");
        }else if(minute==0){
            etHora.setText(hour + ":" + minute+"0");
        }else if(hourOfDay<=9){
            etHora.setText("0"+hour + ":" + minute);
        }else if(hourOfDay<=9 && minute<=9) {
            etHora.setText("0"+ hour + ":0" + minute);
        }else if(minute<=9) {
            etHora.setText(hour + ":0" + minute);
        } else{
            etHora.setText(hour + ":" + minute);
        }
        //etHora.setText(hour + " : " + minute + " " + am_pm);
    }
}
