-- JonLink 财务模块新增菜单
-- 执行日期: 2026-08-23

-- 1. 新增分组：财务扩展（在财务系统下）
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('财务扩展', 2365, 'extended', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', NOW(), '期末结转、银行对账、佣金结算等扩展功能', 60);

SET @parentId = LAST_INSERT_ID();

-- 2. 期末结转
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期末结转', @parentId, 'closing', 'finance/closing/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:closing:list', 'date', 'admin', NOW(), '损益类科目结转到本年利润', 1);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期末结转查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:closing:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期末结转执行', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:closing:execute', '#', 'admin', NOW(), '', 2);

-- 3. 银行对账
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('银行对账', @parentId, 'bankReconcile', 'finance/bankReconcile/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:bankReconcile:list', 'money', 'admin', NOW(), '银行流水与账簿核对', 2);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('银行对账查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:bankReconcile:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('银行对账导入', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:bankReconcile:import', '#', 'admin', NOW(), '', 2);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('银行对账操作', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:bankReconcile:reconcile', '#', 'admin', NOW(), '', 3);

-- 4. 佣金结算
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('佣金结算', @parentId, 'commission', 'finance/commission/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:commission:list', 'percent', 'admin', NOW(), '保险公司佣金自动计算', 3);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('佣金结算查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:commission:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('佣金结算新增', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:commission:add', '#', 'admin', NOW(), '', 2);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('佣金结算编辑', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:commission:edit', '#', 'admin', NOW(), '', 3);

-- 5. 收入确认
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('保单收入确认', @parentId, 'revenue', 'finance/revenue/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:revenue:list', 'check', 'admin', NOW(), '保费收入按会计准则确认', 4);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('收入确认查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:revenue:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('收入确认操作', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:revenue:confirm', '#', 'admin', NOW(), '', 2);

-- 6. 审计日志
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('财务审计日志', @parentId, 'auditLog', 'finance/auditLog/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:auditLog:list', 'log', 'admin', NOW(), '财务操作记录审计', 5);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('审计日志查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:auditLog:query', '#', 'admin', NOW(), '', 1);

-- 7. 预算管理
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('预算管理', @parentId, 'budget', 'finance/budget/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:budget:list', 'chart', 'admin', NOW(), '预算编制、执行、预警', 6);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('预算管理查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:budget:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('预算管理新增', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:budget:add', '#', 'admin', NOW(), '', 2);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('预算管理编辑', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:budget:edit', '#', 'admin', NOW(), '', 3);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('预算管理删除', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:budget:remove', '#', 'admin', NOW(), '', 4);

-- 8. 辅助核算
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('辅助核算', @parentId, 'auxiliary', 'finance/auxiliary/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:auxiliary:list', 'tree', 'admin', NOW(), '按项目/部门/客户维度核算', 7);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('辅助核算查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:auxiliary:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('辅助核算新增', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:auxiliary:add', '#', 'admin', NOW(), '', 2);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('辅助核算编辑', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:auxiliary:edit', '#', 'admin', NOW(), '', 3);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('辅助核算删除', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:auxiliary:remove', '#', 'admin', NOW(), '', 4);

-- 9. 税务管理
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('税务管理', @parentId, 'tax', 'finance/tax/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:tax:list', 'component', 'admin', NOW(), '增值税/所得税计算', 8);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('税务管理查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:tax:query', '#', 'admin', NOW(), '', 1);

-- 10. 期初余额
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期初余额导入', @parentId, 'initBalance', 'finance/initBalance/index', NULL, '', 1, 0, 'C', '0', '0', 'finance:initBalance:list', 'upload', 'admin', NOW(), 'Excel导入科目期初余额', 9);

SET @menuId = LAST_INSERT_ID();
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期初余额查询', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:initBalance:query', '#', 'admin', NOW(), '', 1);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期初余额导入', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:initBalance:import', '#', 'admin', NOW(), '', 2);
INSERT INTO sys_menu (menu_name, parent_id, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark, order_num)
VALUES ('期初余额编辑', @menuId, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'finance:initBalance:edit', '#', 'admin', NOW(), '', 3);
