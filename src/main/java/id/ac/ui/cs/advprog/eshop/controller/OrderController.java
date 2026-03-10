package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_CASH_ON_DELIVERY = "CASH_ON_DELIVERY";

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "orderCreate";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author,
                                  @RequestParam("productName") String productName,
                                  @RequestParam("productQuantity") int productQuantity) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName(productName);
        product.setProductQuantity(productQuantity);

        Order order = new Order(
                UUID.randomUUID().toString(),
                List.of(product),
                Instant.now().getEpochSecond(),
                author
        );
        orderService.createOrder(order);
        return "redirect:/order/pay/" + order.getId();
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "orderHistoryForm";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("author", author);
        model.addAttribute("orders", orders);
        return "orderHistoryList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:/order/history";
        }

        model.addAttribute("order", order);
        return "orderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable("orderId") String orderId,
                               @RequestParam("method") String method,
                               @RequestParam(value = "voucherCode", required = false) String voucherCode,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "deliveryFee", required = false) String deliveryFee,
                               Model model) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:/order/history";
        }

        Map<String, String> paymentData = new HashMap<>();
        String normalizedMethod = method.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        if (METHOD_VOUCHER_CODE.equals(normalizedMethod)) {
            paymentData.put("voucherCode", voucherCode);
        } else if (METHOD_CASH_ON_DELIVERY.equals(normalizedMethod)) {
            paymentData.put("address", address);
            paymentData.put("deliveryFee", deliveryFee);
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("payment", payment);
        return "orderPayResult";
    }
}
