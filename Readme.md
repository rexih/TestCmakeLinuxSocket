[TOC]

![app screenshot](https://github.com/rexih/TestCmakeLinuxSocket/blob/master/screenshot.png)

### 代码说明

1. app如果没有网络权限，`socket(PF_INET, SOCK_STREAM, 0)`将不能创建套接字，fd小于0
2. 如果请求的服务端地址不对，`connect`将返回-1，注意服务端监听套接字时，不要使用`127.0.0.1`或者`localhost`，否则客户端即使连接的是正确的ip，例如`192.168.1.6`，`connect`也会失败
3. `java_socket`库可以通过运行main方法跑起来一个java写的socket服务端，编译出的app可以修改ip和端口去连接



### CMake脚本

参考资料：

[cmake 学习笔记(一)](https://blog.csdn.net/dbzhang800/article/details/6314073)

[使用 CMake 进行 NDK 开发之如何编写 CMakeLists.txt 脚本](https://www.jianshu.com/p/843cf09a1db2)

[AndroidStudio之NDK开发CMake CMakeLists.txt编写入门](https://blog.csdn.net/tabactivity/article/details/78364296)

[Android Studio NDK CMake 指定so输出路径以及生成多个so的案例与总结](http://lib.csdn.net/article/android/63917)



仅列出知识点

- 系统预设变量/环境变量/全局变量/变量
  - CMAKE_CURRENT_SOURCE_DIR
  - PROJECT_SOURCE_DIR
  - PROJECT_BINARY_DIR
  - CMAKE_COMMAND
  - CMAKE_VERBOSE_MAKEFILE
  - CMAKE_LIBRARY_OUTPUT_DIRECTORY
- 系统预设属性
  - LIBRARY_OUTPUT_DIRECTORY
  - OUTPUT_NAME
  - IMPORTED_LOCATION
- cmake指令说明
  - cmake_minimum_required
  - find_library
  - target_link_libraries
  - set
  - project
  - message
  - add_subdirectory
  - add_library
  - set_target_properties
  - target_include_directories
  - add_custom_command
- Module下jni多库、主从库的结构
  - 入口脚本
  - 主、从库脚本
- 引入第三方so库





### 客户端连接服务端

参见[Linux的SOCKET编程详解](https://blog.csdn.net/hguisu/article/details/7445768/)

1. 创建服务端地址的struct
2. 创建客户端的socket套接字，获取到socket描述符
3. setsockopt设置此描述符对应的socket的属性
4. connect连接服务端地址
5. c/s发送和接收数据
6. 关闭socket结束通讯





#### memset

头文件string.h

```c++
void * memset(void *b, int c, size_t len);
```

将b指针指向的对象，使用c的值，填充到对象的中，须要声明长度

```c++
memset(&srvAddr, 0, sizeof(srvAddr));
```

使用0，填充srvAddr结构，从指针指向位置开始，填充长度由len指定

#### timeval

timeval有两个值，一个是秒，一个是微秒(us)

```c++
struct timeval {
  __kernel_time_t tv_sec;
  __kernel_suseconds_t tv_usec;
};
```

#### errno & strerror & perror

> The <errno.h> header file defines the integer variable errno, which
> is set by system calls and some library functions in the event of an
> error to indicate what went wrong.

- errno是系统调用或者某些库函数的错误码，当相关的调用出错会将errno设置为对应的错误码
- 使用`strerror(errno)`可以获取到错误码的简单描述信息
- `perror("tag")`相当于打印`strerror(errno)`并加上"tag"前缀标签

参见[Linux Programmer's Manual ERRNO(3)](http://man7.org/linux/man-pages/man3/errno.3.html) ; [Linux errno详解](https://www.cnblogs.com/Jimmy1988/p/7485133.html)

#### setsockopt

```c++
extern int setsockopt(int __fd, int __level, int __option, const void *__value, socklen_t __value_length)
```

- 根据__option操作编号，对fd表示的套接字设置对应的属性值
- 最后两个参数表示要设置的属性值得指针地址及其长度
- __level表示所要设置的选项所位于的层中。同一个选项可能存在于多个层的协议中
- 返回值：成功0，失败-1,errno被设置为
  - EBADF：__fd不是有效的文件描述词
  - ENOTSOCK：__fd描述的不是套接字
  - ENOPROTOOPT：__level指定的协议层不能识别选项
  - EFAULT：__value指向的内存并非有效的进程空间
  - EINVAL：在调用setsockopt()时，__value_length无效

```c++
setsockopt(socketFd, SOL_SOCKET, SO_SNDTIMEO, (char *) &timeout, sizeof(struct timeval));
```

参见：[setsockopt()函数功能介绍](https://www.cnblogs.com/eeexu123/p/5275783.html)



#### shutdown和close

```c++
extern int shutdown(int __fd, int __how)
```

- shutdown通过第二个参数控制关闭行为：
  1. SHUT_RD：值为0，关闭连接的读这一半。
  2. SHUT_WR：值为1，关闭连接的写这一半。
  3. SHUT_RDWR：值为2，连接的读和写都关闭。


- 多进程共享同一个套接字，每个进程中调用close，当前进程不可再使用此套接字，引用计数-1，直到0则断开连接，释放套接字
- shutdown后所有进程都按照关闭方式关闭使用。

参见[shutdown和close的区别](https://blog.csdn.net/mafuli007/article/details/7544373);[linux网络编程之shutdown() 与 close()函数详解](https://blog.csdn.net/lgp88/article/details/7176509)

## 