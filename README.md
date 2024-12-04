#项目介绍
    这是一个由java语言编写的多人聊天室。
    本项目已经上传至github:
    操作演示: 

#实现功能
    1.能实现多人聊天，群聊、一对多或者一对一的聊天。
    2.带有注册、登录、密码修改功能。
    3.支持用户数据记录，包括用户名、密码、好友、头像。
    4.支持发送上线提示、文字消息、emoji、图片、文件。
    5.能修改用户头像。

#使用说明
    本项目基于b站韩顺平老师和“程下吹霜”同学的代码。使用vs code在jdk-22环境下开发。
    1.首先运行ServerTest类,再运行ClientTest类。先注册，再登录。
    2.进入主菜单，单击头像可更换头像；单击“群聊”可进入群聊；单击“拉取在线人员”可得到在线人员。第一栏姓名为在线用户，第二栏为好友。单击人员姓名可进入聊天，
双击即可发送好友请求，直接添加对方为好友。
    3.进入聊天界面后，单击"emoji"按钮可选择emoji表情，单击“发送”按钮可发送emoji、文字信息；单击“发送文件”、“发送图片”可发送文件、图片。
    4.Client_Part\res\history文件夹存放了图片聊天记录。本项目暂不支持文字聊天消息记录。

#组织架构
D:.
│  ClientTest.java  #客户端入口
│  README           #说明文档
│  ServerTest.java  #服务器入口
│
├─.idea #idea连接文件
│      .gitignore
│      encodings.xml
│      misc.xml
│      modules.xml
│      uiDesigner.xml
│      workspace.xml
│
├─.vscode
│      settings.json
│
├─Client_Part
│  ├─lib
│  │      flatlaf-3.5.2.jar #java.swing皮肤包
│  │
│  ├─res
│  │  ├─fonts
│  │  ├─history #图片聊天记录
│  │  └─image   #图片资源
│  │          broad.png
│  │          exit.gif
│  │          head.jpg
│  │          head1.jpg
│  │          head10.jpg
│  │          head2.jpg
│  │          head3.jpg
│  │          head4.jpg
│  │          head5.jpg
│  │          head6.jpg
│  │          head7.jpg
│  │          head8.jpg
│  │          head9.jpg
│  │          login.gif
│  │          qq.gif
│  │          register.gif
│  │
│  └─src
│      │  GitHub.theme.json #皮肤配置文件
│      │  Gradianto_Nature_Green.theme.json
│      │
│      └─client
│          ├─tools  #客户端后台
│          │      ConnectThread.java    #与服务器的通信通道
│          │      FileSender.java   #文件、图片发送类
│          │      ImageInsert.java  #图片压缩类
│          │      MessSender.java   #文字、emoji发送类
│          │      ThreadManner.java #线程管理类
│          │      UserServe.java    #用户注册、登录类
│          │
│          └─ui #客户端界面
│                  Chat.java    #聊天界面
│                  Emoji.java   #emoji表情盘
│                  Forgot.java  #重置密码功能
│                  Friend.java  #好友界面，其实是主菜单
│                  Login.java   #登录界面
│                  Register.java    #注册界面
│
├─common    #公共类
│      Message.java #消息类
│      MessageType.java #消息类型枚举
│      MyObjectInputStream.java #java的ObjectStream有bug，通过这样处理
│      MyObjectOutputStream.java
│      User.java    #用户信息类
│
└─Server_Part
    └─src
        └─server
            ├─database  #配置文件，充当数据库
            │      UserData.properties
            │
            └─model
                    ClientsThread.java  #线程池，一切消息的中转
                    Server.java #登录、注册、管理数据库的类
                    ThreadManner.java   #线程管理类

#更新摘要
    2024.11.22
        完成了基本功能，即登录界面、好友界面、聊天界面的编写，服务器框架，发送文字、文件信息的框架。
    
    2024.11.23
        修复了消息无法接收的bug，修复了文件无法接收的bug。

    2024.11.24
        修复了消息无法正确显示的bug，摆烂了决定使用固定大小的布局。

    2024.11.25
        完成了登录功能。修复了无法登录的bug。

    2024.11.26
        完成了注册界面和注册功能，决定使用Properties类充当数据库，添加了数据记录功能。

    2024.11.27
        无法忍受bug太多，建立了开发者通道，无需手动登录。完善了ui界面。

    2024.11.28
        新增发送emoji功能，修复了emoji不能显示的bug，完成了图片缩放模块。

    2024.11.29
        添加了获取好友功能，完成了好友列表初始化。修复了好友列表界面崩坏的bug。修复了好友显示失败的bug。

    2024.11.30
        导入了FlatLaf.jar包，配置了Github.json皮肤。写完了项目文档。文件又不能接收了，并且阻止我访问文件夹。

    2024.12.1
        修复了图片不能显示的bug，修复了头像不能正确读取、不能进入聊天界面的bug，完成了头像修改功能。

    2024.12.2
        添加了好友加减功能。修复了好友添加不对称的问题。对代码进行了优化，将冗长部分提取成函数，并进行了结构优化。
        修复了与好友无法私人聊天的bug。修复了删除好友时数据库数据有误的bug。修复了单人聊天不能发送文件的bug。
        添加了提示音。

    2024.12.3
        修复了图片重复发送的bug。修复了音频格式不匹配java的bug。新的音频格式由24bit PCM转换为16bit PCM，可以支持Java播放了。
        修复了文件无法保存的bug。修复了注册后新号信息有误的bug。修复了账号信息更新不及时的bug。
        修复了好友删减有误的bug。
