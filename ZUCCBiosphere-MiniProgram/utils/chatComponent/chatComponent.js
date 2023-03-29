let app = getApp();
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
        title:"用户信息",
        avatarUrl: "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0",
        userName: "信息获取异常",
        userID: 0,

    },
    methods: {
        openrecoedModal(status, e) {
            let that = this;
            if(status){
                that.setData({
                    userID: e.userID,
                    userName: e.userName,
                    avatarUrl: e.avatarUrl
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

        buttonClick:function(e){
            var that = this
            var uid = wx.getStorageSync('uid')
            if(uid != that.data.userID){
                wx.navigateTo({
                    url: '/pages/chatPage/chatPage?target=' + that.data.userID + '&userName=' + that.data.userName + '&targetAvatar=' + that.data.avatarUrl,
                })
            }else{
                wx.showToast({
                    title: '无法私聊自己',
                    icon: 'error',
                    duration: 2000
                })
            }
            this.openrecoedModal(false);
        },

        close:function(){
            this.openrecoedModal(false);
        },

        catchmove: function () { ; },
        
    }
})