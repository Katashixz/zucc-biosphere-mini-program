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

        userInfo: {
            energyPoint: 0,
        //     userName: "蟹黄宝宝宝宝宝宝宝宝宝",
        //     avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMVTMlfE2lQaFmUM5AvQQ4kg/132"
        },
        level: 0,
        hasUserInfo: false,
        isCheckedIn: false,
        totalDays: 0,
        
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
            url: app.globalData.urlHome + '/user/checkIn',
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
                        msg: "重复签到了哦",
                        type: "tip"
                    }
                    that.promptBox.open(obj);
                    // toast.showToastWithMaskAndImage(that, "出错啦", "重复签到了哦","../../icon/error.png")
                    that.setData({
                        isCheckedIn:true,
                        totalDays:res.data.data.totalDays,
                    })
                }
                else{
                    // toast.showToastWithMaskAndImage(that, "出错啦", "请重新签到","../../icon/error.png")
                    var obj = {
                        msg: "出错啦,请重新签到",
                        type: "error"
                    }
                    that.promptBox.open(obj);
                }
            },
            complete: (res) =>{
                wx.hideLoading();
              }
        })
        },

    
    /**
     * 我的帖子
     */
    toMyPost: function (e) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            that.getUserProfile()
        }else{
            wx.navigateTo({
                url: '/pages/templatePage/templatePage?type=1&userID=' + that.data.userInfo.id,
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
        this.promptBox = this.selectComponent("#promptBox");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

        // console.log("onShow")
    
        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
          this.getTabBar().setData({
    
            selected: 3
    
          })
    
        }
        
        var that = this;
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
        wx.showLoading({
            title: '加载中',
          })
        if(that.data.hasUserInfo == true){
            wx.request({
                method: 'GET',
                url: app.globalData.urlHome + '/user/getUserInfo/' + app.globalData.openID,
                header: {
                    'content-type': 'application/json',
                    'token': app.globalData.token
                  },
                success: (res)=>{
                    if(res.data.code == 200){
                        app.globalData.userInfo = res.data.data.userInfo;
                        app.globalData.level = res.data.data.level;
                        that.setData({
                            userInfo: res.data.data.userInfo,
                            level: res.data.data.level,
                        })
                    }else{
                        wx.showToast({
                          title: res.data.msg,
                          duration: 2000,
                          icon: 'error'
                        })
                    }
                },
                complete: (res)=>{
                    wx.hideLoading();
                }
            })
        }else{
            wx.showToast({
              title: '请先登录！',
              icon: 'error',
              duration: 2000
            })
        }
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