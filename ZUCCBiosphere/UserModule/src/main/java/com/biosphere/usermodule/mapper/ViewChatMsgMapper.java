package com.biosphere.usermodule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.biosphere.library.pojo.ViewChatMsg;
import com.biosphere.library.vo.UserInfoVo;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author hyh
 * @since 2023-03-25
 */
public interface ViewChatMsgMapper extends BaseMapper<ViewChatMsg> {
    @Select(
            "SELECT * \n" +
                    "FROM\n" +
                    " (\n" +
                    "SELECT v1.*, v1.targetID AS contactPerson\n" +
                    "FROM view_chat_msg v1\n" +
                    "WHERE ( v1.sourceID = #{userID} ) AND ( v1.targetID <> #{userID} ) UNION\n" +
                    "SELECT v2.*, v2.sourceID AS contactPerson\n" +
                    "FROM view_chat_msg v2\n" +
                    "WHERE ( v2.sourceID <> #{userID} )  AND ( v2.targetID = #{userID} ) \n" +
                    "ORDER BY createdAt DESC\n" +
                    " ) v \n" +
                    "GROUP BY contactPerson \n" +
                    "ORDER BY MAX( createdAt ) DESC"
    )
    List<ViewChatMsg> getLatestMsg(@Param("userID") Integer userID);


    @Select("SELECT chatId, sourceName, sourceAvatar, sourceID, targetID, targetAvatar, targetName, content, msgType, createdAt\n" +
            "FROM view_chat_msg\n" +
            "WHERE (sourceID = #{sourceID} AND targetID = #{targetID}) OR (sourceID = #{targetID} AND targetID = #{sourceID})\n" +
            "ORDER BY createdAt ASC")
    List<ViewChatMsg> getOneChatHistory(@Param("sourceID") Integer sourceID, @Param("targetID") Integer targetID);
}
