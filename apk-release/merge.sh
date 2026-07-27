#!/bin/bash
# 重组分片为完整 APK
# 使用方法: bash merge.sh
# 依赖: cat (系统自带)

set -e

echo "正在合并 APK 分片..."
cat her-cracked-part-00 her-cracked-part-01 her-cracked-part-02 her-cracked-part-03 \
    her-cracked-part-04 her-cracked-part-05 her-cracked-part-06 her-cracked-part-07 \
    > her-cracked.apk

echo "合并完成: her-cracked.apk"
echo "文件大小: $(du -h her-cracked.apk | cut -f1)"

# 校验文件完整性
echo ""
echo "请校验 MD5:"
md5sum her-cracked.apk
echo ""
echo "预期 MD5:"
echo "（请与 release 页面的 MD5 对比）"
echo ""
echo "安装方法:"
echo "  adb install her-cracked.apk"
echo "  或将文件传输到手机后直接点击安装"
