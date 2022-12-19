package operator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import operator.models.*;
import operator.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    public void saveProductAndImage(Product product, MultipartFile file) throws IOException {
        Image image = imageRepository.findImageByProduct(product);
        if (file != null)
            image = saveImageEntity(file, product);
        product.setImage(image);
        imageRepository.save(image);
        productRepository.save(product);
    }

    private Image saveImageEntity(MultipartFile file, Product product) throws IOException {
        Image image;
        if (product.getImage() != null)
            image = product.getImage();
        else
            image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        image.setPreviewImage(true);
        return image;
    }

}
