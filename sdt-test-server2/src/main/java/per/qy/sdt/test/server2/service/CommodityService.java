package per.qy.sdt.test.server2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import per.qy.sdt.client.annotation.SdtTransactional;
import per.qy.sdt.test.server2.dao.CommodityDao;
import per.qy.sdt.test.server2.entity.Commodity;

import java.util.Optional;

@Service
public class CommodityService {

    @Autowired
    private CommodityDao commodityDao;

    @SdtTransactional
    @Transactional(rollbackFor = Exception.class)
    public Commodity commodity(String id, String sdtGroupId) {
        Commodity commodity;
        Optional<Commodity> commodityOpt = commodityDao.findById(id);
        if (commodityOpt.isEmpty()) {
            commodity = new Commodity();
            commodity.setId(id);
            commodity.setName("commodity");
            commodity.setNum(100);
        } else {
            commodity = commodityOpt.get();
            commodity.setNum(commodity.getNum() - 1);
        }
        commodityDao.save(commodity);
        return commodity;
    }
}
