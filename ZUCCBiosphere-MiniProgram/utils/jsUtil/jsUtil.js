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
    // app.globalData.
      context.onMessage(data) //这里定义一个onMessage方法，用于每个页面的回调
    })
   }
   /**
 * 格式化日期为指定格式
 * @param {Date} date 日期对象
 * @param {string} fmt 格式字符串，例如：'YYYY-MM-DD HH:mm:ss'
 * @returns {string} 格式化后的日期字符串
 */
function formatDate(date, fmt) {
    let o = {
      "M+": date.getMonth() + 1,
      "d+": date.getDate(),
      "H+": date.getHours(),
      "m+": date.getMinutes(),
      "s+": date.getSeconds(),
      "q+": Math.floor((date.getMonth() + 3) / 3),
      "S": date.getMilliseconds()
    };
    if (/(Y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (let k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
    }
    return fmt;
  }
  
  /**
   * 获取当前日期的字符串形式
   * @returns {string} 当前日期的字符串形式，例如：'2022-03-28'
   */
  function getCurrentDate() {
    let now = new Date();
    return formatDate(now, 'YYYY-MM-dd');
  }
  
  /**
   * 根据目标日期和当前日期的差值返回不同的格式化字符串
   * @param {string|Date} targetDate 目标日期，可以是字符串或Date类型
   * @returns {string} 格式化后的日期字符串
   */
  function formatDateByDiff(targetDate) {
    // 如果传入的是字符串，转换为Date类型
    if (typeof targetDate === 'string') {
      targetDate = new Date(targetDate);
    }
  
    let currentDate = new Date();
    let targetYear = targetDate.getFullYear();
    let currentYear = currentDate.getFullYear();
  
    if (targetYear < currentYear) {
      // 目标日期早于今年，返回YYYY-MM-DD格式
      return formatDate(targetDate, 'YYYY-MM-dd');
    } else if (targetYear === currentYear && targetDate.getMonth() < currentDate.getMonth()) {
      // 目标日期等于今年但早于当前月份，返回YYYY-MM-DD格式
      return formatDate(targetDate, 'MM-dd');
    } else if (targetDate.getDate() === currentDate.getDate() && targetDate.getMonth() === currentDate.getMonth() && targetYear === currentYear) {
      // 目标日期等于今天，返回HH:mm格式
      return formatDate(targetDate, 'HH:mm');
    } else {
      // 其它情况返回MM-DD格式
      return formatDate(targetDate, 'MM-dd');
    }
  }

   const formatDate2 = function (date, matter) {
    let year = date.getFullYear().toString();
    let month = (date.getMonth() + 1).toString();
    month = (month.length > 1) ? month : ('0' + month);
    let day = date.getDate().toString();
    day = (day.length > 1) ? day : ('0' + day);
    let hours = date.getHours().toString();
    hours = (hours.length > 1) ? hours : ('0' + hours);
    let minutes = date.getMinutes().toString();
    minutes = (minutes.length > 1) ? minutes : ('0' + minutes);
    let seconds = date.getSeconds().toString();
    seconds = (seconds.length > 1) ? seconds : ('0' + seconds);
    let retVal = matter;
    if (matter.indexOf('yyyy') >= 0) {
      retVal = retVal.replace('yyyy', year);
    } else if (matter.indexOf('YYYY') >= 0) {
      retVal = retVal.replace('YYYY', year);
    } else if (matter.indexOf('yy') >= 0) {
      retVal = retVal.replace('yy', year.substring(2));
    } else if (matter.indexOf('YY') >= 0) {
      retVal = retVal.replace('YY', year.substring(2));
    }
  
  
    if (matter.indexOf('mm') > 0) {
      retVal = retVal.replace('mm', month);
    } else if (matter.indexOf('MM') > 0) {
      retVal = retVal.replace('MM', month);
    }
  
  
    if (matter.indexOf('dd') > 0) {
      retVal = retVal.replace('dd', day);
    } else if (matter.indexOf('DD') > 0) {
      retVal = retVal.replace('DD', day);
    }
  
  
    if (matter.indexOf('hh') > 0) {
      retVal = retVal.replace('hh', hours);
    } else if (matter.indexOf('HH') > 0) {
      retVal = retVal.replace('HH', hours);
    }
  
  
    if (matter.indexOf('mi') > 0) {
      retVal = retVal.replace('mi', minutes);
    } else if (matter.indexOf('MI') > 0) {
      retVal = retVal.replace('MI', minutes);
    }
  
  
    if (matter.indexOf('ss') > 0) {
      retVal = retVal.replace('ss', seconds);
    } else if (matter.indexOf('SS') > 0) {
      retVal = retVal.replace('SS', seconds);
    }
    return retVal;
  }
  module.exports = {
    throttle: throttle,
    // sendMessage: sendMessage, 
    // sendChatMessage: sendChatMessage, 
    resiverMessage: resiverMessage,
    formatDate2: formatDate2,
    formatDateByDiff: formatDateByDiff,
  }