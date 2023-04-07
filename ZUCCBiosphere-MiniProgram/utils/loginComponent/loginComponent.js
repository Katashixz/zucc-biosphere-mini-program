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
        title:"登录确认",

    },
    methods: {
        openrecoedModal(status, e) {
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
            this.openrecoedModal(true, e);
            
            
        },

        buttonClick:function(e){
            var that = this
            var page = getCurrentPages().pop();
            return new Promise((resolve, reject) => {
                app.getUserProfile().then((res)=>{
                    resolve(res);
                }).catch(error => {
                    reject(error)
                }).finally(() => {
                    page.onPullDownRefresh();
                    that.openrecoedModal(false);
                })
            })
        },

        close:function(){
            this.openrecoedModal(false);
        },

        catchmove: function () { ; },
        
    }
})