package com.datn.ailms.utils;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import  java.util.Map;
public class imageTest {



    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        System.out.println(cloudinary.config.cloudName);

        Map params1 = ObjectUtils.asMap(
                "use_filename", true,
                "unique_filename", false,
                "overwrite", true
        );

//        try {
//            System.out.println(
//                    cloudinary.uploader().upload("https://cloudinary-devs.github.io/cld-docs-assets/assets/images/coffee_cup.jpg", params1));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            Map params2 = ObjectUtils.asMap(
                    "quality_analysis", true
            );

            System.out.println("get:  "+
                    cloudinary.api().resource("coffee_cup", params2));
        }catch (IOException e){
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
