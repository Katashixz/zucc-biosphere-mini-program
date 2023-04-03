// utils/foodComponent/foodComponent.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')
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
        recoedDalogflash:null,
        title:"商品选择",
        curIndex:-1 ,
        target:-1,
        shopItem:[
        ]
    },
    methods: {
        itemClick(e){
            var that = this;
            var index = e.currentTarget.dataset.index;
            that.setData({
                curIndex : index
            })
        },
        openrecoedModal(status) {
            let that = this;
            if(status){
                
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
            // console.log(e)
            // if(e != undefined)
            var that = this;
            wx.request({
                method: 'GET',
                url: app.globalData.urlHome + '/adopt/exposure/getShopItem?type=1',
                success: (res) => {
                    if(res.data.code == 200){
                        console.log(e);
                        that.setData({
                            shopItem: res.data.data,
                            target: e
                        })
                    this.openrecoedModal(true);
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

        buttonClick: util.throttle(function(e){
            var that = this;
            that.insertEnergyRecord().then(res => {
                that.saveCondition().then(res2 => {
                    wx.showToast({
                        title: '操作成功',
                        icon: 'success',
                        duration: 1500 
                      })
                      setTimeout(() => {
                        this.openrecoedModal(false);
                      }, 1500)
                })
            }).catch(error => {
                
            })
           
        }, 800),
        saveCondition:function(){
            var that = this;
            var index = that.data.curIndex;
            console.log(that.data.shopItem)
            return new Promise((resolve, reject) => {
                wx.request({
                    method: 'POST',
                    url: app.globalData.urlHome + '/adopt/auth/saveCondition/',
                    header: {
                        'content-type': 'application/json',
                        'token': app.globalData.token
                    },
                    data:{
                        sourceID: app.globalData.userInfo.id,
                        targetID: that.data.target,
                        action: 2,
                        shopID: that.data.shopItem[index].id
                    },
                    success: (res) => {
                        if(res.data.code == 200){
                            resolve(res)
                        }
                        else{
                            wx.showToast({
                                title: res.data.msg,
                                icon: 'error',
                                duration: 1500
                                })
                            reject(res)
                        }
                    },
                    fail: (res) => {
                        wx.showToast({
                        title: '服务器错误',
                        icon: 'error',
                        duration: 1500
                        })
                        reject(res)


                    }
                })
            })
        },
        insertEnergyRecord:function(){
            var that = this;
            var index = that.data.curIndex;
            return new Promise((resolve, reject) => {
                wx.request({
                    method: 'POST',
                    url: app.globalData.urlHome + '/user/auth/insertEnergyRecord/',
                    header: {
                        'content-type': 'application/json',
                        'token': app.globalData.token
                    },
                    data:{
                        point: that.data.shopItem[index].price,
                        type: 3,
                        userID: app.globalData.userInfo.id,
                        toUserID: 0
                    },
                    success: (res) => {
                        if(res.data.code == 200){
                            resolve(res)
                        }
                        else{
                            wx.showToast({
                                title: res.data.msg,
                                icon: 'error',
                                duration: 1500
                                })
                        }
                    },
                    fail: (res) => {
                        wx.showToast({
                        title: '服务器错误',
                        icon: 'error',
                        duration: 1500
                        })
                        reject(res)
                    }
                })
            })
        },

        close:function(){
            this.openrecoedModal(false);
        },

        catchmove: function () { ; },
        
    }
})
