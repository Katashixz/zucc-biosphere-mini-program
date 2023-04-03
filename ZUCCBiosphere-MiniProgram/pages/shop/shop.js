// pages/shop/shop.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        itemList:[
            
        ]

    },
    
    /**
     * 加载页面数据
     */
    loadPageInfo(){
        var that = this;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/community/exposure/loadShopList',
            success: (res) => {
                if(res.data.code == 200){
                    console.log(res)
                    that.setData({
                        itemList: res.data.data.shopList
                    })
                }
                else{
                    console.log("error");
                }
            },
            fail: (res) => {
                wx.showToast({
                title: '服务器错误',
                icon: 'error',
                duration: 1500
                })
            }
        })
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
        var that = this;
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
        that.onPullDownRefresh()
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.onPullDownRefresh()
        that.promptBox = this.selectComponent("#promptBox");

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
        that.loadPageInfo();
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