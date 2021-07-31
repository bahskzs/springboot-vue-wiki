package com.yqy.wiki.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author bahsk
 * @createTime 2021-07-29 8:57
 * @description
 */
public interface DocMapperCustom {

    public void increaseViewCountByPrimaryKey(@Param("id") Long id);

    public void increaseVoteCountByPrimaryKey(@Param("id") Long id);

    public void refreshEbookInfo();

}
