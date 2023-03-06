package com.lrl.liustationspring;

import com.lrl.liustationspring.service.Bootstrapper;
import com.lrl.liustationspring.service.S3BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;

import javax.sql.DataSource;

@SpringBootApplication
public class LiuStationSpringApplication {

    private static Logger logger = LoggerFactory.getLogger(LiuStationSpringApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(LiuStationSpringApplication.class, args);

        Bootstrapper.bootstrap();

        try{
            S3BucketService.readContext();
        }
        catch (Exception e){
            logger.warn("Check if running locally. If not, aws service has problems: " + e.getMessage());
        }

    }

}
