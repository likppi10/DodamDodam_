# 빌드 및 배포 문서

## :one: 개발 환경

### DevOps

- AWS EC2 Server(AWS amazon-linux-2)
- Jenkins Server(Ubuntu: 20.04.4 LTS)

#### Android

- Kakao Login (v2) API : 2.9.1 
- Firebase (푸시알림) : 21.1.1
- Hilt (의존성 주입) : 2.41 
- Android JetPack Calendar : 1.0.4 
- Retrofit : 2.9.0 
- Roulette : 1.0.0

#### BE

- Spring Boot: 2.6.6
- openJdk 11
- gradle 7.1

#### DB

- mariaDB: 10.3.34

#### IDE

- IntelliJ Ultimate 21.3.1
- Android Studio



## :two:  ENV(application.yml)

#### 1. Android : 없음

#### 2. BE

1) api/main/resources, api/test/resources

   ```
   spring:
     profiles:
       group:
         "local": "localdb, common"
   
   ---
   spring:
     config:
       activate:
         on-profile: "common"
     mvc:
       pathmatch:
         matching-strategy: ANT_PATH_MATCHER
   
     # JPA 기본 설정
     jpa:
       database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
       properties.hibernate:
         hbm2ddl.auto: none
         format_sql: true
         show_sql: true
         use_sql_comments: true
         default_batch_fetch_size: 100
   
       generate-ddl: true
       open-in-view: false
   
     datasource:
       flyway:
         enabled: false
       config:
         activate:
           on-profile: alpha
   
     #업로드 파일 용량 제한.
     servlet:
       multipart:
         file-size-threshold: 15MB
         max-file-size: 20MB
         max-request-size: 20MB
   
   #swagger 설정
   springdoc:
     version: '1.00v'
     api-docs:
       path: /api-docs
     default-consumes-media-type: application/json
     default-produces-media-type: application/json
     swagger-ui:
       #알파벳 오름차순 정렬, method -> http method순 정렬
       operations-sorter: alpha
       #태그정렬 기준
       tags-sorter: alpha
       #html 주소
       path: /swagger-ui.html
       #swagger-ui default url인 petstore html 문서 비활성화 여부
       disable-swagger-default-url: true
       #json화 된 config파일 대신 파라미터를 이용하여 swagger-ui에 접근하도록 합니다.
       display-query-params-without-oauth2: true
       #tag와 operation을 펼치는 방식에 대한 설정
       #String=["list", "full", "none"]
       #none으로 설정할 경우, tag 및 operation이 모두 닫힌채로 문서가 열립니다
       doc-expansion: none
     paths-to-match:
       - /api/**
   
   server:
     error:
       include-stacktrace: on_param
       include-exception: false
     servlet:
       session:
         timeout: 1440m
     max-http-header-size: 3145728
   #fcm 설정
   fcm:
     key:
       path: dodam-dodam-firebase-adminsdk-2fnrz-20e1d63679.json
       scope: https://fcm.googleapis.com/v1/projects/dodam-dodam/messages:send
   
   management:
     endpoints:
       web:
         exposure:
           include:
             - "health"
   
   # AWS S3 설정.
   cloud:
     aws:
       s3:
         bucket: s3-dodamdodam
       credentials:
         access-key: AKIAYRR6D6BOCQJBOWFS
         secret-key: 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
       region:
         static: ap-northeast-2
         auto: false
       stack:
         auto: false
   
   #ec2 warn 로그 무시
   logging:
     level:
       root: debug
       com.ssafy.api: debug
       org.hibernate.type.descriptor.sql: warn  # trace
       com:
         amazonaws:
           util:
             EC2MetadataUtils: ERROR
   
   ---
   spring:
     config:
       activate:
         on-profile: "local"
   
     datasource:
       url: jdbc:mysql://localhost:3306/dodamdodam
       driver-class-name: com.mysql.cj.jdbc.Driver
       username: root
       password: ssafy
   server:
     port: 8080
   # 개발용 로그 설정
   logging:
     level:
       #root: debug
       #com.ssafy.api: debug
       #org.hibernate.type.descriptor.sql: warn  # trace
   ```
   
   
   
2) batch/main/resources

   ```
   spring:
     batch:
       job:
         names: ${job.name:NONE}
       jdbc:
         initialize-schema: embedded
     jpa:
       database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
       generate-ddl: false
       hibernate:
         ddl-auto: none
       show-sql: true
   
     main:
       web-application-type: none
   
     datasource:
       url: jdbc:mysql://localhost:3306/dodamdodam
       driver-class-name: com.mysql.cj.jdbc.Driver
       username: root
       password: ssafy
   ```

   

## :three:  배포 환경(무중단 자동 배포)

1. 구축 순서

   ```
   1. EC2서버를 Project / Jenkins용으로 2개 만든다.(젠킨스 서버는 사피에서 제공된 서버 사용)
   2. Project서버 AWS 보안 그룹을 각각 22,80,443포트 개방하고 + jenkins ip도 22포트에 열어둔다.(파일 전송용)
   3. 탄력적 IP를 연결하여 IP가 변동되지 않도록 설정한다.
   4. Project서버에는 nginx, ssl, git, 등등.. Jenkins서버에는 jenkins, git, gralde 등등 설치해준다.
   5. RDS 생성한다.
   6. RDS 보안그룹 인바운드 규칙에서 Project서버 퍼블릭 주소를 예외처리 해준다.(본인 ip도...)
   7. 적당한 도메인을 구매한다.
   8. Rout53으로 도메인 연결해준다.
   9. Project서버에서 ssl관련해서 설치한 파일로 ssl인증서를 생성해준다.
   10. 인증서를 nignx.config에 세팅하고, 80포트로 오면 443으로 redirect시키고, 443으로 오면 project port로 return 시킨다.(설정파일 만들어야함.<< switch.sh 참고!)
   11. 젠킨스를 연결시켜 자동 CI / CD를 구축한다.
   12. shell파일들을 만들어 자동 실행 및 사용하지 않는 port kill, 포트 전환을 한다.
   ```

2. stop.sh

   ```
   CURRENT_SERVER=$(curl -s https://happydodam.com/server)
   echo "> $CURRENT_SERVER"
   
   if [ $CURRENT_SERVER == set1 ]
   then
           IDLE_PORT=8082
   elif [ $CURRENT_SERVER == set2 ]
   then
           IDLE_PORT=8081
   else
           echo "> 일치하는 Server가 없습니다. Server: $CURRENT_SERVER"
           echo "> set1을 할당합니다. IDLE_SERVER: set1"
           IDLE_PORT=8081
   fi
   
   echo "> $IDLE_PORT에서 구동중인 pid 확인"
   
   IDLE_PID=$(lsof -i :${IDLE_PORT} -Fp | cut -d'p' -f2)
   
   echo "> $IDLE_PID 랑port 번호 $IDLE_PORT 확인"
   
   if [ -z ${IDLE_PID} ]
   then
           echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
   else
           echo "> kill -15 $IDLE_PID"
           kill -15 ${IDLE_PID}
           sleep 5s
   fi
   
   /home/ec2-user/server/deploy.sh
   
   ```

   

2. deploy.sh

   ```
   echo "> 현재 구동중인 server 확인"
   CURRENT_SERVER=$(curl -s https://happydodam.com/server)
   echo "> $CURRENT_SERVER"
   
   if [ $CURRENT_SERVER == set1 ]
   then
     IDLE_SERVER=set2
     IDLE_PORT=8082
   elif [ $CURRENT_SERVER == set2 ]
   then
     IDLE_SERVER=set1
     IDLE_PORT=8081
   else
     echo "> 일치하는 Server가 없습니다. Server: $CURRENT_SERVER"
     echo "> set1을 할당합니다. IDLE_SERVER: set1"
     IDLE_SERVER=set1
     IDLE_PORT=8081
   fi
   
   echo "> $IDLE_SERVER 배포"
   sudo fuser -k -n tcp $IDLE_PORT
   sudo nohup java -jar -Dspring.config.location=file:/home/ec2-user/server/config/prod-application.yml -Dspring.profiles.active=$IDLE_SERVER /home/ec2-user/server/api-0.0.1.jar &
   
   echo "> $IDLE_SERVER 10초 후 Health check 시작"
   echo "> curl -s http://localhost:$IDLE_PORT/actuator/health "
   sleep 10
   
   for retry_count in {1..10}
   do
     response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
     up_count=$(echo $response | grep 'UP' | wc -l)
   
     if [ $up_count -ge 1 ]
     then
       echo "> Health check 성공"
       break
     else
       echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다."
       echo "> Health check: ${response}"
     fi
   
     if [ $retry_count -eq 10 ]
     then
       echo "> Health check 실패. "
       echo "> Nginx에 연결하지 않고 배포를 종료합니다."
       exit 1
     fi
     echo "> Health check 연결 실패. 재시도..."
     sleep 10
   done
   
   echo "> 스위칭을 시도합니다..."
   sleep 10
   
   /home/ec2-user/server/switch.sh
   ```



3. switch.sh

   ```
   #!/bin/bash
   echo "> 현재 구동중인 Port 확인"
   CURRENT_SERVER=$(curl -s https://happydodam.com/server)
   
   if [ $CURRENT_SERVER == set1 ]
   then
     IDLE_PORT=8082
     PROXY_PORT=8081
   elif [ $CURRENT_SERVER == set2 ]
   then
     IDLE_PORT=8081
     PROXY_PORT=8082
   else
     echo "> 일치하는 Server가 없습니다. Server:$CURRENT_SERVER"
     echo "> 8081을 할당합니다."
     IDLE_PORT=8081
     PROXY_PORT=8082
   fi
   
   echo "> 현재 구동중인 Port: $PROXY_PORT"
   
   echo "> 전환할 Port : $IDLE_PORT"
   echo "> Port 전환"
   echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
   
   echo "> Nginx Reload"
   sudo service nginx reload
   ```

   

4. batch.sh

   ```
   sudo java -jar 
   -Dspring.config.location=file:/home/ec2-user/server/config/batch-application.yml 
   -Dspring.batch.job.names=profileResetJob /home/ec2-user/server/batch-0.0.1.jar
   ```



## :four:  젠킨스 설정

#### 1. CI/CD

```
설정값
1. jenkins URL : jenkins서버 퍼브릭 IP(탄력 IP주입 후 세팅)
2. gitlab : credentials는 gitlab 아이디, 비밀번호로 만들어준다.
3. publish over ssh 플러그인 설치
4. Key에 Project pem키를 넣어준다. (ppk파일을 puttygen 이용해서 변환)
	Name : 아무거나 넣어도 OK
	hostname : 구매한 도메인을 넣거나, Project 퍼블릭 IP주소를 넣는다
	Username : 아무거나 Ok
	RemoteDirectory : Project 서버의 parents 경로를 넣어준다 (명령어 pwd 써서 위치 확인) 예시 : /home/ec2-user
5. GitLab Connection : global 설정으로 만들어 둔 것 선택
6. 소스코드 관리  
	Repository URL : project clone 주소를 적는다.
	Credentials : 
7. Branches to build
	Branch Specifier : 가져올 branch 선택(ex: develop)
8. Build
	Use Gradle Wrapper선택, Make gradlew executable 선택
	Wrapper location : ${workspace}는 위에서 설정한 parents 경로이고 gradlew이 있는 경로까지 써줘야함 (ex: ${workspace}/BackEnd/api)
	Tasks : 실행할 명령어 적으면 된다.(ex: clean build)
	Root Build script : 아마도 빌드하는 장소???
9. 빌드 후 조치
	Name : 4번에서 만들어둔 ssh 설정을 (Name이 뜰거임)집어 넣는다.
	source files : clone 한 폴더 최상위에서 build/libs까지의 경로를 적는다. (ex: BackEnd/api/build/libs/*.jar)
	Remove prefix : 경로 적는다. (ex: BackEnd/api/build/libs/)
	Remote directory : ${workspace} 다음 경로를 적는다. (ex: /home/ec2-user/server에서 /server만 입력)
	Exec command : 실행할 쉘 파일 입력 + 빠져 나올 수 있게 입력 해주자. (/home/ec2-user/server/stop.sh > /dev/null 2>&1) 쉘 파일 위치는 절대경로!
```



## :five:  NginX 설정

```bash
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;

# Load dynamic modules. See /usr/share/doc/nginx/README.dynamic.
include /usr/share/nginx/modules/*.conf;

events {
    worker_connections 1024;
}

http {
    client_max_body_size 30M;
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile            on;
    tcp_nopush          on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 4096;

    include             /etc/nginx/mime.types;
    default_type        application/octet-stream;

    # Load modular configuration files from the /etc/nginx/conf.d directory.
    # See http://nginx.org/en/docs/ngx_core_module.html#include
    # for more information.
    include /etc/nginx/conf.d/*.conf;

    server {
        listen       80;
        listen       [::]:80;
        server_name  happydodam.com, www.happydodam.com;
        root /user/share/nginx/html;

        return 301 https://$host$request_uri;
    }
    
    server {
        listen       443 ssl http2 default_server;
        listen       [::]:443 ssl http2 default_server;
        server_name  happydodam.com, www.happydodam.com;
        root         /usr/share/nginx/html;


        ssl_certificate /etc/letsencrypt/live/happydodam.com/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/happydodam.com/privkey.pem;
        ssl_session_cache shared:SSL:1m;
        ssl_session_timeout  10m;
        ssl_session_cache shared:MozSSL:10m;  # about 40000 sessions
        ssl_session_tickets off;

        ssl_protocols TLSv1.2 TLSv1.3;
        ssl_ciphers ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384;
        ssl_prefer_server_ciphers off;

        # Load configuration files for the default server block.
        include /etc/nginx/default.d/*.conf;
        include /etc/nginx/conf.d/service-url.inc;

        location / {
                include /etc/nginx/proxy_params;

                proxy_pass $service_url;
        }

        error_page 404 /404.html;
            location = /40x.html {
        }

        error_page 500 502 503 504 /50x.html;
            location = /50x.html {
        }
    }

}
```



## :six:  DB 접속 정보

※ 서버 DB 접속시 백엔드 팀원 IP포트만 개방해두었기 때문에, AWS RDS 인바운드 정책 변경 필수.

```
datasource:
    url: jdbc:mysql://dodamdodam-database.ck7amfqzdm1n.ap-northeast-2.rds.amazonaws.com:3306/dodamdodam
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: ssafy
```

