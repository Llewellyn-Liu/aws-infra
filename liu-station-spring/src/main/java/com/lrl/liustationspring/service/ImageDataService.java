package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.mapper.ImageMapper;
import com.lrl.liustationspring.dao.pojo.Image;
import com.lrl.liustationspring.dao.pojo.ImageMeta;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageDataService {

    Logger logger = LoggerFactory.getLogger(ImageDataService.class);

    private static ImageDataService instance = new ImageDataService();

    public Image[] getImagesUsingProductId(int id){

        SqlSession session = SqlConnection.getSession();

        ImageMapper mapper = session.getMapper(ImageMapper.class);

        return mapper.getImagesUsingProductId(id);
    }

    public boolean addImage(Image image){
        SqlSession session = SqlConnection.getSession();
        ImageMapper mapper = session.getMapper(ImageMapper.class);


        logger.info("Debug: Service use image:" + image.toString());
        boolean success = 1 == mapper.addImage(image);

        if(!success) logger.info("Cannot add Image: "+ image.toString());
        return success;
    }

    public ImageMeta getMetaUsingPath(String path){
        SqlSession session = SqlConnection.getSession();
        ImageMapper mapper = session.getMapper(ImageMapper.class);

        return mapper.getMetaUsingPath(path);
    }

    public ImageMeta getMetaUsingId(int id){
        SqlSession session = SqlConnection.getSession();
        ImageMapper mapper = session.getMapper(ImageMapper.class);

        return mapper.getMetaUsingId(id);
    }

    public void deleteMetaUsingId(int imageId) {
        SqlSession session = SqlConnection.getSession();
        ImageMapper mapper = session.getMapper(ImageMapper.class);
        mapper.deleteImageMeta(imageId);
    }

    public static ImageDataService getInstance() {
        return instance;
    }
}




