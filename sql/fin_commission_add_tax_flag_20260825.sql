-- v4 规则: fin_commission 加 tax_flag 列(已存在表用 ALTER, 新表直接走 fin_new_tables.sql)
-- 2026-08-25
ALTER TABLE fin_commission
    ADD COLUMN `tax_flag` CHAR(1) DEFAULT '0' COMMENT '是否含税 0否 1是(v4规则: 含税时佣金基数=保费/1.06)'
    AFTER `premium`;
