// pages/todayCondition/todayCondition.js
const util = require('../../utils/jsUtil/jsUtil')
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        today: '',
        date:'',
        conditionList:[
        ],
    },
    /**
     * 图片预览
     */
    handleImagePreview(e) {
        var that = this;
        var idx = e.currentTarget.dataset.index
        var mediaUrlList = [];
        var imageUrl =  that.data.conditionList[idx].imageUrl
        var temp = {
            url: imageUrl,
            type: "image",
        }
        mediaUrlList.push(temp)
        wx.previewMedia({
          sources: mediaUrlList,
          current: 0,
          showmenu: true
        })
    },
    /**
     * 跳转到发布
     */
    toReleaseCondition(){
        var that = this;
        if(!app.globalData.hasUserInfo){
            app.getUserProfile().finally(() => {
                that.onPullDownRefresh();
            })
        }else{
            wx.navigateTo({
                url: '/pages/releaseCondition/releaseCondition?targetID=' + that.data.target + '&date=' + that.data.date + '&name=' + that.data.name,
              })
        }
        
    },
    /**
     * 加载页面数据
     */
    loadPageInfo(){
        var that = this;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/adopt/exposure/getTodayCondition?targetID=' + that.data.target + '&date=' + that.data.date,
            success: (res) => {
                if(res.data.code == 200){
                    that.setData({
                        conditionList: res.data.data
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
    },
    /**
     * 日期选择
     */
    bindDateChange: function(e) {
        var that = this;
        that.setData({
          date: e.detail.value
        })
        that.onPullDownRefresh();
      },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var date = util.formatDate2(new Date(), 'yyyy-mm-dd');
        that.setData({
            date: date,
            today: date,
            target: options.id,
            name: options.name,
        })
        that.onPullDownRefresh();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.onPullDownRefresh();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        var that = this;
        var date = util.formatDate2(new Date(), 'yyyy-mm-dd');
        that.setData({
            date: date,
            today: date
        })
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