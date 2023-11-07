package com.dtks.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.dtks.common.Result;
import javax.annotation.Resource;
import com.dtks.service.ICommentsService;
import com.dtks.entity.Comments;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jianghui
 * @since 2023-10-10
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {
        @Resource
        private ICommentsService  commentsService;
        //新增或者更新
        @PostMapping
        public Result save(@RequestBody Comments comments){
                commentsService.saveOrUpdate(comments);
                return Result.success();
        }
        //删除
        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
             commentsService.removeById(id);
             return Result.success();

        }
        //删除所有的选中
        @PostMapping("/del/batch")
        public Result deleteBatch(@RequestBody List<Integer> ids){
                commentsService.removeByIds(ids);
                return Result.success();

        }
        //查询所有
        @GetMapping("/one/{fileId}")
        public Result findAll(@PathVariable Integer fileId) {
                QueryWrapper<Comments> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.eq("file_id",fileId);
                return Result.success(commentsService.list(queeryWrapper));
        }
        //根据id查询
        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
                return Result.success(commentsService.getById(id)) ;
        }
        //分页查询
        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize) {
                QueryWrapper<Comments> queeryWrapper = new QueryWrapper<>();
                queeryWrapper.orderByDesc("id");
                return Result.success(commentsService.page(new Page<>(pageNum, pageSize),queeryWrapper));
        }


}

