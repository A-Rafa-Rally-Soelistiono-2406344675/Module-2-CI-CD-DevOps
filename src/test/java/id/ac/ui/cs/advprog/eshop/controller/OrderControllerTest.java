package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Model model;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Map<String, String>> paymentDataCaptor;

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo");
        product.setProductQuantity(1);
        order = new Order("order-1", List.of(product), 1708560000L, "Safira");
    }

    @Test
    void createOrderPageShouldReturnOrderCreateView() {
        assertEquals("orderCreate", orderController.createOrderPage());
    }

    @Test
    void createOrderPostShouldCreateOrderAndRedirectToPayPage() {
        String view = orderController.createOrderPost("Safira", "Sampo", 2);

        verify(orderService).createOrder(orderCaptor.capture());
        Order createdOrder = orderCaptor.getValue();
        assertNotNull(createdOrder.getId());
        assertEquals("Safira", createdOrder.getAuthor());
        assertEquals("Sampo", createdOrder.getProducts().getFirst().getProductName());
        assertEquals(2, createdOrder.getProducts().getFirst().getProductQuantity());
        assertEquals("redirect:/order/pay/" + createdOrder.getId(), view);
    }

    @Test
    void orderHistoryPageShouldReturnHistoryFormView() {
        assertEquals("orderHistoryForm", orderController.orderHistoryPage());
    }

    @Test
    void orderHistoryPostShouldSetModelAndReturnListView() {
        when(orderService.findAllByAuthor("Safira")).thenReturn(List.of(order));

        String view = orderController.orderHistoryPost("Safira", model);

        verify(model).addAttribute("author", "Safira");
        verify(model).addAttribute("orders", List.of(order));
        assertEquals("orderHistoryList", view);
    }

    @Test
    void payOrderPageShouldRedirectWhenOrderNotFound() {
        when(orderService.findById("missing")).thenReturn(null);

        String view = orderController.payOrderPage("missing", model);

        assertEquals("redirect:/order/history", view);
    }

    @Test
    void payOrderPageShouldSetOrderWhenFound() {
        when(orderService.findById("order-1")).thenReturn(order);

        String view = orderController.payOrderPage("order-1", model);

        verify(model).addAttribute("order", order);
        assertEquals("orderPay", view);
    }

    @Test
    void payOrderPostShouldSendVoucherData() {
        Payment payment = new Payment("payment-1", order, "Voucher Code", "SUCCESS",
                Map.of("voucherCode", "ESHOP1234ABC5678"));
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Voucher Code"), any(Map.class)))
                .thenReturn(payment);

        String view = orderController.payOrderPost(
                "order-1",
                "Voucher Code",
                "ESHOP1234ABC5678",
                null,
                null,
                model
        );

        verify(paymentService).addPayment(eq(order), eq("Voucher Code"), paymentDataCaptor.capture());
        assertEquals("ESHOP1234ABC5678", paymentDataCaptor.getValue().get("voucherCode"));
        verify(model).addAttribute("payment", payment);
        assertEquals("orderPayResult", view);
    }

    @Test
    void payOrderPostShouldSendCodData() {
        Payment payment = new Payment("payment-1", order, "Cash on Delivery", "SUCCESS",
                Map.of("address", "Jl. Merdeka", "deliveryFee", "10000"));
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Cash on Delivery"), any(Map.class)))
                .thenReturn(payment);

        String view = orderController.payOrderPost(
                "order-1",
                "Cash on Delivery",
                null,
                "Jl. Merdeka",
                "10000",
                model
        );

        verify(paymentService).addPayment(eq(order), eq("Cash on Delivery"), paymentDataCaptor.capture());
        assertEquals("Jl. Merdeka", paymentDataCaptor.getValue().get("address"));
        assertEquals("10000", paymentDataCaptor.getValue().get("deliveryFee"));
        verify(model).addAttribute("payment", payment);
        assertEquals("orderPayResult", view);
    }

    @Test
    void payOrderPostShouldRedirectWhenOrderNotFound() {
        when(orderService.findById("missing")).thenReturn(null);

        String view = orderController.payOrderPost("missing", "Voucher Code", "A", null, null, model);

        assertEquals("redirect:/order/history", view);
    }

    @Test
    void payOrderPostShouldSendEmptyDataForUnknownMethod() {
        Payment payment = new Payment("payment-2", order, "Unknown", "REJECTED", Map.of());
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Unknown"), any(Map.class))).thenReturn(payment);

        String view = orderController.payOrderPost(
                "order-1",
                "Unknown",
                null,
                null,
                null,
                model
        );

        verify(paymentService).addPayment(eq(order), eq("Unknown"), paymentDataCaptor.capture());
        assertEquals(0, paymentDataCaptor.getValue().size());
        assertEquals("orderPayResult", view);
    }
}
