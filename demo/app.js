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
        
        wx.getUserProfile({
            desc: '获取您的openID用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
            success: (res) => {
                that.globalData.userInfo = res.userInfo;
                // that.globalData.hasUserInfo = true;
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
                url: that.globalData.urlHome + '/user/login',
                data:{
                    code: res.code,
                    avatarUrl: that.globalData.userInfo.avatarUrl,
                    nickName: that.globalData.userInfo.nickName
                },
                fail: (res2) =>{
                    wx.showToast({
                        title: '请重新登录！',
                        icon: 'error',
                        duration: 4000
                      }),
                      that.setData({
                          hasUserInfo: false,
                      })
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
                    wx.showToast({
                        title: '登录成功！',
                        icon: 'success',
                        duration: 2000
                      }),
                    that.globalData.openID = res2.data.data.userInfo.openID;
                    that.globalData.token = res2.data.data.token;
                    that.globalData.hasUserInfo = true;
                    that.globalData.userInfo = res2.data.data.userInfo;
                    wx.setStorageSync('openID', that.openID);
                }
                
                  }
                })
            }
        })

    },
    globalData: {
        userInfo: null,
        // urlHome: 'http://121.40.227.132:8080/api',
        urlHome: 'http://localhost:9000',
        token: '',
        openID: '',
        hasUserInfo: false,
    },
})
