package com.ttb.dao;

import com.ttb.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @project: ShiroOne
 * @author: wangchengda
 * @email: wangchengda1990@gmail.com
 * @date: 2018/10/8
 * @Description:
 * @Copyright: 2018 broderickwang.github.io Inc. All rights reserved.
 * @version: V1.0
 */
@Mapper
@Component
public interface UserDao {

    UserDO getOne(String id);

    UserDO getOneByName(String username);

    List<String> getRoleByUsername(String username);
}
