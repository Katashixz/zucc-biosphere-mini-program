// pages/hotPostList/hotPostList.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        hotPostList:[
            
        ]
    },

    toSelectedPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.hotPostList[e.currentTarget.dataset.index].hotPost.postID
        })
    },

    /**
     * 热帖加载
     */
    loadHotPost() {
        var that = this;
        var url = app.globalData.urlHome + '/community/exposure/loadHotPost';
        // if(app.globalData.hasUserInfo){
        //     url = url + '&userID=' + app.globalData.userInfo.id;
        // }
        wx.request({
            url: url,
            method:"GET",
            success: (res) => {
                if(res.data.code == 200){
                    that.setData({
                        hotPostList: res.data.data.tenHotPosts,
                    })

                }else{
                  wx.showToast({
                      title: res.data.msg,
                      icon: 'error',
                      duration: 2000
                    })
                }
  
            },
            fail: (res) => {
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            }
          
  
          })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            hotPostList: [],
        })
        that.loadHotPost();
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
        that.setData({
            hotPostList: [],
        })
        that.loadHotPost();
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