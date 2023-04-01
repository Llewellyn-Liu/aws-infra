package com.lrl.liustationspring.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.io.File;
import java.util.List;

public class S3BucketService {

    public static void readContext(){
        System.out.println("enter readContext()");
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        System.out.println("after build default client)");
        List<Bucket> list = s3.listBuckets();
        System.out.println("Bucket List: ");
        for(Bucket bk: list){
            System.out.println(bk.getName());
        }
    }

    public static void transferFile(){
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        File f = new File("test.txt");
        String bucketName = System.getenv("s3_bucket_name");
    }
}
