package com.microservices.catalogservice.services;

import com.microservices.catalogservice.models.entities.Media;

import java.util.List;

public interface IMediaService {
    List<Media> getAllMediaUrl();
    List<Media> getMediaByCode(String code);
    List<Media> getMediaByType(String type);
    Media addMediaUrl(Media media);
}
