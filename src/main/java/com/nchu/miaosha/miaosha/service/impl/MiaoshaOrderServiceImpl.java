package com.nchu.miaosha.miaosha.service.impl;

import com.nchu.miaosha.miaosha.dao.MiaoshaOrderDao;
import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import com.nchu.miaosha.miaosha.keys.MiaoshaOrderKey;
import com.nchu.miaosha.miaosha.service.MiaoshaOrderService;
import com.nchu.miaosha.redis.service.RedisService;
import com.nchu.miaosha.scm.domain.OrderBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: MiaoshaOrderServiceImpl
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:20
 * @Version: 1.0
 */
@Service
public class MiaoshaOrderServiceImpl implements MiaoshaOrderService {
    @Autowired
    private MiaoshaOrderDao miaoshaOrderDao;

    @Autowired
    private RedisService redisService;

    // 优化，判断是否秒杀的时候，可以从redis缓存中取
    @Override
    public MiaoshaOrderBill getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(MiaoshaOrderKey.getMiaoshaOrderByUidGid, "" + userId + "_" + goodsId, MiaoshaOrderBill.class);

//        return miaoshaOrderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    public void createMiaoshaOrderByOrder(OrderBill orderBill) {
        // t_scm_miaoshaorderbill建唯一索引 u_uid_gid，防止同一用户秒杀两个商品
        MiaoshaOrderBill bill = new MiaoshaOrderBill();
        bill.setGoodsId(orderBill.getGoodsId());
        bill.setOrderBillId(orderBill.getId());
        bill.setUserId(orderBill.getUserId());
        miaoshaOrderDao.insertMiaoshaOrder(bill);

        // 秒杀订单写入redis缓存
        redisService.set(MiaoshaOrderKey.getMiaoshaOrderByUidGid, "" + orderBill.getUserId() + "_" + orderBill.getGoodsId(), bill);
    }

    @Override
    public void deleteOrders() {
        miaoshaOrderDao.deleteMiaoshaOrders();
    }
}
