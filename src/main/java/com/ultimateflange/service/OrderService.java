package com.ultimateflange.service;

import com.ultimateflange.dto.OrderDTO;
import com.ultimateflange.model.Order;
import com.ultimateflange.model.OrderStatus;
import com.ultimateflange.model.User;
import com.ultimateflange.repository.OrderRepository;
import com.ultimateflange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    EmailService emailService;
    @Autowired
     OrderRepository orderRepository;
    private final UserRepository userRepository;

    public Order createOrder(OrderDTO orderDTO) {
        Order order = new Order();

        // Generate custom order ID
        String orderId = generateOrderId();
        order.setOrderId(orderId);

        // Set supplier
        if (orderDTO.getSupplierId() != null) {
            User supplier = userRepository.findById(orderDTO.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            order.setSupplier(supplier);
        }

        order.setSupplierName(orderDTO.getSupplierName());
        order.setCustomerEmail(orderDTO.getCustomerEmail());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerCompany(orderDTO.getCustomerCompany());
        order.setCustomerPhone(orderDTO.getCustomerPhone());
        order.setProductKey(orderDTO.getProductKey());
        order.setProductName(orderDTO.getProductName());
        order.setQuantity(orderDTO.getQuantity());
        order.setSize(orderDTO.getSize());
        order.setMaterial(orderDTO.getMaterial());
        order.setSpecs(orderDTO.getSpecs());
        order.setAddress(orderDTO.getAddress());
        order.setContactMethod(orderDTO.getContactMethod());
        order.setAmount(orderDTO.getAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());

        // Set estimated delivery (7 days from now)
        Date estimatedDelivery = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        order.setEstimatedDelivery(estimatedDelivery);

        return orderRepository.save(order);
    }

    private String generateOrderId() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String datePart = sdf.format(new Date());
        Random random = new Random();
        int randomPart = 1000 + random.nextInt(9000);
        return "ORD-" + datePart + "-" + randomPart;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order getOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public List<Order> getOrdersBySupplier(Long supplierId) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return orderRepository.findBySupplier(supplier);
    }

    public List<Order> getOrdersByCustomer(String customerEmail) {
        return orderRepository.findByCustomerEmail(customerEmail);
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
public Order createOrder(Order newOrder){
        Order order = orderRepository.save(newOrder);
        emailService.sendOrderNotification("New ORDER Placed =====>"+order.getId());
        return order;
}



}