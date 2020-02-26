package winemall.mapper;

import org.apache.ibatis.annotations.Param;
import winemall.bean.Ser;
import winemall.bean.SerExample;

import java.util.List;

public interface SerMapper {
    long countByExample(SerExample example);

    int deleteByExample(SerExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Ser record);

    int insertSelective(Ser record);

    List<Ser> selectByExample(SerExample example);

    Ser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Ser record, @Param("example") SerExample example);

    int updateByExample(@Param("record") Ser record, @Param("example") SerExample example);

    int updateByPrimaryKeySelective(Ser record);

    int updateByPrimaryKey(Ser record);
}