// pages/postDetails/postDetails.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        postItem: {
            postID: 1,
            userID: "啊啊啊",
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
            postcommentNum: 3,
            postlikeNum: 10,
            postisTop: false,
            postisEssential: true,
            postisLiked: true,
            postisStared: false
        },
        commentList: [
            {
                userID:"啊啊啊",
                userAvatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                commentAccessName: "啊啊啊",
                postDate: "2022年8月19日17:45:53",
                commentAccessID: "111",
                content: "哈哈哈哈",
            },
            {
                userID:"嘿嘿嘿",
                userAvatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                commentAccessName: "eee",
                postDate: "2022年8月19日17:45:53",
                commentAccessID: "222",
                content: "哈哈哈哈2",
            }
        ]
    },

    //   查看图片
    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = this.data.postItem.postImage
        // console.log(images)
        let toImg = []
        images.forEach(element => {
            toImg.push(element.imageUrl)
        });
        wx.previewImage({
            current: toImg[idx2],  //当前预览的图片
            urls: toImg,  //所有要预览的图片
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        console.log(options.postID)
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