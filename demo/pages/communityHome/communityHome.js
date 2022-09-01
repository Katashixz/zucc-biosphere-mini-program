// pages/commuityHome/communityHome.js
const app = getApp()
var startY, endY;
var moveFlag = true; // 判断执行滑动事件

Page({

    /**
     * 页面的初始数据
     */
    data: {
        
        releasePostStyle: "",
        tenHotPosts: [
            {
                postID: 1,
                postContent: "e"
            },
            {
                postID: 2,
                postContent: "ee"
            },
        ],
        postList: [
            {
                state: false,
                click: false,
                postID: 1,
                userID: "yoxi",
                userOpenID: "123",
                userAvatarUrl: "",
                postContent: "789633",
                postDate: "2022年8月19日16:44:33",
                postImage: [],
                postcommentNum: 0,
                postlikeNum: 0,
                postisTop: false,
                postisEssential: true,
                postIsLiked: false,
                postTheme: "啊啊啊"
            },
            {
                state: false,
                click: false,
                postID: 2,
                userID: "啊啊啊aaaaaawcsa啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",
                userOpenID: "111",
                userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                postContent: "ee",
                postTheme: "突击",
                postDate: "2022年8月19日17:45:53",
                postImage: [
                    {
                        imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                    },
                   {
                    imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                   }
                ],
                postcommentNum: 1,
                postlikeNum: 10,
                postisTop: false,
                postisEssential: true,
                postIsLiked: true,
                postisStared: false
            },
            {
                state: false,
                click: false,
                postID: 3,
                userID: "啊啊啊",
                userOpenID: "111",
                userAvatarUrl: "https://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epdCtmKulyPGx60K2JfNGMJNa9ziakjw18puwz0fQ6ibsCl7RcmDxbpPcdq1oE99hJzAVKs7jwkLpVQ/132",
                postContent: "ee",
                postTheme: "突击",
                postDate: "2022年8月19日17:45:53",
                postImage: [
                    {
                        imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                    },
                   {
                    imageUrl:"https://zucc-1308480699.cos.ap-nanjing.myqcloud.com/preview1.jpg"
                   }
                ],
                postcommentNum: 3,
                postlikeNum: 10,
                postisTop: false,
                postisEssential: true,
                postIsLiked: true,
                postisStared: false
            }
            
        ],
        isLoadAllPosts: false,
        aniTime: false,
        curPage: 0,
        releasePostAni: false,
    },
    //   查看图片
    handleImagePreview(e) {
        const idx = e.target.dataset.idx
        const idx2 = e.target.dataset.idx2
        const images = this.data.postList[idx].postImage
        // console.log(images)
        let toImg = []
        images.forEach(element => {
            toImg.push(element.imageUrl)
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
            app.getUserProfile()
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

        const post = that.data.postList[e.currentTarget.dataset.index]
        if (app.globalData.hasUserInfo) {
            if (app.globalData.openid == post.userOpenID) {
                wx.showToast({
                    title: '不能给自己打赏',
                    icon: 'error',
                })
            } else {
                this.userModal.open();
            }

        } else {
            app.getUserProfile()
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
        console.log("onPullDownRefresh Start")
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
                that.setData({
                    isLoadAllPosts: true,
                })
                // wx.request({
                //   url: 'url',
                // }).then((res) => {
                //     // console.log(res)
                //     if (res["isAllLoad"]){
                //         that.setData({
                //             isLoadAllPosts: res["isAllLoad"],
                //         })
    
                //     }
                //     else{
                //         that.setData({
                //             postList: res["postList"],
                //             pullDownCnt: that.data.pullDownCnt + 1,
                //         })
                //     }
                    // resolve()
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

})