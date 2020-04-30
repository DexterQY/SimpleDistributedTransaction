package per.qy.sdt.test.server1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import per.qy.sdt.test.server1.entity.OrderForm;
import per.qy.sdt.test.server1.service.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{commodityId}")
    public OrderForm order(@PathVariable String commodityId) {
        return orderService.order(commodityId);
    }
}
