// pages/adoptDiary/adoptDiary.js
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        today: '',
        date:'',
        diaryList:[
            {
                "userID": 1,
                "userName": "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
                "avatar": "https://bkimg.cdn.bcebos.com/pic/562c11dfa9ec8a139fe24202f903918fa1ecc05c?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto",
                "title": "与它相遇的第一天",
                "body": "啊 席八",
                "imageUrlList": [],
                "createdAt": "2023-3-26 17:12",
                "targetID": 1,
                "targetAvatar": "https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5",
                "targetName": "小黑",
            },
            {
                "userID": 1,
                "userName": "xddddhb",
                "avatar": "https://bkimg.cdn.bcebos.com/pic/562c11dfa9ec8a139fe24202f903918fa1ecc05c?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxMTY=,g_7,xp_5,yp_5/format,f_auto",
                "title": "sddsdsd",
                "body": "啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2啊 席八2",
                "createdAt": "2023-3-26 17:12",
                "imageUrlList": ["https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5","https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5","https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5","https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5"],
                "targetID": 1,
                "targetAvatar": "https://bkimg.cdn.bcebos.com/pic/a8ec8a13632762d0f703fa1f08ba1ffa513d2697684e?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxODA=,g_7,xp_5,yp_5",
                "targetName": "",
            },
        ]
    },

    bindDateChange: function(e) {
        var that = this;
        console.log('picker发送选择改变，携带值为', e.detail.value)
        that.setData({
          date: e.detail.value
        })
      },

    /**
     * 跳转到日记详情
     */
    toDiary(e){
        wx.navigateTo({
          url: '/pages/diaryDetail/diaryDetail',
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

     /**
     * 查看图片
     */
    handleImagePreview(e) {
        var that = this;
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = that.data.diaryList[idx].imageUrlList
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
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        var date = util.formatDate(new Date(), 'yyyy-mm-dd');
        that.setData({
            date: date,
            today: date
        })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

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