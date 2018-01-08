package com.aptoide.uploader.account.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.aptoide.uploader.R;

/**
 * Created by franciscocalado on 1/8/18.
 */

public class CreateStoreDialogFragment extends DialogFragment {

  private EditText storeName;
  private RadioButton privateButton;
  private RadioButton publicButton;
  private EditText storeNamePrivate;
  private EditText storePassword;

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {

    ContextThemeWrapper wrapper = new ContextThemeWrapper(getContext(), R.style.RepoDialog);

    AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);

    LayoutInflater inflater = LayoutInflater.from(wrapper);
    View view = inflater.inflate(R.layout.create_store_dialog, null);

    storeName = (EditText) view.findViewById(R.id.store_name);
    privateButton = (RadioButton) view.findViewById(R.id.private_store);
    publicButton = (RadioButton) view.findViewById(R.id.public_store);
    storeNamePrivate = (EditText) view.findViewById(R.id.username_private);
    storePassword = (EditText) view.findViewById(R.id.password_private);

    builder.setTitle(R.string.repo_creation_title)
        .setView(view)
        .setPositiveButton(R.string.create_repo, (dialogInterface, i) -> {
          //TODO Logic to send the store creation;
          Toast.makeText(getContext(), "create store view not implemented", Toast.LENGTH_SHORT)
              .show();
        });

    privateButton.setOnClickListener(view1 -> {
      if (privateButton.isChecked()) {
        storeNamePrivate.setVisibility(View.VISIBLE);
        storePassword.setVisibility(View.VISIBLE);
      }
    });

    publicButton.setOnClickListener(view12 -> {
      if (publicButton.isChecked()) {
        storeNamePrivate.setVisibility(View.GONE);
        storePassword.setVisibility(View.GONE);
      }
    });
    return builder.create();
  }

  @Override public void onStart() {
    super.onStart();

    AlertDialog dialog = (AlertDialog) getDialog();
    if (dialog != null) {
      Button submit = dialog.getButton(Dialog.BUTTON_POSITIVE);
      submit.setOnClickListener(view -> {
        //TODO Logic to send the store creation;
        Toast.makeText(getContext(), "create store view not implemented", Toast.LENGTH_SHORT)
            .show();
      });
    }
  }

  @Override public void onResume() {
    super.onResume();

    if (privateButton.isChecked()) {
      storeNamePrivate.setVisibility(View.VISIBLE);
      storePassword.setVisibility(View.VISIBLE);
    }
  }
}
