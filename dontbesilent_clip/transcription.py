import os
import whisper
from pathlib import Path
import numpy as np
import soundfile as sf

def format_timestamp(seconds):
    # 将秒数转换为 SRT 格式的时间戳 (HH:MM:SS,mmm)
    hours = int(seconds // 3600)
    minutes = int((seconds % 3600) // 60)
    secs = int(seconds % 60)
    millis = int((seconds - int(seconds)) * 1000)
    return f"{hours:02d}:{minutes:02d}:{secs:02d},{millis:03d}"

def transcribe_wav_files(input_dir):
    # 加载 Whisper 模型
    model = whisper.load_model("medium", device="cpu")
    
    # 获取所有 wav 文件
    wav_files = list(Path(input_dir).glob("*.wav"))
    total_files = len(wav_files)
    
    for idx, wav_path in enumerate(wav_files, 1):
        # 获取字幕文件路径
        output_file = wav_path.with_suffix('.srt')
        
        # 如果输出文件已存在，跳过处理
        if output_file.exists():
            print(f"跳过已存在的文件: {output_file}")
            continue
            
        print(f"正在处理: {wav_path} ({idx}/{total_files})")
    
        try:
            # 读取音频文件
            audio, sample_rate = sf.read(str(wav_path))
            
            # 计算每分钟的采样点数
            samples_per_minute = sample_rate * 60
            
            # 创建空的字幕文件
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write('')
            
            segment_count = 1
            total_minutes = len(audio) // samples_per_minute + 1
            
            for minute in range(total_minutes):
                # 获取当前分钟的音频片段
                start_sample = minute * samples_per_minute
                end_sample = min((minute + 1) * samples_per_minute, len(audio))
                audio_segment = audio[start_sample:end_sample]
                
                # 保存临时音频文件
                temp_wav = wav_path.parent / f"temp_segment_{minute}.wav"
                sf.write(temp_wav, audio_segment, sample_rate)
                
                # 转录当前片段
                result = model.transcribe(
                    str(temp_wav),
                    language="zh",
                    verbose=True,
                    fp16=False
                )
                
                # 写入字幕
                with open(output_file, 'a', encoding='utf-8') as f:
                    for segment in result["segments"]:
                        # 调整时间戳以匹配实际时间
                        adjusted_start = segment["start"] + (minute * 60)
                        adjusted_end = segment["end"] + (minute * 60)
                        
                        f.write(f"{segment_count}\n")
                        f.write(f"{format_timestamp(adjusted_start)} --> {format_timestamp(adjusted_end)}\n")
                        f.write(f"{segment['text'].strip()}\n\n")
                        segment_count += 1
                
                # 删除临时文件
                temp_wav.unlink()
                
                print(f"已完成第 {minute+1}/{total_minutes} 分钟的转录")
            
            print(f"字幕文件生成完成: {output_file}")
            
        except Exception as e:
            print(f"处理文件 {wav_path} 时出错: {str(e)}")

if __name__ == "__main__":
    input_directory = "/Users/tianyw/bt_downloads/dontbesilent"
    transcribe_wav_files(input_directory)