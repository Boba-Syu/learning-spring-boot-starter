package cn.bobasyu.orm.test.mapper;


import cn.bobasyu.orm.test.entity.User;

public interface IUserMapper {
    User queryUserById(Long id);
}
