package com.nchu.miaosha.miaosha.service.impl;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.common.utils.MD5Util;
import com.nchu.miaosha.common.utils.UUIDUtil;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.miaosha.domain.MiaoshaOrderBill;
import com.nchu.miaosha.miaosha.keys.MiaoshaKey;
import com.nchu.miaosha.miaosha.service.MiaoshaGoodsService;
import com.nchu.miaosha.miaosha.service.MiaoshaOrderService;
import com.nchu.miaosha.miaosha.service.MiaoshaService;
import com.nchu.miaosha.rabbitmq.MQSender;
import com.nchu.miaosha.redis.service.RedisService;
import com.nchu.miaosha.scm.domain.OrderBill;
import com.nchu.miaosha.scm.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @ClassName: MiaoshaServiceImpl
 * @Author: 时间
 * @Description: 秒杀服务
 * @Date: 2020/7/16 0:33
 * @Version: 1.0
 */
@Service
public class MiaoshaServiceImpl implements MiaoshaService {
    private static Logger loger = LoggerFactory.getLogger(MiaoshaServiceImpl.class);

    @Autowired
    private MiaoshaGoodsService miaoshaGoodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaOrderService miaoshaOrderService;

    @Autowired
    private RedisService redisService;

    // 操作符
    private static char[] ops = new char[]{'+', '-', '*'};

    @Transactional
    public OrderBill miaosha(User user, GoodsVo goods) {
        // 扣减秒杀商品库存 下订单 写入秒杀订单
        // 接口优化,如果减库存失败,后面不应该做生成订单操作,对reduceStock方法改造,返回boolean判断是否成功
        if (miaoshaGoodsService.reduceStock(goods)) { // 减库存成功
            // 生成普通订单order
            OrderBill orderBill = orderService.createOrder(user, goods);

            // 生成秒杀订单
            miaoshaOrderService.createMiaoshaOrderByOrder(orderBill);
            return orderBill;
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {
        // 根据秒杀订单判断是否秒杀到商品
        MiaoshaOrderBill order = miaoshaOrderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (Objects.nonNull(order)) { // order 不为空,秒杀成功
            return order.getOrderBillId();
        } else {
            if (getGoodsOver(goodsId)) { // 根据商品标记判断秒杀是否成功
                return -1;
            } else {
                return 0; // 没处理完,还在排队中
            }
        }
    }

    /**
     * miaosha做秒杀扣减库存失败的时候,会往缓存里面写入redis缓存对商品标记
     * 通过判断标记是否存在可得知秒杀成功还是失败
     *
     * @param goodsId
     * @return
     */
    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

    /**
     * 秒杀失败,对商品做个标记
     *
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    @Override
    public void reset(List<GoodsVo> goodsList) {
        miaoshaGoodsService.resetStock(goodsList);

        orderService.deleteOrders();

        miaoshaOrderService.deleteOrders();
    }

    @Override
    public String createMiaoshaPath(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    @Override
    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    @Override
    public BufferedImage createVerifyCode(User user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;

        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);

        // create a random instance to generate the codes
        Random rdm = new Random();

        // make some confusion 生成50个干扰的点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }

        // generate a random code 生成数学公式验证码
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();

        //把数学公式验证码结果存到redis中
        int result = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, result);

        //输出图片
        return image;
    }

    @Override
    public boolean checkVerifyCode(User user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }

        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }

        // 防止该验证码下次还能用
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
        return true;
    }

    /**
     * 计算数学公式
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            // ScriptEngine实现字符串公式灵活计算
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String generateVerifyCode(Random random) {
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        int num3 = random.nextInt(10);
        char op1 = ops[random.nextInt(3)];
        char op2 = ops[random.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        loger.info("图形验证码:" + exp);
        return exp;
    }
}
