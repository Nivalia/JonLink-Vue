# JonLink 后端 (JonLink-Vue)

基于 SpringBoot 4 + MyBatis 的企业级后台管理系统后端,服务于 JonLink 保险中介业务(财务核算 + 政策管理)。

## 技术栈

- **语言**: Java 17 (JDK 17+)
- **框架**: Spring Boot 4.0.6、Spring Security
- **持久层**: MyBatis + Druid 连接池 + MySQL/MariaDB 10.11
- **缓存 / 队列**: Redis
- **认证**: JWT (token)
- **API 文档**: SpringDoc / Swagger (`/v3/api-docs`)
- **定时任务**: Quartz (基于 RuoYi 上游基表 `qrtz_*`)
- **基础权限基表**: 沿用 `sys_user / sys_menu / sys_role / sys_dept` 等通用权限基表(残留自 v3.9.2 fork),业务表完全自建 (`fin_* / policy_* / jonlink_* / wx_*`)
- **微信对接**: `wxjava 4.8.0` (公众号 / H5 / OAuth / 模板消息)

## 模块结构

```
JonLink-Vue/
├── jonlink-admin/        # 启动 + 总装配
├── jonlink-framework/    # 框架层(异常/权限/工具类)
├── jonlink-system/       # 系统基表(SysUser/SysMenu/SysRole + wx_* 业务)
├── jonlink-quartz/       # 定时任务
├── jonlink-generator/    # 代码生成器
└── jonlink-admin/target/ # 构建产物 (jonlink-admin.jar)
```

## 启动

```bash
# 1. 确保 MariaDB 10.11 / Redis 已启动
systemctl status mariadb redis

# 2. 确认 application-druid.yml 中的数据库账号可用
mysql -u root -p<password> -e "SHOW DATABASES;"

# 3. 启动
cd /opt/JonLink/JonLink-Vue
java -jar jonlink-admin/target/jonlink-admin.jar
# 或:systemctl start jonlink
```

## 关键配置

| 配置 | 文件 | 备注 |
|---|---|---|
| 端口 / context-path | `application.yml` | 默认 `8080` |
| 数据库 | `application-druid.yml` | master/slave |
| JWT 密钥 | `application.yml` | 开发期可保留默认值,**生产必须改** |
| Druid 监控 | `application-druid.yml` | `/druid/*`,默认白名单为空(任意 IP 可访问) |
| 日志 | logback.xml | 写到 `/home/jonlink/logs/` |

## 部署

- 服务器:火山引擎 ECS (公网 `115.190.215.93`)
- 前端 dist → `/opt/JonLink/JonLink-Vue3-TS/dist`
- nginx 反向代理: `/etc/nginx/sites-available/jonlink`(80 端口)
- 后端 service: `/etc/systemd/system/jonlink.service`

## 安全注意

- JWT secret `abcdefghijklmnopqrstuvwxyz` 是开发默认值,**生产前必须改**。
- Druid 监控控制台未设白名单,**生产前必须收 IP 白名单 + 改密码**。
- 当前 MariaDB 密码为开发环境配置,**生产前必须轮换**。

---

## 历史

本项目初期 fork 自 RuoYi v3.9.2,沿用其通用权限基表与代码生成器框架;**业务层(财务/政策/微信粉丝)完全自建**。上游 RuoYi 项目仓库地址见 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue)。

## 运行时密码备忘

- MySQL: `root / jonlink_root_pwd`(`application-druid.yml`)
- Druid 监控: 用户 `jonlink`,密码见 `/tmp/druid_pwd.txt`(或重打 jar 后读 `application-druid.yml`)
- Redis: 当前**未设密码**(开发期,RuoYi 默认);生产前必须改