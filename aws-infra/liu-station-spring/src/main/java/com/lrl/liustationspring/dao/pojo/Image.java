package com.lrl.liustationspring.dao.pojo;

import java.util.Date;

public class Image {
    private int imageId;

    private int productId;

    private String filename;

    private String file;

    private String type;

    private Date dateCreated;

    private String s3BucketPath;

    public Image() {
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", productId=" + productId +
                ", filename='" + filename + '\'' +
                ", file='" + file + '\'' +
                ", type='" + type + '\'' +
                ", dateCreated=" + dateCreated +
                ", s3BucketPath='" + s3BucketPath + '\'' +
                '}';
    }

    public ImageMeta getMeta(){
        return new ImageMeta(getImageId(),getProductId(),getFilename(),getDateCreated(),getS3BucketPath());
    }
}
