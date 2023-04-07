// pages/commuityHome/communityHome.js
const app = getApp()
var startY, endY;
var moveFlag = true; // 判断执行滑动事件
const util = require('../../utils/jsUtil/jsUtil')

Page({

    /**
     * 页面的初始数据
     */
    data: {
        curPage: 1,
        pageSize: 7,
        currentIndex: 0,
        releasePostStyle: "",
        tenHotPosts: [
            {
                hotPost:{
                    postID:-1
                },
                content:"暂无热帖",
            },
            // {
            //     postID: 1,
            //     content: "热帖"
            // },
            // {
            //     postID: 2,
            //     content: "ee"
            // },
        ],
        postList: [
            
            
        ],
        isLoadAllPosts: false,
        aniTime: false,
        releasePostAni: false,
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
     * 跳转到帖子详情页面
     */
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.postList[e.currentTarget.dataset.index].postID
        })
    },
    //跳转到热帖详情页面
    toHotPost: function (e) {
        if(this.data.tenHotPosts[e.currentTarget.dataset.index].hotPost.postID != -1){
        
            wx.navigateTo({
                url: '/pages/postDetails/postDetails?postID=' + this.data.tenHotPosts[e.currentTarget.dataset.index].hotPost.postID
            })
        }
    },
    /**
     * 搜索页面
     */
    toSearch: function (e) {
        wx.navigateTo({
            url: '/pages/searchPage/searchPage?type=1'
        })
    },
    /**
     * 热帖列表页面
     */
    toHotPostList: function (e) {
        wx.navigateTo({
            url: '/pages/hotPostList/hotPostList'
        })
    },
    /**
     * 打开领养日记
     */
    toAdoptionDiary: function (e) {
        wx.navigateTo({
            url: '/pages/adoptDiary/adoptDiary',
          })
    },
    /**
     * 打开商城
     */
    toShop: function (e) {
        var that = this;
        wx.navigateTo({
          url: '/pages/shop/shop',
        })
    },
    /**
     * 点赞功能
     */
    changeLike: function (e) {
        var that = this;
        //先判断是否登录 登录后才能点赞
        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()

        }else{
            that.changeLikeFunc(e)
        }
        
    },
    
    /**
     * 点赞功能节流实现
     */
    changeLikeFunc: util.throttle(function (e) {
        var that = this;
        var index = e.currentTarget.dataset.index;
        var isLiked = that.data.postList[index].postIsLiked;
        // likeAnimate: "",

        that.setData({
            [`postList[${index}].postIsLiked`]: !isLiked,
            [`postList[${index}].likeAnimate`]: "animation: heartBeat 1s 1;",

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
                postID: that.data.postList[index].postID
            },
            success: (res) => {
                if(res.data.code == 200){
                    var likeNum = that.data.postList[index].likeNum;
                    that.sendLikeMsg(index);
                    if(!isLiked){
                        likeNum = likeNum + 1;
                        that.setData({
                            [`postList[${index}].likeNum`]: likeNum,
                            

                        })
                    }else{
                        likeNum = likeNum - 1;
                        that.setData({
                            [`postList[${index}].likeNum`]: likeNum,
                        })
                    }

                }else{
                  wx.showToast({
                      title: "点赞失败",
                      icon: 'error',
                      duration: 2000
                    })
                    if(res.data.code == 300){
                        app.clearUserData();
                        
                    }
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
                [`postList[${index}].likeAnimate`]: "",
            })
        }, 1500)

    }, 1500),
    /**
     * 发送点赞消息通知
     */   
    sendLikeMsg: function (index) {
        var that = this;
        var uid = wx.getStorageSync('uid');
        var objectID = that.data.postList[index].postID;
        if(uid != undefined && objectID != undefined){
            var mCmd = {
                "code": 10003,
                "userID": uid,
                "action": 1,
                "objectID": objectID,
                "objectType": 2,
                "createdAt": new Date(),
                "removeState": that.data.postList[index].postIsLiked ? 0 : 1,
            }
            app.sendMessage(mCmd);
            util.resiverMessage(that);
        }else {
            wx.showToast({
                title: '信息异常',
                icon: 'error',
                duration: 2000
              })
        }
    },
    
    /**
     * 发送评论和充电消息通知
     */
    sendNotifyMsg: function (action, targetId) {
        var that = this;
        var uid = wx.getStorageSync('uid');
        if(uid != undefined && targetId != undefined){
            var mCmd = {
                "code": action == 2 ? 10004 : action == 4 ? 10005 : -1,
                "userID": uid,
                "action": action,
                "objectID": targetId,
                "objectType": 1,
                "createdAt": new Date(),
                "removeState": 0,
            }
            app.sendMessage(mCmd);
            util.resiverMessage(that);
        }else {
            wx.showToast({
                title: '信息异常',
                icon: 'error',
                duration: 2000
              })
        }
    },
    /**
     * WebSocket页面回调函数
     */
    onMessage: function (res) {
        var that = this;
        console.log(res.data)
        var obj = JSON.parse(res.data);
        console.log("chatPage",obj)
        
      },
    
    /**
     * 评论功能
     */
    comment: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.postList[e.currentTarget.dataset.index].postID
        })
    },
    /**
     * 打赏弹窗
     */
    toReward: function (e) {
        var that = this
        const post = that.data.postList[e.currentTarget.dataset.index]
        if (app.globalData.hasUserInfo) {
            if (app.globalData.userInfo.id == post.userID) {
                // wx.showToast({
                //     title: '不能给自己打赏',
                //     icon: 'error',
                // })
                var obj = {
                    msg: "不能给自己打赏",
                    type: "error"
                }
                that.promptBox.open(obj);
            } else {
                // that.energyBox.open();
                that.energyBox.open(post);
            }

        } else {
            that.loginComponent.open()

        }
    },
    rewardOperation: function (e) {
        var that = this;
        var energy = parseInt(e.detail.energy);
        var toUserID = e.detail.toUserID;
        if(energy == -1){
            var obj = {
                msg: "请选择能量值",
                type: "tip"
            }
            that.promptBox.open(obj);
        }
        else if(isNaN(energy)){
            var obj = {
                msg: "请输入正确的数字",
                type: "error"
            }
            that.promptBox.open(obj);
        }
        else if(toUserID == -1){
            var obj = {
                msg: "打赏对象有误",
                type: "error"
            }
            that.promptBox.open(obj);
        }
        else{
            wx.request({
                method: 'POST',
                url: app.globalData.urlHome + '/user/auth/insertEnergyRecord/',
                header: {
                    'content-type': 'application/json',
                    'token': app.globalData.token
                },
                data:{
                    point: energy,
                    type: 1,
                    userID: app.globalData.userInfo.id,
                    toUserID: toUserID
                },
                success: (res) => {
                    if(res.data.code == 200){
                        that.sendNotifyMsg(4,toUserID);
                        wx.showToast({
                            title: '打赏成功',
                            icon: 'success',
                            duration: 1500
                          });
                          setTimeout(() => {
                              that.energyBox.close();
                          }, 1500);
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
                    icon: 'error',
                    duration: 1500
                    })
                }
            })
        }
        
    },
    /**
     * 更多选择的动画
     */
    moreOptions: function(e){
        // console.log(e)
        var index = e.currentTarget.dataset.index
        var list_state = this.data.postList[index].state,
          first_state = this.data.postList[index].click;
          
        if (!first_state){
          this.setData({
        //    click: true,
           [`postList[${index}].click`]: true,
          });
        }
        if (list_state){
          this.setData({
           [`postList[${index}].state`]: false,
        //    state: false
          });
        }else{
          this.setData({
           [`postList[${index}].state`]: true,

        //    state: true
          });
        }
      },
    /**
     * 关闭更多选择的动画
     */
    closeMoreOptions: function(e){
        var index = e.currentTarget.dataset.index
            this.setData({
            //    click: true,
                // [`postList[${index}].click`]: false,
                [`postList[${index}].state`]: false,

            });
    },
    /**
     * 收藏
     */

    star: util.throttle(function (e) {
        var that = this;
        var index = e.currentTarget.dataset.index
        //先判断是否登录 登录后才能点赞
        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()

        }else{
            wx.request({
                method: 'POST',
                url: app.globalData.urlHome + '/community/auth/starPost/',
                header: {
                    'content-type': 'application/json',
                    'token': app.globalData.token
                },
                data:{
                    postID: that.data.postList[index].postID,
                    userID: app.globalData.userInfo.id,
                },
                success: (res) => {
                    if(res.data.code == 200){
                        wx.showToast({
                            title: '收藏成功',
                            icon: 'success',
                            duration: 1500
                          });
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
                    icon: 'error',
                    duration: 1500
                    })
                },
                complete: (res) => {
                    that.closeMoreOptions(e);

                }
            })
        }
    },1500),
    /**
     * 举报
     */
    report(options) {
        var that = this;

        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()

        }else{
            var uid = wx.getStorageSync('uid')
            var index = options.currentTarget.dataset.index

            var obj = {
            "userID": uid,
            "targetID": that.data.postList[index].userID,
            "postID": that.data.postList[index].postID
            }
            that.reportComponent.open(obj)
        }
    },
    /**
     * 分享
     */
    share(options) {
        console.log("分享")

    },
    /**
     * 热帖加载
     */
    loadHotPost() {
        var that = this;

        that.setData({
            currentIndex: 0,
        })
        var url = app.globalData.urlHome + '/community/exposure/loadHotPost';
        // if(app.globalData.hasUserInfo){
        //     url = url + '&userID=' + app.globalData.userInfo.id;
        // }
        wx.request({
            url: url,
            method:"GET",
            success: (res) => {
                if(res.data.code == 200){
                    that.setData({
                        tenHotPosts: res.data.data.tenHotPosts,
                    })

                }else{
                    var temp = [
                        {
                            hotPost:{
                                postID:-1
                            },
                            content:"暂无热帖",
                        }
                    ]
                    that.setData({
                        tenHotPosts: temp,
                    })
                //   wx.showToast({
                //       title: res.data.msg,
                //       icon: 'error',
                //       duration: 2000
                //     })
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
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        
        that.setData({
            curPage: 1,
            postList: [],
        })
        var pageSize = that.data.pageSize;
        that.loadPost(1,pageSize);
        that.loadHotPost();
    },
    openChatComponent(e){
        var that = this;
        //先判断是否登录 登录后才能点赞
        if(!app.globalData.hasUserInfo){
            that.loginComponent.open()
        }else{
            var index = e.currentTarget.dataset.index
        var obj = {
            userName: that.data.postList[index].userName,
            avatarUrl: that.data.postList[index].avatarUrl,
            userID: that.data.postList[index].userID
        };
            that.chatComponent.open(obj);
        }
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        var that = this;
        that.energyBox = that.selectComponent("#energyBox");
        that.promptBox = that.selectComponent("#promptBox");
        that.chatComponent = that.selectComponent("#chatComponent");
        that.reportComponent = that.selectComponent("#reportComponent");
        that.loginComponent = that.selectComponent("#loginComponent");
        that.loginComponent = that.selectComponent("#loginComponent");

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        var that = this;
        
        that.energyBox = that.selectComponent("#energyBox");
        that.promptBox = that.selectComponent("#promptBox");
        that.chatComponent = that.selectComponent("#chatComponent");
        that.reportComponent = that.selectComponent("#reportComponent");
        that.loginComponent = that.selectComponent("#loginComponent");

        that.setData({
            currentIndex: 0,
        })
        if (typeof this.getTabBar === 'function' && this.getTabBar()) {
    
          this.getTabBar().setData({
    
            selected: 2
    
          })
    
        }
        if (app.globalData.openid != '' && !this.data.isLogin) {
            this.setData({
                isLogin: !this.data.isLogin
            })
        }
        //如果加载时登录状态和上次不一样，就刷新页面
        if(that.data.hasInfoChange == undefined){
            that.setData({
                hasInfoChange: app.globalData.hasUserInfo,
            })
        }else{
            if(that.data.hasInfoChange != app.globalData.hasUserInfo){
                that.setData({
                    hasInfoChange: app.globalData.hasUserInfo,
                })
                that.onPullDownRefresh();
                
            }

        }
        
        
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
        that.setData({
            curPage: 1,
            postList: [],
            tenHotPosts: [],
            currentIndex:0
        })
        var pageSize = that.data.pageSize;
        that.loadPost(1,pageSize);
        that.loadHotPost();

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        var that = this

        setTimeout(() => {
            that.setData({
                aniTime: true
            })
            return new Promise((resolve, reject) => {
                // that.setData({
                //     isLoadAllPosts: true,
                // })
                var that = this;
                if(!that.data.isLoadAllPosts)
                {
                    var curPage = that.data.curPage + 1;
                    that.setData({
                        curPage: curPage,
                    })
                    var pageSize = that.data.pageSize;
                    that.loadPost(curPage,pageSize);
                }
                    resolve()
                // })
                //     .catch((err) => {
                //         console.error(err)
                //         reject(err)
                //     })
            })
        }, 600)
        setTimeout(() => {
            that.setData({
                aniTime: false
            })
        }, 2000)
    },

    toReleasePost: function(e){
        var that = this;
        if (!getApp().globalData.userInfo) {
            that.loginComponent.open()

        }
        else{
        wx.navigateTo({
            url: '/pages/releasePost/releasePost'

        })
    }
    },
    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    touchStart: function(e) {
		startY = e.touches[0].pageY; // 获取触摸时的原点
		moveFlag = true;
	},
	// 触摸移动事件
	touchMove: function(e) {
        var that = this;
		endY = e.touches[0].pageY; // 获取触摸时的原点
		if (moveFlag) {
			if (endY - startY > 5) {
                moveFlag = false;
                that.setData({releasePostStyle:"animation: slideInRight 1s 1;"})
			}
			if (startY - endY > 5 && that.data.releasePostStyle!="visibility: hidden") {
                moveFlag = false;
                that.setData({releasePostStyle:"animation: slideOutRight 0.2s 1;"})
                setTimeout(() => {
                    that.setData({
                        releasePostStyle: "visibility: hidden"
                    })
                }, 200)
			}
		}
	},
	// 触摸结束事件
	touchEnd: function(e) {
		moveFlag = true; // 回复滑动事件
    },

	// 帖子加载事件
    loadPost(curPage, pageSize){
        var that = this;
        // wx.showLoading({
        //   title: '加载中',
        // })
        var url = app.globalData.urlHome + '/community/exposure/loadPostList?curPage=' + curPage + '&pageSize=' + pageSize;
        if(app.globalData.hasUserInfo){
            url = url + '&userID=' + app.globalData.userInfo.id;
        }
        wx.request({
            url: url,
            method:"GET",
            success: (res) => {
                if(res.data.code == 200){
                    // console.log(res)
                    var postList2 = that.data.postList;
                    for(var item of res.data.data.postList){
                        postList2.push(item);
                    }
                    that.setData({
                            postList: postList2,
                            // postList: res.data.data.postList,
                            isLoadAllPosts: res.data.data.isEnd,
                        })

                }else{
                  wx.showToast({
                      title: '服务器错误',
                      icon: 'error',
                      duration: 2000
                    })
                }
  
            },
            complete: (res) =>{
            //   wx.hideLoading();
            },
            fail: (res) => {
                wx.showToast({
                  title: '服务器错误',
                  icon: 'error',
                  duration: 2000
                })
            }
          
  
          })
    },

})