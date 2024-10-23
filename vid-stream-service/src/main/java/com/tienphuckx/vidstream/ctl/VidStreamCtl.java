package com.tienphuckx.vidstream.ctl;

import com.tienphuckx.vidstream.service.VidCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/stream")
public class VidStreamCtl {

    public static final Logger LOGGER = Logger.getLogger(VidStreamCtl.class.getName());

    public static final String VID_STREAM_PATH = "/home/tienphuckx/Downloads/";

    @Autowired
    private VidCatalogService vidCatalogService;


    @GetMapping("/vid/path/{vidPath}")
    public ResponseEntity<InputStreamResource> streamVid(@PathVariable String vidPath)
            throws FileNotFoundException {
        File vidFile = new File(VID_STREAM_PATH + vidPath);
        if (vidFile.exists()) {
            System.out.println("Looking for file at: " + vidFile.getAbsolutePath());
            InputStreamResource inputStreamResource
                    = new InputStreamResource(new FileInputStream(vidFile));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(inputStreamResource);

        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vid/id/{vidId}")
    public ResponseEntity<InputStreamResource> streamVidById(@PathVariable String vidId)
            throws FileNotFoundException {
        Long id = Long.parseLong(vidId);
        String path = vidCatalogService.getVidPath(id);
        LOGGER.log(Level.INFO, "Path get from catalog service: {0}", path);
        return streamVid(path);
    }

    @GetMapping("/vid/v2/id/{vidId}")
    public ResponseEntity<Resource> streamVidById_v2(@PathVariable String vidId)
            throws IOException {
        Long id = Long.parseLong(vidId);
        String path = vidCatalogService.getVidPath(id);
        LOGGER.log(Level.INFO, "Path retrieved from catalog service: {0}", path);

        // Ensure path is valid
        if (path == null || path.isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid video path retrieved for ID: {0}", id);
            return ResponseEntity.notFound().build();  // Return 404 if no valid path is found
        }

        // Forward to the streaming method
        return streamVid_v2(path, null);  // Call with no "Range" header initially

    }

    @GetMapping("/vid/v2/{vidPath}")
    public ResponseEntity<Resource> streamVid_v2(@PathVariable String vidPath,
                                              @RequestHeader(value = "Range", required = false) String httpRangeList)
            throws IOException {
        File vidFile = new File(VID_STREAM_PATH + "/" + vidPath);
        if (!vidFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        long fileLength = vidFile.length();
        String range = httpRangeList;

        if (range == null) {
            // Return full video file
            InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(vidFile));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .contentLength(fileLength)
                    .body(inputStreamResource);
        }

        // Handle range requests (for partial content delivery)
        long rangeStart = 0;
        long rangeEnd = fileLength - 1;

        // Parse the "Range" header
        String[] ranges = range.replace("bytes=", "").split("-");
        rangeStart = Long.parseLong(ranges[0]);
        if (ranges.length > 1) {
            rangeEnd = Long.parseLong(ranges[1]);
        }

        long contentLength = rangeEnd - rangeStart + 1;
        FileInputStream videoStream = new FileInputStream(vidFile);
        videoStream.skip(rangeStart);
        InputStreamResource inputStreamResource = new InputStreamResource(videoStream);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength)
                .contentLength(contentLength)
                .body(inputStreamResource);
    }

}
