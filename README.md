# Opus编解码库-Android端

[![JitPack Version](https://jitpack.io/v/chennevwin/opus-codec-utils-android.svg)](https://jitpack.io/#chennevwin/opus-codec-utils-android)

本项目基于opus开源库[libopus](https://opus-codec.org/)采用JNI封装的一套Android端的编解码库

采用C++和Kotlin作为主要开发语言

## 支持功能

支持pcm编码成opus

支持opus解码成pcm

支持Android 16kb page size

## 如何使用

### 依赖导入

在工程模块中gradle中导入依赖

```kotlin
    implementation("com.github.chennevwin:opus-codec-utils-android:${version}")
```

注意version需要自行填入相关版本号，可以从本文档最上面jitpack图标中查看，也可以在jitpack官网中查看

### opus编码

```kotlin
    //初始化
    val encoder = OpusEncoder() //创建编码器
    encoder.init(16000/*采样率*/, 1/*通道数*/, OpusApplications.OPUS_APPLICATION_VOIP/*应用场景*/) //初始化编码器
    //根据需求设置属性
    // encoder.setVBR(false) //设置vbr模式，若为false则代表cbr模式
    // encoder.setVBRConstraint(false) //vbr模式时候是否启用vbr约束
    // encoder.setComplexity(8) //设置编码质量
    // encoder.setSignal(OpusSignal.OPUS_SIGNAL_VOICE) //设置编码信号类型
    // encoder.setBitrate(16000) //设置比特率
  
    //编码将pcm转为opus
    val opusAudio = encoder.encode(pcmBytes/*pcm音频数据*/, 960/*frame_size*/) 
  
    //释放
    encoder.release()
```

### opus解码

```kotlin
    //初始化
    val decoder = OpusDecoder() //创建解码器
    decoder.init(16000/*采样率*/, 1/*通道数*/) //初始化解码器
  
    //解码工作
    val pcmAudio = decoder.decode(opusBytes/*一包opus数据*/, ret.size/*数据长度*/, 960/*frame_size*/, 0/*fec*/)
  
    //释放
    decoder.release()
```
