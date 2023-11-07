package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.common.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.annotation.Resource;
import com.dtks.service.IRoleService;
import com.dtks.entity.Role;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jianghui
 * @since 2023-10-06
 */
@RestController
@RequestMapping("/role")
public class RoleController {
        @Resource
        private IRoleService  roleService;
        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Role role){
                roleService.saveOrUpdate(role);
                return Result.success();
        }
        //删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
             roleService.removeById(id);
             return Result.success();

        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                roleService.removeByIds(ids);
                return Result.success();

        }
        //查询所有
        @GetMapping
        public Result findAll() {
                return Result.success(roleService.list());
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(roleService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam String name,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
                QueryWrapper<Role> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.like("name",name);
                queeryWrapper.orderByDesc("id");
                return Result.success(roleService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }
        @PostMapping("/roleMenu/{roleId}")
        public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
               roleService.setRoleMenu(roleId,menuIds);
                return Result.success();
        }
        @GetMapping("/roleMenu/{roleId}")
        public Result getRoleMenu(@PathVariable Integer roleId){
                return Result.success(roleService.getRoleMenu(roleId));
        }

}

