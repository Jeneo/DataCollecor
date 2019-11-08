package com.collector.mapper;

import java.util.List;
import java.util.Optional;

import com.collector.entity.User;


/**
 * @Author:abc
 * @Description:
 * @params:$params$
 * @return: $returns$
 * @Date: $date$ $time$
 */

public interface UserMapper {
	void save(User user);
	void deleteById(String userId);
	List<User> findAll();
	Optional<User> findById(String userId);
	List<User> seach(String userName, String phone);
}
