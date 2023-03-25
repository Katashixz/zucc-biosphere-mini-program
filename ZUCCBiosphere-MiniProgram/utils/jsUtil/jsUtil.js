const app = getApp()

//节流函数
function throttle(fn, gapTimes) {
    const gapTime = gapTimes || 1500
    let _lastTime = null
    // 返回新的函数
    return function () {
      let _nowTime = +new Date()
      if (_nowTime - _lastTime > gapTime || !_lastTime) {
        fn.apply(this, arguments) //将this和参数传给原函数
        _lastTime = _nowTime // 点击一次是会执行一次  但在这里进行了判断赋值 把现在时间给上一次时间  当下一次再点击时  现在时间减去上一次时间 如果大于设定间隔时间  才执行  fn()函数  传入的fn  才是我要执行的函数体
      }else{
        wx.showToast({
            title: '请勿频繁操作哦',
            duration: 1500,
            icon: "error"
          })
      }
    }
  }
//   function sendMessage(code, uid) {
    
//     var mCmd = { 
//         "code": code,
//         "userId": uid,
//         "time": new Date(),
//     }
//     wx.sendSocketMessage({
//       data: JSON.stringify(mCmd),
//       success:function(res){
//       }
//     })
//   }
//   function sendChatMessage(code, uid, target, msg) {
    
//     var mCmd = { 
//         "code": code,
//         "userId": uid,
//         "time": new Date(),
//         "target": target,
//         "content": msg
//     }
//     wx.sendSocketMessage({
//       data: JSON.stringify(mCmd),
//       success:function(res){
//       }
//     })
//   }
  function resiverMessage(context) {
    wx.onSocketMessage(function (data) {
        // console.log(data)
        
      context.onMessage(data) //这里定义一个onMessage方法，用于每个页面的回调
    })
   }
  module.exports = {
    throttle: throttle,
    // sendMessage: sendMessage, 
    // sendChatMessage: sendChatMessage, 
    resiverMessage: resiverMessage 
  }