// utils/searchBar/searchBar.js
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        placeHolderContent: {
            type: "String",
            value: "请输入关键词"
        }
    },

    /**
     * 组件的初始数据
     */
    data: {
        isClick: false,
        searchContent: "",
        // placeHolderContent: "请输入关键词",
    },

    /**
     * 组件的方法列表
     */
    methods: {
        /**
         * 个性化
         */
        // setPlaceHolderContent(page, placeHolderContent){
        //     var that = this;
        //     this.setData({
        //         placeHolderContent: placeHolderContent,
        //     })
        // },
        /**
         * 输入框聚焦
         */
        clickFocus(){
            var that = this;
            
            that.setData({
                isClick: true,
            })

        },
        /**
         * 输入框失焦
         */
        clickBlur(){
            var that = this;
            var click;
            if(that.data.searchContent != ""){
                click = true;
            }else{
                click = false;
            }
            that.setData({
                isClick: click,
            })

        },
        /**
         * 获取输入值
         */
        updateContent(e){
            var that = this;
            that.setData({
                searchContent: e.detail.value,
                isClick: true,
            })

        },
        /**
         * 清空输入框
         */
        clearInput(){
            var that = this;
            that.setData({
                searchContent: "",
                isClick: false,

            })

        },
        /**
         * 回车触发搜索
         */
        confirmTap(){
            var that = this;
            console.log("回车")

        },
    }
    
})

