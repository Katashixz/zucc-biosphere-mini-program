// app.js
const app = getApp()

App({
    globalData: {
        userInfo: null,
        // urlHome: 'http://121.40.227.132:8080/api',
        urlHome: 'http://localhost:9000',
        token: '',
        openID: '',
        hasUserInfo: false,
    },
})
