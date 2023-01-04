// pages/noticePage/noticePage.js
const app = getApp()
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