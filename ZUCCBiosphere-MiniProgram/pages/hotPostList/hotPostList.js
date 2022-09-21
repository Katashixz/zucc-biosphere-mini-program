// pages/hotPostList/hotPostList.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        hotPostList:[
            {
                rank: 1,
                postID: 1,
                content: "啊这肯定是带图片的假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊aa a啊这肯定是带图片的假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊aa a",
                image: "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg",
                commentNum: 20,
                favoNum: 10,
                heat: 9999

            },
            {
                rank: 2,
                postID: 2,
                content: "啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊啊这肯定也是假数据啊",
                image: "",
                commentNum: 20,
                favoNum: 10,
                heat: 999
            },
            {
                rank: 3,
                postID: 3,
                content: "发生甚么事了",
                image: "",
                commentNum: 10,
                favoNum: 15,
                heat: 99
            },
            {
                rank: 4,
                postID: 4,
                content: "发生甚么事了",
                image: "",
                commentNum: 10,
                favoNum: 15,
                heat: 99
            },
            {
                rank: 5,
                postID: 5,
                content: "发生甚么事了",
                image: "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/2848null",
                commentNum: 10,
                favoNum: 15,
                heat: 99
            }
        ]
    },

    toSelectedPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.hotPostList[e.currentTarget.dataset.index].postID
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