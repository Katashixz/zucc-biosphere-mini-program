// pages/adoptDiary/adoptDiary.js
const util = require('../../utils/jsUtil/jsUtil')
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        today: '',
        date:'',
        diaryList:[
            {
            },
        ]
    },

    
    bindDateChange: function(e) {
        var that = this;
        console.log('picker发送选择改变，携带值为', e.detail.value)
        that.setData({
          date: e.detail.value
        })
        that.onPullDownRefresh();
      },

    /**
     * 跳转到日记详情
     */
    toDiary(e){
        // wx.navigateTo({
        //   url: '/pages/diaryDetail/diaryDetail',
        // })
    },
    /**
     * 跳转到发布日记
     */
    buttonClick(){
        var that = this;
        if (!getApp().globalData.userInfo) {
            that.loginComponent.open()

        }
        else{
            wx.navigateTo({
            url: '/pages/releaseDiary/releaseDiary?date=' + that.data.date,
            })
        }
    },
    /**
     * 判断是否为视频
     */
    isVideo(target){
        var typeTemp = target.split(".");
        if(typeTemp[typeTemp.length - 1] == 'mp4' || typeTemp[typeTemp.length - 1] == 'mov' || typeTemp[typeTemp.length - 1] == 'wmv' || typeTemp[typeTemp.length - 1] == 'mpg' || typeTemp[typeTemp.length - 1] == 'avi'){
            return true;
        }
        return false;
    },
    /**
     * 查看图片
     */
    loadPageInfo() {
        var that = this;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/community/exposure/loadDiary?&date=' + that.data.date,
            success: (res) => {
                if(res.data.code == 200){
                    that.setData({
                        diaryList: res.data.data.diaryList
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
     * 查看图片
     */
    handleImagePreview(e) {
        var that = this;
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = that.data.diaryList[idx].imageUrlList
        var mediaUrlList = [];
        // console.log(images);
        for(var i = 0; i < images.length; i ++){
            var type = "image";
            if(that.isVideo(images[i])){
                type = "video";
            }
            //视频图片分开判断
            var temp;
            if(type == "video"){
                var position = images[i].lastIndexOf('.');
                var posterUrl = images[i].slice(0, position) + "_0.jpg";
                temp = {
                    url: images[i],
                    type: type,
                    poster: posterUrl,
                }
            }
            else{
                temp = {
                    url: images[i],
                    type: type,
                }
            }
            // console.log(temp)

            mediaUrlList.push(temp);
        }
        // console.log(mediaUrlList);

        wx.previewMedia({
          sources: mediaUrlList,
          current: idx2
        })
        // let toImg = []
        // images.forEach(element => {
        //     toImg.push(element)
        // });
        // wx.previewImage({
        //     current: toImg[idx2],  //当前预览的图片
        //     urls: toImg,  //所有要预览的图片
        // })
        
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var date = util.formatDate2(new Date(), 'yyyy-mm-dd');
        that.setData({
            date: date,
            today: date
        })
        that.onPullDownRefresh();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
        var that = this;
        that.loginComponent = that.selectComponent("#loginComponent");

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
        that.loadPageInfo()
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