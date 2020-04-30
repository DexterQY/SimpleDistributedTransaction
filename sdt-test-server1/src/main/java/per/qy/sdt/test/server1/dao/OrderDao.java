package per.qy.sdt.test.server1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import per.qy.sdt.test.server1.entity.OrderForm;

public interface OrderDao extends JpaRepository<OrderForm, String> {
}
