// pages/wikiHome.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        tabs:[
            "动物",
            "植物"
        ],
        animalData:[
            {
                imageUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                name:"猫科",
                id: 1
            },
            {
                imageUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMqaS60wguK6kX92WcsCKeDA/132",
                name:"犬科",
                id: 2
            },
            {
                imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
                name:"鳌虾科",
                id: 3
            },
            {
                imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
                name:"测试科",
                id: 4
            },
        ],
        plantsData:[
            {
                imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/1133uWkFhVSV16td7d4a75f05905e2103206368251285890.jpg",
                name:"蔷薇科",
                id: 5
            },
            {
                imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/1654161185786.png",
                name:"菊科",
                id: 6
            },
            {
                imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/postImages/%E7%A7%91%E6%99%AE%E5%9B%BE%E7%89%87/%E9%B8%9F/%E9%B8%BD%E5%AD%90/5073635eb632d7ce1adddf837acd7ee.jpg",
                name:"蔷薇科",
                id: 7
            },
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
            url: '/pages/curingGuide/curingGuide?id='+this.data.animalData[e.currentTarget.dataset.index].id+'&name='+this.data.animalData[e.currentTarget.dataset.index].name

        })
        // console.log(this.data.animalData[e.currentTarget.dataset.index].id)
    },
    toPlantsCuringGuide: function(e){
        wx.navigateTo({
            url: '/pages/curingGuide/curingGuide?id='+this.data.plantsData[e.currentTarget.dataset.index].id+'&name='+this.data.plantsData[e.currentTarget.dataset.index].name
        })
        // console.log(this.data.animalData[e.currentTarget.dataset.index].id)
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