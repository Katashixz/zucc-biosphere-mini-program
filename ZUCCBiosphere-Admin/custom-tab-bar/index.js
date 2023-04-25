// custom-tab-bar/index.js
Component({

    /**
     * 页面的初始数据
     */
    data: {
        selected: 0,
        selectedColor: "#fff",
        color: "#7A685B",
        list: [
            {
                "pagePath": "/pages/mainPage/mainPage",
                "text": "首页",
                "iconPath": "/icon/info.png",
                "selectedIconPath": "/icon/binfo.png"
            },
            {
                "pagePath": "/pages/adminInfo/adminInfo",
                "text": "设置",
                "iconPath": "/icon/setting.png",
                "selectedIconPath": "/icon/bsetting.png"
            },

        ],
        "backgroundColor": "#DDD8D0",
    },
    attached() {
    },
    methods: {
      // 路由跳转
      switchTab(e) {
        const data = e.currentTarget.dataset
        var index = data.index
        var that = this;
        const url = data.path
        wx.switchTab({url})
      },
    },
    /**
     * 导航栏切换
     */
    // switchTab(e) {
    //     const data = e.currentTarget.dataset
    //     const url = data.path
    //     wx.switchTab({
    //         url
    //     })
    //     this.setData({
    //         selected: data.index
    //     })
    // },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {

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