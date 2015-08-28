package com.axelfriberg.foodie;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;


    public class MyAlertDialogFragment extends DialogFragment {
        private String input;
        final static String INPUT_TEXT = "inputText";
        EditText inputField = null;

        public static MyAlertDialogFragment newInstance(int title) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int title = getArguments().getInt("title");
            inputField = new EditText(getActivity());
            if(title == R.string.alert_dialog_save_title) {
                return new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setPositiveButton(R.string.alert_dialog_save_yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((AddRecipeActivity) getActivity()).doPositiveClick();
                                    }
                                }
                        )
                        .setNegativeButton(R.string.alert_dialog_save_no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((AddRecipeActivity) getActivity()).doNegativeClick();
                                    }
                                }
                        )
                        .create();
            } else {
                return new AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setView(inputField)
                        .setPositiveButton(R.string.alert_dialog_grocery_ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((GroceryListActivity) getActivity()).doPositiveClick(inputField.getText().toString());
                                    }
                                }
                        )
                        .setNegativeButton(R.string.alert_dialog_grocery_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((GroceryListActivity) getActivity()).doNegativeClick();
                                    }
                                }
                        )
                        .create();
            }

        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            savedInstanceState.putString(INPUT_TEXT, inputField.getText().toString());
            super.onSaveInstanceState(savedInstanceState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (savedInstanceState != null) {
                // Restore last state for checked position.
                inputField.setText(savedInstanceState.getString(INPUT_TEXT));
            }
        }
    }


