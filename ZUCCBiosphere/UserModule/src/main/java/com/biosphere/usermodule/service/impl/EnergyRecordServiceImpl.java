package com.biosphere.usermodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.biosphere.library.vo.*;
import com.biosphere.usermodule.mapper.EnergyRecordMapper;
import com.biosphere.usermodule.mapper.UserMapper;
import com.biosphere.library.pojo.EnergyRecord;
import com.biosphere.library.pojo.User;
import com.biosphere.usermodule.service.IEnergyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyh
 * @since 2022-08-18
 */
@Slf4j
@Service
public class EnergyRecordServiceImpl extends ServiceImpl<EnergyRecordMapper, EnergyRecord> implements IEnergyRecordService {

    @Autowired
    private EnergyRecordMapper energyRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseResult checkIn(User user) {
        // Date curDate = new Date(System.currentTimeMillis());
        if (energyRecordMapper.isCheckedInToday(user.getId()) > 0) {
            return ResponseResult.error(RespBeanEnum.REPEAT_CHECKIN);
        }
        EnergyRecord energyrecord = new EnergyRecord();
        energyrecord.setGetDate(new Date(System.currentTimeMillis()));
        energyrecord.setIschecked(1);
        energyrecord.setPoint(20);
        energyrecord.setToUserID(user.getId());
        energyrecord.setUserID(user.getId());
        energyrecord.setType(0);
        user.setEnergyPoint(user.getEnergyPoint() + 20);
        int userNum = userMapper.updateById(user);
        redisTemplate.opsForValue().set("login:" + user.getOpenID(), user, 1, TimeUnit.DAYS);
        energyRecordMapper.insert(energyrecord);
        if (energyrecord.getId() > 0 && userNum > 0) {
            return ResponseResult.success();
        }else {
            log.info("[用户:{}{} 签到记录插入失败]",user.getUserName(),user.getOpenID());
            throw new ExceptionLogVo(RespBeanEnum.CHECKIN_INSERT_ERROR);
        }
    }

    @Override
    public Integer getDaysSum(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        QueryWrapper<EnergyRecord> queryWrapper = new QueryWrapper<>();
        //取时间进行计算，如果后一天的时间减去一天不等于前一天就直接返回
        queryWrapper.select("DATE_FORMAT(getDate,'%Y-%m-%d') as getDate").eq("userID",user.getId()).eq("type",0).eq("toUserID",user.getId()).orderByDesc("getDate");
        List<EnergyRecord> energyRecordList = energyRecordMapper.selectList(queryWrapper);
        Integer sum = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(energyRecordList.get(0).getGetDate());
        for (int i = 1; i < energyRecordList.size(); i ++) {
            Calendar curCal = Calendar.getInstance();
            curCal.setTime(energyRecordList.get(i).getGetDate());
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            if (curCal.equals(calendar)) {
                sum ++;
            }else
                break;
            calendar.setTime(energyRecordList.get(i).getGetDate());
        }
        return sum;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertEnergyRecord(RewardVo rewardVo) {
        EnergyRecord energyRecord = new EnergyRecord();
        energyRecord.setUserID(rewardVo.getUserID());
        energyRecord.setPoint(rewardVo.getPoint());
        energyRecord.setType(rewardVo.getType());
        energyRecord.setToUserID(rewardVo.getToUserID());
        energyRecord.setGetDate(new Date(System.currentTimeMillis()));
        energyRecord.setIschecked(0);
        energyRecordMapper.insert(energyRecord);
        if (energyRecord.getId() <= 0) {
            log.error("能量值插入失败");
            throw new ExceptionNoLogVo(RespBeanEnum.ENERGY_NOT_ENOUGH);

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserEnergy(Integer userID, Integer point, Integer type) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("openID,energyPoint").eq("id", userID);
        Integer finalPoint = 0;
        User user = userMapper.selectOne(userQueryWrapper);

        if (type == 0) {
            finalPoint = user.getEnergyPoint() - point;
            //先要检查剩余能量是否够减
            if (finalPoint < 0) {
                throw new ExceptionNoLogVo(RespBeanEnum.ENERGY_NOT_ENOUGH);

            }
        } else if (type == 1) {
            finalPoint = user.getEnergyPoint() + point;
        }
        Integer num = userMapper.updateEnergyPoint(finalPoint, userID);
        if (num <= 0) {
            throw new ExceptionLogVo(RespBeanEnum.UPDATE_USER_ENERGY_ERROR);

        }else {
            //查看缓存，这个用户如果有数据就更新，没有就不更新
            if (redisTemplate.hasKey("login:" + user.getOpenID())){
                User userInRedis = (User) redisTemplate.opsForValue().get("login:" + user.getOpenID());
                userInRedis.setEnergyPoint(finalPoint);
                redisTemplate.opsForValue().set("login:" + userInRedis.getOpenID(),userInRedis,5760,TimeUnit.MINUTES);
            }
        }
    }
}
