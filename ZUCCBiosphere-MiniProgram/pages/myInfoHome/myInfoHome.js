// pages/myInfoHome/myInfoHome.js
const app = getApp()
/**
 * toast提示框
 */
var toast = require('../../utils/toast/toast.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        hasMsg: false,
        userInfo: null,
        level: 0,
        hasUserInfo: false,
        isCheckedIn: false,
        totalDays: 0,
        energyPoint: 0,
        
    },
    /**
     * 跳转到个人信息修改
     */
    toUpdateInfo(){
        var that = this;
        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()
        }else{
            wx.navigateTo({
                url: '/pages/updateInfo/updateInfo?nickName=' + that.data.userInfo.userName  + '&avatarUrl=' + that.data.userInfo.avatarUrl + '&userID=' + that.data.userInfo.id,
            })
        }
    },
    /**
     * 跳转到关于我们
     */
    toAboutUs(){
        wx.navigateTo({
            url: '/pages/aboutUs/aboutUs',
        })
    },
    /**
     * 跳转到社区守则
     */
    toCommunityRuleList(){
        wx.navigateTo({
            url: '/pages/communityRule/communityRule',
        })
    },
    /**
     * 微信登录
     */
    getUserProfile(){
        var that = this;
        app.getUserProfile().finally(() => {
            that.setData({
                hasUserInfo: app.globalData.hasUserInfo,
                userInfo: app.globalData.userInfo,
                level: app.globalData.level,
                hasMsg: app.globalData.hasNewMsg,
            })
        })
    },

    checkIn() {
        var that = this;
        wx.showLoading({
            title: '加载中',
          })
        
        wx.request({
            method: 'POST',
            url: app.globalData.urlHome + '/user/auth/checkIn',
            header: {
                'content-type': 'application/json',
                'token': app.globalData.token
              },
              data: {
                  'openID': app.globalData.openID
              },
            success: (res)=>{
                if(res.data.code == 200){
                    toast.showToastWithMaskAndImage(that, "签到成功", "能量值+20","../../icon/checkInSuccess.png")
                    that.setData({
                        isCheckedIn: true,
                        totalDays: res.data.data.totalDays,
                    })
                    that.onPullDownRefresh();
                }
                else if (res.data.code == 1005) {
                    var obj = {
                        msg: res.data.msg,
                        type: "tip"
                    }
                    that.promptBox.open(obj);
                    that.setData({
                        isCheckedIn:true,
                        totalDays:res.data.data.totalDays,
                    })
                }
                else{
                    var obj = {
                        msg: res.data.msg,
                        type: "error"
                    }
                    that.promptBox.open(obj);
                    if(res.data.code == 300){
                        app.clearUserData();
                        that.setData({
                            userInfo: null,
                            level: 0,
                            hasUserInfo:false,
                        })
                    }
                }
            },
            complete: (res) =>{
                wx.hideLoading();
              },
            fail: (res) => {
                wx.showToast({
                    title: '服务器错误',
                    duration: 2000,
                    icon: 'error'
                })
            },
        })
        },

    
    /**
     * 我的帖子
     */
    toMyPost: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            wx.showToast({
              title: '请先登录!',
              icon: 'error',
              duration: 1500
            })
        }else{
            wx.navigateTo({
                url: '/pages/myPostOrMyStarPage/myPostOrMyStarPage?type=1&userID=' + that.data.userInfo.id,
            })
        }
    },

    /**
     * 通知
     */
    toMyMsg: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            wx.showToast({
                title: '请先登录!',
                icon: 'error',
                duration: 1500
              })
        }else{
            wx.navigateTo({
                url: '/pages/noticePage/noticePage?userID=' + that.data.userInfo.id,
            })
        }
    },
    /**
     * 我的收藏
     */
    toMyStar: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            wx.showToast({
                title: '请先登录!',
                icon: 'error',
                duration: 1500
              })
        }else{
            wx.navigateTo({
                url: '/pages/myPostOrMyStarPage/myPostOrMyStarPage?type=2&userID=' + that.data.userInfo.id,
            })
        }
    },
    
    /**
     * 我的评论
     */
    toMyComment: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            wx.showToast({
                title: '请先登录!',
                icon: 'error',
                duration: 1500
              })
        }else{
            wx.navigateTo({
                url: '/pages/myComments/myComments?&userID=' + that.data.userInfo.id,
            })
        }
    },
    /**
     * 跳转到帖子详情页面
     */
    toMyAdopt: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            wx.showToast({
                title: '请先登录!',
                icon: 'error',
                duration: 1500
              })
        }else{
            wx.navigateTo({
            url: '/pages/myAdopt/myAdopt',
            })
        }
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        
    
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");
        that.promptBox = that.selectComponent("#promptBox");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");
        that.onPullDownRefresh();
        if (typeof that.getTabBar === 'function' && that.getTabBar()) {
    
            that.getTabBar().setData({
    
            selected: 3
    
          })
    
        }
        if(app.globalData.hasUserInfo){
            that.setData({
                hasUserInfo: true,
                userInfo: app.globalData.userInfo,
                level: app.globalData.level,
            })
        }
        // console.log(that.data)
      },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
        var that = this;
        if(that.data.hasUserInfo == true){
            wx.showLoading({
                title: '加载中',
              })
            wx.request({
                method: 'GET',
                url: app.globalData.urlHome + '/user/auth/getUserInfo/' + app.globalData.openID,
                header: {
                    'content-type': 'application/json',
                    'token': app.globalData.token
                  },
                success: (res)=>{
                    if(res.data.code == 200){
                        app.globalData.userInfo = res.data.data.userInfo;
                        app.globalData.level = res.data.data.level;
                        app.globalData.hasNewMsg = res.data.data.hasMsg;
                        that.setData({
                            userInfo: res.data.data.userInfo,
                            level: res.data.data.level,
                            hasMsg: res.data.data.hasMsg
                        })
                    }
                    else{
                        var obj = {
                            msg: res.data.msg,
                            type: "error"
                        }
                        that.promptBox.open(obj);
                        if(res.data.code == 300){
                            app.clearUserData();
                            that.setData({
                                userInfo: null,
                                level: 0,
                                hasUserInfo:false,
                            })
                        }
                    }
                },
                complete: (res)=>{
                    wx.hideLoading();
                },
                fail: (res) => {
                    wx.showToast({
                        title: '服务器错误',
                        duration: 2000,
                        icon: 'error'
                    })
                },
            })
        }
        // else{
        //     wx.showToast({
        //       title: '请先登录！',
        //       icon: 'error',
        //       duration: 2000
        //     })
        // }
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})