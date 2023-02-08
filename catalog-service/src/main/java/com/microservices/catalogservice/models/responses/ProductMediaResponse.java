package com.microservices.catalogservice.models.responses;

import com.microservices.catalogservice.models.dtos.MediaDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductMediaResponse {
    private List<MediaDto> mediaList;
}
