package operator.controllers;

import operator.models.Cart;
import operator.models.Cheque;
import operator.models.Employee;
import operator.models.Product;
import operator.repositories.CartRepository;
import operator.repositories.ChequeRepository;
import operator.repositories.EmployeeRepository;
import operator.repositories.ProductRepository;
import operator.services.ExcelService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ChequeController {

    private final EmployeeRepository employeeRepository;

    private final ChequeRepository chequeRepository;

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final ResourceLoader resourceLoader;

    public ChequeController(EmployeeRepository employeeRepository,
                            ChequeRepository chequeRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository, ResourceLoader resourceLoader) {
        this.employeeRepository = employeeRepository;
        this.chequeRepository = chequeRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/cheque/index")
    public String chequeIndex(Model model) {
        model.addAttribute("cheques", chequeRepository.findAll());
        return "cheque/Index";
    }

    @GetMapping("/cheque/filter")
    public String chequeFilter(@RequestParam(required = false) String name, Model model) {
        Iterable<Cheque> cheques;
        if (name != null && !name.equals(""))
            cheques = chequeRepository.findByProductName(name);
        else
            cheques = chequeRepository.findAll();
        model.addAttribute("cheques", cheques);
        return "cheque/Index";
    }

    @PreAuthorize("hasAnyAuthority('SELLER')")
    @PostMapping("/cheque/create")
    public String chequeCreate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeRepository.findEmployeeByUserUsername(authentication.getName());
        Iterable<Cart> carts = cartRepository.findAll();
        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getProduct().getIdProduct()).get();
            product.setCount(product.getCount() - cart.getProductCount());
            Cheque cheque = new Cheque(new Date(), employee, cart.getProduct(), cart.getProductCount());
            chequeRepository.save(cheque);
            productRepository.save(product);
            cartRepository.delete(cart);
        }
        return "redirect:/cart/index";
    }

    @GetMapping("/cheque/generateExcel")
    public ResponseEntity<Resource> chequeGenerateExcel() throws IOException {
        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        ExcelService excelExporter = new ExcelService((List<Cheque>) chequeRepository.findAll());
        excelExporter.export(currentDateTime);
        try {
            Resource resource = resourceLoader.getResource(Paths.get("files/cheque_" + currentDateTime + ".xlsx").toUri().toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}