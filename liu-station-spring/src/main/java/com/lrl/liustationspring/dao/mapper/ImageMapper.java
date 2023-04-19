package com.lrl.liustationspring.dao.mapper;

import com.lrl.liustationspring.dao.pojo.Image;
import com.lrl.liustationspring.dao.pojo.ImageMeta;
import com.lrl.liustationspring.dao.pojo.Product;

import java.util.Date;

public interface ImageMapper {

    Image[] getImagesUsingProductId(int productId);

    int addImage(Image image);

    ImageMeta getMetaUsingPath(String path);

    Product getProductById(int id);

    Product getProductByTimeCreated(Date date);

    int updateProduct(Product product);

    int deleteImageMeta(int imageId);

    ImageMeta getMetaUsingId(int imageId);
}
