package cn.bobasyu.test.mapper;

import cn.bobasyu.test.entity.User;

public interface IUserMapper {
    User queryUserById(Long id);
}
