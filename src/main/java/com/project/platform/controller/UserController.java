package com.project.platform.controller;

import com.project.platform.entity.User;
import com.project.platform.service.UserService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//所有请求都能接受，不然下面的访问不了
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    //查询user表中所有数据
    @GetMapping("list")
    public ResponseVO<List<User>> list(){
        return ResponseVO.ok(userService.list());
    }
    //添加用户
    @PostMapping("add")
    public ResponseVO add(@RequestBody User entity){
        userService.insert(entity);
        return ResponseVO.ok();
    }
    //更新用户
    @PutMapping("update")
    public ResponseVO update(@RequestBody User entity){
        userService.updateById(entity);
        return ResponseVO.ok();
    }

    //删除用户(可以单个或多个)
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids){
        userService.deleteById(ids);
        return ResponseVO.ok();
    }
    //分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<User>> page
            (@RequestParam Map<String, Object> query,
             //起始页默认为1
             @RequestParam(defaultValue = "1")Integer pageNum,
             //分页大小默认为10
             @RequestParam(defaultValue = "10")Integer pageSize){
        PageVO<User> page = userService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }
    //根据id查询数据
    @GetMapping("selectById/{id}")
    public ResponseVO selectById(@PathVariable("id") Integer id){
        User user = userService.selectById(id);
        return ResponseVO.ok(user);
    }
    //充值
    @PostMapping("/topUp/{amount}")
    public ResponseVO topUp(@PathVariable Float amount){
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        userService.topUp(userId,amount);
        return ResponseVO.ok();
    }

    //消费（扣减余额）
    @PostMapping("/consumption/{amount}")
    public ResponseVO consumption(@PathVariable Float amount){
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        userService.consumption(userId, amount);
        return ResponseVO.ok();
    }

}
