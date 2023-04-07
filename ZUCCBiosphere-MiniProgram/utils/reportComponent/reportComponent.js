let app = getApp();
const util = require('../../utils/jsUtil/jsUtil')
// var titleText = '';
Component({
    options: {
        multipleSlots: true // 在组件定义时的选项中启用多slot支持
    },
    properties: {
        step: {
            type: Number,
            value: 0
        },
        // title:{
        //     type: String,
        //     value: titleText
        // }
    },
    lifetimes: {
        ready: function () {
            if(this.data.isShow){
                // this.open();
            }
        },
        detached: function () {
            this.setData({ isShow: false });
        }
    },
    data: {
        isShow:false,
        tipContent: "",
        record:[],
        recoedDalogflash:null,
        title:"举报",
        userID: 0,

    },
    methods: {
        openrecoedModal(status, e) {
            let that = this;
            if(status){
                that.setData({
                    userID: e.userID,
                    targetID: e.targetID,
                    postID: e.postID
                })
            }
            let animation = wx.createAnimation({
                duration: 200, //动画时长
                timingFunction: "ease-in-out",
                delay: 0
            });
            if (status) {
                that.setData({ isShow: status});
                animation.opacity(1).bottom(0).step();
                this.setData({
                    recoedDalogflash: animation.export()
                });
            }
            else {
                animation.opacity(0).bottom("-630rpx").step();
                this.setData({
                    recoedDalogflash: animation.export()
                });
                setTimeout(function () {
                    that.setData({
                        isShow:false,
                        recoedDalogflash: null,
                    })
                }, 200)
            }
        },

        open:function(e){
            console.log(e)
            if(e != undefined)
                this.openrecoedModal(true, e);
            
            
        },
        /**
         * 发送举报信息
         */
        reportOperation: function(e) {
            var that = this;
            return new Promise((resolve, reject) => {
                wx.request({
                    url: app.globalData.urlHome + '/community/auth/report',
                    method: "POST",
                    header: {
                        'token': app.globalData.token
                    },
                    data: {
                        userID: that.data.userID,
                        targetID: that.data.targetID,
                        postID: that.data.postID,
                        content: that.data.content,
                    },
                    success: (loadRes) => {
                        if(loadRes.data.code == 200){
                            resolve()
                        }
                        else{
                            wx.showToast({
                              title: loadRes.data.msg,
                              icon: 'error',
                              duration: 1500
                            })
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
         * 获取帖子内容
         */
        getContent: function (e) {
            var that = this;
            that.setData({
                content: e.detail.value
            })
        },
        buttonClick: util.throttle(function(e){
            var that = this;
            console.log(that.data)
            if(that.data.content == ''){
                wx.showToast({
                  title: '内容不能为空!',
                  icon: 'error',
                  duration: 1500
                })
            }else{
                wx.showLoading({
                  title: '加载中',
                })
                that.reportOperation().then(res => {
                    that.setData({
                        content: ''
                    })
                    wx.hideLoading();
                    wx.showToast({
                        title: '举报成功',
                        icon: 'success',
                        duration: 1500
                      })
                    setTimeout(function () {
                        that.openrecoedModal(false);
                    }, 1600)

                }).catch(error => {
                    wx.hideLoading();
                })
            }
        },1500),


        close:function(){
            this.openrecoedModal(false);
            that.setData({
                content: ''
            })
        },

        catchmove: function () { ; },
        
    }
})