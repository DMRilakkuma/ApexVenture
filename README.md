

# ApexVenture 微服务架构的搭建

## 1. 技术选型

> 1. JDK17
> 2. Spring6
> 3. SpringBoot3
> 4. SpringCloud Alibaba 2022
> 5. Spring Cloud 2022

## 2.项目搭建

### 1. 项目的基础的构建

#### 1. 新建项目

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301647333.png)

**子项目不要创建git仓库**



#### 2.导入maven的pom配置文件

##### 父pom的配置文件

> 因为父pom不参与打包与编译，所以不需要</dependencies>标签内的东西，最终pom文件导入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!--父工程-->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <groupId>com.apxvt</groupId>
    <artifactId>ApexVenture</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>ApexVenture</name>
    <description>ApexVenture</description>

    <!--依赖包管理-->
    <properties>
        <java.version>17</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-cloud.version>2022.0.0</spring-cloud.version>
        <spring-cloud.alibaba.version>2022.0.0.0-RC2</spring-cloud.alibaba.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- spring-cloud-alibaba 依赖 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${spring-cloud.alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.graalvm.buildtools</groupId>
                <artifactId>native-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.project-lombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
        
    </build>

</project>
```







#### 3.开启热部署配置

> 依赖此Maven配置

![image-20230618161055429](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458459.png)

>  开启自动构建项目

![image-20230618160748183](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458460.png)

> 双shift搜索registry  保存后一秒钟进行自动编译

![image-20230618160702865](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458461.png)

> 开启高级设置里面的自动编译

![image-20230618161515989](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458462.png)

### 2.创建普通子模块

> 在父模块下新建一个module，创建一个普通子模块

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301649148.png)

> 可在resources创建一个application的yml配置文件，可配置端口号和服务路径前缀(方便后续网关做转发)，也可以配置日志配置文件，日志配置文件在**优化配置目录中**

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301650932.png)

> 启动类模版，打印启动请求路径和日志

```java
@ComponentScan("com.apxvt")
@SpringBootApplication
public class MemberApplication {
    private static final Logger LOG = LoggerFactory.getLogger(MemberApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MemberApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"),                 			                   env.getProperty("server.servlet.context-path"));
    }
}

```



### 3. 创建公共模块

> 公共模块是把一些常用的工具类或者aop等被多个模块共同调用的单独放一个模块里面

新建一个maven子模块模块common , 将公共代码导入到新模块中，将公共模块放在父pom里面管理，groupid和version直接复制父pom的

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301715852.png)

> 其他子模块需要导入公共模块需要导入pom配置如下，版本号父pom在管理，所有不需要填写，同时公共模块已经引入的jar包，调用模块可不需要在引入重复模块

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301652117.png)

> 公共模块的配置文件优先级要高于调用模块的配置文件的优先级，可以将公共相同的配置放在公共模块
>
> 普通模块的配置文件会直接房子resource下，公共模块最好放在新建的config包下，这个包也会自动被spring扫描到，不然可能会影响配置文件的冲突

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301653926.png)

>  避坑 : 各个模块的启动类一定要记得扫包 ，不然公共模块很多注入会扫描不到

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301653979.png)



### 4.创建网关Gateway模块

> 在父模块下新建一个module创建一个后缀为gateway的子模块

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301654290.png)

> 导入pom配置文件，关联父pom模块，只添加一个gateway依赖即可

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.apxvt</groupId>
        <artifactId>ApexVenture</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>apxvt-gateway</artifactId>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!--gateway网关-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
    </dependencies>
    
</project>
```

> 创建启动类

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301655661.png)

```java
@ComponentScan("com.apxvt")
@MapperScan("com.apxvt.chatbot.mapper")
@SpringBootApplication
public class ChatBotApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ChatBotApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ChatBotApplication.class);
       Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("运行环境: \t{}", env.getProperty("spring.profiles.active"));
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}

```

> 在resources包下创建application.yml的配置文件，配置路由转发
>
> 它的ID是"apxvt-member"，将匹配服务前缀"Path=/chatbot/**"的请求转发到URI"http://127.0.0.1:8001"。
>
> 同时可以配置logback-spring的日志文件，修改存储日志的路径

![image-20230620103651343](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458472.png)

> 配置服务配置

![image-20230620104849318](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458473.png)

>配置VM配置，打印网关的特殊日志 
>
>-Dreactor.netty.http.server.accessLogEnabled=true

![image-20230620104939252](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458474.png)

> 多了一个新的日志，就是配置成功了

![image-20230620105136048](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458475.png)



### 5.数据库（Mysql）

#### 1.本地数据库

> 创建数据库，需要专库专用，指定每个项目的用户和库的权限，看不到其他无关的库，不然容易误操作，尽量不要用root用户，权限太大
>
> 使用Navicat为例，先使用root登录数据库，点击用户选项

![image-20230620111602520](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458476.png)

> 只需要设置用户名，主机和密码即可，其他的自动会生成

![image-20230620111715203](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458477.png)

> 点击权限，不要点击服务器权限，然后点击添加权限

![image-20230620111838040](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458478.png)

> 选择要赋予这个用户的库的权限，选择数据库，给与全部权限，点击保存

![image-20230620111931320](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458479.png)

![image-20230620112108506](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458480.png)

> 我们在用我们设置的用户名和密码去登录就可以看到我们赋予用户权限的库了，看不了没有赋予权限的库，就可以实现专库专用了



### 6.集成Mybatis

> 在父pom导入mybatis和数据库连接的包

![image-20230620142631282](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458481.png)

> 在子模块导入，如果有公共模块，直接在公共模块导入，因为子模块可以去导入公共模块，就不需要再在子模块导入，版本号又父pom管理

![image-20230620142814453](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458482.png)

> 创建两个mapper包，一个放在项目路径，一个放在配置路径，在子模块中的application.yml文件中配置数据库的连接，如果各个模块用的数据库一样，可以直接配置在公共模块中（一般都不会一样)
>
> 配置数据库连接，配置xml的路径，项目要扫描持久层xml路径在哪所以需要配置

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301700863.png)

> 启动类需要配置MapperScan，需要扫描到mapper接口注入到bean

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301700238.png)

### 7.封装接口返回数据Result

> 在公共模块创建response包，专门放响应结果类，将以下两个类的代码复杂粘贴下去

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301701477.png)

**Result**

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7283104959571434994L;

    private boolean success; // 是否成功
    private String code; // 状态码
    private String message; // 消息描述
    private T data; // 数据

    public Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.success = resultEnum == ResultEnum.OK;
    }

    public Result(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
        this.success = resultEnum == ResultEnum.OK;
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
        this.success = false;
    }

    public Result(int code, String message) {
        this.code = String.valueOf(code);
        this.message = message;
        this.success = false;
    }

    public Result(int code, String message, T data) {
        this.code = String.valueOf(code);
        this.message = message;
        this.data = data;
        this.success = false;
    }

    public static <T> Result<T> ok() {
        return new Result<>(ResultEnum.OK);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultEnum.OK, data);
    }

    public static <T> Result<List<T>> ok(List<T> data) {
        return new Result<>(ResultEnum.OK, data);
    }

    public static <T> Result<T> fail(String errorMessage) {
        for (ResultEnum resultEnum : ResultEnum.values()) {
            if (resultEnum.getMessage().equals(errorMessage)) {
                return new Result<>(resultEnum.getCode(), errorMessage);
            }
        }
        return new Result<>(ResultEnum.FAIL.getCode(), errorMessage);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage());
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }
}

```

**ResultEnum**

```java
public enum ResultEnum {
    // ****************************【登录错误】****************************

    /**
     * 600 用户名或密码错误
     */
    PASSWORD_ERROR("600", "用户名或密码错误"),

    /**
     * 601 验证码过期
     */
    CODE_TIME_OUT("601", "验证码过期"),

    /**
     * 602 验证码错误
     */
    CODE_ERROR("602", "验证码错误"),

    // ****************************【公共错误】****************************
    /**
     * 1002(失败)
     */
    FAIL("1002", "失败"),

    // ****************************【文件错误】****************************
    /**
     * 2004(文件不能为空)
     */
    FILE_IS_EMPTY("2004", "文件不能为空"),

    /**
     * 2005（文件不能大于10M）
     */
    FILE_TOO_BIG("2005", "文件不能大于10M"),
    // ****************************【一级错误】****************************
    /**
     * 8001(数据不唯一错误)
     */
    NONUNIQUE_ERROR("8001", "不唯一错误"),
    /**
     * 8002(服务器缓存异常)
     */
    REDIS_NO_OPEN("8002", "服务器缓存异常"),
    /**
     * 8003(执行数据库操作错误)
     */
    EXECUTE_SQL_ERROR("8003", "执行数据库操作错误"),

    /**
     * 8004(参数错误，请确认格式是否正确)
     */
    PARAMS_ERROR("8004", "参数错误，请确认格式是否正确"),

    /**
     * 8005(文件路径错误)
     */
    FILE_PATH_ERROR("8005", "文件路径错误"),

    /**
     * 8006(IO异常)
     */
    IO_ABNORMAL("8006", "IO异常"),

    /**
     * 8007(内容不可读异常)
     */
    HTTP_MESSAGE_NOT_READABLE("8007", "内容不可读异常"),
    /**
     * 8008(json异常)
     */
    JSON_ERROR("8008", "json异常"),

    /**
     * 8009(空指针异常)
     */
    NULL_POINTER("8009", "空指针异常"),
    /**
     * 8010(类型转换异常)
     */
    CLASS_CAST("8010", "类型转换异常"),
    /**
     * 8011(未知方法异常)
     */
    NO_SUCH_METHOD("8011", "未知方法异常"),

    /**
     * 8012(下标越界异常)
     */
    INDEX_OUT_OF_BOUNDS("8012", "下标越界异常"),

    //****************************【http状态码】****************************

    /**
     * 100(继续)
     */
    CONTINUE("100", "继续"),

    /**
     * 101(切换协议)
     */
    SWITCHING_PROTOCOLS("101", "切换协议"),

    /**
     * 200(成功)
     */
    OK("200", "成功"),

    /**
     * 201(已创建)
     */
    CREATED("201", "已创建"),

    /**
     * 202(已接受)
     */
    ACCEPTED("202", "已接受"),

    /**
     * 203(非授权信息)
     */
    NON_AUTHORITATIVE_INFORMATION("203", "非授权信息"),

    /**
     * 204(无内容)
     */
    NO_CONTENT("204", "无内容"),

    /**
     * 205(重置内容)
     */
    RESET_CONTENT("205", "重置内容"),

    /**
     * 206(部分内容)
     */
    PARTIAL_CONTENT("206", "部分内容"),

    /**
     * 300(多种选择)
     */
    MULTIPLE_CHOICES("300", "多种选择"),

    /**
     * 301(永久移动)
     */
    MOVED_PERMANENTLY("301", "永久移动"),

    /**
     * 302(临时移动)
     */
    FOUND("302", "临时移动"),

    /**
     * 303(查看其他位置)
     */
    SEE_OTHER("303", "查看其他位置"),

    /**
     * 304(未修改)
     */
    NOT_MODIFIED("304", "未修改"),

    /**
     * 305(使用代理)
     */
    USE_PROXY("305", "使用代理"),

    /**
     * 306(临时重定向)
     */
    TEMPORARY_REDIRECT("307", "临时重定向"),

    /**
     * 400(错误请求)
     */
    BAD_REQUEST("400", "错误请求"),

    /**
     * 401(未登录)
     */
    UNAUTHORIZED("401", "登录失效"),

    /**
     * 403(未授权)
     */
    FORBIDDEN("403", "未授权"),

    /**
     * 404(未找到)
     */
    NOT_FOUND("404", "未找到"),

    /**
     * 405(方法禁用)
     */
    METHOD_NOT_ALLOWED("405", "方法禁用"),

    /**
     * 406(不接受)
     */
    NOT_ACCEPTABLE("406", "不接受"),

    /**
     * 407(需要代理授权)
     */
    PROXY_AUTHENTICATION_REQUIRED("407", "需要代理授权"),

    /**
     * 408(请求超时)
     */
    REQUEST_TIMEOUT("408", "请求超时"),

    /**
     * 409(冲突)
     */
    CONFLICT("409", "冲突"),

    /**
     * 410(已删除)
     */
    GONE("410", "已删除"),

    /**
     * 411(需要有效长度)
     */
    LENGTH_REQUIRED("411", "需要有效长度"),

    /**
     * 412(未满足前提条件)
     */
    PRECONDITION_FAILED("412", "未满足前提条件"),

    /**
     * 413(请求实体过大)
     */
    REQUEST_ENTITY_TOO_LARGE("413", "请求实体过大"),

    /**
     * 414(请求的 URI 过长)
     */
    REQUEST_URI_TOO_LONG("414", "请求的 URI 过长"),

    /**
     * 415(不支持的媒体类型)
     */
    UNSUPPORTED_MEDIA_TYPE("415", "不支持的媒体类型"),

    /**
     * 416(请求范围不符合要求)
     */
    REQUESTED_RANGE_NOT_SATISFIABLE("416", "请求范围不符合要求"),

    /**
     * 417(未满足期望值)
     */
    EXPECTATION_FAILED("417", "未满足期望值"),

    /**
     * 500(服务器内部错误)
     */
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),

    /**
     * 501(尚未实施)
     */
    NOT_IMPLEMENTED("501", "尚未实施"),

    /**
     * 502(错误网关)
     */
    BAD_GATEWAY("502", "错误网关"),

    /**
     * 503(服务不可用)
     */
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    /**
     * 504(网关超时)
     */
    GATEWAY_TIMEOUT("504", "网关超时"),

    /**
     * 505(版本不受支持)
     */
    HTTP_VERSION_NOT_SUPPORTED("505", "版本不受支持"),
    // ****************************【其他错误】****************************

    /**
     * 9997(该id不存在)
     */
    FINDMSGBYID_FAILURE("9997", "枚举id不存在"),

    /**
     * 9998(该内容不存在)
     */
    FINDIDBYMSG_FAILURE("9998", "枚举内容不存在");

    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ResultEnum(int code, String message) {
        this.code = String.valueOf(code);
        this.message = message;
    }

    /**
     * 通过传入的状态码查找内容
     *
     * @param code 状态码字符串
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:00:53
     */
    public static ResultEnum findMsgByCode(String code) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getCode().equals(code)) {
                return resultEnum;
            }
        }
        return FINDMSGBYID_FAILURE;
    }

    /**
     * 通过传入的状态码查找内容
     *
     * @param code 状态码数字
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:01:56
     */
    public static ResultEnum findMsgByCode(int code) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getCode().equals(String.valueOf(code))) {
                return resultEnum;
            }
        }
        return FINDMSGBYID_FAILURE;
    }

    /**
     * 通过传入的内容查找状态码
     *
     * @param mgs 内容字符串
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:03:43
     */
    public static ResultEnum findCodeByMsg(String mgs) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getMessage().equals(mgs)) {
                return resultEnum;
            }
        }
        return FINDIDBYMSG_FAILURE;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCodeToInt() {
        return Integer.parseInt(code);
    }
}
```

### 8.捕获全局异常处理

> 在公共模块创建exception的包，定义全局异常捕获类，拦截全局异常，打印异常日志

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301701432.png)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.FORBIDDEN.value(), "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                              HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: 不支持 {} 请求", e.getMethod());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    public Result<Object> handleServiceException(ServiceException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("业务异常: {}", e.getMessage());
        logErrorWithSeparator();
        // log.error("异常堆栈: ", e); // 业务异常不打印堆栈信息

        Integer code = e.getCode();
        return code != null ? Result.fail(code, e.getMessage()) : Result.fail(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("异常: 请求参数校验失败 - {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e, HttpServletRequest request) {
        logErrorWithSeparator();
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("绑定异常: 请求参数绑定失败 - {}", e.getMessage());
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return Result.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e, HttpServletRequest request) {
        logErrorWithSeparator();
        String errorMessage = e.getMessage();
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "未知异常，操作失败";
        }
        log.error("请求的接口为: {}", request.getRequestURI());
        log.error("系统异常: {}", errorMessage);
        logErrorWithSeparator();
        log.error("异常堆栈: ", e);

        return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    private void logErrorWithSeparator() {
        log.error("—————————————————Exception——————————————————————");
    }

    // 可以定义更多的异常处理方法...
}
```

> 可以自定义异常类，我自定义了一个专门用于业务逻辑的异常类，如下，可以添加多个

```java
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2653634707900442040L;

    private Integer code;

    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = Integer.parseInt(resultEnum.getCode());
        this.message = resultEnum.getMessage();
    }
}
```

### 9.集成校验框架

> 在公共模块pom中导入校验框架的依赖包

![image-20230621171209041](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458487.png)

> 导入后可以在实体类上的属性进行注解校验，列入下面，如果mobile为空则会报错抛异常

![image-20230621171256282](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458488.png)

> 在上述的全局异常处理中，下列两个捕获异常就是来处理校验框架的，已经在上述写过，这里知道就行

![image-20230621171412435](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458489.png)

### 10.Naocs 的安装与配置（Docker安装)

#### 1. 数据库配置

nacos还支持mysql作为数据源，我们执行要进行以下设置

1.创建名为nacos_config的数据库，运行以下sql，初始化表和数据

> 问题: 发布失败。请检查参数是否正确
>
> nacos 初始化sql与nacos版本不一致
> nacos 2.1.0版本之后初始化数据库中config_info 和 his_config_info 表中新增了encrypted_data_key密钥字段
> nacos.2.1.0 及之前数据库初始化脚本为nacos-mysql.sql，2.2.0 之后重命名为mysql-schema.sql

##### **2.0.4版本及之前sql为：**

```sql
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info   */
/******************************************/
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text,
  `src_ip` varchar(50) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
```

##### 2.0.4版本及之后sql为：

```sql
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info   */
/******************************************/
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text,
  `src_ip` varchar(50) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY,
	`password` varchar(500) NOT NULL,
	`enabled` boolean NOT NULL
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL,
	`role` varchar(50) NOT NULL,
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL,
    `resource` varchar(255) NOT NULL,
    `action` varchar(8) NOT NULL,
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');


```

#### 2. Nacos Docker安装

拉取镜像

>docker pull nacos/nacos-server:latest

创建镜像

> ```dockerfile
> docker run -d --name nacos -p 8848:8848 -p 7848:7848 -p 9848:9848 -e MODE=standalone -e NACOS_AUTH_ENABLE=true -e PREFER_HOST_MODE=hostname -e SPRING_DATASOURCE_PLATFORM=mysql -e MYSQL_SERVICE_HOST=127.0.0.1 -e MYSQL_SERVICE_PORT=3306 -e MYSQL_SERVICE_DB_NAME=nacos_config -e MYSQL_SERVICE_USER=root -e MYSQL_SERVICE_PASSWORD=root -e MYSQL_DATABASE_NUM=1 -e NACOS_AUTH_TOKEN=MjM0MTM0NTc7ABY3NTU9Njc1MzkzODQ0PTP5OTQ1NjY= -e NACOS_AUTH_IDENTITY_KEY=serverIdentity -e NACOS_AUTH_IDENTITY_VALUE=security nacos/nacos-server:latest
> ```
>

> SPRING_DATASOURCE_PLATFORM：数据源MYSQL_SERVICE_HOST：mysql主机地址
> MYSQL_SERVICE_PORT：mysql端口号
> MYSQL_SERVICE_DB_NAME：mysql数据库名称
> MYSQL_SERVICE_USER：连接用户名
> MYSQL_SERVICE_PASSWORD：用户名密码
> MYSQL_DATABASE_NUM：mysql数据库编号
>
> dokcer部署需要放行上下1000浮动的端口
>
> 这是一个使用 Docker 启动 Nacos 服务器容器的命令，具体解释如下：
>
> - `docker run`: 启动一个 Docker 容器。
> - `-d`: 将容器设置为后台运行。
> - `-p 8848:8848`: 将容器内部的 8848 端口映射到主机的 8848 端口。
> - `-e MODE=standalone`: 指定 Nacos 服务器的运行模式为独立模式。
> - `-e PREFER_HOST_MODE=hostname`: 指定 Nacos 服务器在容器环境中以主机名模式运行。
> - `-e SPRING_DATASOURCE_PLATFORM=mysql`: 指定 Nacos 服务器使用 MySQL 数据库。
> - `-e MYSQL_SERVICE_HOST=127.0.0.1`: 指定 MySQL 数据库所在的主机 IP 地址。
> - `-e MYSQL_SERVICE_PORT=3306`: 指定 MySQL 数据库的端口号。
> - `-e MYSQL_SERVICE_DB_NAME=nacos_config`: 指定 Nacos 服务器使用的数据库名称。
> - `-e MYSQL_SERVICE_USER=root`: 指定连接 MySQL 数据库时使用的用户名。
> - `-e MYSQL_SERVICE_PASSWORD=123456`: 指定连接 MySQL 数据库时使用的密码。
> - `-e MYSQL_DATABASE_NUM=1`: 指定使用的数据库数量。
> - `-e NACOS_AUTH_TOKEN=SecretKeylF3tWWqh7mYHuBDwXNPtshsqEJbdJdva5xI9ox2sr5NASp4swoCHBE5VJ0PT`：设置访问 Nacos 服务器 API 时的身份验证令牌。这有助于确保只有授权的客户端可以访问服务器的配置和发现信息。向 Nacos 服务器发出一个请求，获取当前注册的所有服务。在请求头中，我们添加了一个 `Authorization` 字段，其值为 `Bearer SecretKeylF3tWWqh7mYHuBDwXNPtshsqEJbdJdva5xI9ox2sr5NASp4swoCHBE5VJ0PT`，以使用我们设置的身份验证令牌进行身份验证。
> - `-e NACOS_AUTH_IDENTITY_KEY=serverIdentity`：指定进行身份验证时 Nacos 服务器的身份验证密钥。该密钥用于验证身份验证请求来自受信任的源。
>
> - `-e NACOS_AUTH_IDENTITY_VALUE=security`：指定进行身份验证时 Nacos 服务器的身份验证值。该值用于验证身份验证请求来自受信任的源。向 Nacos 服务器发出一个请求，获取当前注册的所有服务。在请求头中，我们添加了一个 `Nacos-Server-Identity` 字段，其值为 `serverIdentity:security`，以使用我们设置的身份验证密钥和身份验证值进行身份验证。
>
>   >`curl -H "Authorization: Bearer SecretKey012345678901234567890123456789012345678901234567890123456789" -H "Nacos-Server-Identity: serverIdentity:security" http://localhost:8848/nacos/v1/ns/catalog/services`
>
> - `-restart always`: 容器在启动时自动重启。
> - `--name nacos`: 指定容器的名称为 nacos。
> - `nacos/nacos-server:v2.2.1`: 指定使用的 Nacos 服务器镜像及其版本号。
>
> 总之，这个命令将会启动一个在独立模式下运行的 Nacos 服务器容器，使用 MySQL 数据库，同时进行身份验证和授权，以确保只有受信任的客户端可以访问服务器的配置和发现信息。
>
> 
>
> ---   如果mysql8.0不能用ip访问请进入mysql
>
> create user root@'{ip地址}' identified by '{密码}';    // 授权指定ip地址可以登录数据库
>
> GRANT ALL ON *.* TO 'root'@'{ip}'    // 授权指定ip地址可以访问所有数据库
>
> flush privileges 刷新权限
>
> 重启mysql服务

#### 3.项目中的配置

> 在父pom中引入springcloud alibaba的依赖管理

![image-20230623191651330](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231916370.png)

> 选择一个需要进行配置的业务服务注入以下依赖

![image-20230623193255329](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231932362.png)

> 此服务在resourse下新建一个bootstrap.yml配置文件  优先级 ：bootstrap.yml > application.yml  

> bootstrap 一般配置不怎么变化的配置，所以一般用于cloud的配置，配置如下

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301706535.png)

> 需要读取bootstrap.yml配置需要在公共模块或者当前模块引入以下包，在2.4版本后必须引入，不然读取不到配置文件

![image-20230623192508780](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231925811.png)

> 可将配置放入到maven里面，然后在配置文件中进行填充，更加maven选择环境的不同来运行和打包不同环境

```java
<profiles>
        <profile>
            <id>dev</id>
            <properties>
                <profiles.active>dev</profiles.active>
                <nacos.namespace>15678feb-c941-40fa-b5a1-7459fc092ac3</nacos.namespace>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>

        <profile>
            <id>test</id>
            <properties>
                <profiles.active>test</profiles.active>
                <nacos.namespace>23335660-cdcf-4f2a-961d-ed3730315c2d</nacos.namespace>
            </properties>
        </profile>

        <profile>
            <id>prod</id>
            <properties>
                <profiles.active>prod</profiles.active>
                <nacos.namespace>4cc446f8-1d9f-4211-9e8b-66f1beb76e79</nacos.namespace>
            </properties>
        </profile>
    </profiles>
```

> 公共模块的参数可以放在properties中

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301707541.png)

> 创建bootstrap.yml文件进行配置

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301708110.png)

```yaml
server:
  port: 8501
  servlet:
    context-path: /chatbot    # 服务路径/前缀

spring:
  application:
    name: chatbot
  profiles:
    active: @profiles.active@
  cloud:
    nacos:
      config:
        server-addr: @nacos.server-addr@
        username: @nacos.username@
        password: @nacos.password@
        namespace: @nacos.namespace@
        group: @nacos.group@
        file-extension: yml
```

> 配置nacos的地址还有命名空间的id和分组即可使用nacos的配置中心，可以将多环境配置都配置在nacos上

#### 4.nacos中的配置（配置中心）

> DataID 为 {服务名/应用名}-{启动环境}.{配置文件格式}
>
> 这些都能在项目配置文件中找到
>
> 配置内容优先级nacos最高，如果在项目的配置文件有同样的配置名，nacos会将其覆盖，创建成功后就可启动项目了

![image-20230623192708939](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231927992.png)

> value注解可以根据配置的建获取配置的值
>
> 
>
> ![image-20230623193044505](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231930530.png)

> 如果当前类需要动态更新nacos配置，不需要手动重启服务，可在当前类上加一个注解RefreshScope

![image-20230623193519913](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231935965.png)

#### 5.nacos注册中心(注册服务到nacos)

> 在指定需要监控的服务或者公共模块的pom里面加入nacos的pom配置

![image-20230628094843811](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306280948045.png)

```xml
       <!--nacos注册中心-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```

> 同时需要在指定服务里面配置discovery注册中心

![image-20230628095426514](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306280954155.png)

> 查看nacos的服务管理就能看到已经注册的服务，对此可以对服务做监控

![image-20230628100109539](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281001210.png)



#### 6.配置gateway和nacos支持应用名进行路由转发

> uri可以从ip+端口修改成lb://{应用名},会根据nacos配置自动识别到指定模块并进行路由转发

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301710687.png)

#### 7.gateway配置负载均衡

> 如果项目有多个同样的服务，可以在gateway添加负载均衡均衡

```xml
       <!--负载均衡loadbalance-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
```

> 选择一个服务copy一个新的服务出来

![image-20230628101917424](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281019342.png)

> 在vm中配置需要的服务端口

![image-20230628101956514](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281019443.png)

> 如果配置项没有vm，可以点击modify options，选择vm

![image-20230628102119785](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281021368.png)

> 启动两个同名的服务

![image-20230628102213827](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281022664.png)

> 在nacos的服务列表就会发现有两个实例数

![image-20230628102325514](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281023294.png)

> 点击服务详情可以看到有两个服务

![image-20230628102411423](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281024038.png)

> 可以编辑上线下线，请求经过gateway后转发的指定服务，如果配置了多个实例，会进行轮询的以此请求这些服务

![image-20230628102550674](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281025412.png)



### 11.swagger-knife4j 配置

#### 1.swagger集成网关配置

> 在gateway服务中导入下列包

```xml
       <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
        </dependency>
```

> 在gateway的配置文件中导入下列配置

```xml
#swagger
knife4j:
  gateway:
    enabled: true
    # 指定服务发现的模式聚合微服务文档，并且是默认`default`分组
    strategy: discover
    discover:
      enabled: true
      # 指定版本号(Swagger2|OpenAPI3)
      version: openapi3
      # 需要排除的微服务(eg:网关服务)
      excluded-services:
        - gateway
```

> 在需要集成聚合的api文档的服务或者公共模块中导入以下配置

```xml
 <!-- swagger-ui Knife4j SpringBoot3-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springdoc</groupId>
                    <artifactId>springdoc-openapi-webmvc-ui</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-ui</artifactId>
        </dependency>
```

> 在服务或者公共模块中配置

```yml
knife4j:
  enable: true
  setting:
    language: zh-CN
    enable-swagger-models: true
    enable-document-manage: true
    swagger-model-name: 实体类列表
    enable-after-script: true
    enable-filter-multipart-api-method-type: POST
    enable-request-cache: true
    enable-footer-custom: true
    footer-custom-content: ApexVenture 1.0 | Copyright  2023-[开源])
```

> 访问ip:host/doc.html ,发现配置过的服务已经导入进来了

![image-20230628154032111](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306281540048.png)

## end-优化配置

### 1.导入日志配置文件（每个模块都有自定义的日志配置）

> 在，每个模块的resources导入logback-spring.xml的日志文件 , 直接导入复杂进去

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
    小技巧: 在根pom里面设置统一存放路径，统一管理方便维护
    <properties>
        <log-path>/Users/lengleng</log-path>
    </properties>
    1. 其他模块加日志输出，直接copy本文件放在resources 目录即可
    2. 注意修改 <property name="${log-path}/log.path" value=""/> 的value模块
-->
<configuration debug="false" scan="false">
    <property name="log.path" value="./logs"/>
    <!-- 彩色日志格式 -->
    <property name="CONSOLE_LOG_PATTERN"
              value="${CONSOLE_LOG_PATTERN:-%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}"/>


    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule conversionWord="clr" converterClass="org.springframework.boot.logging.logback.ColorConverter"/>
    <conversionRule conversionWord="wex"
                    converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter"/>
    <conversionRule conversionWord="wEx"
                    converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter"/>

    <!-- Console log output -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${CONSOLE_LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- Log file debug output -->
    <appender name="debug" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/debug.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/%d{yyyy-MM, aux}/debug.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxFileSize>50MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Log file error output -->
    <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${log.path}/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${log.path}/%d{yyyy-MM}/error.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
            <maxFileSize>50MB</maxFileSize>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%date [%thread] %-5level [%logger{50}] %file:%line - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
    </appender>

    <!-- Level: FATAL 0  ERROR 3  WARN 4  INFO 6  DEBUG 7 -->
    <root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="debug"/>
        <appender-ref ref="error"/>
    </root>

    <!--mybatis拦截器配置，没有拦截器可以删除这个模块-->
    <logger name="com.apxvt.common.interceptor.SqlLoggerInterceptor" level="TRACE">
    </logger>

</configuration>
```

> 每个模块可以修改成自己模块名路径下，比如网关模块修改为./logs/gateway （单体项目可不修改）

![image-20230620105320122](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458499.png)

> 修改拦截器的路径，这里的配置是将指定类的的TRACE日志控制台输出出来但是不输出到文件中（此处是为了配合Mybatis拦截器使用，不需要可以删除）

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301713570.png)

> 日志会在根目录下生成一个网关模块日志

![image-20230620105437733](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458501.png)





### 2.Mybatis拦截器打印完整sql日志(续先导入上述的日志配置文件)  （放在公共模块）

> 关闭在配置文件中配置的mybatis的日志配置文件 ， 注释掉

![image-20230619113101198](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458502.png)

> 在项目模块代码或者公共模块中加入interceptor包专门放拦截器

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301711235.png)

> 创建新的class文件SqlLoggerInterceptor，粘贴以下Mybatis拦截器代码  (需要当前模块有启动类，可配置到公共模块中)

```java
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.*;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})
})
public class SqlLoggerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs().length > 1 ? invocation.getArgs()[1] : null;
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;
        String sql = generateSql(configuration, boundSql);

        long end;
        long start;
        try {
            start = System.currentTimeMillis();
            returnValue = invocation.proceed();
            end = System.currentTimeMillis();
        } catch (InvocationTargetException | IllegalAccessException e) {
            logSqlError(sqlId, sql, e);
            return null;
        }
        logSql(sqlId, sql, returnValue, end - start, mappedStatement);

        return returnValue;
    }

    private void logSqlError(String sqlId, String sql, Exception e) {
        log.error("\n" +
                "\033[31m----------------------------------SqlLogs ----------------------------------------\n" +
                "==> 调用方法 : " + sqlId + "\n" +
                "==> sql : " + sql + "\n" +
                "==> sql异常 : " + e.getClass().getSimpleName() + "\n" +
                "----------------------------------SqlLogs ----------------------------------------\033[m\n\n", e);
    }

    private void logSql(String sqlId, String sql, Object returnValue, long time, MappedStatement mappedStatement) {
        long count = 0;
        String resultType = "";
        if (returnValue instanceof Number) {
            Number number = (Number) returnValue;
            if (number instanceof Long) {
                count = number.longValue();
            } else if (number instanceof Integer) {
                count = number.intValue();
            } else if (number instanceof Short) {
                count = number.shortValue();
            } else if (number instanceof Byte) {
                count = number.byteValue();
            }
            if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 插入了 \033[33m" + count + "\033[m 条数据\n";
            } else if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 更新了 \033[33m" + count + "\033[m 条数据\n";
            } else if (SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
                resultType = "==> 删除了 \033[33m" + count + "\033[m 条数据\n";
            }
        } else {
            List resultList = (ArrayList) returnValue;
            if (resultList.size() == 1 && resultList.get(0) instanceof Number) {
                Number number = (Number) resultList.get(0);
                if (number instanceof Long) {
                    count = number.longValue();
                } else if (number instanceof Integer) {
                    count = number.intValue();
                } else if (number instanceof Short) {
                    count = number.shortValue();
                } else if (number instanceof Byte) {
                    count = number.byteValue();
                }
                resultType = "==> 查询到一条数值为 \033[33m" + count + "\033[m\n";
            } else {
                count = resultList.size();
                resultType = "==> 查询到 \033[33m" + count + "\033[m 条数据\n";
            }
        }
        System.out.print("\n");
        log.trace("\n" +
                "\033[32m----------------------------------SqlLogs ----------------------------------------\033[m\n" +
                "==> 调用方法 : " + sqlId + "\n" +
                "==> sql : \033[33m" + sql + "\033[m\n" +
                resultType +
                "==> 耗时 : " + time + "ms\n" +
                "\033[32m----------------------------------SqlLogs ----------------------------------------\033[m\n");
    }

    private static String getParameterValue(Object obj) {
        if (obj instanceof String) {
            return "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + formatter.format(obj) + "'";
        } else {
            return obj != null ? obj.toString() : "";
        }
    }

    public static String generateSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // Handle properties if needed
    }
}

```

> 需要将此拦截器注入到sqlsession中，在config目录中写一个MyBatisConfig类，复杂粘贴以下代码

```java
import cn.pydance.chat.interceptor.SqlLoggerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MyBatisConfig {
    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
        //实例化插件
        SqlLoggerInterceptor sqlInterceptor = new SqlLoggerInterceptor();
        //创建属性值
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1"); // 可设置可不设置
        //将属性值设置到插件中
        sqlInterceptor.setProperties(properties);
        //将插件添加到SqlSessionFactory工厂
        sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        return "interceptor";
    }
}

```

> 运行效果如图

![image-20230619110957074](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458504.png)



### 3.定义Aop切面拦截请求参数和返回结果 （公共模块）

> 新建一个aspect的包，将以下代码放入进去

```java
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
    public LogAspect() {
        System.out.println("Common LogAspect");
    }

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 定义一个切点
     */
    @Pointcut("execution(public * cn.apxvt..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        System.out.println("\n");
        // 打印请求信息
        LOG.info("------------- 请求开始 -------------");
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址: {}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();
        // LOG.info("请求参数: {}", JSONObject.toJSONString(args));

        // 排除特殊类型的参数，如文件类型
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("请求参数: {}", JSONObject.toJSONString(arguments, excludefilter));

    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludefilter = filters.addFilter();
        excludefilter.addExcludes(excludeProperties);
        LOG.info("返回结果: {}", JSONObject.toJSONString(result, excludefilter));
        LOG.info("------------- 结束 耗时: {} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }

}

```

> 配置敏感字段，将敏感的信息不显示到控制台上，只需要添加敏感的字段名即可

> ![image-20230621110619599](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458505.png)此类依赖fastjson的包，导入fastjson包

![image-20230619163602825](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458506.png)

> 修改切面为自己需要生效的路径

![](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306301714549.png)



### 4.java对象序列化生成唯一的uuid

![image-20230621102849657](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458508.png)

> **AIT + Enter** 第一个为生成uuid

![image-20230621102928254](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231458509.png)

### 5.雪花算法作为分布式数据主键Id

#### 1.雪花算法在项目中的使用

> 基于hutool工具类二次封装的雪花算法工具类（需要在pom文件中导入hutool的jar包）导入到公共模块的utils包下

```java
import cn.hutool.core.util.IdUtil;

/**
 * 封装hutool雪花算法
 */
public class SnowUtil {

    private static long dataCenterId = 1;  //数据中心
    private static long workerId = 1;     //机器标识

    public static long getSnowflakeNextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String getSnowflakeNextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}
```

> 不要使用雪花算法生成string类型，字符串类型在数据库查询效率低
>
> 由于雪花算法生成的ID是19位的Long类型，前端用js转化会存在精度丢失的问题，因为js最大只能接受16位的整行，所以需要将Long类型返回给前端的时候转化为String，在公共模块的config包下配置json转化类

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * JacksonConfig
 * @author MC
 * @description
 * @date 2023/6/21 17:56
 * @version 1.0
 */
@Configuration
public class JacksonConfig {

  @Bean
  @Primary
  @ConditionalOnMissingBean(ObjectMapper.class)
  public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder)
  {
    ObjectMapper objectMapper = builder.createXmlMapper(false).build();

    // 全局配置序列化返回 JSON 处理
    SimpleModule simpleModule = new SimpleModule();
    //JSON Long ==> String
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    objectMapper.registerModule(simpleModule);
    return objectMapper;
  }

}
```

#### 2.雪花算法的优点

**雪花算法，它至少有如下4个优点：**

**1.系统环境ID不重复**

能满足高并发分布式系统环境ID不重复，比如大家熟知的分布式场景下的数据库表的ID生成。

 

**2.生成效率极高**

在高并发，以及分布式环境下，除了生成不重复 id，每秒可生成百万个不重复 id，生成效率极高。

 

**3.保证基本有序递增**

基于时间戳，可以保证基本有序递增，很多业务场景都有这个需求。

 

**4.不依赖第三方库**

不依赖第三方的库，或者中间件，算法简单，在内存中进行。

 

**雪花算法，有一个比较大的缺点：**

依赖服务器时间，服务器时钟回拨时可能会生成重复 id。(不过不影响，百分之90的业务保证主键趋势递增就行)



#### 3.雪花算法原理

详细的雪花算法构造如下图所示：

![雪花算法详解(原理优缺点及代码实现)-mikechen的互联网架构](https://typore-makdown.oss-cn-beijing.aliyuncs.com/img/202306231501165.png)

雪花算法的原理：就是生成一个的 64 位的 long 类型的唯一 id，**主要分为如下4个部分组成：**

**1）1位保留 (基本不用)**

1位标识：由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0，所以这第一位都是0。

 

**2）41位时间戳** 

接下来 41 位存储毫秒级时间戳，41位可以表示2^41-1个毫秒的值，转化成单位年则是:(2^41−1)/(1000∗60∗60∗24∗365)=69年 。

41位时间戳 ：也就是说这个时间戳可以使用69年不重复，大概可以使用 69 年。

注意：41位时间截不是存储当前时间的时间截，而是存储时间截的差值“**当前时间截 – 开始时间截**”得到的值。

这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的，**一般设置好后就不要去改变了，切记！！！**

因为，雪花算法有如下缺点：依赖服务器时间，**服务器时钟回拨时可能会生成重复 id**。

 

**3）10位机器**

10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId，最多可以部署 2^10=1024 台机器。

这里的5位可以表示的最大正整数是2^5−1=31，即可以用0、1、2、3、….31这32个数字，来表示不同的datecenterId，或workerId。

 

**4） 12bit序列号**

用来记录同毫秒内产生的不同id，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号。

理论上雪花算法方案的QPS约为409.6w/s，这种分配方式可以保证在任何一个IDC的任何一台机器在任意毫秒内生成的ID都是不同的。



SnowFlake算法在同一毫秒内最多可以生成多少个全局唯一ID呢：： **同一毫秒的ID数量 = 1024 X 4096 = 4194304**
