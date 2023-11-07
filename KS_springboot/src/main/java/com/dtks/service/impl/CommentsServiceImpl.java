package com.dtks.service.impl;

import com.dtks.entity.Comments;
import com.dtks.mapper.CommentsMapper;
import com.dtks.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-10
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {

}
