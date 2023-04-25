// pages/loginPage/loginPage.js
const utilMd5 = require('../../utils/md5/md5.js');
const app = getApp()
const util = require('../../utils/jsUtil/jsUtil')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    accountInput: "",
    pwdInput: "",
  },
  /**
   * 更新账号
   */
  changeAccount(e){
    var that = this;
    that.setData({
      accountInput: e.detail.value
    })
  },
  /**
   * 更新密码
   */
  changePwd(e){
    var that = this;
    that.setData({
      pwdInput: e.detail.value
    })
  },
  /**
   * 登录验证
   */
  login: util.throttle(function (e){
    var that = this;
    
    if(that.data.accountInput == ''){
      wx.showToast({
        title: '请输入账号',
        icon: 'error',
        duration: 1500
      })
    }
    else if(that.data.pwdInput == ''){
      wx.showToast({
        title: '请输入密码',
        icon: 'error',
        duration: 1500
      })
    }
    else{
      wx.showLoading({
        title: '加载中',
      })
      that.loginReq().then((res) => {
        wx.hideLoading();

        wx.showToast({
          title: '登录成功',
          icon: 'success',
          duration: 1500
        })
        
        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/mainPage/mainPage',
          })
        }, 1600)
      }).catch((error) =>{
        wx.hideLoading();
        wx.showToast({
          title: error.data.msg,
          icon: 'error',
          duration: 1500
        })
      })
      
    }
  },1500),

  /**
   * 登录请求
   */
  loginReq(e){
    var that = this;
    return new Promise((resolve, reject) => {
      var password = utilMd5.hexMD5(that.data.pwdInput); 
      var url = app.globalData.urlHome + '/admin/exposure/login?account=' + that.data.accountInput + '&pwd=' + password;
        wx.request({
            method: 'GET',
            url: url,
            success: (res)=>{
                if(res.data.code == 200){
                    app.globalData.token = res.data.data.token;
                    resolve(res)
                }else{
                    reject(res)
                }
            },
            fail: (res)=>{
                reject(res)
            },
        })
  })
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