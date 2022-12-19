package operator.controllers;

import operator.models.Image;
import operator.models.Product;
import operator.repositories.ImageRepository;
import operator.repositories.ProductRepository;
import operator.services.ProductImageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Random;

@PreAuthorize("hasAnyAuthority('ADMIN')")
@Controller
public class ProductController {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final ProductImageService productImageService;

    public ProductController(ProductRepository productRepository,
                             ImageRepository imageRepository,
                             ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.productImageService = productImageService;
    }

    @GetMapping("/product/index")
    public String productIndex(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "product/Index";
    }

    @GetMapping("/product/filter")
    public String productFilter(@RequestParam(required = false) String name, Model model) {
        Iterable<Product> products;
        if (name != null && !name.equals(""))
            products = productRepository.findByNameContains(name);
        else
            products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product/Index";
    }

    @PostMapping("/product/editProduct")
    public String productEdit(@RequestParam("file") MultipartFile file,
                              @ModelAttribute("product") @Valid Product product, BindingResult bindingResult,
                              Model model) throws IOException {
        if (productRepository.findProductByName(product.getName()) != null
                && !productRepository.findProductByName(product.getName()).getIdProduct().equals(product.getIdProduct())) {
            bindingResult.addError(new ObjectError("name", "Данный товар уже существует"));
            model.addAttribute("errorMessage", "Данный товар уже существует");
        }
        if (bindingResult.hasErrors())
            return "product/Edit";
        productImageService.saveProductAndImage(product, file);
        return "redirect:/product/index";
    }

    @PostMapping("/product/edit")
    public String productEdit(@RequestParam long idProduct, Model model) {
        Product product = productRepository.findById(idProduct).get();
        model.addAttribute("product", product);
        model.addAttribute("image", imageRepository.findImageByProduct(product));
        return "product/Edit";
    }

    @PostMapping("/product/delete")
    public String productDelete(@RequestParam long idProduct) {
        Product product = productRepository.findById(idProduct).get();
        product.setCount(0);
        productRepository.save(product);
        return "redirect:/product/index";
    }

    @PostMapping("/product/create")
    public String productCreate(@RequestParam("file") MultipartFile file,
                                @ModelAttribute("product") @Valid Product product,
                                BindingResult bindingResult, Model model) throws IOException {
        if (productRepository.findProductByName(product.getName()) != null) {
            bindingResult.addError(new ObjectError("name", "Данный товар уже существует"));
            model.addAttribute("errorMessage", "Данный товар уже существует");
        }
        if (bindingResult.hasErrors())
            return "product/Create";
        Random random = new Random();
        product.setCharacteristic(String.valueOf(random.nextInt((9 - 1) + 1) + 1) + " " +
                String.valueOf(random.nextInt((999999 - 100000) + 1) + 100000) + " " +
                String.valueOf(random.nextInt((999999 - 100000) + 1) + 100000));
        productImageService.saveProductAndImage(product, file);
        return "redirect:/product/index";
    }

    @GetMapping("/product/create")
    public String productCreate(@ModelAttribute("product") Product product) {
        return "product/Create";
    }

    @PostMapping("/product/details")
    public String productDetails(@RequestParam long idProduct, Model model) {
        model.addAttribute("product", productRepository.findById(idProduct).get());
        return "product/Details";
    }

}