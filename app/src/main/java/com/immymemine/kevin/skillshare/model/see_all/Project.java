package com.immymemine.kevin.skillshare.model.see_all;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by quf93 on 2017-12-10.
 */

public class Project implements Parcelable {
    String _id;
    String projectPictureUrl;
    String projectLikeCount;
    String projectTitle;
    String subscriberName;

    public Project(String _id, String projectPictureUrl, String projectLikeCount, String projectTitle, String subscriberName) {
        this._id = _id;
        this.projectPictureUrl = projectPictureUrl;
        this.projectLikeCount = projectLikeCount;
        this.projectTitle = projectTitle;
        this.subscriberName = subscriberName;
    }

    protected Project(Parcel in) {
        _id = in.readString();
        projectPictureUrl = in.readString();
        projectLikeCount = in.readString();
        projectTitle = in.readString();
        subscriberName = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public String getProjectPictureUrl() {
        return projectPictureUrl;
    }

    public String getProjectLikeCount() {
        return projectLikeCount;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(projectPictureUrl);
        dest.writeString(projectLikeCount);
        dest.writeString(projectTitle);
        dest.writeString(subscriberName);
    }
}
