package com.immymemine.kevin.skillshare.model.m_class;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by quf93 on 2017-12-05.
 */

public class Project /*implements Parcelable*/ {
    String _id;
    String projectId;
    String classId;
    String projectPictureUrl;

    public Project(String _id, String projectId, String classId, String projectPictureUrl) {
        this._id = _id;
        this.projectId = projectId;
        this.classId = classId;
        this.projectPictureUrl = projectPictureUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getProjectPictureUrl() {
        return projectPictureUrl;
    }

    public void setProjectPictureUrl(String projectPictureUrl) {
        this.projectPictureUrl = projectPictureUrl;
    }
    //    public Project(String _id, String imageUrl) {
//        this._id = _id;
//        this.imageUrl = imageUrl;
//    }
//
//    protected Project(Parcel in) {
//        _id = in.readString();
//        imageUrl = in.readString();
//    }
//
//    public static final Creator<Project> CREATOR = new Creator<Project>() {
//        @Override
//        public Project createFromParcel(Parcel in) {
//            return new Project(in);
//        }
//
//        @Override
//        public Project[] newArray(int size) {
//            return new Project[size];
//        }
//    };
//
//    public String get_id() {
//        return _id;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(_id);
//        dest.writeString(imageUrl);
//    }
}
