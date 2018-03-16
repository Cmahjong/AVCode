package com.yinjin.expandtextview.avcode.play.servicehttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yinjin.expandtextview.avcode.play.util.BeanUtil;

import java.io.Serializable;


public class BaseBean implements Parcelable {


    @Override
    public String toString() {
        return BeanUtil.bean2string(this);
    }

    public BaseBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected BaseBean(Parcel in) {
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel source) {
            return new BaseBean(source);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };
}
