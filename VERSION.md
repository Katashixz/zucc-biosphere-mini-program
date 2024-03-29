# 版本更新说明
## v0.1.0 (2022-09-23)
  - 完成了百科模块的主界面和数据加载，可以正常浏览
  - 完成了社区模块的主界面和数据加载，可以浏览贴子、发帖、查看热帖、热帖滚动，支持图片格式、视频格式上传，但是还不能评论和点赞
  - 完成了个人信息模块的主界面，登录以及每日签到展示
---
## v0.1.2 (2022-09-26)
  - 更新社区帖子的点赞动画以及后端逻辑实现
  - 修复视频播放时会出现的一些bug
  - 修复近段时间没热帖时前后端的报错
---
## v0.2.1 (2022-10-10)
  - 更新了百科模块的搜索功能
  - 更新了社区模块的搜索功能
  - 新增了自定义提示框，部分提示改用新提示框
  - 实现了帖子的评论功能
  - 实现了帖子的正常打赏功能，但是打赏完个人资料部分需要刷新
  - 实现了点赞、评论、发帖、搜索、打赏按钮节流
  - 评论和发帖等加入了敏感词检测
---
## v0.3.1 (2022-10-19)
  - 实现了社区模块的收藏功能、按钮节流
  - 部分页面css样式调整
  - 优化了帖子的分页加载，更加迅速合理
  - 修复了搜索未缓存帖子数据时报错的问题
  - 新增了我的发帖页面和功能
  - 调整了部分缓存的加载机制
---
## v0.4.1 (2022-10-28)
  - 调整了登录信息过期、失效之后的返回提示，统一封装了返回方法。
  - 调整了部分缓存的存储、加载机制
  - 新增了我的收藏页面和功能
  - 实现了删除收藏、删除帖子的功能，帖子删除操作使其数据库相关字段标记为1，并在固定时间点统一删除
  - 更新了域名
  - 修复了数据库引擎问题导致事务不生效的BUG
---
## v0.4.2 (2023-1-4)
  - 实现了我的评论页面和功能，可以删除评论，评论删除操作使其数据库相关字段标记为1，并在固定时间点统一删除
  - 各个删除功能新增了确认机制，防止误操作
  - 实现了通知页面的前端部分
  - 实现了关于我们页面
  - 实现了社区守则页面
  - 因小程序头像昵称获取规则调整通知，设计实现了头像名称修改的功能
  - 因小程序头像昵称获取规则调整通知，设计实现了初次注册随机生成名称和随机获取图像的功能
  - 因小程序头像昵称获取规则调整通知，重写了前端登录逻辑
  - 后端优化全局统一返回格式与全局异常处理
  - 后端优化自定义参数注解校验
  - 修改了底部导航栏样式
  - 修复了评论0回复的帖子不能及时更新状态的BUG
  - 修复了我的评论模块中不显示视频的BUG
  - 修复了我的评论模块中图片显示的BUG
  - 修复了删除评论后帖子评论总数未更新的BUG
  - 修复了评论过的帖子删帖之后，我的评论里帖子显示null且无法删除评论的BUG
  - 修复了可以利用自己的登录状态获取他人评论信息等的BUG
---
## v0.5.1 (2023-3-27)
  - 修复了头像名称修改功能上传头像必须点击一次，否则无法获取文件的BUG
  - 修复了搜索栏点击一次也会提示频繁操作的BUG
  - 实现了初步的消息通知系统
  - 实现了私聊功能
  - 修复了MQ异常导致的消费者死循环
  - 实现了商城的前端页面
  - 实现了领养日记主页的前端页面
---
## v0.5.2 (2023-3-28)
  - 实现了点赞通知以及进入点赞通知页面已读的操作
  - 实现了评论通知以及进入点赞通知页面已读的操作
  - 实现了充电通知以及进入点赞通知页面已读的操作
  - 实现了私聊组件，从社区主页或者帖子详情中点击用户头像就可以进行私聊
  - 实现了聊天通知以及进入聊天详情已读通知的操作
  - 实现了完整的消息通知以及已读系统
---
## v0.6.1 (2023-4-2)
  - 实现了领养主页显示，滑动查看流浪动物档案
  - 实现了云喂养功能，付费暂时以能量值为货币
  - 实现了今日状况显示
  - 实现了今日状况的发布，主动发布和通过云投食发布的显示不同
  - 实现了查看我的投食功能
  - 百科页面显示动物的领养状态
  - 实现了领养日记按日期显示
  - 实现了领养日记的发布
  - 实现了对帖子的举报框
---
## v0.6.2 (2023-4-7)
  - 实现了举报功能
  - 新增了置顶帖功能
  - 新增了登录询问弹窗代替微信接口

---
## v1.0.0 (2023-4-18)
  - 用户端正式版发布！除商城功能都能正常使用！
  - 新增了管理员前台的首页
  - 新增了管理员后台部分功能
  - 修复了webSocket连接断开后无法自动恢复连接的问题
  - 修复了领养主页显示除猫狗以外的动物
  - 修复百科页面有冗余的部分以及数据过多时的滑动问题
  - 领养日记新增私信组件
  - 修复了自定义头像上传失败的问题
  - 修复了评论数错误的问题
  - 修复了图片URL不变，内容变，但是客户端显示图片还是和原来一样的问题
  - 完善了RabbitMQ消费者中容易抛空指针的部分
  - 修复了热帖自动加载时取缓存报错的问题
  - 修复了上传大图后台报错的问题
  - 修复了WebSocket保持连接报错的问题
  - 修复了帖子详情图片错位的问题
  - 修复热帖中点赞数不显示的问题
  - 修复投食按钮样式的问题
  - 修复了修改名称失败的问题

---
# 待解决的BUG
1. ~~已知IOS部分机型用户发帖时id会为null~~
2. 在社区页面登录后会提示"[Component] <swiper>: current 属性无效,请修改 current 值"，已设置current默认为0并每次刷新页面都初始化，但仍然报错，可能是微信开发者工具的BUG