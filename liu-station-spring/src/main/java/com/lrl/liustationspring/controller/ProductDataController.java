package com.lrl.liustationspring.controller;

import com.lrl.liustationspring.dao.pojo.Product;
import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.service.DataManipulationService;
import com.lrl.liustationspring.service.ProductDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

@RestController
public class ProductDataController {

    Logger logger = LoggerFactory.getLogger(ProductDataController.class);

    @RequestMapping(value = "/data/product/{productId}", method = RequestMethod.GET)
    private Product getProductDataById(@PathVariable int productId){
        return ProductDataService.getInstance().getProductInfo(productId);
    }

    @RequestMapping(value = "/data/product", method = RequestMethod.POST)
    private Product createNewProductData(@RequestParam(value = "name") String name,
                                         @RequestParam(value = "description") String description,
                                         @RequestParam(value = "sku") String sku,
                                         @RequestParam(value = "manufacturer") String manufacturer,
                                         @RequestParam(value = "quantity")int quantity,
                                         HttpServletRequest request,
                                         HttpServletResponse response){
        if(quantity < 0)
            response.setStatus(400);

        if(request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return null;
        }

        if(!authentication(request)){
            logger.info("Authentication failed.");
            response.setStatus(403);
            return null;
        }

        int userId = readCurrentUserId(request);
        System.out.println("This,"+userId);
        Date now = new Date();
        Product product = new Product(null, name, description, sku, manufacturer, now, now, userId, quantity);


        ProductDataService.getInstance().createProductInfo(product);
        Product returnValue = ProductDataService.getInstance().getProductByTimeCreated(product.getDateAdded());


        return returnValue;
    }

    @RequestMapping(value = "/data/product/{id}", method = RequestMethod.PUT)
    private Product updateProductDataById(@PathVariable int id,
                                          @RequestParam(value = "name") String name,
                                          @RequestParam(value = "description") String description,
                                          @RequestParam(value = "sku") String sku,
                                          @RequestParam(value = "manufacturer") String manufacturer,
                                          @RequestParam(value = "quantity")int quantity,
                                          HttpServletRequest request,
                                          HttpServletResponse response){

        if(ProductDataService.getInstance().getProductInfo(id) == null){
            response.setStatus(204);
            return null;
        }

        if(quantity < 0) {
            logger.info("Quantity lower than 0");
            response.setStatus(400);
            return null;
        }

        if(request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return null;
        }

        if(!authentication(request)){
            response.setStatus(403);
            return null;
        }

        if(!checkCorrectOwner(request, id)){
            response.setStatus(400);
            return null;
        }

        Product product = ProductDataService.getInstance().getProductInfo(id);
        product.setName(name);
        product.setDescription(description);
        product.setSku(sku);
        product.setManufacturer(manufacturer);
        product.setQuantity(quantity);
        product.setDateLastModified(new Date());

        return ProductDataService.getInstance().updateProduct(product);

    }


    @RequestMapping(value = "/data/product/{id}", method = RequestMethod.PATCH)
    private Product updateProductDataByIdWithPatch(@PathVariable int id,
                                          @RequestParam(value = "name") String name,
                                          @RequestParam(value = "description") String description,
                                          @RequestParam(value = "sku") String sku,
                                          @RequestParam(value = "manufacturer") String manufacturer,
                                          @RequestParam(value = "quantity")int quantity,
                                          HttpServletRequest request,
                                          HttpServletResponse response){
        if(quantity < 0) {
            logger.info("Quantity lower than 0");
            response.setStatus(400);
            return null;
        }
        if(!authentication(request)){
            response.setStatus(401);
            return null;
        }

        if(ProductDataService.getInstance().getProductInfo(id) == null){
            response.setStatus(400);
            return null;
        }

        if(!checkCorrectOwner(request, id)){
            response.setStatus(403);
            return null;
        }

        Product product = ProductDataService.getInstance().getProductInfo(id);
        product.setName(name);
        product.setDescription(description);
        product.setSku(sku);
        product.setManufacturer(manufacturer);
        product.setQuantity(quantity);
        product.setDateLastModified(new Date());

        return ProductDataService.getInstance().updateProduct(product);

    }

    @RequestMapping(value = "/data/product/{id}", method = RequestMethod.DELETE)
    private String deleteProductData(@PathVariable int id,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){

        if(request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return null;
        }

        if(!authentication(request)){
            response.setStatus(403);
            return null;
        }

        if(ProductDataService.getInstance().getProductInfo(id) == null){
            response.setStatus(400);
            return null;
        }

        if(!checkCorrectOwner(request, id)){
            response.setStatus(403);
            return null;
        }


        if(ProductDataService.getInstance().deleteProductData(id)){
            logger.info("Delete successfully");
            return "Success";
        }

        return "Delete Failed";

    }


    //Don't delete!!!
    private int readCurrentUserId(HttpServletRequest request) {
        String attr = request.getHeader("Authorization");
        if(attr == null) {
            logger.info("No header Authorization found.");
            return -1;
        }

        logger.info("WWW-Authenticate field perceived: " + attr);
        //position 0 has the Authorization Label "Basic", pos 1 has encoded username + password
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");

        return DataManipulationService.getInstance().getIdByUsername(decodedToken[0]);
    }



    private boolean authentication(HttpServletRequest request) {
        String attr = request.getHeader("Authorization");
        if(attr == null) return false;

        logger.info("WWW-Authenticate field perceived: " + attr);
        //position 0 has Authorization Labels, pos 1 has encoded username + password
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");

        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        return BCrypt.checkpw(decodedToken[1], user.getPassword());
    }

    private boolean checkCorrectOwner(HttpServletRequest request, int id) {
        String attr = request.getHeader("Authorization");
        if(attr == null) return false;

        logger.info("WWW-Authenticate field perceived: " + attr);
        //position 0 has Authorization Labels, pos 1 has encoded username + password
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");

        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        Product product = ProductDataService.getInstance().getProductInfo(id);

        if(user.getId() == product.getOwnerUserId()) {
            System.out.println("Correct!" + user.toString() + product.toString());
            return true;
        }
        else {
            System.out.println("Wrong!!!" + user.toString() + product.toString());
            return false;
        }
    }

}
