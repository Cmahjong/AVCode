package com.yinjin.expandtextview.avcode.play.servicehttp.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class RadioLiveChannelBean extends BaseBean implements Parcelable {

    private String id;
    private String name;
    private String img;
    private List<RadioStreamsBean> streams;
    private String shareUrl;
    private String liveSectionName;
    private String commentId;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getLiveSectionName() {
        return liveSectionName;
    }

    public void setLiveSectionName(String liveSectionName) {
        this.liveSectionName = liveSectionName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<RadioStreamsBean> getStreams() {
        return streams;
    }

    public void setStreams(List<RadioStreamsBean> streams) {
        this.streams = streams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.img);
        dest.writeList(this.streams);
        dest.writeString(this.shareUrl);
        dest.writeString(this.liveSectionName);
        dest.writeString(this.commentId);
    }

    public RadioLiveChannelBean() {
    }

    protected RadioLiveChannelBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.img = in.readString();
        this.streams = new ArrayList<RadioStreamsBean>();
        in.readList(this.streams, RadioStreamsBean.class.getClassLoader());
        this.shareUrl = in.readString();
        this.liveSectionName = in.readString();
        this.commentId = in.readString();
    }

    public static final Parcelable.Creator<RadioLiveChannelBean> CREATOR = new Parcelable.Creator<RadioLiveChannelBean>() {
        @Override
        public RadioLiveChannelBean createFromParcel(Parcel source) {
            return new RadioLiveChannelBean(source);
        }

        @Override
        public RadioLiveChannelBean[] newArray(int size) {
            return new RadioLiveChannelBean[size];
        }
    };
}
