// app.js
const app = getApp()
var websocket_connected_count = 0;
var onclose_connected_count = 0;

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
                    console.log(res);
                    that.globalData.userInfo = res.userInfo;
                    // that.globalData.hasUserInfo = true;
                    that.userLogin().then(res => {
                        if(that.globalData.hasUserInfo == true){
                            that.WebSocketInit();
                        }
                        resolve(res)
                    }).catch(error => {
                        reject(error)
                    })
                },
                fail: (res) =>{
                    reject("error")

                }
            }) 
            // 可以通过 wx.getSetting 先查询一下用户是否授权了 "scope.record" 这个 scope
            // wx.getSetting({
            //     success(res) {
            //         console.log(res.authSetting);
            //         // if (!res.authSetting['scope.userInfo']) {
            //             wx.authorize({
            //                 scope: 'scope.werun',
            //                 success () {
            //                     that.userLogin().then(res => {
            //                     resolve(res)
            //                     }).catch(error => {
            //                         reject(error)
            //                     })
            //                 }
            //             })
            //         // }
            //         // else{
            //             // resolve(res);
            //         // }
            //     },
            //     fail: (res) =>{
            //         reject("error");
            //     }
            // })           
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
                    console.log(res);
                    wx.request({
                    method:"POST",
                    url: that.globalData.urlHome + '/user/exposure/login',
                    data:{
                        code: res.code,
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
                        console.log(res2);
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
                        that.globalData.hasNewMsg = res2.data.data.hasMsg;
                    // console.log(that.globalData.userInfo);
                        // 下载头像到本地
                        wx.downloadFile({
                          url: res2.data.data.userInfo.avatarUrl,
                          success (res) {
                            // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
                            if (res.statusCode === 200) {
                                wx.setStorageSync('tempAvatarFile', res.tempFilePath);
                            }
                            else{
                                wx.showToast({
                                    title: '头像下载失败',
                                    icon: 'error',
                                    duration: 4000
                                })
                            }
                          }
                        })
                        wx.setStorageSync('openID', that.openID);
                        wx.setStorageSync('uid', res2.data.data.userInfo.id);
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
    //连接websocket
    WebSocketInit: function () {
        var that = this;
        that.socketTask = wx.connectSocket({
            url: that.globalData.webSocketHome,
            data: {},
            method: 'GET',
            success: function (res) {
            console.log("connectSocket 成功")
            },
            fail: function (res) {
            console.log("connectSocket 失败")
            }
        })
        // 需要注意的是wx.connectSocket代表客户端首次和服务器建立联系，wx.onSocketOpen才是正式打开通道，wx.onSocketMessage必须在 wx.onSocketOpen 回调之后发送才生效。
        that.socketTask.onOpen((res) => {
            var cmd = {
                "code": 10001,
                "userId": wx.getStorageSync('uid'),
            }
            that.sendMessage(cmd)
            // 成功建立连接后，重置心跳检测
            heartCheck.reset().start();
        })
        that.socketTask.onMessage((res) => {
            if(res.data != "保持连接成功！" && res.data != "服务器连接成功！"){
                that.globalData.hasNewMsg = true
            }
            // 如果获取到消息，说明连接是正常的，重置心跳检测
            heartCheck.reset().start();
            that.globalData.webSocketReadyState = 1;
            console.log("收到消息 ", res)
        })
        that.socketTask.onError((res) => {
            that.globalData.webSocketReadyState = 0;
            
            console.log("连接失败 ", res)
            websocket_connected_count++;
            if(websocket_connected_count <= 5){
                that.WebSocketInit()
            }
        })
        that.socketTask.onClose((res) => {
            that.globalData.webSocketReadyState = 0;
            console.log("连接断开 ", res.code, res.reason)
        })
        // 心跳检测, 每隔一段时间检测连接状态，如果处于连接中，就向server端主动发送消息，来重置server端与客户端的最大连接时间，如果已经断开了，发起重连。
        var heartCheck = {
            timeout: 540000, 
            serverTimeoutObj: null,
            reset: function(){
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                return this;
            },
            start: function(){
                var self = this;
                self.serverTimeoutObj = setInterval(function(){
                    if(that.globalData.webSocketReadyState == 1){
                        console.log("连接状态，发送消息保持连接")
                        var cmd = {
                            "code": 10001,
                            "userId": wx.getStorageSync('uid'),
                        }
                        that.sendMessage(cmd);
                        heartCheck.reset().start();
                    }else{
                        console.log("断开状态，尝试重连")
                        that.WebSocketInit();
                    }
                }, self.timeout)
            }
        }
    },
    sendMessage: function (data) {
        var that = this;
        that.socketTask.send({
            data: JSON.stringify(data),
            success(res){
                console.log("webSocket消息发送成功",res)
            },
            fail(err){
                console.log("webSocket消息发送失败",err)
            }
        });
    },
    globalData: {
        userInfo: null,
        // urlHome: 'http://124.221.252.162:9000',
        // urlHome: 'http://localhost:9000',
        urlHome: 'https://katashix.top',
        webSocketHome: 'ws://124.221.252.162:58080/webSocket',
        token: '',
        openID: '',
        hasUserInfo: false,
        level: 0,
        hasNewMsg: false,
        webSocketReadyState: 0,
    },
})
