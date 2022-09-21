// pages/curingGuide/curingGuide.js
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        curingGuide:[
            {
                type: 2,
                contentFinal: ["等待补充~"],
                keyIndex: [0],
                keyFinal: ["等待补充~"],
            }
        ],
        targetList:[
            // {
            //     avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqJwGZQtVpq3s0QWb1F0bPsAtuVZS1GuZhNCmwyo4JLxVOr7Z0vibgoPmbldGlWeYv4KtfUhibqXACow/132",
            //     name:"小小",
            //     id:"1",
            // },
            // {
            //     avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI0a5ozvUlwJtmUqZYS0g1MgmzjmicaHl0ibVa3B5WRLo3ha1k4PxIjR05y9ZLZmwecLoo6AzCjVDxg/132",
            //     name:"大大",
            //     id:"2",
            // },
            // {
            //     avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/hVy5mysSB7BSX8iarNyS2wicEFuoiaiaEkQfONDiaMI1xRXqRe1rHs4dMVEbaWzZTN4vvTLSCKpw2RHsYSa6WTkxSZg/132",
            //     name:"中中",
            //     id:"3",
            // },
            // {
            //     avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTImicSGGrnTWbaa37GclaN3icWI9RJaqlkJr8WA7xdIic2ztS9kl0VickjpBUDCbZBudAgQIELJFVc8HQ/132",
            //     name:"大小",
            //     id:"3",
            // },
            // {
            //     avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epVibo86CUJNY9K4EHialCDTvNHJ2aWH1Odc6OjeM4q1DBL2FkiavxWZ0O5w2cNKIcChyxWxlKdKwuvw/132",
            //     name:"小大",
            //     id:"3",
            // },
        ]

    },
    /**
     * 页面跳转
     */
    toWikiDetail: function(e){
        wx.navigateTo({
            url: '/pages/animalWikiDetail/animalWikiDetail?id='+this.data.targetList[e.currentTarget.dataset.index].id+'&name='+this.data.targetList[e.currentTarget.dataset.index].nickName+'&type='+this.data.pageData.type

        })
    },
    /**
     * 页面数据加载
     */
    loadInfo(){
        var that = this;
        var options = that.data.pageData;
        wx.showLoading({
            title: '加载中',
          })
        if(options.name != undefined){
            wx.setNavigationBarTitle({
                title: options.name,
              })

        }
        var animalOrPlant = options.type == '动物'? 1:0;
          wx.request({
            url: app.globalData.urlHome + '/wikiData/exposure/getCuringGuideData/' + animalOrPlant + '/' + options.name,
            method:"GET",
            success: (res) => {
                if(res.data.code != 200){
                    wx.showToast({
                        title: '服务器错误',
                        icon: 'error',
                        duration: 2000
                      })
                }
                else{
                    console.log(res)
                    that.setData({
                        targetList: res.data.data.targetList,
                    })
                    if(res.data.data.curingGuide.length > 0){
                        that.setData({
                            curingGuide: res.data.data.curingGuide,
                        })
                    }
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