package jdo;

import java.io.Serializable;

public class Books implements Serializable {

    String mId, mTitle, mDescription, mPageCount, mExcerpt, mPublishDate;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmPageCount() {
        return mPageCount;
    }

    public void setmPageCount(String mPageCount) {
        this.mPageCount = mPageCount;
    }

    public String getmExcerpt() {
        return mExcerpt;
    }

    public void setmExcerpt(String mExcerpt) {
        this.mExcerpt = mExcerpt;
    }

    public String getmPublishDate() {
        return mPublishDate;
    }

    public void setmPublishDate(String mPublishDate) {
        this.mPublishDate = mPublishDate;
    }

    @Override
    public String toString() {
        return "{" +
                "mId=\"" + mId + "\"" +
                ", mTitle=\"" + mTitle + "\"" +
                ", mDescription=\"" + mDescription + "\"" +
                ", mPageCount=\"" + mPageCount + "\"" +
                ", mExcerpt=\"" + mExcerpt + "\"" +
                ", mPublishDate=\"" + mPublishDate + "\"" +
                '}';
    }
}
