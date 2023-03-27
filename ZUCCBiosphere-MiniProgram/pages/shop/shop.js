// pages/shop/shop.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        itemList:[
            {
                "id": 1,
                "name": "猫猫钥匙扣",
                "cost": 200,
                "image": "https://img.zcool.cn/community/0179a45d634ae9a80120695c3bc94c.jpg@1280w_1l_2o_100sh.jpg",
                "stock": 4

            },
            {
                "id": 2,
                "name": "猫猫钥匙扣",
                "cost": 200,
                "image": "https://img.zcool.cn/community/0179a45d634ae9a80120695c3bc94c.jpg@1280w_1l_2o_100sh.jpg",
                "stock": 4


            },
            {
                "id": 3,
                "name": "猫猫钥匙扣钥匙扣钥匙扣",
                "cost": 200,
                "image": "https://img.zcool.cn/community/0179a45d634ae9a80120695c3bc94c.jpg@1280w_1l_2o_100sh.jpg",
                "stock": 3

            },
            {
                "id": 4,
                "name": "猫猫钥匙扣",
                "cost": 200,
                "image": "https://img.zcool.cn/community/0179a45d634ae9a80120695c3bc94c.jpg@1280w_1l_2o_100sh.jpg",
                "stock": 1

            },
            {
                "id": 5,
                "name": "猫猫钥匙扣",
                "cost": 200,
                "image": "https://img.zcool.cn/community/0179a45d634ae9a80120695c3bc94c.jpg@1280w_1l_2o_100sh.jpg",
                "stock": 0

            },
        ]

    },
    /**
     * 兑换操作
     */
    sumbit(target){
        var that = this;
        var index = target.currentTarget.dataset.index;
        if(that.data.itemList[index].stock <= 0){
            var obj = {
                msg: "啊哦！库存没有啦！",
                type: "error"
            }
            that.promptBox.open(obj);
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if(app.globalData.hasUserInfo == false){
            wx.showToast({
              title: '请先登录',
              icon: 'error',
              duration: 1500 
            })
            setTimeout(() => {
                wx.navigateBack({
                    delta: 0,
                  })
            }, 1500)
            
        }
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        this.promptBox = this.selectComponent("#promptBox");

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