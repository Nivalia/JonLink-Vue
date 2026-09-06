-- JonLink 财务模块新增表
-- 执行日期: 2026-08-23

-- 1. 银行对账单
CREATE TABLE IF NOT EXISTS `fin_bank_reconcile` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `bank_account_id` BIGINT NOT NULL COMMENT '银行账户ID',
    `bank_trans_no` VARCHAR(64) DEFAULT NULL COMMENT '银行流水号',
    `trans_date` DATE DEFAULT NULL COMMENT '交易日期',
    `trans_amount` DECIMAL(18,2) DEFAULT NULL COMMENT '交易金额(正=收入 负=支出)',
    `summary` VARCHAR(255) DEFAULT NULL COMMENT '摘要',
    `counter_party` VARCHAR(128) DEFAULT NULL COMMENT '对方户名',
    `status` CHAR(1) DEFAULT '0' COMMENT '对账状态:0=未对 1=已对 2=调整',
    `cash_flow_id` BIGINT DEFAULT NULL COMMENT '关联系统流水ID',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_bank_account` (`bank_account_id`),
    KEY `idx_trans_date` (`trans_date`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='银行对账单';

-- 2. 佣金结算
CREATE TABLE IF NOT EXISTS `fin_commission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `policy_no` VARCHAR(64) DEFAULT NULL COMMENT '保单号',
    `company_id` BIGINT DEFAULT NULL COMMENT '保险公司ID',
    `company_name` VARCHAR(128) DEFAULT NULL COMMENT '保险公司名称',
    `premium` DECIMAL(18,2) DEFAULT NULL COMMENT '保单保费',
    `tax_flag` CHAR(1) DEFAULT '0' COMMENT '是否含税 0否 1是(v4规则: 含税时佣金基数=保费/1.06)',
    `commission_rate` DECIMAL(8,4) DEFAULT NULL COMMENT '佣金比例(%)',
    `commission_amount` DECIMAL(18,2) DEFAULT NULL COMMENT '佣金金额',
    `direction` CHAR(1) DEFAULT '0' COMMENT '方向:0=上游(应收) 1=下游(应付)',
    `status` CHAR(1) DEFAULT '0' COMMENT '结算状态:0=待结算 1=已确认 2=已支付 3=已对账',
    `policy_date` DATE DEFAULT NULL COMMENT '保单日期',
    `settle_date` DATE DEFAULT NULL COMMENT '结算日期',
    `receipt_id` BIGINT DEFAULT NULL COMMENT '关联收款单ID',
    `payment_id` BIGINT DEFAULT NULL COMMENT '关联付款单ID',
    `policy_user` VARCHAR(64) DEFAULT NULL COMMENT '保单录入人员',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_policy_no` (`policy_no`),
    KEY `idx_company` (`company_id`),
    KEY `idx_direction` (`direction`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金结算';

-- 3. 审计日志
CREATE TABLE IF NOT EXISTS `fin_audit_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `module` VARCHAR(64) DEFAULT NULL COMMENT '模块',
    `action` VARCHAR(64) DEFAULT NULL COMMENT '操作',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
    `target_no` VARCHAR(64) DEFAULT NULL COMMENT '目标编号',
    `detail` TEXT DEFAULT NULL COMMENT '详情(JSON)',
    `operator` VARCHAR(64) DEFAULT NULL COMMENT '操作人',
    `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP地址',
    PRIMARY KEY (`id`),
    KEY `idx_module` (`module`),
    KEY `idx_operator` (`operator`),
    KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务审计日志';

-- 4. 预算管理
CREATE TABLE IF NOT EXISTS `fin_budget` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `subject_code` VARCHAR(32) DEFAULT NULL COMMENT '科目编码',
    `subject_name` VARCHAR(128) DEFAULT NULL COMMENT '科目名称',
    `period_code` VARCHAR(16) DEFAULT NULL COMMENT '期间编码(yyyyMM)',
    `budget_amount` DECIMAL(18,2) DEFAULT 0 COMMENT '预算额',
    `actual_amount` DECIMAL(18,2) DEFAULT 0 COMMENT '实际额',
    `alert_threshold` DECIMAL(5,2) DEFAULT 80 COMMENT '预警阈值(%)',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态:0=停用 1=启用',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_subject_period` (`subject_code`, `period_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算管理';

-- 5. 辅助核算
CREATE TABLE IF NOT EXISTS `fin_auxiliary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `aux_type` VARCHAR(32) DEFAULT NULL COMMENT '类型:dept=部门 project=项目 customer=客户',
    `aux_code` VARCHAR(64) DEFAULT NULL COMMENT '编码',
    `aux_name` VARCHAR(128) DEFAULT NULL COMMENT '名称',
    `status` CHAR(1) DEFAULT '1' COMMENT '状态:0=停用 1=启用',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_aux_type` (`aux_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='辅助核算';

-- 6. 期初余额
CREATE TABLE IF NOT EXISTS `fin_init_balance` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `subject_id` BIGINT NOT NULL COMMENT '科目ID',
    `subject_code` VARCHAR(32) DEFAULT NULL COMMENT '科目编码(冗余)',
    `subject_name` VARCHAR(128) DEFAULT NULL COMMENT '科目名称(冗余)',
    `init_debit` DECIMAL(18,2) DEFAULT 0 COMMENT '期初借方余额',
    `init_credit` DECIMAL(18,2) DEFAULT 0 COMMENT '期初贷方余额',
    `create_by` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_subject` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科目期初余额';

-- 7. 保单收入确认字段(扩展 jonlink_insurance_ledger 表)
ALTER TABLE `jonlink_insurance_ledger`
    ADD COLUMN IF NOT EXISTS `revenue_confirmed` CHAR(1) DEFAULT '0' COMMENT '收入确认 0未确认 1已确认' AFTER `profit`,
    ADD COLUMN IF NOT EXISTS `revenue_voucher_id` BIGINT DEFAULT NULL COMMENT '收入确认关联凭证ID' AFTER `revenue_confirmed`;
