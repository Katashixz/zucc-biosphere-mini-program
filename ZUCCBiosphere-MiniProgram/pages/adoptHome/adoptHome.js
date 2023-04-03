// pages/adoptHome/adoptHome.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')


Page({

    data: {
        currentTab: 0,
        sleft: "", //横向滚动条位置
        // list: [1, 2, 3, 4, 5, 6, 7, 22, 32],//测试列表
        list: [
            {}
        ]
      },
      handleTabChange(e) {
        let { current } = e.target.dataset;
        if (this.data.currentTab == current || current === undefined) return;
        this.setData({
          currentTab: current,
        });
      },
      handleSwiperChange(e) {
        var that = this;
        var index = e.detail.current;
        if(that.data.list.length == index){
            index = 0;
        }
        this.setData({
          currentTab: index,
        });
        this.getScrollLeft();

      },
      getScrollLeft() {
        const query = wx.createSelectorQuery();
        query.selectAll(".item").boundingClientRect();
        query.exec((res) => {
          let num = 0;
          for (let i = 0; i < this.data.currentTab; i++) {
            num += res[0][i].width;
          }
          this.setData({
            sleft: Math.ceil(num),
          });
        });
      },
      toTodayCondition(){
          var that = this;
          var index = that.data.currentTab
          var target = that.data.list[index].id
          var name = that.data.list[index].nickName
          wx.navigateTo({
            url: '/pages/todayCondition/todayCondition?id=' + target + '&name=' + name,
          })
      },
     /**
     * 打开线下领养
     */
      openOfflineAdoption(){
        var that = this;
        var obj = {
            msg: "请联系校动保组织",
            type: "tip"
        }
        that.promptBox.open(obj);
      },
      /**
     * 打开投食组件
     */
    openFoodComponent(){
          var that = this;
          var index = that.data.currentTab
          var target = that.data.list[index].id
        if(!app.globalData.hasUserInfo){
            app.getUserProfile().finally(() => {
                that.onPullDownRefresh();
            })
        }else{
            that.foodComponent.open(target);
        }
      },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.onPullDownRefresh();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        this.promptBox = this.selectComponent("#promptBox");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        var that = this;
        this.foodComponent = this.selectComponent("#foodComponent");
        this.promptBox = this.selectComponent("#promptBox");

        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
            this.getTabBar().setData({
      
              selected: 1
      
            })
      
          }
          that.onPullDownRefresh();
    },

    loadPageInfo(){
        var that = this;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/adopt/exposure/getHomelessAnimals',
            success: (res) => {
                if(res.data.code == 200){
                    console.log(res)
                    that.setData({
                        list: res.data.data
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