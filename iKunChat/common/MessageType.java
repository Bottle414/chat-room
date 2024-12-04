package common;

public interface MessageType {
    String noUser = "NU";// 用户未注册
    String loginSuccess = "LT";
    String loginFail = "LF";// 登陆失败返回值
    String changeRequest = "CR";// 修改密码请求
    String changeSuccess = "CS";// 修改密码成功
    String registerRequest = "RR";// 请求注册
    String registerSuccess = "RT";// 注册成功
    String registerFail = "RF";// 注册失败
    String headRequest = "HR";// 头像修改请求
    String headSuccess = "HS";// 头像修改成功
    String partnerRequest = "PR";// 请求返回好友
    String partnerSuccess = "PS";// 好友获取成功
    String commonMessAll = "0";// 普通文字消息-群发
    String commonMessOne = "1";// 普通文字消息-单发
    String fileMessAll = "2";// 文件消息-群发
    String fileMessOne = "3";// 文件消息-单发
    String imageOne = "4";// 图片消息-私发
    String imageAll = "5";// 图片消息-群发
    String emojiOne = "6";// emoji-群发
    String emojiAll = "7";// emoji-私发
    String getFriend = "GF";// 请求获得在线好友
    String retFriend = "RF";// 返回在线好友
    String addFriend = "AF";// 修改好友
    String friendSuccess = "AS";// 修改好友成功
    String beRemove = "BF";//被别人修改了好友
    String friendFail = "FF";// 修改失败
    String online = "OL";// 有人上线了
    String Exit = "-1";
}
