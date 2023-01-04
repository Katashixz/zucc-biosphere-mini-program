// pages/updateInfo/updateInfo.js
const defaultAvatarUrl = "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0"
const util = require('../../utils/jsUtil/jsUtil')
const app = getApp()

Page({

    /**
     * 页面的初始数据
     */
    data: {
        avatarUrl: defaultAvatarUrl,
        inputLength: 170,
        input: "",
        },
    /**
     * 提交节流实现
     */
    sumbit: util.throttle(function (e) {
        var that = this;
        console.log(that.data);
        var url = app.globalData.urlHome + '/user/auth/updateInfo';
        wx.showLoading({
            title: '加载中',
          })
        wx.uploadFile({
            filePath: that.data.avatarUrl,
            name: 'file',
            url: url,
            header: {
                'token': app.globalData.token
            },
            formData:{
                'id': that.data.pageData.userID,
                'nickName': that.data.input,
            },
            success: (res) => {
                var data = JSON.parse(res.data);
                console.log(data)
                if(data.code == 200){
                    wx.showToast({
                        title: '更新成功',
                        duration: 2000,
                        icon: 'success',
                        })
                        setTimeout(function () {
                            wx.reLaunch({
                                url: '../myInfoHome/myInfoHome',
                            })
                        }, 2100)
                }else{
                    var obj = {
                        msg: data.msg,
                        type: "error"
                    }
                    that.promptBox.open(obj);
                }
            },
            fail: (res) => {
                console.log(res);
                wx.showToast({
                    title: '服务器错误',
                    duration: 2000,
                    icon: 'error'
                })
            },
            complete: (res) => {
                wx.hideLoading();
            }
            
        })

        // wx.navigateBack({
        //   delta: 0,
        // })
    }, 2500),
    /**
     * 动态输入框长度
     */
    changeWidth(e){
        var that = this;
        var value = e.detail.cursor * 34;
        
        console.log(e)
        if(value == 0){
            value = 170;
        }
        that.setData({
            inputLength: value,
            input: e.detail.value,
        })
    },
    /**
     * 改变头像
     */
    onChooseAvatar(e) {
        var that = this;
        const { avatarUrl } = e.detail 
        that.setData({
          avatarUrl,
        })
      },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        that.setData({
            pageData: options,
            avatarUrl: options.avatarUrl,
            input: options.nickName,
            inputLength: options.nickName.length * 34,
        })
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