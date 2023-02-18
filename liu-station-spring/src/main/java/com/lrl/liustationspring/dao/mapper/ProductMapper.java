package com.lrl.liustationspring.dao.mapper;

import com.lrl.liustationspring.dao.pojo.Product;
import com.lrl.liustationspring.service.ProductDataService;
import org.apache.ibatis.annotations.Options;

import java.util.Date;

public interface ProductMapper {

    int insertNewProduct(Product product);

    Product getProductById(int id);

    Product getProductByTimeCreated(Date date);

    int updateProduct(Product product);

    int deleteProductData(int id);
}
