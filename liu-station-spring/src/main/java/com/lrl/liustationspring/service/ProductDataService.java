package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.mapper.ProductMapper;
import com.lrl.liustationspring.dao.pojo.Product;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class ProductDataService {

    private static ProductDataService instance = new ProductDataService();

    Logger logger = LoggerFactory.getLogger(ProductDataService.class);

    public Product getProductInfo(Integer id){

        SqlSession session = SqlConnection.getSession();

        ProductMapper mapper = session.getMapper(ProductMapper.class);

        return mapper.getProductById(id);
    }

    public int createProductInfo(Product product){
        SqlSession session = SqlConnection.getSession();

        ProductMapper mapper = session.getMapper(ProductMapper.class);

        int rev = mapper.insertNewProduct(product);

        logger.debug("insert ->" + product.toString() + " return value: "+ rev);

        return rev;
    }

    public Product getProductByTimeCreated(Date date){
        SqlSession session = SqlConnection.getSession();

        ProductMapper mapper = session.getMapper(ProductMapper.class);

        return mapper.getProductByTimeCreated(date);
    }

    public Product updateProduct(Product product){
        SqlSession session = SqlConnection.getSession();

        ProductMapper mapper = session.getMapper(ProductMapper.class);

        mapper.updateProduct(product);

        return mapper.getProductById(product.getId());
    }

    public boolean deleteProductData(int id){
        SqlSession session = SqlConnection.getSession();

        ProductMapper mapper = session.getMapper(ProductMapper.class);

        return mapper.deleteProductData(id) == 1;
    }
    public static ProductDataService getInstance() {
        return instance;
    }
}
