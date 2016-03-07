# webRequestdesignDemo
参考一些开源架构根据Handler + threadPool + message + HttpUrlConnection自己写的一个异步网络请求框架，对于一般项目直接导入使用，
使用该框架好处如下：
build模式实例化该框架；
支持android四大线程池配置,默认线程池次配置为指定线程数newFixedThreadPool线程池；
支持普通请求和json格式请求；
集成了gjson解析服务器返回数据parse回调；
可配置返回字节流是否装饰缓冲区；
可配置字节流写入速度；
可配置请求头；
可配置返回json结构调整；
支持网络返回回调处理；



