package com.bull.android.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


public class CommonDialog extends Dialog {
    public OnClickListener listener;
    protected View barDivider;
    protected View buttonDivider;
    protected FrameLayout container;
    protected View content;
    private final int contentPadding;

    protected TextView titleTv;
    protected Button negativeBt;
    protected Button positiveBt;
    protected OnClickListener dismissClick = new OnClickListener() {

        @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private Context mContext;

    public CommonDialog(Context context) {
        this(context, R.style.dialog_base);
    }

    public CommonDialog(Context context, int defStyle) {
        super(context, defStyle);
        mContext = context;
        contentPadding = 15;
        init(context);
    }

    @SuppressLint("InflateParams") @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void init(final Context context) {
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        content = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        titleTv = (TextView) content.findViewById(R.id.txt_title);
        container = (FrameLayout) content.findViewById(R.id.content_container);
        barDivider = content.findViewById(R.id.button_bar_divider);
        buttonDivider = content.findViewById(R.id.button_divder);
        positiveBt = (Button) content.findViewById(R.id.positive_bt);
        negativeBt = (Button) content.findViewById(R.id.negative_bt);

        super.setContentView(content);
    }

    @Override protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) ( content.getResources().getDisplayMetrics().widthPixels * 0.85);
        getWindow().setAttributes(params);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        this.dismiss();
    }

    public void setContent(View view) {
        setContent(view, contentPadding);
    }

    public void setContent(View view, int padding) {
        container.removeAllViews();
        container.setPadding(padding, padding, padding, padding);
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);
        container.addView(view, lp);
    }

    @Override public void setContentView(int i) {
        setContent(null);
    }

    @Override public void setContentView(View view) {
        setContentView(null, null);
    }

    @Override public void setContentView(View view, ViewGroup.LayoutParams layoutparams) {
        throw new Error("Dialog created error: Use setContent (View view) instead!");
    }

    public void setItems(BaseAdapter adapter, AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        listview.setDivider(null);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItems(CharSequence[] items, AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        listview.setAdapter(new DialogAdapter(items));
        listview.setDivider(null);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItemsWithoutChk(CharSequence[] items,
                                   AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        DialogAdapter adapter = new DialogAdapter(items);
        adapter.setShowChk(false);
        listview.setDivider(null);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setItems(CharSequence[] items, int index,
                         AdapterView.OnItemClickListener onItemClickListener) {
        ListView listview = new ListView(content.getContext());
        listview.setCacheColorHint(0);
        listview.setDivider(null);
        listview.setLayoutParams(
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listview.setAdapter(new DialogAdapter(items, index));
        listview.setOnItemClickListener(onItemClickListener);
        setContent(listview, 0);
    }

    public void setMessage(int resId) {
        setMessage(getContext().getResources().getString(resId));
    }

    public void setMessage(Spanned spanned, int... gravity) {

        TextView tvMessage = new TextView(getContext(), null, R.style.dialog_pinterest_text);
        tvMessage.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        tvMessage.setPadding(contentPadding, contentPadding, contentPadding, 0);
        //tvMessage.setLineSpacing(0.0F, 1.3F);
        tvMessage.setText(spanned);
        //tvMessage.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.content_size));
        tvMessage.setTextColor(getContext().getResources().getColor(android.R.color.black));
        tvMessage.setGravity(gravity.length == 0 ? Gravity.CENTER : gravity[0]);
        setContent(tvMessage, 0);
    }

    public void setMessage(String message) {
        setMessage(Html.fromHtml(message));
    }

    public void setMessage(String message, int gravity) {
        setMessage(Html.fromHtml(message), gravity);
    }

    public void setNegativeButton(int negative, OnClickListener listener) {
        setNegativeButton(getContext().getString(negative), listener);
    }

    public void setNegativeButton(String text, final OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            negativeBt.setText(text);
            negativeBt.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(CommonDialog.this, 0);
                    } else {
                        dismissClick.onClick(CommonDialog.this, 0);
                    }
                }
            });
            negativeBt.setVisibility(View.VISIBLE);
            if (positiveBt.getVisibility() == View.VISIBLE) buttonDivider.setVisibility(View.VISIBLE);
        } else {
            negativeBt.setVisibility(View.GONE);
            buttonDivider.setVisibility(View.GONE);
        }
        if (positiveBt.getVisibility() == View.VISIBLE || negativeBt.getVisibility() == View.VISIBLE) {
            barDivider.setVisibility(View.VISIBLE);
        } else {
            barDivider.setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(int positive, OnClickListener listener) {
        setPositiveButton(getContext().getString(positive), listener);
    }

    public void setPositiveButtonVisible(boolean visible) {
        positiveBt.setVisibility(visible ? View.VISIBLE : View.GONE);
        buttonDivider.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setPositiveButton(String positive, final OnClickListener listener) {
        if (!TextUtils.isEmpty(positive)) {
            positiveBt.setText(positive);
            positiveBt.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(CommonDialog.this, 0);
                    } else {
                        dismissClick.onClick(CommonDialog.this, 0);
                    }
                }
            });
            positiveBt.setVisibility(View.VISIBLE);
            if (negativeBt.getVisibility() == View.VISIBLE) buttonDivider.setVisibility(View.VISIBLE);
        } else {
            positiveBt.setVisibility(View.GONE);
            buttonDivider.setVisibility(View.GONE);
        }
        if (positiveBt.getVisibility() == View.VISIBLE || negativeBt.getVisibility() == View.VISIBLE) {
            barDivider.setVisibility(View.VISIBLE);
        } else {
            barDivider.setVisibility(View.GONE);
        }
    }

    @Override public void setTitle(int title) {
        setTitle((getContext().getResources().getString(title)));
    }

    @Override public void setTitle(CharSequence title) {
        if (title != null && title.length() > 0) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
        }
    }
}
