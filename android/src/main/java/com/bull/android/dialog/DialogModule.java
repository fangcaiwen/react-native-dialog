package com.bull.android.dialog;

import android.content.DialogInterface;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

import java.util.ArrayList;
import java.util.List;

public class DialogModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext context;

    public DialogModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }
    @Override
    public String getName() {
        return "Dialog";
    }

    @ReactMethod
    public void showAlert(String title, String message, String leftText, String rightText,final Callback leftCb, final Callback rightCb) {
        final CommonDialog commonDialog = new CommonDialog(getCurrentActivity(), R.style.dialog_common);
        commonDialog.setTitle(title);
        commonDialog.setMessage(message);
        commonDialog.setPositiveButton(rightText, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(rightCb!=null){
                    rightCb.invoke();
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.setNegativeButton(leftText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(leftCb!=null){
                    leftCb.invoke();
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    @ReactMethod
    public void showPrompt(String title, String rightText, String leftText, final Callback rightCb,final Callback leftCb) {
        final InputDialog builder = new InputDialog(getCurrentActivity());
        builder.setTitle(title) ;
        builder.setCancelable(false);
        builder.setPositiveButton(rightText, new DialogInterface.OnClickListener( ) {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(rightCb!=null){
                    rightCb.invoke();
                }
                builder.dismiss();
            }
        });
        builder.setNegativeButton(leftText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(leftCb!=null){
                    String txt = builder.getInputText().toString();
                    if(txt.length()==0){
                        Toast.makeText(context,"请输入...",Toast.LENGTH_SHORT).show();
                    }else{
                        leftCb.invoke(txt);
                        builder.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    //仿ios底部弹窗
    @ReactMethod
    public void ActionSheetShow(String mTitle, final ReadableArray mArray, final Callback cb){
        String title=null;
        if(mTitle.length()>0){
            title=mTitle;
        }
        List<String> mList = new ArrayList<String>();
        for(int i=0;i<mArray.size();i++){
            mList.add(i,mArray.getString(i));
        }
        ActionSheet.show(getCurrentActivity(), title, mList, new ActionSheet.SelecteItemListener() {
            @Override
            public void onSelecteItemListener(int postion, String text) {
                cb.invoke(String.valueOf(postion));
            }
        });
    }
}
