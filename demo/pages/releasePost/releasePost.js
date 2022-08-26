// pages/releasePost/releasePost.js

const app = getApp()

const api = app.globalData.api
Page({

     /**
     * 页面的初始数据
     */
    data: {
        textDown: "",
        postTheme: {
            isHidden: true,
            default: "选择主题",
            select: -1,
            txt: ["吐槽", "晒动物", "晒植物", "求助"]
        },
        images: [],
        imagesBase: [],
        tmpImages: [],
        maxImg: 3
        
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




    /* 获取帖子内容 */
    getPostContent: function (e) {
        this.setData({
            postContent: e.detail.value
        })
    },

    /* 发布帖子 */
    release: function () {
        this.imageToBase();

        if (this.data.postTheme.select == -1) {
            wx.showToast({
                title: '请选择主题!',
                icon: 'error',  // 图标类型，默认success
                duration: 1500  // 提示窗停留时间，默认1500ms
            })
        }

        else if (!this.data.postContent ) {
            wx.showToast({
                title: '请输入完整!',
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
            var that = this;
            app.darkin(that, 'ani', 0.7, 'rgb(67, 196, 88)', 'rgb(36, 100, 47)');
            api.showLoading('上传中...')
            for (let i = 0; i < that.data.images.length; i++) {
                wx.getFileSystemManager().readFile({


                    filePath: this.data.images[i], //选择图片返回的相对路径
                    encoding: "base64", //这个是很重要的
                    success: res => {
                        //返回base64格式

                        that.data.imagesBase.push(res.data);
                        this.setData({})
                        if (that.data.imagesBase.length == that.data.images.length) {
                            // console.log(this.data.imagesBase[0])
                            that.upCount();
                        }
                    }
                })
            }

            if (that.data.images.length == 0) {
                that.upCount();
            }
        }
    },

    upCount: function () {
        var that = this
        // wx.request({
        //     url: getApp().globalData.urlHome + '/postBar/releasePost',
        //     // url: 'http://121.40.227.132:8078/indeximages/uploadImages',
        //     method: 'POST',
        //     header: {
        //         'content-type': 'application/json;charset=utf-8',
        //         'x-auth-token': getApp().globalData.token
        //     },
        //     data: {
        //         postTheme: this.data.theme,
        //         postContent: this.data.postContent,
        //         userID: getApp().globalData.openid,
        //         userAvatarUrl: getApp().globalData.userInfo.avatarUrl,
        //         'picType1': that.data.tmpImages[0] ? that.data.tmpImages[0].split('.')[1] : null,
        //         'picType2': that.data.tmpImages[1] ? that.data.tmpImages[1].split('.')[1] : null,
        //         'picType3': that.data.tmpImages[2] ? that.data.tmpImages[2].split('.')[1] : null,
        //         'picName1': that.data.tmpImages[0] ? that.data.tmpImages[0] : null,
        //         'picName2': that.data.tmpImages[1] ? that.data.tmpImages[1] : null,
        //         'picName3': that.data.tmpImages[2] ? that.data.tmpImages[2] : null,
        //         'picture1': that.data.imagesBase[0] ? that.data.imagesBase[0] : null,
        //         'picture2': that.data.imagesBase[1] ? that.data.imagesBase[1] : null,
        //         'picture3': that.data.imagesBase[2] ? that.data.imagesBase[2] : null,
                
        //     },
        //     success(r) {
        //         wx.showToast({
        //             title: '发布成功！', // 标题
        //             icon: 'success',  // 图标类型，默认success
        //             duration: 1500  // 提示窗停留时间，默认1500ms
        //         })
        //         api.hideLoading('上传中...')

        //         setTimeout(function () {
        //             wx.reLaunch({
        //                 url: '../home/home',
        //             })
        //         }, 1500)

        //         that.clearData()
        //     }
        // })

    },

    chooseImage: function () {
        const that = this
        // console.log(this.data.images)

        if (that.data.images.length < that.data.maxImg) {

            let images = that.data.images
            wx.chooseImage({
                count: that.data.maxImg - that.data.images.length,
                sizeType: ['original', 'compressed'],  //可选择原图或压缩后的图片
                sourceType: ['album', 'camera'], //可选择性开放访问相册、相机
                success: res => {


                    // console.log(res)
                    for (let i = 0; i < res.tempFiles.length; i++) {
                        images.push(res.tempFilePaths[i]);
                        this.setData({
                            images: images
                        })
                    }
                }
            });
        }
        else {
            wx.showToast({
                title: "最多上传" + that.data.maxImg + "张照片！"
            })

        }

    },

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

    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const images = this.data.images
        wx.previewImage({
            current: images[idx],  //当前预览的图片
            urls: images,  //所有要预览的图片
        })
    },

    // 图片转base64
    imageToBase() {
        const that = this


        this.setData({
            tmpImages: [],
        })


        this.data.images.forEach(element => {

            that.data.tmpImages.push(element.split('/')[3])

        });

    },

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
            tmpImages: [],
            textDown: "",
        })

    },
})