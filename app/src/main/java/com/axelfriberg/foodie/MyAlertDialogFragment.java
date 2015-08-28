package com.axelfriberg.foodie;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

    /**
     * A simple {@link Fragment} subclass.
     */
    public class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //int title = getArguments().getInt("title");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.alert_dialog_save_title)
                    .setPositiveButton(R.string.alert_dialog_save_yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ManageRecipeActivity)getActivity()).doPositiveClick();
                                }
                            }
                    )
                    .setNegativeButton(R.string.alert_dialog_save_no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    ((ManageRecipeActivity)getActivity()).doNegativeClick();
                                }
                            }
                    )
                    .create();
        }
    }


