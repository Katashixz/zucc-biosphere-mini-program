// pages/searchPage/searchPage.js

Page({

    /**
     * 页面的初始数据
     */
    data: {
        
        searchResult: [
            {
                postID: 2,
                userID: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
                userOpenID: "111",
                userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                postContent: "ee",
                postTheme: "突击",
                postDate: "2022年8月19日17:45:53",
                postImage: [
                    
                ],

            },
            {
                postID: 2,
                userID: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
                userOpenID: "111",
                userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                postContent: "ee",
                postTheme: "突击",
                postDate: "2022年8月19日17:45:53",
                postImage: [
                    {
                        imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                    },
                   {
                    imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                   }
                ],
            }
        ],
    },
    //跳转到帖子详情页面
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.searchResult[e.currentTarget.dataset.index].postID
        })
    },
    
    
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            pageData: options,
        })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        // searchBar.setPlaceHolderContent(that, "请输入帖子内容关键词")
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