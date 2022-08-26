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
    globalData: {
        userInfo: null,
        // urlHome: 'http://121.40.227.132:8080/api',
        urlHome: 'http://localhost:9000',
        token: '',
        openID: '',
        hasUserInfo: false,
    },
})
