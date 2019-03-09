package com.bull.android.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class InputDialog extends CommonDialog {
    private EditText mEdit = null;
    public InputDialog(Context context) {
        super(context, R.style.dialog_common);
        View shareView = getLayoutInflater().inflate(R.layout.view_dialog_input_layout, null);
        mEdit = (EditText)shareView.findViewById(R.id.mEditText);

        setContent(shareView, 0);
        initView();
    }

    private void initView() {

    }

    public EditText getInputEditText() {
        return mEdit;
    }

    public String getInputText() {
        return mEdit.getText().toString();
    }
}
