// pages/commuityHome/communityHome.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        tenHotPosts: [
            {
                postID: "",
                postContent: "e"
            },
            {
                postID: "",
                postContent: "ee"
            },
        ],
        postList: [
            {
                postID: 1,
                userID: "yoxi",
                userOpenID: "",
                userAvatarUrl: "",
                postContent: "789633",
                postDate: "2022年8月19日16:44:33",
                postImage: [],
                postcommentNum: 0,
                postlikeNum: 0,
                postisTop: false,
                postisEssential: true,
                postisLiked: false,
                postTheme: "啊啊啊"
            },
            {
                postID: 1,
                userID: "啊啊啊",
                userOpenID: "",
                userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                postContent: "ee",
                postTheme: "突击",
                postDate: "2022年8月19日17:45:53",
                postImage: [
                    {
                        imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/2848null"
                    },
                   {
                    imageUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132"
                   }
                ],
                postcommentNum: 1,
                postlikeNum: 10,
                postisTop: false,
                postisEssential: true,
                postisLiked: true,
                postisStared: false
            }
            
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
    onShow: function () {

        console.log("onShow")
    
        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
          this.getTabBar().setData({
    
            selected: 2
    
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