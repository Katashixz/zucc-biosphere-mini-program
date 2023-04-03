// pages/releaseCondition/releaseCondition.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        items: [
            {value: '1', name: '遇见',},
            {value: '2', name: '喂养'},
          ],
        curAction: 2,
        images: [],
        imagesBase: [],
        maxImg: 1

    },
    /**
     * 发布今日状况
     */
    release: util.throttle(function() {
        var that = this;
        if(that.data.images == undefined || that.data.images.length == 0){
            wx.showToast({
              title: '请选择图片!',
              icon: 'error',
              duration: 1500
            })
        }else{
            wx.showLoading({
              title: '加载中',
            })
            that.uploadImg().then(res => {
                console.log(res);
                that.uploadCondition(res).then(res => {
                    wx.hideLoading();
                    wx.showToast({
                      title: '发布成功',
                      icon: 'success',
                      duration: 1500
                    })
                    setTimeout(function () {
                        wx.navigateBack();
                    }, 1600)
                }).catch(error => {
                    wx.hideLoading();
                    wx.showToast({
                      title: '上传失败',
                      icon: 'error',
                      duration: 1500
                    })
                    setTimeout(function () {
                        wx.navigateBack();
                    }, 1600)
                })
            }).catch(error => {
                wx.hideLoading();
                wx.showToast({
                  title: '上传失败',
                  icon: 'error',
                  duration: 1500
                })
                setTimeout(function () {
                    wx.navigateBack();
                }, 1600)
            })
        }
    },800),
    /**
     * 上传内容
     */
    uploadCondition: function(image){
        var that = this;
        var uid = wx.getStorageSync('uid')
        return new Promise((resolve, reject) => {
            wx.request({
                url: app.globalData.urlHome + '/adopt/auth/saveConditionPersonally',
                method: "POST",
                header: {
                    'token': app.globalData.token
                },
                data: {
                    sourceID: uid,
                    targetID: that.data.target,
                    action: that.data.curAction,
                    date: that.data.date,
                    time: that.data.time,
                    imageUrl: image
                },
                success: (loadRes) => {
                    if(loadRes.data.code == 200){
                        resolve()
                    }
                    else{
                        wx.showToast({
                            title: loadRes.data.msg,
                            duration: 2000,
                            icon: 'error'
                            })
                        if(res.data.code == 300){
                            app.clearUserData();
                            
                        }
                        reject()
                    }
                },
                fail: (loadRes) => {
                    wx.showToast({
                        title: '服务器错误',
                        duration: 2000,
                        icon: 'error'
                    })
                    reject()

                },

            })
        })
    },
    /**
     * 先上传图片，再上传内容
     */
    uploadImg: function(){
        var that = this;
        var url = app.globalData.urlHome + '/community/auth/uploadImg';

        return new Promise((resolve, reject) => {
            wx.uploadFile({
                filePath: that.data.images[0].tempFilePath,
                name: 'file',
                url: url,
                header: {
                    'token': app.globalData.token
                },
                success: (res) => {
                    var data = JSON.parse(res.data);
                    if(data.code == 200){
                        resolve(data.data.imgUrl)
                    }else{
                        reject(data.msg)    
                        if(res.data.code == 300){
                            app.clearUserData();
                            
                        }
                    }
                },
                fail: (res) => {
                    reject("服务器错误")    

                }
            })
        })
    },
    /**
     * 选择图片
     */
    chooseImage: function () {
        const that = this
        // console.log(this.data.images)

        if (that.data.images.length < that.data.maxImg) {

            let images = that.data.images
            wx.chooseMedia({
                mediaType: ['image'],
                count: that.data.maxImg - that.data.images.length,
                sizeType: ['original', 'compressed'],  //可选择原图或压缩后的图片
                sourceType: ['album', 'camera'], //可选择性开放访问相册、相机
                maxDuration: 15,
                success: res => {
                    // console.log(res)
                    for (let i = 0; i < res.tempFiles.length; i++) {
                        images.push(res.tempFiles[i]);
                        this.setData({
                            images: images
                        })
                    }
                }
            });
        }
        else {
            wx.showToast({
                title: "最多上传" + that.data.maxImg + "张照片！",
                duration: 2000
            })

        }

    },

    /**
     * 移除图片
     */
    removeImage: function (e) {
        var that = this;
        var images = that.data.images;
        var imagesBase = that.data.imagesBase;
        // 获取要删除的第几张图片的下标
        const idx = e.currentTarget.dataset.idx
        // splice  第一个参数是下表值  第二个参数是删除的数量
        images.splice(idx, 1)
        imagesBase.splice(idx, 1)
        this.setData({
            images: images,
            imagesBase: imagesBase
        });
    },
    /**
     * 图片预览
     */
    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const images = this.data.images
        var mediaUrlList = [];

        // console.log(images);
        for(var i = 0; i < images.length; i ++){
            var temp = {
                url: images[i].tempFilePath,
                type: images[i].fileType,
                poster: images[i].thumbTempFilePath,
            }
            mediaUrlList.push(temp);
        }
        // console.log(mediaUrlList);

        wx.previewMedia({
          sources: mediaUrlList,
          current: idx
        })
    },
    /**
     * 设置单选value
     */
    radioChange:function(e){
        var that = this;
        that.setData({
            curAction: e.detail.value
        })
    },
    /**
     * 日期选择
     */
    bindDateChange: function(e) {
        var that = this;
        that.setData({
          date: e.detail.value
        })
        that.onPullDownRefresh();
      },
    /**
     * 日期选择
     */
    bindTimeChange: function(e) {
        var that = this;
        that.setData({
          time: e.detail.value
        })
        that.onPullDownRefresh();
      },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var today = new Date();
        console.log(today.getMinutes());
        that.setData({
            target: options.targetID,
            date: options.date,
            name: options.name,
            time: (today.getHours().length<10?('0'+today.getHours()):today.getHours()) + ':' + (today.getMinutes()<10?('0'+today.getMinutes()):today.getMinutes()),
            today: today,
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