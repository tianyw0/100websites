import os
import whisper
from pathlib import Path

def transcribe_wav_files(input_dir):
    # 加载 Whisper 模型
    model = whisper.load_model("medium", device="cpu")
    
    # 获取所有 wav 文件
    wav_files = list(Path(input_dir).glob("*.wav"))
    total_files = len(wav_files)
    
    for idx, wav_path in enumerate(wav_files, 1):
        # 获取不带扩展名的文件名
        output_file = wav_path.with_suffix('.txt')
        
        # 如果输出文件已存在，跳过处理
        if output_file.exists():
            print(f"跳过已存在的文件: {output_file}")
            continue
            
        print(f"正在处理: {wav_path} ({idx}/{total_files})")
        
        try:
            # 使用 Whisper 进行转录
            result = model.transcribe(
                str(wav_path),
                language="zh",  # 使用 zh-cn 指定简体中文
                verbose=True,
                fp16=False
            )
            
            # 将转录结果写入文本文件
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(result["text"])
            
            print(f"转录完成: {output_file}")
            
        except Exception as e:
            print(f"处理文件 {wav_path} 时出错: {str(e)}")

if __name__ == "__main__":
    input_directory = "/Users/tianyw/bt_downloads/dontbesilent"
    transcribe_wav_files(input_directory)