// pages/noticePage/noticePage.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        optionList: [
            {
                "backgroundColor": "rgba(252,241,229,1)",
                "text": "收到的赞",
                "iconPath": "/icon/myLikes.png",
                "hasMsg": true,
            },
            {
                "backgroundColor": "rgba(252,241,229,1)",
                "text": "回复我的",
                "iconPath": "/icon/myComments.png",
                "hasMsg": false,
            },
            {
                "backgroundColor": "rgba(252,241,229,1)",
                "text": "帕瓦!",
                "iconPath": "/icon/power.png",
                "hasMsg": false,
            },
            // {
            //     "backgroundColor": "rgba(252,241,229,0.8)",
            //     "text": "社区",
            //     "iconPath": "/icon/Community.png",
            // },
            // {
            //     "backgroundColor": "rgba(252,241,229,0.8)",
            //     "text": "我的",
            //     "iconPath": "/icon/Info.png",
            // }
        ],
        chatList:[
            {
                avatar:"https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0",
                userName:"老虎",
                userID:"8",
                msg:"我超",
                time:"16:54",
            },
            {
                avatar:"https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0",
                userName:"老虎",
                userID:"1",
                msg:"我超",
                time:"16:54",
            },
            {
                avatar:"https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0",
                userName:"老虎",
                userID:"8",
                msg:"我这边大晴天我这边大晴天我这边大晴天",
                time:"16:54",
            },
        ],
    },
    onMessage: function (res) {
        this.setData({ data: res.data });
        console.log("noticePage",res);
      },
    toChatPage: function(e){
        var that = this
        wx.navigateTo({
            url: '/pages/chatPage/chatPage?target=' + that.data.chatList[e.currentTarget.dataset.index].userID + '&userName=' + that.data.chatList[e.currentTarget.dataset.index].userName,
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var uid = wx.getStorageSync('uid');
        var mCmd = { 
            "code": 10001,
            "userId": uid,
            "time": new Date(),
        }
        app.sendMessage(mCmd)
        // util.sendMessage(10001,uid);
        util.resiverMessage(that);
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

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