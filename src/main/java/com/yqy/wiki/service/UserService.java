package com.yqy.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yqy.wiki.domain.User;
import com.yqy.wiki.domain.UserExample;
import com.yqy.wiki.exception.BusinessException;
import com.yqy.wiki.exception.BusinessExceptionCode;
import com.yqy.wiki.mapper.UserMapper;
import com.yqy.wiki.req.UserQueryReq;
import com.yqy.wiki.req.UserSaveReq;
import com.yqy.wiki.resp.CommonResp;
import com.yqy.wiki.resp.UserQueryResp;
import com.yqy.wiki.resp.PageResp;
import com.yqy.wiki.util.CopyUtil;
import com.yqy.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bahsk
 * @createTime 2021-07-26 14:52
 * @description
 */

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * @author: bahsk
     * @date: 2021/6/18 21:47
     * @description: 返回输入模糊匹配电子书列表
     * @params: String name
     * @return: List<User>
     */
    public PageResp<UserQueryResp> list(UserQueryReq userQueryVO) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        List<User> userList = new ArrayList<>();

        //类似一个where子句
        if (StringUtils.hasLength(userQueryVO.getLoginName())) {

            criteria.andLoginNameLike("%" + userQueryVO.getLoginName() + "%");

        }

        PageHelper.startPage(userQueryVO.getPage(), userQueryVO.getSize());
        userList = userMapper.selectByExample(userExample);


        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<UserQueryResp> userVOPageResp = new PageResp<>();
        PageInfo<UserQueryResp> userVOPageInfo = new PageInfo<>((List) userList);

        userVOPageResp.setTotal(userVOPageInfo.getTotal());
        userVOPageResp.setList(CopyUtil.copyList(userList, UserQueryResp.class));
        return userVOPageResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/6/18 21:47
     * @description: 返回电子书列表
     * @params:
     * @return:
     */
    public List<User> list() {
        return userMapper.selectByExample(null);
    }


    /**
     * @author: bahsk
     * @date: 2021/6/29 22:04
     * @description: 保存
     * @params:
     * @return:
     */
    public CommonResp save(UserSaveReq userSaveReq) {
        User user = CopyUtil.copy(userSaveReq, User.class);
        CommonResp commonResp = new CommonResp();
        int flag = 0;
        if (ObjectUtils.isEmpty(user.getId())) {
            //新增
            if (ObjectUtils.isEmpty(selectByLoginName(userSaveReq.getLoginName()))) {
                user.setId(snowFlake.nextId());
                flag = userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        } else {
            //防止登录名称被修改的办法  将loginName置空 + updateByPrimaryKeySelective 有值才更新
//            flag = userMapper.updateByPrimaryKey(user);
            user.setLoginName(null);
            flag = userMapper.updateByPrimaryKeySelective(user);
        }
//        if(flag == 0){
//            commonResp.setSuccess(false);
//        }
        return commonResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/6/29 23:11
     * @description: 删除
     * @params:
     * @return:
     */
    public CommonResp delete(Long id) {

        CommonResp commonResp = new CommonResp();
        int flag = 0;
        flag = userMapper.deleteByPrimaryKey(id);
        if (flag == 0) {
            commonResp.setSuccess(false);
        }
        return commonResp;
    }

    /**
     * @author: bahsk
     * @date: 2021/7/26 17:11
     * @description: 根据loginName 查询用户信息
     * @params:
     * @return:
     */
    public User selectByLoginName(String loginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();

        criteria.andLoginNameEqualTo(loginName);
        List<User> users = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(users)) {
            return null;
        } else {
            return users.get(0);
        }

    }
}
