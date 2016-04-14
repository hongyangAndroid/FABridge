package com.zhy.fabridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.zhy.fabridge.lib.Fabridge;


/**
 * Created by jeff on 2014-04-11.
 */
public class NewMenuItemDialog extends DialogFragment
{

    public static final String EVENT_CREATE_NEW_ITEM = "event_create_new_item";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView;
        AlertDialog.Builder dialogBuilder;

        dialogView = inflater.inflate(R.layout.dialog_folder, null);
        dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("请输入Item标题");
        dialogBuilder.setView(dialogView);

        final EditText folderNameEditText = (EditText) dialogView.findViewById(R.id.folder_name);
        dialogBuilder.setPositiveButton("确定", new
                DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // TODO Broadcast result to MainActivity
                        Fabridge.call(getActivity(), EVENT_CREATE_NEW_ITEM, folderNameEditText.getText().toString());
                    }
                });

        dialogBuilder.setNegativeButton("取消", new
                DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.show();


        return dialog;
    }

}
