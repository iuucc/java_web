package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.common.Constants;
import com.dtks.entity.Commodity;
import com.dtks.service.ICommodityService;
import com.dtks.service.IUserService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.dtks.common.Result;
import javax.annotation.Resource;
import com.dtks.service.IOrdersService;
import com.dtks.entity.Orders;
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
@RequestMapping("/orders")
public class OrdersController {
        @Resource
        private IUserService userService;
        @Resource
        private ICommodityService commodityService;
        @Resource
        private IOrdersService  ordersService;
        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Orders orders){
                Integer number=orders.getAmount();
                Integer id=orders.getFileId();
                Commodity file = commodityService.getById(id);
                Integer inventory = file.getInventory();
                if(inventory>=number){
                        Integer new_inventory=inventory-number;
                        commodityService.updateInventory(id,new_inventory);
                        ordersService.saveOrUpdate(orders);
                        return Result.success();
                }else {
                        return Result.error(Constants.CODE_500,"库存不足");
                }


        }
        //删除
        @DeleteMapping("/{id}/{fileId}")
        public Result delete(@PathVariable Integer id,@PathVariable Integer fileId) {
                Integer inventory = commodityService.getById(fileId).getInventory();
                Integer newInventory=inventory+1;
                commodityService.updateInventory(fileId,newInventory);
                ordersService.removeByOrderNumber(id);
             return Result.success();

        }
        @GetMapping("/updata/{id}/{paid}")
        public Result updataPaid(@PathVariable Integer id,@PathVariable Integer paid) {
                String name="已支付";
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(date);
                ordersService.updataPaid(id,name,formattedDate);
                return Result.success();
        }
        @GetMapping("/updata1/{id}")
        public Result updataDelivery(@PathVariable Integer id) {
                String name="已发货";
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(date);
                ordersService.updataDelivery(id,name,formattedDate);
                return Result.success();
        }
        @GetMapping("/updata2/{id}")
        public Result updataDelivery1(@PathVariable Integer id) {
                String name="已收货";
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(date);
                ordersService.updataReceipttime(id,name,formattedDate);
                return Result.success();
        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                ordersService.removeByIds(ids);
                return Result.success();

        }
        //查询所有
        @GetMapping
        public Result findAll() {
                return Result.success(ordersService.list());
        }
        //根据id查询
        @GetMapping("/finduser/{username}")
        public Result finduser(@PathVariable String username) {
                return Result.success(userService.getByUsername(username)) ;
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(ordersService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize, @RequestParam Integer id,@RequestParam(defaultValue = "") String orderNumber, @RequestParam(defaultValue = "") String fileName) {

                QueryWrapper<Orders> queeryWrapper = new QueryWrapper<>();
                if (!"".equals(orderNumber)) {
                        queeryWrapper.like("order_number", orderNumber);
                }
                if (!"".equals(fileName)) {
                        queeryWrapper.like("file_name", fileName);
                }
                queeryWrapper.eq("user_id",id);
                queeryWrapper.orderByDesc("order_number");
                return Result.success(ordersService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }
        @GetMapping("/sell")
        public Result findPageSell(@RequestParam Integer pageNum,@RequestParam Integer pageSize, @RequestParam String name,@RequestParam(defaultValue = "") String orderNumber) {
                QueryWrapper<Orders> queeryWrapper = new QueryWrapper<>();
                if (!"".equals(orderNumber)) {
                        queeryWrapper.like("order_number", orderNumber);
                }
                queeryWrapper.eq("sellername",name);
                queeryWrapper.eq("paid","已支付");
                queeryWrapper.orderByDesc("order_number");
                return Result.success(ordersService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }


}

