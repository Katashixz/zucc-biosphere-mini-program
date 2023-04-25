// pages/templatePage/templatePage.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        startX: 0,
        startY: 0,
        postList:[
            {

            }
        ],
        isLoadAllPosts: false,
        isEmpty: true,
        aniTime: false,
    },
 // 开始滑动
 touchStart(e) {
    // console.log('touchStart=====>', e);
    let dataList = [...this.data.postList]
    dataList.forEach(item => {
    // 让原先滑动的块隐藏
      if (item.isTouchMove) {
        item.isTouchMove = !item.isTouchMove;
      }
    });
   // 初始化开始位置
    this.setData({
      postList: dataList,
      startX: e.touches[0].clientX,
      startY: e.touches[0].clientY
    })
  },
// 滑动~
  touchMove(e) {
    // console.log('touchMove=====>', e);
    let moveX = e.changedTouches[0].clientX;
    let moveY = e.changedTouches[0].clientY;
    let indexs = e.currentTarget.dataset.index;
    let dataList = [...this.data.postList]
// 拿到滑动的角度，判断是否大于 30°，若大于，则不滑动
    let angle = this.angle({
      X: this.data.startX,
      Y: this.data.startY
    }, {
      X: moveX,
      Y: moveY
    });

    dataList.forEach((item, index) => {
      item.isTouchMove = false;
      // 如果滑动的角度大于30° 则直接return；
      if (angle > 30) {
        return
      }
    
    // 判断是否是当前滑动的块，然后对应修改 isTouchMove 的值，实现滑动效果
      if (indexs === index) {
        if (moveX > this.data.startX) { // 右滑
          item.isTouchMove = false;
        } else { // 左滑
          item.isTouchMove = true;
        }
      }
    })

    this.setData({
      postList: dataList
    })
  },

  /**
   * 计算滑动角度
   * @param {Object} start 起点坐标
   * @param {Object} end 终点坐标
   */
  angle: function (start, end) {
    var _X = end.X - start.X,
      _Y = end.Y - start.Y
    //返回角度 /Math.atan()返回数字的反正切值
    return 360 * Math.atan(_Y / _X) / (2 * Math.PI);
  },
    /**
     * 删除
     */  
    delItem(e){
        var that = this;
        var index = e.currentTarget.dataset.index;
        var postID = that.data.postList[index].postID;
        // 删除帖子
        wx.showModal({
            content:'是否确定删除',
            success:({confirm})=>{
              if(confirm){
                    wx.request({
                        url: app.globalData.urlHome + '/user/auth/deletePost',
                        method: "POST",
                        header: {
                            'token': app.globalData.token
                        },
                        data: {
                            postID: postID,
                            userID: userID,
                        },
                        success: (res) => {
                            if(res.data.code == 200){
                                wx.showToast({
                                title: '删除成功',
                                duration: 2000,
                                icon: 'success',
                                })
                                setTimeout(function () {
                                    that.onPullDownRefresh();
                                }, 2100)
                            }
                            else{
                                var obj = {
                                    msg: res.data.msg,
                                    type: "error"
                                }
                                that.promptBox.open(obj);
        
                                if(res.data.code == 300){
                                    app.clearUserData();
                                    
                                }
                            }
                        },
                        fail: (res) => {
                            wx.showToast({
                                title: '服务器错误',
                                duration: 2000,
                                icon: 'error'
                            })
                        },
        
                    })
                
              }
            },
          fail: () => { 
            wx.showToast({
                title: '服务器错误',
                duration: 2000,
                icon: 'error'
            })
          },     
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
     * 加载数据
     */
    loadPost(){
        var that = this;
        // 加载前清空
        that.setData({
            postList: [],
        })
        var url = app.globalData.urlHome + '/admin/auth/loadPostList'
        wx.request({
            method: 'GET',
            header: {
              'token': app.globalData.token
            },
            url: url,
            success: (res)=>{
                if(res.data.code == 200){
                    res.data.data.postList.forEach((item, index) => {
                        item.isTouchMove = false
                    })
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
            fail: (res)=>{
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            },
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
            wx.setNavigationBarTitle({
                title: "帖子维护",
              })
        that.loadPost();
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
        this.promptBox = this.selectComponent("#promptBox");
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

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})