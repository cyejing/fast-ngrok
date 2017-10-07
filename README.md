### Fast-Ngrok

ngrok集成spring-boot,随应用启动快速映射内网地址

### 使用方法

添加POM依赖: 
```
<dependency>
    <groupId>cn.cyejing</groupId>
    <artifactId>fast-ngrok-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
### 默认配置
```
ngrok.serevr-address=b.cyejing.cn 
ngrok.server-port=4443
ngrok.proto=http
ngrok.subdomain 默认为空,随机子域名
ngrok.hostname 默认为空,自定义域名
```
