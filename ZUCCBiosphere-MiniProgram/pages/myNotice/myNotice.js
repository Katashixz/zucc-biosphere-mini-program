// pages/myNotice/myNotice.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        console.log(options)
        that.setData({
            type: options.type
        })
        switch(options.type){
            case "0": wx.setNavigationBarTitle({
                title: '收到的点赞',
              });break;
            case "1": wx.setNavigationBarTitle({
                title: '收到的评论',
              });break;
            case "2": wx.setNavigationBarTitle({
                title: '能量值记录',
              });break;
        }
        that.onPullDownRefresh();
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
        var that = this;
        that.getNotifyData();
    },
    /**
     * 获取通知信息
     */
    getNotifyData(){
        var that = this;
        var uid = wx.getStorageSync('uid');
        that.setData({
            uid: uid
        })
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/user/auth/getNotifyDetail?userID=' + uid + '&notifyType=' + that.data.type,
            header: {
                'content-type': 'application/json',
                'token': app.globalData.token
            },
            success: (res) => {
                if(res.data.code == 200){
                    console.log(res);
                    if(that.data.type == 0){
                        var list = res.data.data.likeMsgList;
                    for(var i = 0, len = list.length; i < len; i++) {
                        var dateFormat = util.formatDateByDiff(new Date(list[i].createdAt));
                        list[i].createdAt = dateFormat;
                      }
                    that.setData({
                        list: list,
                    })
                    }else if(that.data.type == 1){
                        var list = res.data.data.commentMsgList;
                        for(var i = 0, len = list.length; i < len; i++) {
                            var dateFormat = util.formatDateByDiff(new Date(list[i].createdAt));
                            list[i].createdAt = dateFormat;
                        }
                        that.setData({
                            list: list,
                        })
                    }else if(that.data.type == 2){
                        var list = res.data.data.energyMsgList;
                        for(var i = 0, len = list.length; i < len; i++) {
                            var dateFormat = util.formatDate2(new Date(list[i].getDate), 'yyyy-mm-dd hh:mi:ss');
                            list[i].getDate = dateFormat;
                        }
                        that.setData({
                            list: list,
                        })
                    }
                    
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
    },

    /**
     * 点赞跳转帖子
     */
    onLikeClick(e){
        var that = this;
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + that.data.list[e.currentTarget.dataset.index].postID
        })
    },
    /**
     * 评论跳转帖子
     */
    onCommentClick(e){
        var that = this;
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + that.data.list[e.currentTarget.dataset.index].postID
        })
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