# AIHer - 完全仿照原版

完全仿照 her-latest.apk (v1.0.19) 的重签名版本，保留所有功能和完整开发运行时。

## APK 信息

- **原始大小**: 701 MB
- **版本**: 1.0.19
- **包名**: com.perhaps.her
- **签名**: 自签名 (AIHer keystore)

## 下载和安装

### 方法一：直接下载分片并合并

1. 下载以下所有文件到同一目录：
   - `her-part-00` ~ `her-part-07` (8个分片)
   - `merge_apk.sh` (合并脚本)

2. 运行合并脚本：
   ```bash
   chmod +x merge_apk.sh
   ./merge_apk.sh
   ```

3. 得到 `her-final.apk`，传输到手机安装

### 方法二：直接下载（如果网络允许）

访问 GitHub Release 页面下载完整 APK。

## 包含的完整运行时

- OpenJDK 17 (192 MB)
- Gradle 7.5 (166 MB)
- Android JDK (130 MB)
- Kotlin 编译器 (60 MB)
- Python 3.12 (39 MB)
- Ubuntu Base 22.04 (27 MB)
- Android SDK build-tools (26 MB)
- MNN AI 推理引擎
- Flutter SDK
- proot (Linux 环境模拟)

## 功能列表

- AI 对话生成应用
- AI 配置（支持 OpenAI/Claude/Gemini）
- 功能商店
- 插件管理
- 连接 Mac
- Root VM / Frida
- 终端环境
- 桌面扩展
- Web App 生成
- 文件更改编辑器
- 无障碍服务
- 后台任务服务

## 注意事项

- 安装前请开启「未知来源」权限
- 首次运行需要解压运行时环境，请耐心等待
- 建议预留至少 2GB 存储空间
