package per.qy.sdt.test.server2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import per.qy.sdt.test.server2.entity.Commodity;
import per.qy.sdt.test.server2.service.CommodityService;

@RestController
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @PostMapping("/commodity/{id}")
    public Commodity commodity(@PathVariable String id) {
        return commodityService.commodity(id);
    }
}
