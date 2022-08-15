// pages/myInfoHome/myInfoHome.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {

        userInfo: {
            level: 0,
            energyPoint: 0,
        },
        hasUserInfo: false,
        
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
                    app.globalData.openid = res2.data.data.userInfo.openid;
                    app.globalData.token = res2.data.data.token;
                    that.setData({
                        hasUserInfo: true,
                    })
                    wx.setStorageSync('openid', app.globalData.openid);
                }
                
                  }
                })
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