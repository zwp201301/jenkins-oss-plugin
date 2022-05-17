# jenkins-oss-plugin
H5部署到CDN

## 1、mvn package -DskipTests
## 2、将上一步打包得到的hpi文件安装到jenkins插件
## 3、pipeline的使用示例：
```
uploadFile debug: true, ossType: 'aliyun', debug:true, targetPath: 'dist', bucketName: 'html5'
```
