package com.jonlink.system.wx.service;

import java.util.List;
import java.util.Map;
import com.jonlink.system.domain.WxDistMember;
import com.jonlink.system.mapper.WxBizMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分销服务。
 * R7 分销员=系统用户(scene=user_id) / R8 关系链无限长 / R9 每笔核销佣金100%直接上级
 * R10 分销员停用: 本人停积分, 下级不受影响
 *
 * @author jonlink
 */
@Service
public class WxDistService
{
    private static final Logger log = LoggerFactory.getLogger(WxDistService.class);

    @Autowired
    private WxBizMapper wxBizMapper;

    /**
     * 上级关系链(无限递归): [本人, 上级, 上上级...]
     * 以 user_id 为节点, 深度上限 100 防环
     */
    public java.util.List<WxDistMember> relationChain(Long userId)
    {
        java.util.LinkedList<WxDistMember> chain = new java.util.LinkedList<>();
        Long cur = userId;
        int guard = 0;
        while (cur != null && cur > 0 && guard++ < 100)
        {
            WxDistMember m = wxBizMapper.selectDistByUserId(cur);
            if (m == null)
            {
                break;
            }
            chain.add(m);
            cur = m.getParentId();
        }
        return chain;
    }

    /** 下级列表(直接下级, 树形前端展开用) */
    public List<WxDistMember> children(Long parentId)
    {
        return wxBizMapper.selectDistChildren(parentId);
    }

    /**
     * 分销团队树(无限递归向下): 以 userId 为根, 返回整棵子树
     * 深度上限 100 防环; 节点附带 depth 供前端展示层级
     */
    public List<WxDistMember> tree(Long userId)
    {
        List<WxDistMember> roots = new java.util.ArrayList<>();
        WxDistMember me = wxBizMapper.selectDistByUserId(userId);
        if (me != null)
        {
            me.setDepth(1L);
            roots.add(me);
        }
        buildTreeRecursive(roots, 2);
        return roots;
    }

    private void buildTreeRecursive(List<WxDistMember> parents, int depth)
    {
        if (parents == null || parents.isEmpty() || depth > 100)
        {
            return;
        }
        List<WxDistMember> nextLevel = new java.util.ArrayList<>();
        for (WxDistMember p : parents)
        {
            List<WxDistMember> kids = wxBizMapper.selectDistChildren(p.getUserId());
            for (WxDistMember k : kids)
            {
                k.setDepth((long) depth);
            }
            p.setChildren(kids);
            nextLevel.addAll(kids);
        }
        buildTreeRecursive(nextLevel, depth + 1);
    }
}
