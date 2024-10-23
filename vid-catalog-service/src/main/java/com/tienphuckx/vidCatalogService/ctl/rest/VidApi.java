package com.tienphuckx.vidCatalogService.ctl.rest;

import com.tienphuckx.vidCatalogService.entity.Vid;
import com.tienphuckx.vidCatalogService.repo.VidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class VidApi {

    @Autowired
    private VidRepository vidRepository;

    @PostMapping("/vid/add")
    public List<Vid> addVids(@RequestBody List<Vid> vids) {
        return vidRepository.saveAll(vids);
    }

    @GetMapping("/vid/list")
    public List<Vid> getVids() {
        return vidRepository.findAll();
    }

    // this api for service stream call
    @GetMapping("/vid/find-path-by-id/{vidId}")
    public String getVidById(@PathVariable("vidId") Long vidId) {
        return vidRepository.findById(vidId)
                .map(Vid::getVidPath)
                .orElse(null);
    }


}
