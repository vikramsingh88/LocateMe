package com.vikram.locateme.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vikram.locateme.R;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class DialogHelper {

    public static Dialog showProgressDialog(Context context, String label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.dialog_progress, null);
        TextView textLabel = (TextView)view.findViewById(R.id.txt_label);
        textLabel.setText(label);
        builder.setView(view);
        return builder.create();
    }

    public static AlertDialog.Builder showErrorDialog(Context context, String label) {
        if (context!= null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_error, null);
            TextView textLabel = (TextView) view.findViewById(R.id.txt_label);
            textLabel.setText(label);
            builder.setView(view);

            return builder;
        }
        return null;
    }

    public static AlertDialog.Builder showConfirmationDialog(Context context, String label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(label);

        return builder;
    }

    public static AlertDialog.Builder showSuccessDialog(Context context, String label) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(label);

        return builder;
    }
}
