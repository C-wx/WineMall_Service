package winemall.mapper;

import org.apache.ibatis.annotations.Param;
import winemall.bean.Order;
import winemall.bean.OrderExample;

import java.util.List;

public interface OrderMapper {
    long countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<String> getOrderCodes(@Param("orderStatus") String orderStatus);

    List<Order> getALlOrderByOrderCode(@Param("orderCode") String orderCode);
}