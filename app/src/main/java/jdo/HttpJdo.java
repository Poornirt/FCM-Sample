package jdo;

import java.util.HashMap;

public class HttpJdo {
    String mUrl,mRequestmethod,mResponsebody;
    int mRequestcode;
    String mPayload;
    HashMap<String,String> mHeader;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmRequestmethod() {
        return mRequestmethod;
    }

    public void setmRequestmethod(String mRequestmethod) {
        this.mRequestmethod = mRequestmethod;
    }

    public String getmResponsebody() {
        return mResponsebody;
    }

    public void setmResponsebody(String mResponsebody) {
        this.mResponsebody = mResponsebody;
    }

    public int getmRequestcode() {
        return mRequestcode;
    }

    public void setmRequestcode(int mRequestcode) {
        this.mRequestcode = mRequestcode;
    }

    public HashMap<String, String> getmHeader() {
        return mHeader;
    }

    public void setmHeader(HashMap<String, String> mHeader) {
        this.mHeader = mHeader;
    }

    public String getmPayload() {
        return mPayload;
    }

    public void setmPayload(String mPayload) {
        this.mPayload = mPayload;
    }

    @Override
    public String toString() {
        return "HttpJdo{" +
                "mUrl='" + mUrl + '\'' +
                ", mRequestmethod='" + mRequestmethod + '\'' +
                ", mResponsebody='" + mResponsebody + '\'' +
                ", mRequestcode=" + mRequestcode +
                ", mPayload=" + mPayload +
                ", mHeader=" + mHeader +
                '}';
    }
}
