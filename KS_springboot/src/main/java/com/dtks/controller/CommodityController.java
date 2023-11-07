package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.entity.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dtks.common.Result;
import javax.annotation.Resource;
import com.dtks.service.ICommodityService;
import com.dtks.entity.Commodity;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jianghui
 * @since 2023-10-08
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {
        @Resource
        private ICommodityService  commodityService;
        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Commodity commodity){

                commodityService.saveOrUpdate(commodity);
                return Result.success();
        }
        //删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
             commodityService.removeById(id);
             return Result.success();

        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                commodityService.removeByIds(ids);
                return Result.success();
        }
        //查询所有
        @GetMapping("/all")
        public Result findAll() {
                List<Commodity> list = commodityService.list();
                return Result.success(list);
        }

        @PostMapping("/whattype/type")
        public Result whattype(@RequestBody String type){
                type = type.replaceAll("^\"|\"$", "");
                QueryWrapper<Commodity> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.like("type", type);
                return Result.success(commodityService.list(queeryWrapper));
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(commodityService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam(defaultValue = "") String tradename,@RequestParam(defaultValue = "") String type, @RequestParam String id) {
                IPage<Commodity> page = new Page<>(pageNum, pageSize);
                QueryWrapper<Commodity> queeryWrapper = new QueryWrapper<>();
                if (!"".equals(tradename)) {
                        queeryWrapper.like("tradename", tradename);
                }
                if (!"".equals(type)) {
                        queeryWrapper.like("type", type);
                }
                queeryWrapper.eq("userid",id);
                queeryWrapper.orderByDesc("id");
                return Result.success(commodityService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }


}

