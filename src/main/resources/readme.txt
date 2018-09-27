# 分布式锁的使用

1.在使用的项目需要添加spring-boot-start-aop依赖

2.可以使用显示使用
try{
GlobalLock.lock(需要用的锁);  这里锁对象支持Class对象和String
代码执行
}finally{
GlobalLock.unlock();
}

3.使用注解使用
@AGlobalLock(value="test") 如果不指定锁对象默认是当前类的全称和方法名称组成