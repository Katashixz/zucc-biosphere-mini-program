package com.biosphere.usermodule.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机形容词+动物名生成器
 * @Author Katashixz
 * @Date 2022/12/12 15:52
 * @Version 1.0
 */
public class RandomNameGenerator {
    public static String getAvatar(){
        final String[] url = {
                "https://bkimg.cdn.bcebos.com/pic/6159252dd42a2834349b37095de3deea15ce36d31121?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5",
                "https://bkimg.cdn.bcebos.com/pic/f11f3a292df5e0fe5c047433576034a85edf72e0?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5",
                "https://bkimg.cdn.bcebos.com/pic/a1ec08fa513d2697985550cb51fbb2fb4216d8a6?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UxNTA=,g_7,xp_5,yp_5",
                "http://i1.bagong.cn/02/1b/29b0a92022d02be700da1a77e53c_640x.jpg",

        };
        Random r = new Random();
        String res = url[r.nextInt(url.length)];

        return res;
    }

    public static List<String> getWholeName(int cnt){
        List<String> res = new ArrayList<>(cnt);
        // 进行拼接
        List<String> adjList = getAdj(cnt);
        List<String> nameList = getAnimalName(cnt);
        for (int i = 0; i < cnt; i++) {
            res.add(adjList.get(i) + nameList.get(i));
        }
        return res;
    }

    public static List<String> getAnimalName(int cnt){
        final String[] animalName = {
                "野猫", "宠物狗", "狼狗", "猎狗", "狼犬", "导盲犬", "猫科动物", "斗牛犬", "苏格兰牧羊犬", "狮子狗",
                "野生大熊猫", "杜宾犬", "萨摩耶犬", "折耳猫", "犬科动物", "萨摩犬", "牛头犬", "柯基犬", "小鹿犬", "喜乐蒂牧羊犬",
                "长毛猫", "猎狐犬", "冠毛犬", "苏格兰折耳猫", "纽芬兰犬", "威尔士柯基犬", "猫爪草", "布偶猫", "英国斗牛犬", "枪猎犬",
                "中国猫", "法国斗牛犬", "中国冠毛犬", "指狗", "英国可卡犬", "盆景猫", "短尾猫", "非洲野猫", "猫眼草", "日本犬",
                "指示犬", "指示猎犬", "喜马拉雅猫", "赴台大熊猫", "萨摩狗", "迷你杜宾犬", "皇家狗粮", "米格鲁猎犬", "加拿大无毛猫", "卷毛寻回犬",
                "薮猫", "荒漠猫", "苏俄牧羊犬", "日本秋田犬", "袖珍狗", "亚成体大熊猫", "日本柴犬", "斗牛獒犬", "德国犬", "非纯种猫",
                "罗特韦尔犬", "熊猫犬", "罗维纳犬", "奥达猎犬", "野生龙猫", "猎鹿犬", "猎枪犬", "奶牛猫", "新加坡猫", "杜高犬",
                "挪威森林猫", "纯种博美犬", "约克郡犬", "墨西哥无毛犬", "珍岛犬", "杜伯文犬", "袖狗", "米格鲁犬", "柯尼斯卷毛猫", "小斗牛犬",
                "狗仔鲸", "台湾犬", "猫头鹰蝶", "小灵狗", "英国猎狐犬", "萨路基犬", "日本池英犬", "猫亚科", "马林诺斯犬", "黑褐猎浣熊犬",
                "雪鞋猫", "波士顿梗犬", "阿根廷杜高犬", "欧洲短毛猫", "多伯曼犬", "古典牧羊犬", "米格鲁猎兔犬", "非洲野犬", "珍珠狗", "史毕诺犬",
                "维兹拉犬", "萨摩耶狗", "特种警犬", "田野猎犬", "猫猫向前冲", "猫猫兔", "中华田园犬", "猫尾木", "印度猫", "克隆缉毒犬",
                "格里芬凡丁犬", "猎枪犬种", "特弗伦犬", "苏格兰猎鹿犬", "德文卷毛猫", "希姆利克猫", "狗狗向前冲", "欧西猫", "四川简州猫", "苏格兰猫",
                "纽波利顿犬", "魏玛犬", "马雷马牧羊犬", "约瑟犬", "达尔马提亚狗", "短毛家猫", "阿富汗猎狗", "山寨熊猫", "猎狐梗犬", "爪哇猫",
                "猎狐狗", "拿波里獒犬", "舒伯齐犬", "萨卢基犬", "曼岛无尾猫", "挪威猎鹿犬", "勒车犬", "小型雪纳瑞犬", "欧洲野猫", "萨路基猎犬",
                "英国指示猎犬", "科克犬", "挪威猫", "纯种比熊犬", "沙特尔猫", "挪威猎糜犬", "鹰叭犬", "中国斗犬", "杜尔基犬", "纯种斗牛犬",
                "杜宾犬", "中华犬", "魏玛猎犬", "越南猫", "比利牛斯獒犬", "周末小狗", "美国硬毛猫", "喜乐帝犬", "熊猫牛", "约克夏梗犬",
                "谷仓猫头鹰", "大熊猫细胞库", "茶杯犬", "维希拉猎犬", "九耳犬", "巴厘岛猫", "西班牙指示犬", "韦尔斯猫", "侏儒猫头鹰",
                "双头小猫", "纽波利顿獒犬", "斑点猫头鹰", "广东潮州犬", "纯种喜乐蒂犬", "小猎兔狗", "四耳猫", "日本短毛猫", "猫型亚目", "英国长毛猫",
                "武汉猫狮", "呜呜", "英国寻血猎犬", "┭┮﹏┭┮", "珍珠狗头", "犬羚", "猫坚强", "英国大猫", "长毛暹罗猫", "冰岛牧羊犬",
                "希腊猎犬", "大熊猫泉泉", "维那犬", "欧洲缅甸猫", "短毛细犬", "大比利牛斯犬", "北京大熊猫馆", "牛猫加菲", "小齿灵猫", "英国第一猫",
                "小猫虎子", "基里奥犬", "中华田园猫", "小斑猫", "熊猫异形", "卢犬", "茶杯贵宾犬", "云纹猫鲨", "重点色短毛猫", "多伯曼平犬",
                "不列颠猫", "莫斯科护卫犬", "猫鲨科", "英国栗色猫", "法国斗牛狗", "狗绦虫", "美国卷毛猫", "墨西哥无毛狗", "犬型亚目", "挪威猎犬",
                "牡丹犬", "四国犬", "印度野狗", "莫罗苏斯犬", "台湾土犬", "戈登蹲猎犬", "高地折耳猫", "斯芬克司猫", "中国大白猫", "芬兰丝毛犬",
                "粉白龙猫", "德国宾莎犬", "澳大利亚犬", "意大利灰狗", "牛头獒犬", "苏格兰弯耳猫", "截尾犬", "扭玻利顿犬", "瑞典柯基犬", "夜光猫",
                "青川犬", "忠犬八千", "长尾龙猫", "沙特尔蓝猫", "平毛巡回猎犬", "斜纹猫蛛", "小齿椰子猫", "曼赤肯猫", "克里小猎犬", "蒙面狗",
                "非纯种长毛猫", "欧亚犬", "伊维萨猎犬", "小耳犬", "蓝战狗", "日本丝毛犬", "兰开夏跟脚犬", "猫头鹰环蝶", "希腊牧羊犬", "日本盆景猫",
                "小叶猫", "曼切堪猫", "白宫第一狗", "猫毛囊炎", "瑜伽猫", "英国长毛猎犬", "不懂", "小小彩蛋", "汉诺威猎犬", "贝林登梗犬",
                "普德尔指示犬", "印加无毛犬", "四川青川犬", "灯芯绒贵妇犬", "讨饭猫", "戈登长毛猎犬", "拉普赫德犬", "瑞典农场犬", "奈贝长毛猫", "什么",
                "小熊猫亚科", "猫咪摄影师", "田野小猎犬", "平滑毛觅拾犬", "兰开夏赫勒犬", "长卷毛猎犬", "布鲁塞尔小狗", "波旁指示犬", "海豹重点色猫", "越人道上遇狗",
                "萨卢基猎犬", "印度小猫鼬", "罗威纳犬舍", "澳大利亚梗犬", "德国丝毛犬", "德国魏玛犬", "奇努克犬", "绿眼白色猫", "啊哈", "德联牧战犬",
                "中华古猫", "木炭色短毛猫", "羊肉", "夏尔特流猫", "索诺拉虎猫", "齿突猫萤", "日本无尾猫", "小猫蛛", "玻利维亚虎猫", "牛犬鼠",
                "新澳海狗", "胖花猫宝螺", "花尾猫鲛", "德国卷毛猫", "胖虎猫", "非洲狮子猫", "猎狗芋螺", "阿依努犬", "暗灰黑色猫", "非洲椰子猫",
                "短脚长耳犬", "英国田野猎犬", "席勒猎犬", "猫面鹰", "金眼萨摩犬", "金毛犬末末", "小鸟", "励志狗", "蒜皮猫芋螺", "澳大利亚雾猫",
                "玻璃花猫狮", "波萨维茨猎犬", "小瑞士猎犬", "爱尔兰萨特狗", "西藏长耳猎犬", "马来犬吻蝠属", "苏哈犬", "考德阿古阿犬", "诺夫犬齿兽属", "坎德奎芮瑙犬",
                "豹斑硬皮猫鲨", "猫目大蚕蛾", "瑞典拉普杭犬", "红牛头犬蚁", "犬牙帆花鮨", "奈德劳佛犬", "罗德西亚背犬", "千年义犬", "泥里狗子", "韦特豪犬",
                "褐圆吻猫鲨", "诺里奇犭更犬", "硬毛维兹拉犬", "蓝眼豹纹狗头", "黄斑猫鲨", "科摩罗猫鲨", "康沃尔帝王猫", "德国绒毛狼犬", "毛毛狗头", "小型施诺泽犬",
                "戈耶特犬", "不等齿突猫萤", "大熊猫喜兰", "冈底斯山猫", "墨西哥秃耳犬", "禄丰始猫熊", "美洲猎狐犬", "杂种长毛猫", "非洲野犬属", "迷你型品彻犬",
                "科门德耳犬", "科尔索犬", "德国寻血猎犬", "硬毛猎狐犬", "隆德杭犬", "中国长毛狼犬", "加氏犬浣熊", "猫眼尺蛾", "马略卡牧羊犬", "uu",
                "忠狗德拉姆", "奥地利平犬", "猫科动物简介", "大格林芬犬", "小型苏格兰犬", "狗肝菜属", "日本田园猫", "蛋黄巧克力猫", "拉品坡考亚犬", "猫爪牡蛎",
                "犬齿龙附目", "斗牛马士刺犬", "纸质猫砂", "奥弗涅指示犬", "卡迪博斗犬", "科达布犬", "个个", "没词了", "就就",
                "斑点圆吻猫鲨", "蒙古国细狗", "拟犬同盘吸虫", "庇里牛斯大狗", "比利时料诺犬", "猫斑环纹蝶", "英国雷克斯猫", "小小牛犬", "树丛小犬", "小狗",
                "波伦亚伴随犬", "德国灰犬", "因纽特犬", "红战狗深红型", "新斯路猎鸭犬", "卷毛指示犬", "发发", "花猫卷管螺", "河北细犬", "撒莫依犬",
                "圆斑长须猫鲨", "啊啊啊", "哼哼", "加泰隆牧羊犬", "中华奎木犬", "威廉山猫宝螺", "异种短毛猫", "法国黄白猎犬", "是的", "的的",
                "刺猫爪蛤", "满卢犬", "波美莫亚犬", "小犬魮", "中国狮子狗", "美洲猎鹿犬", "卢斯纳劳佛犬", "加斯科大猎犬", "犬齿珊瑚属", "比利时牧牛犬",
                "哥伦比亚虎猫", "新疆猫蛛", "四国猎犬", "犬鸟虱", "喜马拉雅种猫", "梢迁狗", "萨鲁奇猎犬", "曼彻斯特更犬", "哈瓦那小犬", "柯氏犬羚",
                "薮犬亚科", "赤犬吻蝠", "长岛长尾狸猫", "大力金刚狗", "猫舌海菊蛤", "英法小猎犬", "北大校猫", "广西笔尾灰犬", "小多伯曼狗", "苏格兰猎鹿狗",
                "影子基因犬", "塞伦盖蒂猫", "犬猫莱姆病", "库里瑞短尾猫", "真兽小灵猫", "湖北箭毛猎犬", "犬首鮈", "原犬鳄龙科", "猫眼蝾螺", "杜父石狗公",
                "斗牛犬蚁", "小古猫属", "原小熊猫", "夏多流猫", "瑞木颇灵缇犬", "茶杯猫", "凯斯荷德犬", "小型品舍狗", "猫卷叶蛛", "枯木猫",
                "欧亚大陆犬", "腊肠猫", "挪威黑猎鹿犬", "恩特布山地犬", "豹猫守宫", "通古尔中鬣狗", "缅甸蜜蜂猫", "盖伦", "法英普通猎犬",
                "比熊约克犬", "中国鸡冠犬", "额", "库达犬", "西藏蝇犬", "塞蒙地犬", "英国茛犬", "莫罗索犬", "英特布彻山犬", "希姆魮猫",
                "趋魔犬", "卵叶似猫乳", "斯恰潘道斯犬", "伏地魔猫", "没词了", "不知道啊", "咕咕是只猫", "猫舌樱蛤", "中国青猫鸽", "斯洛伐克猎犬",
                "犬齿兽次亚目", "查默斯王犬", "绿眼白猫", "德国莱克斯猫", "好的", "棕簑猫", "小耳犬属", "恐犬类动物", "猫形食肉类", "艾努犬",
                "饿", "鲍温猫爪守宫", "北美环尾猫熊", "夏约克猎犬", "帕特大勒梗犬", "秏鸡猫儿", "佩狄芬犬", "加勒比海虎猫", "笑笑萌猫", "中国冠毛犬犬",
                "哼哼", "爪哇猫蛛", "峇厘猫", "犬齿赤莲", "哈瓦那褐猫", "的", "查默斯王猎犬", "塞巴英努犬", "小瑞士犬", "小型犬浣熊",
                "中华男儿", "哈哈哈哈哈", "114514", "秘鲁无毛犬", "喜马拉雅野犬", "一棵树"
        };
        // 用随机数读取数组，生成后半部分的名字
        Random r = new Random();
        List<String> animalNameList = new ArrayList<>(cnt);
        int len = animalName.length;
        int arrSize = animalNameList.size();
        String temp = "";
        while (arrSize < cnt) {
            temp = animalName[r.nextInt(len)];
            animalNameList.add(temp);
            arrSize = animalNameList.size();
        }
        return animalNameList;
    }

    public static List<String> getAdj(int cnt){
        final String[] adj = {
                "暖洋洋的", "醉醺醺的", "香喷喷的", "沉甸甸的", "羞答答的", "亮晶晶的", "沉甸甸的", "白花花的", "绿油油的",
                "黑黝黝的", "慢腾腾的", "阴森森的", "皱巴巴的", "亮铮铮的", "笑嘻嘻的", "香喷喷的", "乱哄哄的", "黑漆漆的", "轻飘飘的",
                "湿漉漉的", "红彤彤的", "骨碌碌的", "雾朦朦的", "喜盈盈的", "亮晶晶的", "黄灿灿的", "孤零零的", "毛绒绒的", "胖乎乎的",
                "绿油油的", "黄澄澄的", "红彤彤的", "光闪闪的", "油乎乎的", "光溜溜的", "黑油油的", "粘糊糊的", "亮晶晶的", "脏兮兮的",
                "皱巴巴的", "干巴巴的", "松塌塌的", "胖墩墩的", "肉墩墩的", "瘦巴巴的", "瘦嶙嶙的", "喜洋洋的", "喜滋滋的", "喜冲冲的",
                "兴冲冲的", "乐悠悠的", "乐陶陶的", "乐滋滋的", "吃饱了的", "可恨的", "可爱的", "生气的", "开心的", "胖胖的",
                "瘦瘦的", "大眼睛的", "蠢蠢的", "爱卖萌的", "高兴的", "爽快的", "大方的", "舒畅的", "晒太阳的", "认真的",
                "专注的", "大方的", "年轻的", "聪明的", "雪白的", "漂亮的", "平等的", "优秀的", "慌张的", "俗气的",
                "马虎的", "博学的", "明快的", "高兴的", "幸福的", "结实的", "具体的", "伟大的", "勇敢的", "坚强的",
                "温柔的", "平淡的", "简单的", "固执的", "醒目的", "干净的", "傲慢的", "倔强的", "脆弱的", "乐观的",
                "爽朗的", "豪放的", "开朗的", "爱笑的", "娇柔的", "友好的", "活泼的", "昂贵的", "孤独的", "好动的",
                "愉快的", "热情的", "可亲的", "健谈的", "轻松的", "机敏的", "外向的", "兴奋的", "强烈的", "率直的",
                "善良的", "文雅的", "整洁的", "内向的", "沉静的", "稳重的", "顺从的", "温和的", "老实的", "沉著的",
                "和平的", "体贴的", "忠诚的", "知足的", "果断的", "善变的", "细节的", "保守的", "忠心的", "自信的",
                "独立的", "不凡的", "悠然的", "从容的", "迷人的", "淡定的", "海涵的", "洋气的", "高雅的", "风度的",
                "随和的", "在打游戏的", "潇洒的", "宽容的", "迷茫的", "困惑的", "乏困的", "疲倦的",
        };
        // 用随机数读取数组，生成前半部分的形容词
        Random r2 = new Random();
        List<String> adjList = new ArrayList<>(cnt);
        int len = adj.length;
        int arrSize = adjList.size();
        String temp = "";
        while (arrSize < cnt) {
            temp = adj[r2.nextInt(len)];
            adjList.add(temp);
            arrSize = adjList.size();
        }
        return adjList;
    }

}