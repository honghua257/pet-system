package com.project.platform.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.project.platform.dto.CurrentUserDTO;
import com.project.platform.dto.RetrievePasswordDTO;
import com.project.platform.dto.UpdatePasswordDTO;
import com.project.platform.entity.User;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.UserMapper;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Value("${resetPassword}")
    private String resetPassword;

    //登录功能
    @Override
    public CurrentUserDTO login(String username, String password) {
        //查询用户名是否存在
        User user = userMapper.selectByUsername(username);
        if (user == null || !password.equals(user.getPassword())){
            throw new CustomException("用户名或密码错误");
        }
        //查询是否禁用
        if("禁用".equals(user.getStatus())){
            throw new CustomException("用户已禁用");
        }
        CurrentUserDTO currentUserDTO = new CurrentUserDTO();
        BeanUtils.copyProperties(user,currentUserDTO);
        return currentUserDTO;
    }
    //注册功能
    @Override
    public void register(JSONObject data) {
        User user = new User();
        user.setUsername(data.getString("username"));
        user.setPassword(data.getString("password"));
        user.setAvatarUrl(data.getString("avatarUrl"));
        user.setNickname(data.getString("nickname"));
        user.setBalance(100F);
        user.setStatus("启用");
        insert(user);
    }

    @Override
    public void updateCurrentUserInfo(CurrentUserDTO currentUserDTO) {
        User user = userMapper.selectById(currentUserDTO.getId());
        user.setId(currentUserDTO.getId());
        user.setNickname(currentUserDTO.getNickname());
        user.setAvatarUrl(currentUserDTO.getAvatarUrl());
        user.setTel(currentUserDTO.getTel());
        user.setEmail(currentUserDTO.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void updateCurrentUserPassword(UpdatePasswordDTO updatePassword) {
        User user = userMapper.selectById(CurrentUserThreadLocal.getCurrentUser().getId());
        if(!user.getPassword().equals(updatePassword.getOldPassword())){
            throw new CustomException("旧密码错误");
        }
        user.setPassword(updatePassword.getNewPassword());
        userMapper.updateById(user);
    }

    @Override
    public void resetPassword(Integer id) {
        User user = userMapper.selectById(id);
        user.setPassword(resetPassword);
        userMapper.updateById(user);
    }

    @Override
    public void retrievePassword(RetrievePasswordDTO retrievePasswordDTO) {

    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public void topUp(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance()+amount);
        userMapper.updateById(user);
    }

    @Override
    public void consumption(Integer userId, Float amount) {
        User user = selectById(userId);
        user.setBalance(user.getBalance() - amount);
        if (user.getBalance() < 0){
            throw new CustomException("余额不足");
        }
        userMapper.updateById(user);
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public void insert(User entity) {
        //检查一下用户名不能重复
        check(entity);
        //密码为空时，设置密码为123456
        if(entity.getPassword() == null){
            entity.setPassword(resetPassword);
        }
        userMapper.insert(entity);
    }

    @Override
    public void updateById(User entity) {
        check(entity);
        userMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        userMapper.deleteById(ids);
    }

    @Override
    public PageVO<User> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<User> page = new PageVO<>();
        //传进来一般是1，但是是从0开始，所以要-1
        List<User> list = userMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(userMapper.queryCount(query));
        return page;
    }


    //更新或插入数据检查的办法，用户名不能重复
    private void check(User entity) {
        User user = userMapper.selectByUsername(entity.getUsername());
        if (user != null && user.getId()!= entity.getId()) {
            throw new CustomException("用户名已存在");
        }
    }

}
