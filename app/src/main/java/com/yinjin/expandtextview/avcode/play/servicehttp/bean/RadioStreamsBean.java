package com.yinjin.expandtextview.avcode.play.servicehttp.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class RadioStreamsBean extends BaseBean implements Parcelable {

    private String bitstreamType;
    private String resolution;
    private String url;

    public String getBitstreamType() {
        return bitstreamType;
    }

    public void setBitstreamType(String bitstreamType) {
        this.bitstreamType = bitstreamType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bitstreamType);
        dest.writeString(this.resolution);
        dest.writeString(this.url);
    }

    public RadioStreamsBean() {
    }

    protected RadioStreamsBean(Parcel in) {
        this.bitstreamType = in.readString();
        this.resolution = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<RadioStreamsBean> CREATOR = new Parcelable.Creator<RadioStreamsBean>() {
        @Override
        public RadioStreamsBean createFromParcel(Parcel source) {
            return new RadioStreamsBean(source);
        }

        @Override
        public RadioStreamsBean[] newArray(int size) {
            return new RadioStreamsBean[size];
        }
    };
}
