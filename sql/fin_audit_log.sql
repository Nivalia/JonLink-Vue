-- ----------------------------
-- 审计日志表
-- ----------------------------
drop table if exists fin_audit_log;
create table fin_audit_log (
  id              bigint(20)      not null auto_increment    comment '日志ID',
  module          varchar(50)     default ''                 comment '模块',
  action          varchar(50)     default ''                 comment '操作',
  target_id       bigint(20)      default null               comment '目标ID',
  target_no       varchar(100)    default ''                 comment '目标编号',
  detail          text                                    comment '详情(JSON)',
  operator        varchar(64)     default ''                 comment '操作人',
  operate_time    datetime                                   comment '操作时间',
  ip_address      varchar(128)    default ''                 comment 'IP地址',
  remark          varchar(500)    default null               comment '备注',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  primary key (id)
) engine=innodb comment = '审计日志表';

-- ----------------------------
-- 审计日志索引
-- ----------------------------
create index idx_audit_log_module on fin_audit_log(module);
create index idx_audit_log_action on fin_audit_log(action);
create index idx_audit_log_target on fin_audit_log(target_id);
create index idx_audit_log_operator on fin_audit_log(operator);
create index idx_audit_log_time on fin_audit_log(operate_time);
