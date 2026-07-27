#!/bin/bash
# APK 合并脚本
# 用法: 在下载所有 her-part-XX 文件后运行此脚本
# 合并完成后得到 her-final.apk，可直接安装

echo "正在合并 APK 分片..."
cat her-part-00 her-part-01 her-part-02 her-part-03 her-part-04 her-part-05 her-part-06 her-part-07 > her-final.apk

if [ $? -eq 0 ]; then
    echo "合并完成! 文件: her-final.apk"
    ls -lh her-final.apk
    echo ""
    echo "请将 her-final.apk 传输到 Android 设备并安装。"
    echo "安装前请确保已开启「未知来源」权限。"
else
    echo "合并失败，请检查所有分片是否已完整下载。"
    exit 1
fi
