package com.jnm.mallJnm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.jnm.Tutor.model.enums.OrderStatus;

@SpringBootTest
class QuestionBankApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(OrderStatus.CANCELLED.name());
        System.out.println(OrderStatus.CANCELLED.values());
        System.out.println(OrderStatus.CANCELLED.ordinal());
    }

}
