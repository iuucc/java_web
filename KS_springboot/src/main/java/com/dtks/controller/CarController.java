package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.entity.Orders;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dtks.common.Result;
import javax.annotation.Resource;
import com.dtks.service.ICarService;
import com.dtks.entity.Car;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jianghui
 * @since 2023-10-09
 */
@RestController
@RequestMapping("/car")
public class CarController {
        @Resource
        private ICarService  carService;

        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Car car){
                carService.saveOrUpdate(car);
                return Result.success();
        }
        //删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
             carService.removeById(id);
             return Result.success();

        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                carService.removeByIds(ids);
                return Result.success();

        }

        //查询所有
        @GetMapping
        public Result findAll() {
                return Result.success(carService.list());
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(carService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize, @RequestParam Integer id, @RequestParam(defaultValue = "") String carnumber, @RequestParam(defaultValue = "") String fileName) {
                QueryWrapper<Car> queeryWrapper = new QueryWrapper<>();
                if (!"".equals(carnumber)) {
                        queeryWrapper.like("carnumber", carnumber);
                }
                if (!"".equals(fileName)) {
                        queeryWrapper.like("file_name", fileName);
                }
                queeryWrapper.eq("user_id",id);
                queeryWrapper.orderByDesc("carnumber");
                return Result.success(carService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }


}

