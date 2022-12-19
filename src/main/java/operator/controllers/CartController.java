package operator.controllers;

import operator.models.Cart;
import operator.models.Product;
import operator.repositories.CartRepository;
import operator.repositories.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasAnyAuthority('SELLER')")
@Controller
public class CartController {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository,
                          ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/cart/index")
    public String cartIndex(Model model) {
        Iterable<Cart> carts = cartRepository.findAll();
        int cartCount = 0;
        for (Cart cart : carts)
            cartCount++;
        model.addAttribute("carts", carts);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("products", productRepository.findAll());
        return "cart/Index";
    }

    @PostMapping("/cart/search")
    public String cartSearch(@RequestParam(required = false) String name, @RequestParam(required = false) int count) {
        Product product = productRepository.findProductByName(name);
        if (product == null && count == 0)
            return "redirect:/cart/index";
        Cart cart = new Cart(product, count);
        cartRepository.save(cart);
        return "redirect:/cart/index";
    }

    @PostMapping("/cart/delete")
    public String cartDelete(@RequestParam long idCart) {
        Cart cart = cartRepository.findById(idCart).get();
        cartRepository.delete(cart);
        return "redirect:/cart/index";
    }

}