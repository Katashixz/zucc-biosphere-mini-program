// pages/releasePost/releasePost.js

const app = getApp()
/**
 * toast提示框
 */
var toast = require('../../utils/toast/toast.js');
const api = app.globalData.api
Page({

     /**
     * 页面的初始数据
     */
    data: {
        content: "",
        postTheme: {
            isHidden: true,
            default: "选择主题",
            select: -1,
            txt: ["吐槽", "晒动物", "晒植物", "求助"]
        },
        images: [],
        imagesBase: [],
        maxImg: 9
        
    },

    /**
     * 展示或隐藏选项
     */
    showSelect: function (e) {
        //获取当前数据
        var that = this
        var data = this.data.postTheme;

        //变更isHidden属性
        data["isHidden"] = !data.isHidden
        //执行变更
        this.setData({
            postTheme: data,
        })

        if (data["isHidden"]){
            app.showToNone(that, 'selectAni');
            app.rotate(that, 'rotateAni', 0);
        }
        else{
            app.noneToShow(that, 'selectAni');
            app.rotate(that, 'rotateAni', 45);
            app.darkin(that, 'selectDarkAni', 0.9, '#EEEAE7', 'rgb(215,193,168)');
        }
    },

    /**
     * 设置当前选项的值
     */

    SelectVal: function (e) {
        var that = this
        // 获取到点击的列表下标。
        var index = e.target.dataset.index;
        var data = this.data.postTheme

        //获取选中的选项的值
        var test_name = data.txt[index];

        //设置区域默认和隐藏
        data["default"] = test_name;
        data["select"] = index;
        data["isHidden"] = false;
        
        app.noneToShow(that, 'selectAni');
        app.rotate(that, 'rotateAni', 45);

        this.setData({
            postTheme: data,
            theme: test_name

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
     * 发布帖子
     */
    release: function () {
        var that = this;
        if (that.data.postTheme.select == -1) {
            wx.showToast({
                title: '请选择主题!',
                icon: 'error',  // 图标类型，默认success
                duration: 1500  // 提示窗停留时间，默认1500ms
            })
        }

        else if (!that.data.content ) {
            wx.showToast({
                title: '请输入内容!',
                icon: 'error',  // 图标类型，默认success
                duration: 1500  // 提示窗停留时间，默认1500ms
            })
        }
        else if (!getApp().globalData.userInfo) {
            wx.showToast({
                title: '请先登录!', // 标题
                icon: 'error',  // 图标类型，默认success
                duration: 1500  // 提示窗停留时间，默认1500ms
            })
        }
        else {
            app.darkin(that, 'ani', 0.7, 'rgb(215,193,168)', 'rgb(126, 113, 98)');
            wx.showLoading({
              title: '上传中...',
            })
            if (that.data.images.length == 0) {
                var url = app.globalData.urlHome + '/community/auth/uploadPost'
                wx.request({
                    url: url,
                    method: "POST",
                    header: {
                        'token': app.globalData.token
                    },
                    data: {
                        userID: app.globalData.userInfo.id,
                        theme: that.data.theme,
                        content: that.data.content,
                    },
                    success: (loadRes) => {
                        if(loadRes.data.code == 200){
                            wx.showToast({
                            title: '发帖成功',
                            duration: 2000,
                            icon: 'success',
                            })
                            setTimeout(function () {
                                wx.reLaunch({
                                    url: '../communityHome/communityHome',
                                })
                            }, 2100)
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
                        }
                    },
                    fail: (loadRes) => {
                        wx.showToast({
                            title: '服务器错误',
                            duration: 2000,
                            icon: 'error'
                        })
                    },
                    complete: (loadRes) => {
                        wx.hideLoading();
                    }

                })
                // that.uploadImages();
            }
            else{
                //先上传图片，获取到所有上传成功的url数组后再上传帖子
                that.uploadImages().then(res => {
                    var url = app.globalData.urlHome + '/community/auth/uploadPost'
                    wx.request({
                        url: url,
                        method: "POST",
                        header: {
                            'token': app.globalData.token
                        },
                        data: {
                            images: res,
                            userID: app.globalData.userInfo.id,
                            theme: that.data.theme,
                            content: that.data.content,
                        },
                        success: (loadRes) => {
                            if(loadRes.data.code == 200){
                                console.log(loadRes)
                                wx.showToast({
                                title: '发帖成功',
                                duration: 2000,
                                icon: 'success',
                                })
                                setTimeout(function () {
                                    wx.reLaunch({
                                        url: '../communityHome/communityHome',
                                    })
                                }, 2100)
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
                            }
                        },
                        fail: (loadRes) => {
                            wx.showToast({
                                title: '服务器错误',
                                duration: 2000,
                                icon: 'error'
                            })
                        },
                        complete: (loadRes) => {
                            wx.hideLoading();
                        }

                    })
                }).catch(error => {
                    console.log(error);
                })
            }
            
            // that.upCount();

            
        }
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
     * 选择图片
     */
    chooseImage: function () {
        const that = this
        // console.log(this.data.images)

        if (that.data.images.length < that.data.maxImg) {

            let images = that.data.images
            wx.chooseMedia({
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

        console.log(images)
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
     * 初始化数据
     */
    clearData: function () {
        this.setData({
            postTheme: {
                isHidden: true,
                default: "选择主题",
                select: -1,
                txt: ["吐槽", "晒动物", "晒植物", "求助"]
            },
            images: [],
            imagesBase: [],
            content: "",
        })

    },
})