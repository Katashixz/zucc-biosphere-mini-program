package com.biosphere.communitymodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.communitymodule.mapper.CommentMapper;
import com.biosphere.library.pojo.Comment;
import com.biosphere.communitymodule.service.ICommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
