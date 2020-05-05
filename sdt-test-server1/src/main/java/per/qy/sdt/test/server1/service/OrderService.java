package per.qy.sdt.test.server1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import per.qy.sdt.client.annotation.SdtTransactional;
import per.qy.sdt.test.server1.dao.OrderDao;
import per.qy.sdt.test.server1.entity.OrderForm;

import java.math.BigDecimal;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @SdtTransactional
    @Transactional(rollbackFor = Exception.class)
    public OrderForm order(String commodityId, String sdtGroupId) {
        OrderForm orderForm = new OrderForm();
        orderForm.setCommodityId(commodityId);
        orderForm.setPrice(BigDecimal.valueOf(12.5));
        new RestTemplate().postForLocation("http://localhost:9002/commodity/" + commodityId
                + "?sdtGroupId=" + sdtGroupId, null);
        orderDao.save(orderForm);
        System.out.println("order success " + orderForm);
        int i = 1 / 0;
        return orderForm;
    }
}
