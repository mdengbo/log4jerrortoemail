#springboot + log4j和logback 实现日志邮件发送
#logback maven：
  <!-- logback start -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.5</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </dependency>
        <!-- logback end -->
        <!-- mail start -->
        <dependency>
            <groupId>javax.mail</groupId>
            <artifactId>mail</artifactId>
            <version>1.4.7</version>
        </dependency>
        <dependency>
            <groupId>org.codehaus.janino</groupId>
            <artifactId>janino</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>activation</artifactId>
            <version>1.1.1</version>
        </dependency>
        <!-- mail end -->
        
#logback.xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="./home" />
    <!-- 控制台输出 -->
   <!-- <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            &lt;!&ndash;格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符&ndash;&gt;
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>-->
    <!-- 按照每天生成日志文件 -->
    <appender name="FILE"  class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/TestWeb.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!--日志异步到数据库 -->
    <!--  邮件 -->
    <!-- SMTP server的地址，必需指定。如网易的SMTP服务器地址是： smtp.163.com -->
    <property name="smtpHost" value="smtp.qq.com"/><!--填入要发送邮件的smtp服务器地址-->
    <!-- SMTP server的端口地址。默认值：25   QQ:587-->
    <property name="smtpPort" value="587"/>
    <!-- 发送邮件账号，默认为null -->
    <property name="username" value="144086471@qq.com"/><!--发件人账号-->
    <!-- 发送邮件密码，不是登录密码，服务登录第三方授权码  默认为null-->
    <property name="password" value="XXXXXXXXX"/><!--发件人密码-->
    <!-- 如果设置为true，appender将会使用SSL连接到日志服务器。默认值：false -->
    <property name="SSL" value="false"/>
    <!-- 指定发送到那个邮箱，可设置多个<to>属性，指定多个目的邮箱 -->
    <property name="email_to" value="144086471@163.com"/><!--收件人账号多个可以逗号隔开-->
    <!-- 指定发件人名称。如果设置成“&lt;ADMIN&gt; ”，则邮件发件人将会是“<ADMIN> ” -->
    <property name="email_from" value="14406471@qq.com" />
    <!-- 指定emial的标题，它需要满足PatternLayout中的格式要求。如果设置成“Log: %logger - %msg ”，就案例来讲，则发送邮件时，标题为“【Error】: com.foo.Bar - Hello World ”。 默认值："%logger{20} - %m". -->
    <property name="email_subject" value="【fiel-serverError】: %logger" />
    <!-- ERROR邮件发送 -->
    <appender name="EMAIL" class="ch.qos.logback.classic.net.SMTPAppender">
        <smtpHost>${smtpHost}</smtpHost>
        <smtpPort>${smtpPort}</smtpPort>
        <username>${username}</username>
        <password>${password}</password>
        <!--是否开启异步发送 默认true （开启后一直没有发送成功） 关掉 false 发送成功-->
        <asynchronousSending>false</asynchronousSending>
        <SSL>${SSL}</SSL>
        <to>${email_to}</to>
        <from>${email_from}</from>
        <subject>${email_subject}</subject>
        　　　　 <!-- html格式-->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>%date%level%thread%logger{0}%line%message</Pattern>
        </layout>
        　　　　 <!-- 这里采用等级过滤器 指定等级相符才发送 -->
        <filter class="ch.qos.logback.core.filter.EvaluatorFilter">
            <evaluator class="ch.qos.logback.classic.boolex.JaninoEventEvaluator">
                <expression>
                    <!-- 这里可以用java里的表达式 -->
                    if(level >= WARN &amp;&amp; null != throwable) {
                    return true;
                    }
                    return false;
                </expression>
            </evaluator>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <cyclicBufferTracker class="ch.qos.logback.core.spi.CyclicBufferTracker">
            <!-- 每个电子邮件只发送一个日志条目 -->
            <bufferSize>1</bufferSize>
        </cyclicBufferTracker>
    </appender>
    <!-- 日志输出级别 -->
     <!-- 多环境 日志输出  指定在 application-prod.yml  application-test.yml 生效-->
    <springProfile name="prod,test">
        <root level="info">
            <appender-ref ref="EMAIL"/>
        </root>
    </springProfile>
    
</configuration>
        
#log4j maven
 <!--SpringBoot - log4j-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j</artifactId>
            <version>1.3.8.RELEASE</version>
        </dependency>
        <!--log4j 实现发邮件必须包-->
        <dependency>
            <groupId>javax.activation</groupId>
            <artifactId>activation</artifactId>
            <version>1.1.1</version>
        </dependency>

        <!--javax.mail-->
        <dependency>
            <groupId>javax.mail</groupId>
            <artifactId>mail</artifactId>
            <version>1.4.7</version>
        </dependency>
        
 #log4j.properties
 # 发送日志到指定邮件
log4j.appender.mail=org.apache.log4j.net.SMTPAppender
log4j.appender.mail.Threshold=DEBUG
log4j.appender.mail.BufferSize=10

log4j.appender.mail.From=144086471116@qq.com
#QQ固定格式
log4j.appender.mail.SMTPHost=smtp.qq.com
#发送邮件箱的用户
log4j.appender.mail.SMTPUsername=144086471116@qq.com
#发送邮件箱的密码 使用的是QQ 邮箱设置 里面授权生成的授权码 并不是QQ登录密码
log4j.appender.mail.SMTPPassword=XXXXXXX

#mail服务器端口，qq为587

log4j.appender.mail.SMTPPort=587
log4j.appender.mail.Subject=Log4J Message
log4j.appender.mail.To=1440864716@qq.com
log4j.appender.mail.layout=org.apache.log4j.PatternLayout
log4j.appender.mail.layout.ConversionPattern=%d %-5p [%t] (%c{1}:%L) - %m%n
### set log levels - for more verbose logging change 'info' to 'debug' ###
log4j.rootLogger=debug, mail
log4j.appender.A1.Encoding=UTF-8  
        
        
