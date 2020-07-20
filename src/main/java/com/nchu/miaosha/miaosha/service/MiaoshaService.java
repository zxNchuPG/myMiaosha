package com.nchu.miaosha.miaosha.service;

import com.nchu.miaosha.bd.domain.User;
import com.nchu.miaosha.materials.vo.GoodsVo;
import com.nchu.miaosha.scm.domain.OrderBill;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @ClassName: MiaoshaService
 * @Author: 时间
 * @Description:
 * @Date: 2020/7/16 0:32
 * @Version: 1.0
 */
public interface MiaoshaService {
    /**
     * 执行秒杀操作
     *
     * @param user
     * @param goods
     * @return
     */
    OrderBill miaosha(User user, GoodsVo goods);

    /**
     * 前端不断轮询该接口 ,查询排队中的秒杀请求是否处理成功
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     *
     * @param userId
     * @param goodsId
     * @return
     */
    long getMiaoshaResult(Long userId, long goodsId);

    /**
     * 数据重置,测试用
     *
     * @param goodsList
     */
    void reset(List<GoodsVo> goodsList);

    /**
     * 生成秒杀隐藏地址path
     *
     * @param user
     * @param goodsId
     * @return
     */
    String createMiaoshaPath(User user, long goodsId);

    /**
     * 校验秒杀地址path参数
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    boolean checkPath(User user, long goodsId, String path);

    /**
     * 生成图形验证码
     *
     * @param user
     * @param goodsId
     * @return
     */
    BufferedImage createVerifyCode(User user, long goodsId);

    /**
     * 校验图形验证码
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    boolean checkVerifyCode(User user, long goodsId, int verifyCode);


}
