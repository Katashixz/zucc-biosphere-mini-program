// pages/commuityHome/communityHome.js
const app = getApp()
var startY, endY;
var moveFlag = true; // 判断执行滑动事件

Page({

    /**
     * 页面的初始数据
     */
    data: {
        curPage: 1,
        pageSize: 3,
        releasePostStyle: "",
        tenHotPosts: [
            {
                postID: 1,
                content: "e"
            },
            {
                postID: 2,
                content: "ee"
            },
        ],
        postList: [
            // {
            //     state: false,
            //     click: false,
            //     postID: 1,
            //     userName: "yoxi",
            //     userID: 1,
            //     avaratUrl: "",
            //     content: "789633",
            //     postDate: "2022年8月19日16:44:33",
            //     postImage: [],
            //     commentNum: 0,
            //     postlikeNum: 0,
            //     postisTop: false,
            //     postisEssential: true,
            //     postIsLiked: false,
            //     postTheme: "啊啊啊"
            // },
            // {
            //     state: false,
            //     click: false,
            //     postID: 2,
            //     userName: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
            //     userID: 2,
            //     avaratUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     content: "ee",
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
            //     commentNum: 1,
            //     postlikeNum: 10,
            //     postisTop: false,
            //     postisEssential: true,
            //     postIsLiked: true,
            //     postisStared: false
            // },
            // {
            //     state: false,
            //     click: false,
            //     postID: 3,
            //     userName: "啊啊啊",
            //     userID: 3,
            //     avaratUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
            //     content: "ee",
            //     postTheme: "突击",
            //     postDate: "2022年8月19日17:45:53",
            //     // postImage: [
            //     //     {
            //     //         imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
            //     //     },
            //     //    {
            //     //     imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
            //     //    }
            //     // ],
            //     imageUrlList:[
            //         "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg",
            //         "https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview2.jpg"
            //     ],
            //     commentNum: 3,
            //     postlikeNum: 10,
            //     postisTop: false,
            //     postisEssential: true,
            //     postIsLiked: true,
            //     postisStared: false
            // }
            
        ],
        isLoadAllPosts: false,
        aniTime: false,
        releasePostAni: false,
    },
    //   查看图片
    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = this.data.postList[idx].imageUrlList
        // console.log(images)
        let toImg = []
        images.forEach(element => {
            toImg.push(element)
        });
        wx.previewImage({
            current: toImg[idx2],  //当前预览的图片
            urls: toImg,  //所有要预览的图片
        })
    },

    
    
    //跳转到帖子详情页面
    toPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.postList[e.currentTarget.dataset.index].postID
        })
    },
    //跳转到热帖详情页面
    toHotPost: function (e) {
        wx.navigateTo({
            url: '/pages/postDetails/postDetails?postID=' + this.data.tenHotPosts[e.currentTarget.dataset.index].postID
        })
    },
    /**
     * 搜索页面
     */
    toSearch: function (e) {
        wx.navigateTo({
            url: '/pages/searchPage/searchPage'
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
     * 点赞功能
     */
    changeLike: function (e) {
        var that = this;
        //先判断是否登录 登录后才能点赞
        if(!app.globalData.hasUserInfo){
            app.getUserProfile().finally(() => {
                that.onPullDownRefresh();
            })
        }else{
            var index = e.currentTarget.dataset.index;
            var isLiked = that.data.postList[index].postIsLiked;
            that.setData({
                [`postList[${index}].postIsLiked`]: !isLiked,
            })

        }

        
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
        console.log(app.globalData)
        const post = that.data.postList[e.currentTarget.dataset.index]
        if (app.globalData.hasUserInfo) {
            if (app.globalData.userInfo.id == post.userID) {
                wx.showToast({
                    title: '不能给自己打赏',
                    icon: 'error',
                })
            } else {
                this.userModal.open();
            }

        } else {
            app.getUserProfile().finally(() => {
                that.onPullDownRefresh();
            })
        }
    },
    /**
     * 更多选择的动画
     */
    moreOptions: function(e){
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
     * 分享
     */
    share(options) {
        console.log("分享")

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

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        this.userModal = this.selectComponent("#userModal");
        // this.userModal.close();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        var that = this;
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
            postList: []
        })
        var pageSize = that.data.pageSize;
        that.loadPost(1,pageSize);
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
        wx.navigateTo({
            url: '/pages/releasePost/releasePost'

        })
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
        wx.showLoading({
          title: '加载中',
        })
        var url = app.globalData.urlHome + '/community/exposure/loadPostList?curPage=' + curPage + '&pageSize=' + pageSize;
        if(app.globalData.hasUserInfo){
            url = url + '&userID=' + app.globalData.userInfo.id;
        }
        wx.request({
            url: url,
            method:"GET",
            success: (res) => {
                if(res.data.code == 200){
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
              wx.hideLoading();
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