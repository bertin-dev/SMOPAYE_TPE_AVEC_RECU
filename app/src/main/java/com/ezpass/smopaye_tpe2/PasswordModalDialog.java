package com.ezpass.smopaye_tpe2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class PasswordModalDialog extends AppCompatDialogFragment {

    private PasswordModalDialog.ExampleDialogListener listener;
    private TextInputLayout mPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_password, null);


        mPassword = view.findViewById(R.id.password);

        //mPassword.setErrorTextColor(ColorStateList.valueOf(Color.BLUE));

        builder.setView(view)
                .setTitle("Mot de Passe")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String pwd = mPassword.getEditText().getText().toString().trim();

                        if(!validatePassword()){
                            return;
                        }

                        listener.applyTexts(pwd.trim());
                    }
                });

        return builder.create();
    }

    private boolean validatePassword(){
        String password = mPassword.getEditText().getText().toString().trim();

        if(password.isEmpty()){
            mPassword.setError("Veuillez inserer le mot de passe.");
            return false;
        } else if(password.length() < 5){
            mPassword.setError("Votre mot de passe est court");
            return false;
        } else {
            mPassword.setError(null);
            return true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PasswordModalDialog.ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyTexts(String password);
    }
}
