# 手写 RPC 项目：knqiufan-rpc

从 0 开始的手写 RPC 项目。

![image](RPC.png)

## 项目依赖版本

* JDK：17
* SpringBoot: 3.0.6
* OkHttp: 4.10.0
* FastJson: 1.2.83

## 实现功能（2024-04-05 更新）
> 打勾为已实现
* [√] 消息通讯
  * [√] 使用方法签名处理方法重载问题
  * [√] 请求/响应 参数的类型转换
    * 基本类型与其包装类
    * String
    * 数组
    * List
    * Map
    * JSON
* [√] 过滤器
  * [√] 请求前置处理
  * [√] 响应后置处理
  * [√] mock
  * [√] Cache 地址缓存过滤
* [] 启动扫描优化
* [√] 过滤 Object 内置的方法：判断是否为 Object 类
* [] 方法白名单
* [√] 一个接口存在多个实现类的处理
* [√] 负载均衡
  * [√] 随机
  * [√] 轮询
  * [] 轮询权重
  * [] 最少活跃度
* [√] 注册中心
  * [√] 极简静态注册中心
  * [√] zookeeper 注册中心
    * [√] 服务的注册与注销
    * [√] 服务的发布与订阅
* [√] 统一异常处理
* [√] 超时与自旋重试
* [] 多版本功能
* [] 容错，节点故障隔离（断开与恢复）
* [] 灰度发布