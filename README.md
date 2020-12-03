# 2주안에 FlO를 만들어보자!

## 현재 주요기능
- 스플래시 스크린
- 음악재생화면 ( 음악정보, 가사(하이라이팅), SeekBar, play/stop )
- 전체가사보기 (특정 가사 이동 토글 버튼, 닫기버튼, SeekBar, play/stop )

## 동작 화면
![flo (2)](https://user-images.githubusercontent.com/74610959/100705466-929c1b00-33ea-11eb-94b1-9bbc3f8aecab.gif)

## 서버에서 받은 JSON 파일
{  
 ...  
  "singer": "챔버오...",  
  "album": "캐롤",  
  "title": "We Wish You A Mer..s",  
  "duration": 198,  
  "image": ".../cover.jpg",  
  "file": ".../music.mp3",  
  "lyrics": "[00:16:200]we wish you a merry chr..."  
  ...  
}

## 사용한 라이브러리
- MVVM패턴을 위한 Livedata
- 통신 : Retrofit + RxJava
- 이미지 : Picasso
- 의존성 : Koin
- com.android.support -> AndroidX(참조 : https://thdev.tech/google%20io/2018/05/12/Android-New-Package-AndroidX/)
- 비디오, 오디오 : Exoplayer
- sliding-up-panal : sorhree

### sliding-up
##### implementation 'com.sothree.slidinguppanel:library:3.4.0'

### EXO-player
##### implementation "com.google.android.exoplayer:exoplayer-core:2.12.0"
##### implementation "com.google.android.exoplayer:exoplayer-ui:2.12.0"
##### implementation "com.google.android.exoplayer:exoplayer-hls:2.12.0"

### androidx
##### implementation 'androidx.appcompat:appcompat:1.2.0'
##### implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
##### implementation 'androidx.core:core-ktx:1.3.2'
##### implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'

### rxjava
##### implementation "io.reactivex.rxjava2:rxjava:2.2.0"
##### implementation "io.reactivex.rxjava2:rxandroid:2.0.2"

### koin
##### implementation "org.koin:koin-androidx-scope:1.0.2"
##### implementation "org.koin:koin-androidx-viewmodel:1.0.2"

### picasso
##### implementation 'com.squareup.picasso:picasso:2.5.2'

### retrofit
##### implementation 'com.squareup.retrofit2:retrofit:2.9.0'
##### implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
##### implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
##### implementation 'com.squareup.retrofit2:retrofit-mock:2.9.0'
##### implementation 'com.android.support:design:28.0.0'
