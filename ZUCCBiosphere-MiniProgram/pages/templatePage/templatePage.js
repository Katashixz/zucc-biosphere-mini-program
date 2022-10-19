// pages/templatePage/templatePage.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        postList:[
            {

            }
        ],
        isLoadAllPosts: false,
        isEmpty: true,
        aniTime: false,
    },

    
    /**
     * 判断是否为视频
     */
    isVideo(target){
        var typeTemp = target.split(".");
        if(typeTemp[typeTemp.length - 1] == 'mp4' || typeTemp[typeTemp.length - 1] == 'mov' || typeTemp[typeTemp.length - 1] == 'wmv' || typeTemp[typeTemp.length - 1] == 'mpg' || typeTemp[typeTemp.length - 1] == 'avi'){
            return true;
        }
        return false;
    },
    /**
     * 加载数据
     */
    loadPost(){
        var that = this;
        var userID = that.data.pageData.userID;
        wx.request({
            method: 'GET',
            url: app.globalData.urlHome + '/user/loadMyPost?userID=' + userID,
            header: {
                'token': app.globalData.token
              },
            success: (res)=>{
                if(res.data.code == 200){
                    that.setData({
                        isEmpty: false,
                        postList: res.data.data.postList,
                    })
                    
                }else{
                    var obj = {
                        msg: res.data.msg,
                        type: "tip"
                    }
                    that.promptBox.open(obj);
                }
            },
            complete: (res)=>{
                wx.hideLoading();
            }
        })
    },

    /**
     * 查看图片
     */
    handleImagePreview(e) {
        var that = this;
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = that.data.postList[idx].imageUrlList
        var mediaUrlList = [];
        // console.log(images);
        for(var i = 0; i < images.length; i ++){
            var type = "image";
            if(that.isVideo(images[i])){
                type = "video";
            }
            //视频图片分开判断
            var temp;
            if(type == "video"){
                var position = images[i].lastIndexOf('.');
                var posterUrl = images[i].slice(0, position) + "_0.jpg";
                temp = {
                    url: images[i],
                    type: type,
                    poster: posterUrl,
                }
            }
            else{
                temp = {
                    url: images[i],
                    type: type,
                }
            }
            // console.log(temp)

            mediaUrlList.push(temp);
        }
        // console.log(mediaUrlList);

        wx.previewMedia({
          sources: mediaUrlList,
          current: idx2
        })
        
    },
    /**
     * 跳转到帖子详情页面
     */
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.postList[e.currentTarget.dataset.index].postID
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        if(options.type == 1) {
            wx.setNavigationBarTitle({
                title: "我的帖子",
              })
        }
        that.setData({
            pageData: options,
        })
        that.loadPost();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        this.promptBox = this.selectComponent("#promptBox");
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

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
        var that = this;
        that.loadPost();
    },

    
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        // var that = this
        // console.log(1)
        // setTimeout(() => {
        //     that.setData({
        //         aniTime: true
        //     })
        //     return new Promise((resolve, reject) => {
                
        //         var that = this;
        //         if(!that.data.isLoadAllPosts)
        //         {
        //             var curPage = that.data.curPage + 1;
        //             that.setData({
        //                 curPage: curPage,
        //             })
        //             var pageSize = that.data.pageSize;
        //             that.loadPost(curPage,pageSize);
        //         }
        //             resolve()
        //     })
        // }, 600)
        // setTimeout(() => {
        //     that.setData({
        //         aniTime: false
        //     })
        // }, 2000)
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})