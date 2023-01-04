// pages/wikiHome.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        type: 0,
        tabs:[
            "动物",
            "植物"
        ],
        animalData:[
            // {
            //     image:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     familyID:"猫科",
            // },
            // {
            //     image:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMqaS60wguK6kX92WcsCKeDA/132",
            //     familyID:"犬科",
            // },
            // {
            //     image:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
            //     familyID:"鳌虾科",
            // },
            // {
            //     image:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
            //     familyID:"测试科",
            // },
        ],
        plantsData:[
            // {
            //     image:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/1133uWkFhVSV16td7d4a75f05905e2103206368251285890.jpg",
            //     familyID:"蔷薇科",
            //     id: 5
            // },
            // {
            //     image:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/1133uWkFhVSV16td7d4a75f05905e2103206368251285890.jpg",
            //     familyID:"菊科",
            //     id: 6
            // },
            // {
            //     image:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
            //     familyID:"蔷薇科",
            //     id: 7
            // },
        ],
        checked:0
    },

    bindChange: function (e) { 
        this.setData({
            checked: e.detail.current 
        });   
    }, 
    navTip: function (e) { 
         if (this.data.checked === e.target.dataset.idx) { 
            return false; 
         } else { 
            this.setData({ 
                checked: e.target.dataset.idx 
            }) 
        } 
    },
    
    toAnimalCuringGuide: function(e){
        wx.navigateTo({
            url: '/pages/curingGuide/curingGuide?id='+this.data.animalData[e.currentTarget.dataset.index].id+'&name='+this.data.animalData[e.currentTarget.dataset.index].familyID+'&type=动物'

        })
        // console.log(this.data.animalData[e.currentTarget.dataset.index].id)
    },
    toPlantsCuringGuide: function(e){
        wx.navigateTo({
            url: '/pages/curingGuide/curingGuide?id='+this.data.plantsData[e.currentTarget.dataset.index].id+'&name='+this.data.plantsData[e.currentTarget.dataset.index].familyID+'&type=植物'
        })
        // console.log(this.data.animalData[e.currentTarget.dataset.index].id)
    },

    /**
     * 数据加载
     */
    loadInfo(){
        var that = this;
        wx.showLoading({
          title: '加载中',
        })
        wx.request({
          url: app.globalData.urlHome + '/wikiData/exposure/getWikiMainPage',
          method:"GET",
          success: (res) => {
              if(res.data.code == 200){
                that.setData({
                    animalData: res.data.data.animalData,
                    plantsData: res.data.data.plantsData,
                  })
              }else{
                var obj = {
                    msg: res.data.msg,
                    type: "error"
                }
                that.promptBox.open(obj);
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
        that.loadInfo();
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
    onShow: function () {
        var that = this;
        that.data.type = 0;
        
        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
          this.getTabBar().setData({
    
            selected: 0
    
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