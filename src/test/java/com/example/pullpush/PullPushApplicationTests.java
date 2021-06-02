package com.example.pullpush;

import com.example.pullpush.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class PullPushApplicationTests {

    @Test
    void contextLoads() {
        String token = MD5Utils.generateToken("yq_yuzhong", 1622603086L);
        System.out.println("token = " + token);
    }

}
