// pages/postDetails/postDetails.js
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        state: false,
        click: false,
        postItem: {
            
        },
        commentList: [
            
        ],
        isFocus: false,
    },
    // 判断是否为视频
    isVideo(target){
        var typeTemp = target.split(".");
        if(typeTemp[typeTemp.length - 1] == 'mp4' || typeTemp[typeTemp.length - 1] == 'mov' || typeTemp[typeTemp.length - 1] == 'wmv' || typeTemp[typeTemp.length - 1] == 'mpg' || typeTemp[typeTemp.length - 1] == 'avi'){
            return true;
        }
        return false;
    },
    //   查看图片
    handleImagePreview(e) {
        var that = this;
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = that.data.postItem.imageUrlList
        var mediaUrlList = [];
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
    
    //刷新页面数据的封装函数
    refreshData(postID){
        var that = this;
        that.loadPostDetail(postID);
        that.loadPostComment(postID);
    },

    //点赞功能
    changeLike: function (e) {
        var that = this;
        //先判断是否登录 登录后才能点赞
        if(!app.globalData.hasUserInfo){
            app.getUserProfile().finally(() => {
                that.refreshData(that.data.postID);
            })
            
        }else{
            that.changeLikeFunc(e)

        }
    },

    /**
     * 点赞功能节流实现
     */
    changeLikeFunc: util.throttle(function (e) {
        var that = this;
        // var index = e.currentTarget.dataset.index;
        var isLiked = that.data.postItem.postIsLiked;
        that.setData({
            [`postItem.postIsLiked`]: !isLiked,
            [`postItem.likeAnimate`]: "animation: heartBeat 1s 1;",

        })
        var url = app.globalData.urlHome + '/community/auth/changeLikeStatus';
        wx.request({
            url: url,
            method:"POST",
            header: {
                'token': app.globalData.token
            },
            data: {
                status: !isLiked,
                userID: app.globalData.userInfo.id,
                postID: that.data.postItem.postID
            },
            success: (res) => {
                if(res.data.code == 200){
                    
                    var likeNum = that.data.postItem.likeNum;
                    if(!isLiked){
                        likeNum = likeNum + 1;
                        that.setData({
                            [`postItem.likeNum`]: likeNum,
                        })
                    }else{
                        likeNum = likeNum - 1;
                        that.setData({
                            [`postItem.likeNum`]: likeNum,
                        })
                    }

                }else{
                  wx.showToast({
                      title: "点赞失败",
                      icon: 'error',
                      duration: 2000
                    })
                }
  
            },
            fail: (res) => {
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            }
          
  
          })
          setTimeout(() => {
            that.setData({
                [`postItem.likeAnimate`]: "",
            })
        }, 1500)
    }, 1500),
        
    /**
     * 打赏弹窗
     */
    toReward: function (e) {
        var that = this;

        const post = that.data.postItem
        if (app.globalData.hasUserInfo) {
            if (app.globalData.userInfo.id == post.userID) {
                wx.showToast({
                    title: '不能给自己打赏',
                    icon: 'error',
                    duration: 2000,
                })
            } else {
                this.userModal.open();
            }

        } else {
            app.getUserProfile().finally(() => {
                that.refreshData(that.data.postID);
            })

        }
    },
    /**
     * 分享此页面
     */
    onShareAppMessage: function (e) {
        this.closeMoreOptions();
        return {
          title: '城院生态圈',
          path: '/pages/postDetails/postDetails?postID=' + this.data.postItem.postID,
         
        }
      },
    
    /**
     * 更多选择的动画
     */
    moreOptions: function(e){
        var list_state = this.data.state,
          first_state = this.data.click;
          
        if (!first_state){
          this.setData({
           click: true,
          });
        }
        if (list_state){
          this.setData({
           state: false
          });
        }else{
          this.setData({

           state: true
          });
        }
      },
    /**
     * 关闭更多选择的动画
     */
    closeMoreOptions: function(e){
            this.setData({
            //    click: true,
                // [`postList[${index}].click`]: false,
                state: false,

            });
    },
    /**
     * 收藏
     */
    star(options) {
        console.log("收藏")
    },
    /**
     * 举报
     */
    report(options) {
        console.log("举报")

    },
    
    /**
     * 输入框聚焦
     */
    confirmFocus(options) {
        var that = this;
        that.setData({
            isFocus: true,
        })
    },
    
    /**
     * 移出焦点监听
     */
    inputCommentsBlur(options) {
        var that = this;
        that.setData({
            isFocus: false,
        })
        
    },
    /**
     * 输入事件监听
     */
    inputCommentsContentListening(options) {
        var that = this;
    },

    loadPostDetail(postID) {
        var that = this;
        wx.showLoading({
          title: '加载中',
        })
        var url = app.globalData.urlHome + '/community/exposure/loadPostDetail?postID=' + postID;
        if(app.globalData.hasUserInfo){
            url = url + '&userID=' + app.globalData.userInfo.id;
        }
        wx.request({
            url: url,
            method:"GET",
            success: (res) => {
                wx.hideLoading();
                if(res.data.code == 200){
                    that.setData({
                        postItem: res.data.data.postDetail,
                        [`postItem.postIsLiked`]: res.data.data.isLiked,
                    })
                }else{
                  wx.showToast({
                      title: '详情加载错误',
                      icon: 'error',
                      duration: 2000
                    })
                }
  
            },
            fail: (res) => {
                wx.hideLoading();
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            }
          
  
          })
    },
    loadPostComment(postID) {
        var that = this;
        wx.showLoading({
            title: '加载中',
          })
          var url = app.globalData.urlHome + '/community/exposure/loadComment?postID=' + postID;
          wx.request({
              url: url,
              method:"GET",
              success: (res) => {
                wx.hideLoading();
                  if(res.data.code == 200){
                      that.setData({
                        commentList: res.data.data.commentList,
                      })
                  }else{
                    wx.showToast({
                        title: '评论加载错误',
                        icon: 'error',
                        duration: 2000
                      })
                  }
    
              },
              fail: (res) => {
                wx.hideLoading();
                  wx.showToast({
                    title: '服务器错误',
                    icon: 'error',
                    duration: 2000
                  })
              }
            })
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;

        that.setData({
            postItem: [],
            commentList: [],
        })
        that.setData({
            postID: options.postID,
        })
        that.refreshData(options.postID)
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
        this.userModal = this.selectComponent("#userModal");

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
        var postID = that.data.postID;
        that.refreshData(postID)
        
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

})