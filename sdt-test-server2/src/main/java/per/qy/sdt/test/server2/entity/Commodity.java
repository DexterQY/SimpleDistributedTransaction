package per.qy.sdt.test.server2.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myId")
    @GenericGenerator(name = "myId", strategy = "per.qy.sdt.test.server2.entity.MyIdGenerator")
    private String id;
    private String name;
    private long num;
    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
}
