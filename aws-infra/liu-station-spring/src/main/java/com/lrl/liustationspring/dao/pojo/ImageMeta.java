package com.lrl.liustationspring.dao.pojo;

import java.util.Date;

public class ImageMeta {

    private int imageId;

    private int productId;

    private String filename;

    private Date dateCreated;

    private String s3BucketPath;

    public ImageMeta() {
    }

    public ImageMeta(int imageId, int productId, String filename, Date dateCreated, String s3BucketPath) {
        this.imageId = imageId;
        this.productId = productId;
        this.filename = filename;
        this.dateCreated = dateCreated;
        this.s3BucketPath = s3BucketPath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getS3BucketPath() {
        return s3BucketPath;
    }

    public void setS3BucketPath(String s3BucketPath) {
        this.s3BucketPath = s3BucketPath;
    }
}
