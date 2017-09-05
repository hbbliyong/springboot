[教程](http://www.cnblogs.com/skyblog/p/5129603.html)  
[github](https://github.com/zpng/spring-cloud-7simple)


## 程序打包
1. 进入工程目录运行mvn package
    ````
    mvn package
    ````
2. 打包过后就可以进入target目录使用java原生命令执行这个应用了
````
D:\cloud-simple-helloword\target>java -jar cloud-simple-helloword-0.0.1.jar --server.port=8081
````
