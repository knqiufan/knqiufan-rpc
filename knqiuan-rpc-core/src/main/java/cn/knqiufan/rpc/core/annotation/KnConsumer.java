package cn.knqiufan.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * 服务消费者
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 9:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface KnConsumer {
}
