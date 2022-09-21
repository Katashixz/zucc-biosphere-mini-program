// pages/wikiDetail/wikiDetail.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
    
            
    },

    /**
     * 跳转到其他动物
     */
    toAnotherAnimal: function(e){
        wx.navigateTo({
            url: '/pages/animalWikiDetail/animalWikiDetail?id='+this.data.relation[e.currentTarget.dataset.index].id+'&name='+this.data.relation[e.currentTarget.dataset.index].name+'&type='+this.data.pageData.type

        })
    },

    /**
     * 页面数据加载
     */
    loadInfo(){
        var that = this;
        var options = that.data.pageData;
        if(options.name != undefined){
            wx.setNavigationBarTitle({
                title: options.name,
              })

        }
        wx.showLoading({
            title: '加载中',
          })
        var animalOrPlant = options.type == '动物'? 1:0;
          wx.request({
            url: app.globalData.urlHome + '/wikiData/exposure/getWikiDetail/' + animalOrPlant + '/' + options.id,
            method:"GET",
            success: (res) => {
                console.log(res)

                if(res.data.code == 200){
                    that.setData({
                        title : res.data.data.detail.title,
                        imageList : res.data.data.detail.imageList,
                        nickName : res.data.data.detail.nickName,
                        content : res.data.data.detail.content,
                        relation : res.data.data.detail.relation,
                      })
                  }else{
                    wx.showToast({
                        title: '服务器错误',
                        icon: 'error',
                        duration: 2000
                      })
                  }
            },
            complete: (res) =>{
              wx.hideLoading();
            },
            fail: (res) => {
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            }
  
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
        that.loadInfo();
        
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
        var that = this;
        that.loadInfo();

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