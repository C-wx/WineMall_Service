package winemall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("winemall.mapper")
@EnableTransactionManagement  // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
public class WinemallApplication {
    public static void main(String[] args) {
        SpringApplication.run(WinemallApplication.class, args);
        System.out.println("-------------------启动完成-----------------");
    }
}
