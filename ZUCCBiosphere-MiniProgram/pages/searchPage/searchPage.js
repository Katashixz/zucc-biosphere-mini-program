// pages/searchPage/searchPage.js

Page({

    /**
     * 页面的初始数据
     */
    data: {
        isResEmpty: false,
        typeText: "",
        searchContent:"",
        searchResult: [
            // {
            //     postID: 2,
            //     userID: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
            //     userOpenID: "111",
            //     userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     postContent: "ee",
            //     postTheme: "突击",
            //     postDate: "2022年8月19日17:45:53",
            //     postImage: [
                    
            //     ],

            // },
            // {
            //     postID: 2,
            //     userID: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
            //     userOpenID: "111",
            //     userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     postContent: "ee",
            //     postTheme: "突击",
            //     postDate: "2022年8月19日17:45:53",
            //     postImage: [
            //         {
            //             imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
            //         },
            //        {
            //         imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
            //        }
            //     ],
            // }
            // {
            //     image: "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg",
            //     nickName: "花花"
            // },
            // {
            //     image: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     nickName: "小草"
            // },
            // {
            //     image: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     nickName: "小草"
            // },
            // {
            //     image: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     nickName: "小草"
            // },
            // {
            //     image: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     nickName: "小草"
            // },
            
        ],
    },
    showErrorBar: function (msg) {
        var that = this;
        var obj = {
            msg: msg.detail,
            type: "error"
        }
        that.promptBox.open(obj);
    },

    handleImagePreview(e) {
        var that = this;
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = that.data.searchResult[idx].imageUrlList
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
        // let toImg = []
        // images.forEach(element => {
        //     toImg.push(element)
        // });
        // wx.previewImage({
        //     current: toImg[idx2],  //当前预览的图片
        //     urls: toImg,  //所有要预览的图片
        // })
        
    },
    
    /**
     * 跳转到帖子 
     */
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.searchResult[e.currentTarget.dataset.index].postID
        })
    },

    /**
     * 跳转到百科详情 
     */
    toWikiDetail: function(e){
        wx.navigateTo({
            url: '/pages/animalWikiDetail/animalWikiDetail?id='+this.data.searchResult[e.currentTarget.dataset.index].id+'&name='+this.data.searchResult[e.currentTarget.dataset.index].nickName+'&type='+this.data.searchResult[e.currentTarget.dataset.index].type

        })
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
    onGetData: function (e) {
        var that = this;
        var searchResult = e.detail.data.data.searchResult
        //先清空数据
        that.setData({
            searchResult: [],
            isResEmpty: false
        })
        if(searchResult.length == 0){
            that.setData({
                isResEmpty: true
            });
        }
        else{
            that.setData({
                searchResult: searchResult,
                isResEmpty: false
            })
        }
        console.log(searchResult)

    },
    //跳转到帖子详情页面
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.searchResult[e.currentTarget.dataset.index].postID
        })
    },
    
    
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            pageData: options,
        })
        console.log(that.data.pageData)
        if(options.type == 1){
            that.setData({
                typeText: "请输入帖子关键词",
            })
        }else{
            that.setData({
                typeText: "请输入关键词",
            })
        }
        if(options.searchContent != undefined){
            //如果是从百科传来并且已有搜索关键字，就直接触发搜索
            that.setData({
                searchContent: options.searchContent
            })
            that.selectComponent("#searchBar").confirmTap();

        }
        


    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.promptBox = that.selectComponent("#promptBox");
        // searchBar.setPlaceHolderContent(that, "请输入帖子内容关键词")
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

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})