// pages/curingGuide/curingGuide.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        curingGuide:[
            {
                type: 0,
                context: "忌食：巧克力、葡萄制品、大葱、洋葱等食品对猫有毒，可能会引起急性肾衰竭，切不可以喂食！火腿肠和牛奶也不适合猫咪，会引起肠胃反应，造成腹泻等状况。"
            },
            {
                type: 1,
                context: "猫粮作为猫的主要食物来源可以给猫提供均衡营养。猫罐头、猫条等食物也可以作为猫咪补充营养和零食。猫麦草可以帮助猫咪排出毛球、缓解肠胃不适。"
            },
            {
                type: 2,
                context: "当我们把心爱的猫咪带回家之后，先不要着急与猫咪建立感情，一定要第一时间带猫咪去看宠物医生哦，在给猫咪做完全面的常规检查之后，我们就能清楚了解猫咪的身体健康状态了，如果猫咪还没有做驱虫的话，我们要请宠物医生及时给猫咪驱虫，最好给猫咪注射疫苗。"
            },
        ],
        targetList:[
            {
                avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/jGgQ7iaRQVqJwGZQtVpq3s0QWb1F0bPsAtuVZS1GuZhNCmwyo4JLxVOr7Z0vibgoPmbldGlWeYv4KtfUhibqXACow/132",
                name:"小小",
                id:"1",
            },
            {
                avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI0a5ozvUlwJtmUqZYS0g1MgmzjmicaHl0ibVa3B5WRLo3ha1k4PxIjR05y9ZLZmwecLoo6AzCjVDxg/132",
                name:"大大",
                id:"2",
            },
            {
                avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/hVy5mysSB7BSX8iarNyS2wicEFuoiaiaEkQfONDiaMI1xRXqRe1rHs4dMVEbaWzZTN4vvTLSCKpw2RHsYSa6WTkxSZg/132",
                name:"中中",
                id:"3",
            },
            {
                avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTImicSGGrnTWbaa37GclaN3icWI9RJaqlkJr8WA7xdIic2ztS9kl0VickjpBUDCbZBudAgQIELJFVc8HQ/132",
                name:"大小",
                id:"3",
            },
            {
                avatarUrl:"https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epVibo86CUJNY9K4EHialCDTvNHJ2aWH1Odc6OjeM4q1DBL2FkiavxWZ0O5w2cNKIcChyxWxlKdKwuvw/132",
                name:"小大",
                id:"3",
            },
        ]

    },
    /**
     * 页面跳转
     */
    toWikiDetail: function(e){
        wx.navigateTo({
            url: '/pages/animalWikiDetail/animalWikiDetail?id='+this.data.targetList[e.currentTarget.dataset.index].id+'&name='+this.data.targetList[e.currentTarget.dataset.index].name

        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        // console.log(options)
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