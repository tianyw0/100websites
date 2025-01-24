import subprocess
import ffmpeg

def get_video_duration(video_path):
    """ 获取视频时长（秒） """
    try:
        result = subprocess.run(
            ['ffmpeg', '-i', video_path],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            encoding='utf-8'  # 使用 utf-8 编码
        )
        # ffmpeg 将信息输出到 stderr
        output = result.stderr  # 直接使用 stderr，不需要 decode()
        
        # 查找 Duration 行
        for line in output.split('\n'):
            if 'Duration:' in line:
                # 提取时间字符串 "HH:MM:SS.ms"
                time_str = line.split('Duration:')[1].split(',')[0].strip()
                h, m, s = time_str.split(':')
                # 转换为秒
                total_seconds = float(h) * 3600 + float(m) * 60 + float(s)
                return total_seconds
                
        raise ValueError("无法在输出中找到 Duration 信息")
    except Exception as e:
        print(f"错误: {e}")
        return None

def seconds_to_hms(seconds):
    """将秒数转换为时:分:秒格式"""
    hours = int(seconds // 3600)
    minutes = int((seconds % 3600) // 60)
    seconds = int(seconds % 60)
    return f"{hours:02d}:{minutes:02d}:{seconds:02d}"

def extract_audio(video_path, audio_path):
    """ 从视频中提取音频并确保音频时长与视频一致 """
    video_duration = get_video_duration(video_path)
    if video_duration:
        print(f"视频时长: {seconds_to_hms(video_duration)} ({video_duration:.2f}秒)")
        try:
            # 使用 subprocess 直接调用 ffmpeg 命令
            subprocess.run([
                'ffmpeg', '-i', video_path,
                '-vn',  # 不处理视频
                '-acodec', 'pcm_s16le',
                '-ar', '44100',
                '-ac', '2',
                '-shortest',  # 确保输出不超过输入时长
                '-y',  # 覆盖已存在的文件
                audio_path
            ], check=True)
            
            # 验证生成的音频文件时长
            audio_duration = get_video_duration(audio_path)
            print(f"生成的音频时长: {seconds_to_hms(audio_duration)} ({audio_duration:.2f}秒)")
        except subprocess.CalledProcessError as e:
            print(f"FFmpeg 错误: {e}")
    else:
        print("无法提取音频，视频时长获取失败")

# 处理指定目录下的所有 MP4 文件
import os
import glob

# 指定视频文件夹路径
video_folder = '/Users/tianyw/bt_downloads/dontbesilent'

# 获取所有 MP4 文件
video_files = glob.glob(os.path.join(video_folder, '*.mp4'))

# 处理每个视频文件
for video_file in video_files:
    # 生成对应的音频文件路径（保持相同文件名，改为 wav 后缀）
    audio_file = os.path.splitext(video_file)[0] + '.wav'
    print(f"\n处理视频: {os.path.basename(video_file)}")
    extract_audio(video_file, audio_file)
