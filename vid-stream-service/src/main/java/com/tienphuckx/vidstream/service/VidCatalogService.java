package com.tienphuckx.vidstream.service;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VidCatalogService {

    public static final String CATALOG_SERVICE = "http://vid-catalog-service";

    @Autowired
    RestTemplate restTemplate;

    // call api of vid-catalog-service
    public String getVidPath(Long vidId){
        var res = restTemplate.getForEntity(CATALOG_SERVICE + "/api/catalog/vid/find-path-by-id/{vidId}",
                String.class, vidId);
        return res.getBody();
    }
}
