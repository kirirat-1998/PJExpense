package com.example.pjexpense;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ItemDialog extends DialogFragment {
    public ItemDialog() {
    }

    public static ItemDialog newInstance(String title, String[] items) {
        ItemDialog dialog = new ItemDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putStringArray("items", items);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        final String[] items = getArguments().getStringArray("items");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFinishDialog(which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFinishDialog(-1);
                    }
                });

        return alertDialogBuilder.create();
    }

    interface OnFinishDialogListener {
        void onFinishDialog(int selectedIndex);
    }

    private OnFinishDialogListener mListener;

    public void setOnFinishDialogListener(OnFinishDialogListener listener) {
        mListener = listener;
    }
}
