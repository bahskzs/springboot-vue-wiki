package com.yqy.wiki.mapper;

import com.yqy.wiki.domain.ChartData;
import com.yqy.wiki.domain.ChartDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChartDataMapper {
    long countByExample(ChartDataExample example);

    int deleteByExample(ChartDataExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ChartData record);

    int insertSelective(ChartData record);

    List<ChartData> selectByExample(ChartDataExample example);

    ChartData selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ChartData record, @Param("example") ChartDataExample example);

    int updateByExample(@Param("record") ChartData record, @Param("example") ChartDataExample example);

    int updateByPrimaryKeySelective(ChartData record);

    int updateByPrimaryKey(ChartData record);
}