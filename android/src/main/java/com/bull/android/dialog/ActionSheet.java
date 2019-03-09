package com.bull.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class ActionSheet {
    private static Dialog dialog;
    private static TextView txt_title;
    private static TextView txt_cancel;
    private static LinearLayout lLayout_content;
    private static ScrollView sLayout_content;
    private static boolean showTitle = false;
    private static List<String> sheetItemList;
    private static Display display;

    public interface SelecteItemListener {
        void onSelecteItemListener(int postion, String text);
    }

    private static Context context;
    private static String title;
    private static List<String> items;
    private static SelecteItemListener selecteItemListener;

    public static void show(Context mcontext, String mtitle, List<String> mitems,
                            SelecteItemListener mselecteItemListener) {
        context = mcontext;
        title = mtitle;
        items = mitems;
        selecteItemListener = mselecteItemListener;
        initActionSheet();
        setSheetItems();
        dialog.show();
    }

    private static void initActionSheet() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());

        // 获取自定义Dialog布局中的控件
        sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
        lLayout_content = (LinearLayout) view.findViewById(R.id.lLayout_content);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        if (!isEmpty(title)) {
            showTitle = true;
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override public void onDismiss(DialogInterface dialog) {
                if (context instanceof ActionSheetDismissListener) {
                    ((ActionSheetDismissListener) context).onActionSheetDismiss();
                }
            }
        });

        sheetItemList = items;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        String input = String.valueOf(obj);
        if (input.length() == 0) return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置条目布局
     */
    private static void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // TODO 高度控制，非最佳解决办法
        // 添加条目过多的时候控制高度
        if (size >= 7) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) sLayout_content.getLayoutParams();
            params.height = display.getHeight() / 2;
            sLayout_content.setLayoutParams(params);
        }

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final String strItem = sheetItemList.get(i - 1);
            TextView textView = new TextView(context);
            textView.setText(strItem);
            textView.setTextSize(18f);
            textView.setGravity(Gravity.CENTER);

            // 背景图片
            if (size == 1) {
                if (showTitle) {
                    textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                } else {
                    textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            } else {
                if (showTitle) {
                    if (i >= 1 && i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (i < size) {
                        textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }

            textView.setTextColor(Color.parseColor("#4F4F4F"));

            // 高度
            float scale = context.getResources().getDisplayMetrics().density;
            int height = (int) (45 * scale + 0.5f);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

            // 点击事件
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    selecteItemListener.onSelecteItemListener(finalI - 1, strItem);
                    dialog.dismiss();
                }
            });

            lLayout_content.addView(textView);
        }
    }
}
