package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.services.IMediaService;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.repositories.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements IMediaService {
    private final MediaRepository mediaRepository;

    @Override
    public List<Media> getAllMediaUrl() {
        return mediaRepository.findAll();
    }

    @Override
    public List<Media> getMediaByCode(String code) {
        return mediaRepository.findByCode(code);
    }

    @Override
    public List<Media> getMediaByType(String type) {
        return mediaRepository.findByType(type);
    }

    @Override
    public Media addMediaUrl(Media media) {
        return mediaRepository.save(media);
    }
}
