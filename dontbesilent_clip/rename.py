import os
from datetime import datetime
import glob

def timestamp_to_datetime(timestamp_ms):
    # 将毫秒时间戳转换为秒
    timestamp_s = timestamp_ms / 1000
    # 转换为datetime对象
    dt = datetime.fromtimestamp(timestamp_s)
    # 格式化为年月日时分秒
    return dt.strftime('%Y%m%d_%H%M%S')

def rename_files(directory):
    # 获取目录下所有的mp4文件
    mp4_files = glob.glob(os.path.join(directory, '*.mp4'))
    
    for file_path in mp4_files:
        try:
            # 获取文件名（不含路径）
            filename = os.path.basename(file_path)
            # 获取时间戳部分（第一个下划线之前的部分）
            timestamp_str = filename.split('_')[0]
            # 转换为整数
            timestamp_ms = int(timestamp_str)
            
            # 生成新的文件名
            new_datetime = timestamp_to_datetime(timestamp_ms)
            # 保持原文件名中下划线后面的部分
            remaining_parts = '_'.join(filename.split('_')[1:])
            new_filename = f"{new_datetime}_{remaining_parts}"
            
            # 构建新的完整文件路径
            new_file_path = os.path.join(directory, new_filename)
            
            # 重命名文件
            os.rename(file_path, new_file_path)
            print(f"已重命名: {filename} -> {new_filename}")
            
        except Exception as e:
            print(f"处理文件 {filename} 时出错: {str(e)}")

if __name__ == "__main__":
    directory = "/Users/tianyw/bt_downloads/dontbesilent"
    rename_files(directory)