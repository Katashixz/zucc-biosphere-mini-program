let app = getApp();
Component({
    options: {
        multipleSlots: true // 在组件定义时的选项中启用多slot支持
    },
    properties: {
        step: {
            type: Number,
            value: 0
        },
        title:{
            type:String,
            value:"提示框"
        }
    },
    lifetimes: {
        ready: function () {
            if(this.data.isShow){
                this.open();
            }
        },
        detached: function () {
            this.setData({ isShow: false });
        }
    },
    data: {
        isShow:false,
        tipContent: "错误",
        record:[],
        energyRecord:[10,66,99],
        recoedDalogflash:null,
    },
    methods: {
        openrecoedModal(status) {
            let that = this;
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
            this.openrecoedModal(true);
            
            
        },

        buttonClick:function(e){
            this.openrecoedModal(false);
        },

        close:function(){
            this.openrecoedModal(false);
        },

        catchmove: function () { ; },
        
    }
})