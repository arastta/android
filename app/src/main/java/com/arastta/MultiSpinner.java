package com.arastta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

public class MultiSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private Context context;
    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText })
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                ((TextView) v).setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8),0,0,0);
                //((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBg));
                ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));
                return v;
            }
        };

        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(final Context context, final List<String> items, String allText, int[] trues, MultiSpinnerListener listener) {
        this.context = context;
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++){
            selected[i] = false;
            for (int j = 0; j < trues.length; j++){
                if(i == trues[j])selected[i] = true;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new String[] { allText })//items
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                ((TextView) v).setPadding((int)ConstantsAndFunctions.convertDpToPixel_PixelToDp(context,true,8),0,0,0);
                //((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }
            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.colorBg));
                ((TextView) v).setTextColor(getResources().getColor(R.color.colorAccent));
                return v;
            }
        };

        setAdapter(adapter);
    }

    public interface MultiSpinnerListener
    {
        public void onItemsSelected(boolean[] selected);
    }
}