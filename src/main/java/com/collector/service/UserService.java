package com.collector.service;

import com.collector.entity.User;
import java.util.List;

/**
 * @Author:abc
 * @Description:
 * @params:$params$
 * @return: $returns$
 * @Date: $date$ $time$
 */

public interface UserService {

    boolean add(User user);

    boolean del(String userId);

    List<User> findAll();

    User sel(String userId);

    List<User> seach(String userName, String phone);

}
