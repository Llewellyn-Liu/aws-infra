package com.lrl.liustationspring.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lrl.liustationspring.dao.pojo.Image;
import com.lrl.liustationspring.dao.pojo.ImageMeta;
import com.lrl.liustationspring.dao.pojo.Product;
import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.service.DataManipulationService;
import com.lrl.liustationspring.service.ImageDataService;
import com.lrl.liustationspring.service.ProductDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Base64;
import java.util.Date;

@RestController
public class ProductImageController {
    Logger logger = LoggerFactory.getLogger(ProductImageController.class);

    @RequestMapping(value = "/data/product/{id}/image", method = RequestMethod.GET)
    public Image[] readImagesUsingProductId(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
        if (!authentication(request)) {
            response.setStatus(401);
            logger.info("Authentication Failed.");
            return null;
        }

        ImageDataService imageService = ImageDataService.getInstance();
        return imageService.getImagesUsingProductId(id);
    }

    @RequestMapping(value = "/data/product/{pathProductId}/image", method = RequestMethod.POST)
    public ImageMeta createNewImageForProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int pathProductId, @RequestBody Image img) {

        if (!authentication(request)) {
            response.setStatus(401);
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();


        logger.info("Read from inputStream: " + img.toString());

        if (!validateProductId(pathProductId, request)) {
            response.setStatus(400);
            logger.info("Validation for productId failed.");
            return null;
        }

        String imgBase64 = saveImage(img.getFile(), pathProductId, img.getFilename(), img.getType());
        logger.info("saved path: " + imgBase64);
        img.setS3BucketPath(imgBase64);
        img.setProductId(pathProductId);
        img.setDateCreated(new Date(System.currentTimeMillis()));
        boolean addSuccess = ImageDataService.getInstance().addImage(img);

        if (!addSuccess) {
            logger.info("Image cannot be added: " + img.toString());
            return null;
        }

        ImageMeta rev = ImageDataService.getInstance().getMetaUsingPath(imgBase64);


        return rev;

    }

    @RequestMapping(value = "/data/product/{productId}/image/{imageId}", method = RequestMethod.GET)
    public ImageMeta getMetaUsingId(HttpServletRequest request, HttpServletResponse response, @PathVariable int productId, @PathVariable int imageId) {

        if (!authentication(request)) {
            response.setStatus(401);
            logger.info("Authentication Failed.");
            return null;
        }

        if(!validateProductId(productId, request)) {
            response.setStatus(400);
            logger.info("ProductId validation failed");
            return null;
        }

        ImageMeta meta = ImageDataService.getInstance().getMetaUsingId(imageId);
        if(meta == null){
            response.setStatus(400);
            return null;
        }

        return meta;
    }

    @RequestMapping(value = "/data/product/{productId}/image/{imageId}", method = RequestMethod.DELETE)
    public void deleteImageUsingId(HttpServletRequest request, HttpServletResponse response, @PathVariable int productId, @PathVariable int imageId){
        if (!authentication(request)) {
            response.setStatus(401);
            logger.info("Authentication Failed.");
            return;
        }

        if(!validateProductId(productId, request)) {
            response.setStatus(400);
            logger.info("ProductId validation failed");
            return;
        }

        //Delete file record
        ImageMeta meta = ImageDataService.getInstance().getMetaUsingId(imageId);
        ImageDataService.getInstance().deleteMetaUsingId(meta.getImageId());

        //Delete file on disks
        if(!deleteFile(new File(meta.getS3BucketPath()))){
            response.setStatus(404);
            logger.info("delete failed: file not exists locally or on s3");
        }

        response.setStatus(204);
    }


    private boolean validateProductId(int pathProductId, HttpServletRequest request) {

        String attr = request.getHeader("Authorization");
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");
        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        Product product = ProductDataService.getInstance().getProductInfo(pathProductId);
        if (product == null || product.getOwnerUserId() != user.getId()) return false;
        if (product.getId() != pathProductId) return false;

        return true;
    }

    private String saveImage(String base64Img, int productId, String filename, String suffix) {
        String dirPath = "img";
        File dir = new File(dirPath);
        if(!dir.exists()) dir.mkdir();
        logger.info("dir status after mkdir: "+dir.exists());

        File file = new File(dir + "/product-" + productId + "-img-" + (int) (Math.random() * 10000) + "-" + filename + "." + suffix);

        System.out.println(file.getAbsolutePath());
        String[] imgStructure = base64Img.split(",");
        byte[] decodedImg = Base64.getDecoder().decode(imgStructure[1]);

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(decodedImg);
            out.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        transferFile(file);

        return file.getAbsolutePath();
    }



    /**
     *
     * @param f
     */
    private void transferFile(File f){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        String bucketName = "this-is-lrl-bucket-01";

        s3.putObject(bucketName, f.getName(), f);
    }

    private boolean deleteFile(File f){

        //Delete from local
        if(!f.exists()){
            logger.info("File not exists.");
            return false;
        }
        f.delete();
        //Delete from
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        String bucketName = "this-is-lrl-bucket-01";

        if(!s3.doesObjectExist(bucketName, f.getName())){
            return false;
        }
        s3.deleteObject(bucketName, f.getName());
        return true;
    }

    private boolean authentication(HttpServletRequest request) {
        String attr = request.getHeader("Authorization");
        if (attr == null) return false;

        logger.info("Authenticating: WWW-Authenticate field perceived: " + attr);
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");
        logger.info("Authenticating: Name: " + decodedToken[0] + "Pswd: " + decodedToken[1]);

        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        if (user == null) return false;

        return BCrypt.checkpw(decodedToken[1], user.getPassword());
    }
}
