package com.datn.ailms.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jmx.ParentAwareNamingStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service

public class CloudinaryService {


    private  Cloudinary cloudinary;




    public Map uploadFile(MultipartFile file) throws IOException {

        Map params = ObjectUtils.asMap("folder", "product_images");

        return cloudinary.uploader().upload(file.getBytes(), params);
    }


}
