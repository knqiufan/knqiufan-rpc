package cn.knqiufan.rpc.core.meta;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/20 21:30
 */
public class ServiceMeta {
  private String app;
  private String namespace;

  private String env;
  private String name;

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getEnv() {
    return env;
  }

  public void setEnv(String env) {
    this.env = env;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toPath() {
    return String.format("%s_%s_%s_%s", app, name, env, name);
  }
}
