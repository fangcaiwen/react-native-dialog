# react-native-dialog
react-native调用android原生弹窗

import React, { PureComponent } from 'react';
import { NativeModules,ActionSheetIOS,Alert,AlertIOS } from 'react-native';
import { dimens } from '../../resource/index';

export default class Dialog extends PureComponent {

    // Dialog.showActionSheet('标题',['相册','拍照'],(buttonIndex) => {
    //    console.log(buttonIndex);
    // });

    static showActionSheet(title, mOptions, callBack) {
        if (dimens.isAndroid) {
            NativeModules.Dialog.ActionSheetShow(title,mOptions,callBack);
        } else {
            const newObj = {
                options:mOptions.concat(['取消']),
                cancelButtonIndex: mOptions.length
            };
            if(title.length>0) {
                newObj.title=title;
            }
            ActionSheetIOS.showActionSheetWithOptions(newObj,callBack);
        }
    };

    // Dialog.showAlert('标题','信息？',[{text:'确定',onPress:() => {
    //      此处写事件
    //     }},{text:'取消'}]);

    static showAlert(title,message,actions){
        if(dimens.isAndroid){
            let titleF,
                actionF,
                titleS,
                actionS;
            if(!actions[0]) {
                titleF = '';
                actionF = null;
            }else{
                titleF = actions[0].text;
                actionF = actions[0].onPress;
            }

            if(!actions[1]) {
                titleS = '';
                actionS = null;
            }else{
                titleS = actions[1].text;
                actionS = actions[1].onPress;
            }

            NativeModules.Dialog.showAlert(title,message,titleF,titleS,!actionF?() => {}:() => actionF(),!actionS?() => {}:() => actionS() );
        }else{
            Alert.alert(title,message,actions);
        }
    }

    // Dialog.showPrompt('弹窗输入标题','弹窗输入信息',[{text:'确定',onPress:(value) => {
    //         console.log(value);
    //     }},{text:'取消'}]);

    static showPrompt(title,message,actions){
        if(dimens.isAndroid){
            let titleF,
                actionF,
                titleS,
                actionS;
            if(!actions[0]) {
                titleF = '';
                actionF = null;
            }else{
                titleF = actions[0].text;
                actionF = actions[0].onPress;
            }

            if(!actions[1]) {
                titleS = '';
                actionS = null;
            }else{
                titleS = actions[1].text;
                actionS = actions[1].onPress;
            }

            NativeModules.Dialog.showPrompt(title,message,titleS,titleF,!actionS?() => {}:() => actionS(),!actionF?() => {}:(value) => actionF(value));
        }else{
            AlertIOS.prompt(title,message,actions);
        }
    }
}
