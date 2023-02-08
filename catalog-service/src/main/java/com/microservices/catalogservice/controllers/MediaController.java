package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.services.IMediaService;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.responses.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final FileController fileController;
    private final IMediaService mediaService;

    @GetMapping("")
    public List<Media> getAllMediaUrl(){
        return mediaService.getAllMediaUrl();
    }
    @GetMapping("/{code}")
    public ResponseEntity<HashMap<Object, Object>> getMediaByCode(@PathVariable String code){
        List<Media> mediaList= mediaService.getMediaByCode(code);
        return ResponseEntity.ok().body(new HashMap<>(){{put("mediaList",mediaList);}});
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<HashMap<Object, Object>> getMediaByType(@PathVariable String type){
        List<Media> mediaList= mediaService.getMediaByType(type);
        return ResponseEntity.ok().body(new HashMap<>(){{put("mediaList",mediaList);}});
    }
    @PostMapping("/add")
    public ResponseEntity<?> addMedia(@RequestParam("code") String code,
                                      @RequestParam("type") String type,
                                      @RequestParam("media") MultipartFile multipartFile){
        try{
            Media media = Media.builder()
                    .code(code)
                    .type(type)
                    .build();
            UploadFileResponse uploadFileResponse = fileController.uploadFile(multipartFile);
            media.setImgUrl(uploadFileResponse.getFileDownloadUri());
            return ResponseEntity.ok(mediaService.addMediaUrl(media));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating media: "+ e.getMessage());
        }
    }
}
