// pages/myComments/myComments.js
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        startX: 0,
        startY: 0,
        commentList:[
            // {
            //     "postID":1,
            //     "userID":1,
            //     "userName":"屑黄宝",
            //     "userAvatarUrl":"https:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMVTMlfE2lQaFmUM5AvQQ4kg\/132",
            //     "commentAccessName":"屑黄宝",
            //     "commentDate":"2022-09-09 10:49:18",
            //     "commentAccessID":1,
            //     "image":"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/BiospherePostImage/a1d1f477acd0373c036387e69b57c1d1_45701663921985330.jpg",
            //     "content":"csaa"
            // },
            // {
            //     "postID":16,
            //     "userID":1,
            //     "userName":"屑黄宝屑黄宝屑黄宝屑黄宝",
            //     "userAvatarUrl":"https:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/DYAIOgq83eobfn8t4BNhx1KDDpoZyQHTxW3II5D5cw6DibYcSVPJsd8xntgXmK6rMVTMlfE2lQaFmUM5AvQQ4kg\/132",
            //     "commentAccessName":"🍼喝前摇依瑶嗷嗷嗷嗷〜",
            //     "commentDate":"2022-09-28 11:29:53",
            //     "commentAccessID":4,
            //     "image":null,
            //     "postText":"什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事",

            //     "content":"什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事什么什么什么什么什么什么没事没事"
            // }
        ]
    },
    /**
     * 跳转到帖子详情页面
     */
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.commentList[e.currentTarget.dataset.index].postID
        })
    },
    /**
     * 加载评论
     */
    loadComments(){
        var that = this;
        var userID = that.data.pageData.userID;
        var url = app.globalData.urlHome + '/user/auth/loadMyComment?userID=' + userID;
        wx.request({
            method: 'GET',
            url: url,
            header: {
                'token': app.globalData.token
            },
            success: (res)=>{
                if(res.data.code == 200){
                    res.data.data.commentList.forEach((item, index) => {
                        item.isTouchMove = false
                    })
                    that.setData({
                        isEmpty: false,
                        commentList: res.data.data.commentList,
                    })
                    
                }else{
                    var obj = {
                        msg: res.data.msg,
                        type: "tip"
                    }
                    that.promptBox.open(obj);
                    if(res.data.code == 300){
                            app.clearUserData();
                            
                        }
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

 // 开始滑动
 touchStart(e) {
    // console.log('touchStart=====>', e);
    let dataList = [...this.data.commentList]
    dataList.forEach(item => {
    // 让原先滑动的块隐藏
      if (item.isTouchMove) {
        item.isTouchMove = !item.isTouchMove;
      }
    });
   // 初始化开始位置
    this.setData({
      commentList: dataList,
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
    let dataList = [...this.data.commentList]
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
        commentList: dataList
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
        var id = that.data.commentList[index].id;
        var postID = that.data.commentList[index].postID;
        var userID = app.globalData.userInfo.id;
        // 删除评论
        
        wx.showModal({
            content:'是否确定删除',
            success:({confirm})=>{
              if(confirm){
                wx.request({
                    url: app.globalData.urlHome + '/user/auth/deleteComment',
                    method: "POST",
                    header: {
                        'token': app.globalData.token
                    },
                    data: {
                        postID: postID,
                        id: id,
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
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            pageData: options,
        })
        that.loadComments();
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
        that.loadComments();
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