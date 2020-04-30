package per.qy.sdt.test.server2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import per.qy.sdt.test.server2.entity.Commodity;

public interface CommodityDao extends JpaRepository<Commodity, String> {
}
