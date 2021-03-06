package com.lxgy.storm.bigdata.drpc;

/**
 * 用户的服务接口
 */
public interface UserService {

    public static final long versionID = 88888888;

    /**
     * 添加用户
     * @param name 名字
     * @param age  年龄
     */
    public void addUser(String name, int age);
}
