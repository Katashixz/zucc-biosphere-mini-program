// utils/searchBar/searchBar.js
const util = require('../../utils/jsUtil/jsUtil')
const app = getApp()

Component({
    /**
     * 组件的属性列表
     */
    properties: {
        placeHolderContent: {
            type: String,
            value: "请输入关键词"
        },
        searchContent: {
            type: String,
            value: ""
        },
        //0代表先跳转到搜索页面，1代表是帖子搜索，2代表是百科搜索
        searchType: {
            type: Number,
            value: 8888
        } 
    },

    /**
     * 组件的初始数据
     */
    data: {
        isClick: false,
        // searchContent: "",
        resData: [],
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
        confirmTap: util.throttle(function (e) {
            var that = this;
            var content = that.data.searchContent;
            var type = that.data.searchType;
            var url = app.globalData.urlHome + '/community/exposure/search';

            if(type == 2){
                url = app.globalData.urlHome + '/wikiData/exposure/search';
            }
            // 0代表从百科页面点入
            if(type == 0){
                setTimeout(() => {
                    wx.navigateTo({
                        url: '/pages/searchPage/searchPage?type=2' + (content == "" ? "" : ('&searchContent=' + content))
                    })
                }, 700)
            }else{
                //输入为空需要做处理
                if(content == ""){
                    this.triggerEvent('errorBar', "不能输入为空");
                    return;
                }
                wx.request({
                url: url + '?type=' + type + '&content=' + content,
                method: 'GET',
                success: (res) => {
                    
                    this.triggerEvent('changeNaviIndex', res)
                },
                fail: (res) => {
                    wx.showToast({
                        title: '服务器错误',
                        icon: 'error',
                        duration: 2000
                    })
                }
                })
        }

        },800),
        
    }
    
})

