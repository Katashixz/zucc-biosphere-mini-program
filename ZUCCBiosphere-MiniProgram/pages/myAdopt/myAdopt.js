// pages/myAdopt/myAdopt.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {

    },
    /**
     * 加载页面数据
     */
    loadPageInfo(){
        var that = this;
        var uid = wx.getStorageSync('uid');
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/user/auth/loadMyAdopt?userID=' + uid,
            header: {
                'token': app.globalData.token
            },
            success: (res)=>{
                if(res.data.code == 200){
                    res.data.data.adoptList.forEach((item, index) => {
                        item.createdAt = util.formatDate2(new Date(item.createdAt), 'yyyy-mm-dd HH:mi')
                    })
                    that.setData({
                        adoptList: res.data.data.adoptList,
                    })
                }else{
                    var obj = {
                        msg: res.data.msg,
                        type: "tip"
                    }
                    that.promptBox.open(obj);
                    if(res.data.code == 300){
                            app.clearUserData();
                            
                        }
                }
            },
            fail: (res)=>{
                wx.showToast({
                title: '服务器错误',
                icon: 'error',
                duration: 2000
                })
            },
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
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
        var that = this;
        that.promptBox = this.selectComponent("#promptBox");
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
        that.loadPageInfo();
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