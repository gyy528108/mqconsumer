package com.lowi.mqconsumer.dao;

import com.lowi.mqconsumer.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2020-08-13
 */
@Repository
public interface ProductDao extends BaseMapper<Product> {
    /**
     * 更新产品的数据
     * @param id 产品id
     * @auther gengyy
     * @date 2020-08-13 17:57
     * @return 返回是否成功
     */
    void updateCount(Integer id);
}
