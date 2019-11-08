package com.collector.service.impl;

import com.collector.entity.User;
import com.collector.mapper.UserMapper;
import com.collector.service.UserService;
import com.collector.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author:abc
 * @Description:
 * @params:$params$
 * @return: $returns$
 * @Date: $date$ $time$
 */

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public boolean add(User userDto) {
		User user = new User();
		BeanUtils.copyProperties(userDto, user);
		userMapper.save(user);
		return true;
	}

	@Override
	public boolean del(String userId) {
		userMapper.deleteById(userId);
		return false;
	}

	@Override
	public List<User> findAll() {
		List<User> all = userMapper.findAll();
		return all;
	}

	@Override
	public User sel(String userId) {
		User userDto = new User();
		Optional<User> opUser = userMapper.findById(userId);
		User user = opUser.get();
		BeanUtils.copyProperties(user, userDto);
		return userDto;
	}

	@Override
	public List<User> seach(String userName, String phone) {
		List<User> list = userMapper.seach(userName, phone);
		return list;
	}

//    @Override
//    public List<User> fuzzyQuery(UserDto userDto) {
//        List<User> users = userMapper.fuzzyQuery(userDto);
//        return users;
//    }
}
