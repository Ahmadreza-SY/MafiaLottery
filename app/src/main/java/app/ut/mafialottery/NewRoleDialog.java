package app.ut.mafialottery;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class NewRoleDialog extends DialogFragment {

    public interface DialogListener {
        public void onPosBtnClick(DialogFragment dialog, View v);
        public void onNegBtnClick(DialogFragment dialog);
    }

    DialogListener dialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View formView = inflater.inflate(R.layout.new_role_dialog, null);
        builder.setView(formView)
                .setPositiveButton(R.string.dialog_newrole_posbtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onPosBtnClick(NewRoleDialog.this, formView);
                    }
                })
                .setNegativeButton(R.string.dialog_newrole_negbtn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogListener.onNegBtnClick(NewRoleDialog.this);
                    }
                });

        return builder.create();
    }
}
