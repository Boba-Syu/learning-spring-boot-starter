package cn.bobasyu.orm.spring.test.mapper;

import cn.bobasyu.orm.spring.test.entity.User;

public interface IUserMapper {
    User queryUserById(Long id);
}
