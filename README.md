# 兴趣社交 App（Android）

基于 PRD V1.1 的 Kotlin + Jetpack Compose 原生 Android 演示项目。

## 已实现

- 首页：关注/兴趣/广场切换、活动 Banner、兴趣标签、双列动态流、搜索入口、发布入口。
- 我的：个人资料、社交统计、资料/动态/赞过 Tab、个人信息、加入的圈子。
- 消息：看过你、回复与@、获赞、新粉丝入口，会话列表和未读角标。
- 圈子：分类切换、圈子列表、加入交互。
- 身边：在线/附近切换、圈子筛选、用户资料入口、打招呼反馈。
- 深色主题，底部五 Tab 导航。

## 当前版本边界

PRD 未定义后端 API、数据库、账号体系和消息协议。本版本使用本地演示数据，详情页和部分入口为交互占位；附近页显示定位接入提示。真实登录、云端数据、实时私信、推送和地图定位需后续接入服务端。

## 构建

### 本地（推荐）

要求：
- JDK 17 或 21
- Android SDK Platform 35
- Android Build Tools

1. 用 **Android Studio** 打开本项目根目录
2. 等待 Gradle Sync 完成
3. 菜单 **Build → Build Bundle(s) / APK(s) → Build APK(s)**

或命令行：

```bash
chmod +x ./gradlew
./gradlew :app:assembleDebug
```

APK 输出路径：
```
app/build/outputs/apk/debug/app-debug.apk
```

### GitHub Actions 自动构建

推送到 GitHub 后（任意分支），`.github/workflows/android.yml` 会自动：

1. 构建 Debug APK
2. 上传为 Workflow Artifact（名称：`interest-social-debug-apk`）

你也可以在 GitHub 仓库的 **Actions** 页面手动触发（workflow_dispatch）。

下载方式：
1. 进入仓库 → Actions
2. 点击最新成功的 workflow run
3. 在 Artifacts 区域下载 `interest-social-debug-apk`
4. 解压即可得到 `app-debug.apk`

## 项目结构

```
InterestSocialApp/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/example/interestsocial/MainActivity.kt
├── .github/workflows/android.yml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
└── gradle/wrapper/
```
