package com.lrl.liustationspring.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    public String methodDispatcher(HttpServletRequest request, HttpServletResponse response, @PathVariable int pathProductId, @RequestBody String input){

        ObjectMapper mapper = new ObjectMapper();
        boolean isSingle = true;
        try{
            Image i = mapper.readValue(input, Image.class);
        } catch (JsonMappingException e) {
            isSingle = false;
        } catch (JsonProcessingException e) {
            isSingle = false;
        }


        try{
            Image[] images = mapper.readValue(input, Image[].class);
            System.out.println(images[0].getFilename() + ", "+ images[1].getFilename());
        } catch (JsonMappingException e) {
            if(!isSingle){
                logger.info("Neither single image nor multiple. Please check your input");
                response.setStatus(400);
                return "";
            }
        } catch (JsonProcessingException e) {
            if(!isSingle){
                logger.info("Neither single image nor multiple. Please check your input");
                response.setStatus(400);
                return "";
            }
        }

        if(isSingle) {
            try {
                ImageMeta meta = createNewImageForProduct(request, response, pathProductId, mapper.readValue(input, Image.class));
                return mapper.writeValueAsString(meta);
            } catch (JsonProcessingException e) {
                logger.error("This is not supposed to happen. Please check json data format");
            }
        }
        else {
            try {
                ImageMeta[] metas = createImagesForProduct(request, response, pathProductId, mapper.readValue(input, Image[].class));
                return mapper.writeValueAsString(metas);
            } catch (JsonProcessingException e) {
                logger.error("This is not supposed to happen. Please check json data format(multi)");
            }
        }

        response.setStatus(400);
        return "";
    }


    public ImageMeta createNewImageForProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int pathProductId, @RequestBody Image img) {

        logger.debug("Enter single image method.");

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


    public ImageMeta[] createImagesForProduct(HttpServletRequest request, HttpServletResponse response, @PathVariable int pathProductId, @RequestBody Image[] imgs){

        logger.debug("Enter multiple image method.");
        if (!authentication(request)) {
            response.setStatus(401);
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();


        logger.info("Read from inputStream: " + imgs.toString());

        if (!validateProductId(pathProductId, request)) {
            response.setStatus(400);
            logger.info("Validation for productId failed.");
            return null;
        }

        ImageMeta[] metas = new ImageMeta[imgs.length];

        for(int i = 0; i< imgs.length; i++){
            Image img = imgs[i];
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

            metas[i] = ImageDataService.getInstance().getMetaUsingPath(imgBase64);
        }

        return metas;

    }
    @RequestMapping(value = "/data/product/{pathProductId}/image/{imageId}", method = RequestMethod.DELETE)
    public String deleteImageFromProduct(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable int pathProductId, @PathVariable int imageId) {
        if (!authentication(request)) {
            response.setStatus(401);
            return null;
        }

        if(!validateProductId(pathProductId, request)){
            response.setStatus(400);
            return null;
        }

        if(!validateImageId(pathProductId, imageId)){
            response.setStatus(400);
            return null;
        }

        ImageMeta imageMeta = ImageDataService.getInstance().getImageUsingId(imageId);

        //Remove from local
        File file = new File(imageMeta.getS3BucketPath());
        if(file.exists()){
            file.delete();
        }
        else {
            logger.info("File doesn't exist.");
        }

        //Remove from S3 bucket
        removeFileFromBucket(file.getName());

        //Remove image meta
        ImageDataService.getInstance().deleteImageMetaById(imageId);

        return "Successfully removed";




    }


    public String saveImage(String base64Img, int productId, String filename, String suffix) {
        String dirPath = "img";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdir();
        logger.info("dir status after mkdir: " + dir.exists());

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


    //S3 manipulation
    public static void transferFile(File f) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        String bucketName = System.getenv("S3_NAME");

        s3.putObject(bucketName, f.getName(), f);
    }

    public static void removeFileFromBucket(String fileName){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        String bucketName = System.getenv("S3_NAME");

        s3.deleteObject(bucketName, fileName);
    }



    //Authentication methods
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

    private boolean validateProductId(int pathProductId, HttpServletRequest request) {

        String attr = request.getHeader("Authorization");
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");
        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        Product product = ProductDataService.getInstance().getProductInfo(pathProductId);
        if (product == null) return false;
        if (product.getId() != user.getId()) return false;

        return true;
    }


    private boolean validateImageId(int pathProductId, int imageId) {
        //Check if the ImageId belongs to the productId

        ImageMeta image = ImageDataService.getInstance().getImageUsingId(imageId);
        Product product = ProductDataService.getInstance().getProductInfo(pathProductId);

        if(image == null || product == null) return false;
        else if(image.getProductId() != product.getId()) return false;
        else return true;

    }

}
