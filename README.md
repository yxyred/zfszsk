代码注意提防


1，有统一的返回封装  R
2，有统一的异常抛出封装   CvaException
3，请注意自定义注解 @Login的使用。如果在ShiroConfig配置中放开接口，则需要在接口处加添加@Login注解，走springboot自定义拦截器。如果
接口走shiro则不需要在接口处加@login注解。
