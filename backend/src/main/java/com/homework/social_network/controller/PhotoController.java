package com.homework.social_network.controller;

import com.homework.social_network.dto.response.PhotoResponseDto;
import com.homework.social_network.dto.response.PhotosResponseDto;
import com.homework.social_network.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @RequestMapping(value = "/{id}/add", method = RequestMethod.POST)
    public void addPhotos(@PathVariable UUID id, @ModelAttribute List<MultipartFile> photos) {
        photoService.addPhotos(id, photos);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PhotosResponseDto getPhotos(@PathVariable UUID id, @RequestParam int start, @RequestParam int end) {
        return photoService.getPhotos(id, start, end);
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.POST)
    public void remove(@RequestBody List<UUID> ids) {
        photoService.removePhotos(ids);
    }

}
