package operator.controllers;

import operator.models.Image;
import operator.repositories.ImageRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class ImageController {

    private final ImageRepository imageRepository;

    public ImageController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @GetMapping("/image/{id}")
    private ResponseEntity<?> getImageByProductId(@PathVariable Long id) {
        Image image = imageRepository.findById(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

}