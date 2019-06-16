package com.kfit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ：youq
 * @date ：Created in 2019/2/12 21:38
 * @modified By：
 */
@Component
public class Role {
    @Value("3e")
    String a;
    @Value("5")
    String b;
}
