package cn.knqiufan.rpc.core.meta;

import java.util.Map;

/**
 * 描述服务实例的元数据
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/20 21:05
 */
public class InstanceMeta {

  private String scheme;
  private String host;
  private Integer port;
  private String context;
  /**
   * online or offline
   */
  private boolean status;

  private Map<String, String> parameters;

  public String toPath() {
    return String.format("%s_%d", host, port);
  }

  public static InstanceMeta http(String host, Integer port) {
    return new InstanceMeta("http", host, port, "");
  }
  public String toUrl() {
    return String.format("%s://%s:%d/%s", scheme, host, port, context);
  }

  public InstanceMeta() {
  }

  public InstanceMeta(String scheme, String host, Integer port, String context) {
    this.scheme = scheme;
    this.host = host;
    this.port = port;
    this.context = context;
  }

  @Override
  public String toString() {
    return "InstanceMeta{" +
            "scheme='" + scheme + '\'' +
            ", host='" + host + '\'' +
            ", port=" + port +
            ", context='" + context + '\'' +
            ", status=" + status +
            ", parameters=" + parameters +
            '}';
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }
}
