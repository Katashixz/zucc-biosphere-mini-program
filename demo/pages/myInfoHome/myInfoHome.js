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
            userName: "蟹黄宝宝宝宝宝宝宝宝宝",
            avatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMVTMlfE2lQaFmUM5AvQQ4kg/132"
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
        
        wx.getUserProfile({
            desc: '获取您的openID用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
            success: (res) => {
                app.globalData.userInfo = res.userInfo;
                app.globalData.hasUserInfo = true;
                that.setData({
                    // hasUserInfo: true,
                    userInfo: res.userInfo
                })
                // console.log(res);
                that.userLogin();
            },

        })
    },

    userLogin() {
        var that = this;

        // 登录
        wx.login({
            success: (res) => {
                // console.log(app.globalData.userInfo);

                wx.request({
                method:"POST",
                url: app.globalData.urlHome + '/user/login',
                data:{
                    code: res.code,
                    avatarUrl: app.globalData.userInfo.avatarUrl,
                    nickName: app.globalData.userInfo.nickName
                },
                  
                success: (res2) => {
                    console.log(res2);
                if(res2.data.code != 200) {
                    wx.showToast({
                      title: '请重新登录！',
                      icon: 'error',
                      duration: 4000
                    }),
                    that.setData({
                        hasUserInfo: false,
                    })
                }
                else{
                    app.globalData.openID = res2.data.data.userInfo.openID;
                    app.globalData.token = res2.data.data.token;
                    app.globalData.hasUserInfo = true;

                    that.setData({
                        hasUserInfo: true,
                        userInfo: res2.data.data.userInfo
                    })
                    wx.setStorageSync('openID', app.globalData.openID);
                }
                
                  }
                })
            }
        })

    },

    checkIn() {
        var that = this;
        console.log(app.globalData.openID)
        // wx.showToast({
        //   title: '签到成功\n能量值+20',
        //   icon: 'success',
        //   duration: 2000
        // })
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
                        isCheckedIn:true,
                        totalDays:res.data.data.totalDays,
                    })
                    that.onPullDownRefresh();
                }
                else if (res.data.code == 1005) {
                    toast.showToastWithMaskAndImage(that, "出错啦", "重复签到了哦","../../icon/error.png")
                    that.setData({
                        isCheckedIn:true,
                        totalDays:res.data.data.totalDays,
                    })
                }
                else{
                    toast.showToastWithMaskAndImage(that, "出错啦", "请重新签到","../../icon/error.png")

                }
                
            }
        })
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
            })
        }
        console.log(that.data)
    
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
            wx.request({
                method: 'GET',
                url: app.globalData.urlHome + '/user/getUserInfo/' + app.globalData.openID,
                header: {
                    'content-type': 'application/json',
                    'token': app.globalData.token
                  },
                success: (res)=>{
                    that.setData({
                        hasUserInfo: true,
                        userInfo: res.data.data.userInfo
                    })
                }
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