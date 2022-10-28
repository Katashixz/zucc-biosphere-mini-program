// app.js
const app = getApp()

App({
    // 变暗动画
    darkin: function (that, param, opacity, color1 = '#fff', color2 = '#8d728a', duration = 300) {
        var animation = wx.createAnimation({
            duration: duration,
            timingFunction: 'ease',
            delay: 100
        });
        animation.opacity(opacity).backgroundColor(color2).step()
        var json = '{"' + param + '":""}'
        json = JSON.parse(json);
        json[param] = animation.export()
        that.setData(json)
        setTimeout(() => {
            animation = wx.createAnimation({
                duration: duration - 100,
                timingFunction: 'ease',
            });

            animation.opacity(1).backgroundColor(color1).step()
            json[param] = animation.export()
            that.setData(json)
        }, duration + 100)
    },

    // 动画:透明到出现
    noneToShow: function (that, param) {
        var animation = wx.createAnimation({
            duration: 300,
            timingFunction: 'ease',
            delay: 100
        });
        animation.opacity(1).step()
        var json = '{"' + param + '":""}'
        json = JSON.parse(json);
        json[param] = animation.export()
        that.setData(json)
    },

    // 动画:出现到透明
    showToNone: function (that, param) {
        var animation = wx.createAnimation({
            duration: 300,
            timingFunction: 'ease',
            delay: 100
        });
        animation.opacity(0).step()
        var json = '{"' + param + '":""}'
        json = JSON.parse(json);
        json[param] = animation.export()
        that.setData(json)
    },

    // 动画:顺时针旋转angle度
    rotate: function (that, param, angle) {
        var animation = wx.createAnimation({
            duration: 300,
            timingFunction: 'ease',
            delay: 100
        });
        animation.rotate(angle).step()
        var json = '{"' + param + '":""}'
        json = JSON.parse(json);
        json[param] = animation.export()
        that.setData(json)
    },

    //封装登录
    getUserProfile:function(){
        var that = this;

        return new Promise((resolve, reject) => {
            wx.getUserProfile({
                desc: '获取您的openID用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
                success: (res) => {
                    that.globalData.userInfo = res.userInfo;
                    // that.globalData.hasUserInfo = true;
                    that.userLogin().then(res => {
                        resolve(res)
                    }).catch(error => {
                        reject(error)
                    })
                },
                fail: (res) =>{
                    reject("error")

                }
            })            
        })
    },
    userLogin() {
        var that = this;

        return new Promise((resolve, reject) => {
            wx.showLoading({
              title: '加载中',
            })
            wx.login({
                success: (res) => {

                    wx.request({
                    method:"POST",
                    url: that.globalData.urlHome + '/user/exposure/login',
                    data:{
                        code: res.code,
                        avatarUrl: that.globalData.userInfo.avatarUrl,
                        nickName: that.globalData.userInfo.nickName
                    },
                    fail: (res2) =>{
                        wx.hideLoading();
                        wx.showToast({
                            title: '请重新登录！',
                            icon: 'error',
                            duration: 4000
                        }),
                        that.globalData.hasUserInfo = false;

                        reject("error")
                    },
                    success: (res2) => {
                        wx.hideLoading();
                    if(res2.data.code != 200) {
                        wx.showToast({
                            title: '请重新登录！',
                            icon: 'error',
                            duration: 4000
                        }),
                        that.globalData.hasUserInfo = false;
                        reject("error")
                    }
                    else{
                        wx.showToast({
                            title: '登录成功！',
                            icon: 'success',
                            duration: 2000
                        }),
                        that.globalData.openID = res2.data.data.userInfo.openID;
                        that.globalData.token = res2.data.data.token;
                        that.globalData.hasUserInfo = true;
                        that.globalData.userInfo = res2.data.data.userInfo;
                        that.globalData.level = res2.data.data.level;
                    // console.log(that.globalData.userInfo);

                        wx.setStorageSync('openID', that.openID);
                        resolve("success")
                    }
                    
                    }
                    })
                }
            })
        })
    },
    /**
     * 清除登录数据
     */
    clearUserData() {
        var that = this;
        that.globalData.hasUserInfo = false;
        that.globalData.userInfo = null;
        that.globalData.token = '';
        that.globalData.openID = '';
        that.globalData.level = 0;
    },
    
    globalData: {
        userInfo: null,
        // urlHome: 'http://124.221.252.162:9000',
        // urlHome: 'http://localhost:9000',
        urlHome: 'https://katashix.top',
        token: '',
        openID: '',
        hasUserInfo: false,
        level: 0,
    },
})
