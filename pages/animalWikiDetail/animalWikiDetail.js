// pages/wikiDetail/wikiDetail.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        
        wikiData:{
            map:[
                {
                    title:"学名",
                    content:"中华田园猫",
                },
                {
                    title:"性别",
                    content:"公",
                },
                {
                    title:"状况",
                    content:"健康",
                },
                {
                    title:"绝育情况",
                    content:"未绝育",
                },
                {
                    title:"性格",
                    content:"谨慎，喂养要有耐心",
                },
                {
                    title:"外貌",
                    content:"黑色毛发为主，颈部为白色，鼻头上方为白色",
                },
                {
                    title:"关系",
                    content:"是小黑の崽的母亲，育有三个孩子",
                }
            ],
            nickName: "小黑",
            imageList:[
                {
                    url:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E7%8C%AB%E5%92%AA/%E5%B0%8F%E9%BB%91%E5%9B%BE%E7%89%87/159be5936f8d529b5dcd20f431ec11a.jpg"
                },
                {
                    url:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E7%8C%AB%E5%92%AA/%E5%B0%8F%E9%BB%91%E5%9B%BE%E7%89%87/5b8686e20921f4fb9f900c014f32639.jpg"
                },
                {
                    url:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E7%8C%AB%E5%92%AA/%E5%B0%8F%E6%A9%98/4c8e4d7d726f37e09aa03132ced83ec.jpg"
                },
            ],
            relation: [
                {
                    name: "小黑の崽",
                    imageUrl: "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E7%8C%AB%E5%92%AA/%E5%B0%8F%E9%BB%91%E5%9B%BE%E7%89%87/d40a35270267f578eb05d2b8b8a88b2.jpg"
                }
            ]
            
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        console.log(options)
        if(options.name != undefined){
            wx.setNavigationBarTitle({
                title: options.name,
              })

        }
        that.setData({
            pageData: options,
        })
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