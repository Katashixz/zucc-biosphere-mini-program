// pages/releaseDiary/releaseDiary.js
const util = require('../../utils/jsUtil/jsUtil')
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {

        animalImages: [],
        animalImagesBase: [],
        maxAnimalImg: 1,
        images: [],
        imagesBase: [],
        maxImg: 9,
        content: '',

    },
    /**
     * 发布日记
     */
    release: util.throttle(function(){
        var that = this;
        var uid = wx.getStorageSync('uid');
        if(that.data.date == undefined || that.data.date == null){
            wx.showToast({
              title: '请选择日期',
              icon: 'error',
              duration: 1500
            })
        }else if(that.data.animalImages == undefined || that.data.animalImages.length == 0){
            wx.showToast({
                title: '请选择图片!',
                icon: 'error',
                duration: 1500
              })
        }else if(that.data.content.length == 0){
            wx.showToast({
                title: '请输入正文!',
                icon: 'error',
                duration: 1500
              })
        }else{
            wx.showLoading({
              title: '上传中',
            })
            that.uploadAnimalImg().then(res => {
                // 无图情况
                var data;
                if(that.data.images.length == 0){
                    data = {
                        userID : uid,
                        targetImage: res,
                        content: that.data.content,
                        createdAt: that.data.date,
                    }
                    that.releaseDiary(data).then(res3 => {
                        wx.showToast({
                            title: '发布成功',
                            duration: 2000,
                            icon: 'success',
                            })
                            setTimeout(function () {
                                wx.reLaunch({
                                    url: '../adoptDiary/adoptDiary',
                                })
                            }, 2100)
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
                    });
                }else{
                    that.uploadImages().then(res2 =>{
                        data = {
                            userID : uid,
                            targetImage: res,
                            content: that.data.content,
                            createdAt: that.data.date,
                            images: res2,
                        }
                        that.releaseDiary(data).then(res3 => {
                            wx.showToast({
                                title: '发布成功',
                                duration: 2000,
                                icon: 'success',
                                })
                                setTimeout(function () {
                                    wx.reLaunch({
                                        url: '../adoptDiary/adoptDiary',
                                    })
                                }, 2100)
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
    }, 2800),
    /**
     * 发布接口
     */
    releaseDiary: function (data) {
        var that = this;
        return new Promise((resolve, reject) => {
            wx.request({
                url: app.globalData.urlHome + '/community/auth/releaseDiary',
                method: "POST",
                header: {
                    'token': app.globalData.token
                },
                data: data,
                success: (loadRes) => {
                    if(loadRes.data.code == 200){
                        resolve()
                    }
                    else{
                        var obj = {
                            msg: loadRes.data.msg,
                            type: "error"
                        }
                        that.promptBox.open(obj);
                        if(loadRes.data.code == 300){
                            app.clearUserData();
                            
                        }
                        reject()
                    }
                },
                fail: (loadRes) => {
                    reject()
                },
            })

        })


    },

    
    /**
     * 上传图片
     */
    uploadImages: function () {
        var that = this;
        var media = that.data.images;
        var result = [];
        var asyncList = [];
        return new Promise((resolve, reject) => {
            //循环发送请求上传文件
            var flag = true;
            var i = 0;

            media.forEach(element => {
                
                var p1 = that.uploadSingleImage(element).then(function(resInfo){
                    
                    result.push(resInfo);

                }).catch(function(reason){
                    // flag = false;
                    result.push(reason);
                    
                })
                asyncList.push(p1);

                
            });
            Promise.all(asyncList).then(
                function(res){
                    resolve(result)
                }
            ).catch(
                function(error){
                    wx.showToast({
                        title: reason,
                        duration: 2000,
                        icon: "error"
                    })
                    reject(result);
                }
            )
        })
        

    },
    uploadSingleImage: function (imageInfo) {
        var that = this;
        var url = app.globalData.urlHome + '/community/auth/uploadImg';

        return new Promise((resolve, reject) => {
            wx.uploadFile({
                filePath: imageInfo.tempFilePath,
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
     * 上传小动物图片
     */
    uploadAnimalImg: function(){
        var that = this;
        var url = app.globalData.urlHome + '/community/auth/uploadImg';

        return new Promise((resolve, reject) => {
            wx.uploadFile({
                filePath: that.data.animalImages[0].tempFilePath,
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
     * 获取帖子内容
     */
    getContent: function (e) {
        this.setData({
            content: e.detail.value
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
     * 选择图片
     */
    chooseAnimalImage: function () {
        const that = this
        if (that.data.animalImages.length < that.data.maxAnimalImg) {

            let images = that.data.animalImages
            wx.chooseMedia({
                mediaType: ['image'],
                count: that.data.maxAnimalImg - that.data.animalImages.length,
                sizeType: ['original', 'compressed'],  //可选择原图或压缩后的图片
                sourceType: ['album', 'camera'], //可选择性开放访问相册、相机
                maxDuration: 15,
                success: res => {
                    // console.log(res)
                    for (let i = 0; i < res.tempFiles.length; i++) {
                        images.push(res.tempFiles[i]);
                        this.setData({
                            animalImages: images
                        })
                    }
                }
            });
        }
        else {
            wx.showToast({
                title: "最多上传" + that.data.maxAnimalImg + "张照片！",
                duration: 2000
            })

        }

    },

    /**
     * 移除图片
     */
    removeAnimalImage: function (e) {
        var that = this;
        var images = that.data.animalImages;
        var imagesBase = that.data.animalImagesBase;
        // 获取要删除的第几张图片的下标
        const idx = e.currentTarget.dataset.idx
        // splice  第一个参数是下表值  第二个参数是删除的数量
        images.splice(idx, 1)
        imagesBase.splice(idx, 1)
        this.setData({
            animalImages: images,
            animalImagesBase: imagesBase
        });
    },
    /**
     * 图片预览
     */
    handleAnimalImagePreview(e) {
        const idx = e.target.dataset.idx
        const images = this.data.animalImages
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
     * 图片预览
     */
    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const images = this.data.images
        var mediaUrlList = [];

        console.log(images)
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
    bindDateChange: function(e) {
        var that = this;
        console.log('picker发送选择改变，携带值为', e.detail.value)
        that.setData({
          date: e.detail.value
        })
      },
    /**
     * 获取帖子内容
     */
    getPostContent: function (e) {
        this.setData({
            content: e.detail.value
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var today = new Date();
        that.setData({
            date: options.date,
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
        this.promptBox = this.selectComponent("#promptBox");

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