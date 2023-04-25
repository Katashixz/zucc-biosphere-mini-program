// pages/chatPage/chatPage.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isFocus: false,
        placeholderText: "请输入文字",
        chatRecord:[
            ],
    },
    
    /**
     * 输入框聚焦
     */
    confirmFocus(options) {
        var that = this;
        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()
            
        }else{
            that.setData({
                isFocus: true,
            })
        }
    },
    
    /**
     * 移出焦点监听
     */
    inputBlur(options) {
        var that = this;
        that.setData({
            isFocus: false,
        })
        
    },
    /**
     * 输入事件监听
     */
    inputContentListening(options) {
        var that = this;
        that.setData({
            resContent: options.detail.value,
        })
    },
    /**
     * 发送消息
     */
    uploadMessage: util.throttle(function(e){
        var that = this;
        var uid = wx.getStorageSync('uid')
        var target = that.data.pageData.target
        var text = that.data.resContent
        if(uid != undefined && target != undefined){
        var mCmd = { 
            "code": 10002,
            "sourceID": uid,
            "createdAt": new Date(),
            "targetID": target,
            "content": text,
            "msgType": 1
        }
            // util.sendChatMessage(10002, uid, target, text);
            app.sendMessage(mCmd);
            util.resiverMessage(that);
            // console.log(mCmd);
            var time = new Date().getHours() + ':' + new Date().getMinutes().toString().padStart(2, "0");
            var obj = {
                sourceID: uid,
                content: text,
                sourceAvatar: app.globalData.userInfo.avatarUrl,
                createdAt: time,
            }
            var record = that.data.chatRecord;
            record.push(obj);
            that.setData({
                resContent: '',
                chatRecord: record,
                toView: `item${that.data.chatRecord.length-1}`
            })
            // that.onPullDownRefresh();

        }else{
            wx.showToast({
              title: '私聊信息异常',
              icon: 'error',
              duration: 2000
            })
        }
    },1500),
    /**
     * WebSocket页面回调函数
     */
    onMessage: function (res) {
        var that = this;
        var obj = JSON.parse(res.data);
        if(obj.data.code == undefined){
            var time = new Date(obj.data.createdAt).getHours() + ':' + new Date(obj.data.createdAt).getMinutes().toString().padStart(2, "0");
        var chat = {
            sourceID: obj.data.userId,
            sourceName: that.data.pageData.userName,
            content: obj.data.content,
            sourceAvatar: that.data.pageData.targetAvatar,
            createdAt: time,
            
        }
        var temp = that.data.chatRecord
        temp.push(chat)
        that.setData({
            chatRecord: temp,
            toView: `item${that.data.chatRecord.length-1}`

        })
        }
        
        // that.onPullDownRefresh();
        
      },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            pageData: options,
        })
        if(options.userName != undefined){
            wx.setNavigationBarTitle({
                title: options.userName,
              })
        }
        if(app.globalData.hasUserInfo){
            that.setData({
                uid: app.globalData.userInfo.id
            })
        }
        util.resiverMessage(that)
        that.onPullDownRefresh();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");
        that.onPullDownRefresh();
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
     * 加载聊天记录
     */
    loadHistory(){
        var that = this;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/user/auth/getOneChatHistory?sourceID=' + that.data.uid + '&targetID=' + that.data.pageData.target,
            header: {
                'content-type': 'application/json',
                'token': app.globalData.token
            },
            success: (res) => {
                if(res.data.code == 200){
                    console.log(res);
                    var chatList = res.data.data.history;
                    for(var i = 0, len = chatList.length; i < len; i++) {
                        var dateFormat = util.formatDateByDiff(new Date(chatList[i].createdAt));
                        chatList[i].createdAt = dateFormat;
                      }
                    that.setData({
                        chatRecord: chatList,
                    })
                }
                else{
                    console.log("error");
                }
            },
            fail: (res) => {
                wx.showToast({
                title: '服务器错误',
                icon: 'error',
                duration: 1500
                })
            }
        })
        setTimeout(() => {
            that.setData({
                toView: `item${that.data.chatRecord.length-1}`
            })
        }, 500)
    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
        var that = this;
        that.loadHistory();
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