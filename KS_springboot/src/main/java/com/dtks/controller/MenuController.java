package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.common.Constants;
import com.dtks.common.Result;
import com.dtks.entity.Dict;
import com.dtks.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import com.dtks.service.IMenuService;
import com.dtks.entity.Menu;
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
@RequestMapping("/menu")
public class MenuController {
        @Resource
        private IMenuService  menuService;
        @Resource
        private DictMapper dictMapper;
        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Menu menu){
                menuService.saveOrUpdate(menu);
                return Result.success();
        }
        //删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
             menuService.removeById(id);
             return Result.success();

        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                menuService.removeByIds(ids);
                return Result.success();

        }
        //查询所有
        @GetMapping
        public Result findAll(@RequestParam(defaultValue = "") String name) {


                return Result.success(menuService.findMenus(name));
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(menuService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam String name,@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
                QueryWrapper<Menu> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.like("name",name);
                queeryWrapper.orderByDesc("id");
                return Result.success(menuService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }
        @GetMapping("/icons")
        public Result getIcons() {
                QueryWrapper<Dict> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.eq("type", Constants.DICT_TYPE_ICON);
                return Result.success(dictMapper.selectList(queeryWrapper)) ;
        }

}

