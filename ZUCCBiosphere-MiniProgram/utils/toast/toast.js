// toast.js
/**
 * 显示toast 默认3000ms
 */
function showToast(page, toastText, count, isShowMask) {

  // toast时间  
  count = parseInt(count) ? parseInt(count) : 3000;

  page.setData({
    //设置toast时间，toast内容  
    count: count,
    toastText: toastText,
    // 显示toast  
    isShowToast: true,
    isShowMask: isShowMask,
  });

  // 定时器关闭  
  setTimeout(function () {
    page.setData({
      isShowToast: false,
      isShowMask: false
    });
  }, count);
}

/**
 * 传图片
 */
function showToastWithImage(page, toastText, toastText2, count, isShowMask, image) {

    // toast时间  
    count = parseInt(count) ? parseInt(count) : 3000;
  
    page.setData({
      //设置toast时间，toast内容  
      count: count,
      toastText: toastText,
      toastText2: toastText2,
      // 显示toast  
      isShowToast: true,
      isShowMask: isShowMask,
      image: image
    });
  
    // 定时器关闭  
    setTimeout(function () {
      page.setData({
        isShowToast: false,
        isShowMask: false
      });
    }, count);
  }
/**
 * 显示toast 默认1500ms 
 */
function showToastDefault(page, toastText) {
  this.showToast(page, toastText, 1500, false);
}

/**
 * 全屏不可点击 显示toast 默认2000ms 
 */
function showToastWithMask(page, toastText) {
  this.showToast(page, toastText, 2000, true);
}
function showToastWithMaskAndImage(page, toastText, toastText2, image) {
    this.showToastWithImage(page, toastText, toastText2,2000, true, image);
  }

module.exports = {
  showToast: showToast,
  showToastDefault: showToastDefault,
  showToastWithMask: showToastWithMask,
  showToastWithMaskAndImage: showToastWithMaskAndImage,
  showToastWithImage: showToastWithImage
}