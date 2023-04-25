// pages/mainPage/mainPage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [
      {
        text: "百科管理",
        targetPage: "/pages/wikiManagement/wikiManagement"
      },
      {
        text: "帖子维护",
        targetPage: "/pages/postManagement/postManagement"
      },
      {
        text: "日记维护",
        targetPage: "/pages/wikiManagement/wikiManagement"
      },
      {
        text: "用户维护",
        targetPage: "/pages/wikiManagement/wikiManagement"
      },
      {
        text: "举报处理",
        targetPage: "/pages/wikiManagement/wikiManagement"
      },
      {
        text: "领养维护",
        targetPage: "/pages/wikiManagement/wikiManagement"
      },
    ]
  },
  /**
   * 跳转页面
   */
  toPage(target){
    var that = this;
    var index = target.currentTarget.dataset.index;
    wx.navigateTo({
      url: that.data.list[index].targetPage
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    var that = this;
    that.promptBox = that.selectComponent("#promptBox");

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
      this.getTabBar().setData({

        selected: 0

      })

    }
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