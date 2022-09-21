package com.biosphere.communitymodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.Post;
import com.biosphere.library.vo.CommunityPostVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2022-09-08
 */
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT p.id as postID, p.userID as userID, u.avatarUrl as avatarUrl, u.userName as userName,p.theme as theme, p.content as content, p.postDate as postDate, p.isTop as isTop, p.isEssential as isEssential, p.imageUrl, DATE_FORMAT(p.postDate,\"%Y-%m-%d %H:%m\") as dateformat, COUNT(DISTINCT c.id) as commentNum, COUNT(DISTINCT l.id) as likeNum\n" +
            "from post p \n" +
            "LEFT JOIN `comment` c on p.id = c.postID \n" +
            "LEFT JOIN like_record l on p.id = l.postID\n" +
            "LEFT JOIN `user` u on p.userID = u.id\n" +
            "GROUP BY p.id\n" +
            "ORDER BY p.postDate DESC\n" +
            "LIMIT #{start},#{end}")
    List<CommunityPostVo> loadPostWithPage(@Param("start")Integer start, @Param("end")Integer end);

}
