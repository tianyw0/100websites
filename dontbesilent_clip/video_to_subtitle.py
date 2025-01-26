import subprocess
import os
import glob
import whisper
from pathlib import Path
import numpy as np
import soundfile as sf
import argparse
from typing import Optional

def get_video_duration(video_path):
    """获取视频时长（秒）"""
    try:
        result = subprocess.run(
            ['ffmpeg', '-i', video_path],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            encoding='utf-8'
        )
        output = result.stderr
        
        for line in output.split('\n'):
            if 'Duration:' in line:
                time_str = line.split('Duration:')[1].split(',')[0].strip()
                h, m, s = time_str.split(':')
                total_seconds = float(h) * 3600 + float(m) * 60 + float(s)
                return total_seconds
                
        raise ValueError("无法在输出中找到 Duration 信息")
    except Exception as e:
        print(f"错误: {e}")
        return None

def extract_audio(video_path: str, audio_path: str) -> bool:
    """从视频中提取音频并返回是否成功"""
    try:
        subprocess.run([
            'ffmpeg', '-i', video_path,
            '-vn', '-acodec', 'pcm_s16le',
            '-ar', '44100', '-ac', '2',
            '-shortest', '-y',
            audio_path
        ], check=True)
        return True
    except subprocess.CalledProcessError as e:
        print(f"FFmpeg 错误: {e}")
        return False

def format_timestamp(seconds: float) -> str:
    """将秒数转换为 SRT 格式的时间戳"""
    hours = int(seconds // 3600)
    minutes = int((seconds % 3600) // 60)
    secs = int(seconds % 60)
    millis = int((seconds - int(seconds)) * 1000)
    return f"{hours:02d}:{minutes:02d}:{secs:02d},{millis:03d}"

def process_video(video_path: str, output_dir: Optional[str] = None, keep_audio: bool = False) -> None:
    """处理单个视频文件"""
    video_path = Path(video_path)
    if output_dir:
        output_dir = Path(output_dir)
        output_dir.mkdir(parents=True, exist_ok=True)
    else:
        output_dir = video_path.parent

    # 生成临时音频文件路径
    audio_path = output_dir / f"{video_path.stem}.wav"
    srt_path = output_dir / f"{video_path.stem}.srt"

    print(f"\n处理视频: {video_path.name}")
    
    # 提取音频
    if not extract_audio(str(video_path), str(audio_path)):
        print("音频提取失败，跳过转录")
        return

    try:
        print("正在加载 Whisper 模型...")
        model = whisper.load_model("small", device="cpu")
        
        print("使用设备: CPU")
        print("开始转录...")
        result = model.transcribe(
            str(audio_path),
            language="zh",
            verbose=True,
            fp16=False,
            # 添加以下参数来控制分段
            word_timestamps=True,    # 启用词级时间戳
            # max_words_per_line=10,    # 每行最多显示的字数
            condition_on_previous_text=False,  # 减少上下文依赖
            no_speech_threshold=0.6  # 提高静音检测的灵敏度
        )
        
        # 写入字幕文件
        with open(srt_path, 'w', encoding='utf-8') as f:
            for i, segment in enumerate(result["segments"], 1):
                f.write(f"{i}\n")
                f.write(f"{format_timestamp(segment['start'])} --> {format_timestamp(segment['end'])}\n")
                f.write(f"{segment['text'].strip()}\n\n")

        print(f"字幕文件已生成: {srt_path}")

    except Exception as e:
        print(f"转录过程出错: {str(e)}")
    finally:
        # 如果不保留音频文件，则删除
        if not keep_audio and audio_path.exists():
            audio_path.unlink()

def main():
    parser = argparse.ArgumentParser(description='视频字幕提取工具')
    parser.add_argument('input', help='输入的视频文件或文件夹路径')
    parser.add_argument('-o', '--output', help='输出目录路径（可选）')
    parser.add_argument('--keep-audio', action='store_true', help='保留中间生成的音频文件')
    
    args = parser.parse_args()
    input_path = Path(args.input)
    
    if input_path.is_file():
        # 处理单个文件
        process_video(input_path, args.output, args.keep_audio)
    
    elif input_path.is_dir():
        # 处理目录下的所有视频文件
        video_files = list(input_path.glob("*.mp4"))
        total_files = len(video_files)
        
        for idx, video_file in enumerate(video_files, 1):
            print(f"\n[{idx}/{total_files}] 处理文件")
            process_video(video_file, args.output, args.keep_audio)
    
    else:
        print(f"错误：输入路径 '{args.input}' 不存在")

if __name__ == "__main__":
    main()