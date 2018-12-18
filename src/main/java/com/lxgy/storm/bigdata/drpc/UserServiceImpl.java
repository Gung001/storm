package com.lxgy.storm.bigdata.drpc;

/**
 * 用户的服务接口实现类
 */
public class UserServiceImpl implements UserService{

    public void addUser(String name, int age) {
        System.out.println("From Server Invoked: add user success... , name is :" + name);
    }
}
