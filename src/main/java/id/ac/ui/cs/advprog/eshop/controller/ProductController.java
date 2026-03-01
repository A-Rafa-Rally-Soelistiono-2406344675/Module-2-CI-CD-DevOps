package id.ac.ui.cs.advprog.eshop.controller;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    //implemented srp: kelas ini hanya menangani alur web untuk produk.
    //implemented dip: bergantung pada abstraksi ProductService melalui constructor.
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") String id, Model model) {
        Product product = service.findById(id);
        if (product == null) {
            return "redirect:/product/list";
        }
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.update(product);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        service.deleteById(id);
        return "redirect:/product/list";
    }
}
