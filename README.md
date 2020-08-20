[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
# Movie-Poster-Android

## Intended precondition
- Didn't use 3rd party library except for following list
  - `io.reactivex.rxjava3:rxandroid:3.0.0`
  - `io.reactivex.rxjava3:rxjava:3.0.4`
  - `org.apache.httpcomponents.client5:httpclient5:5.0.1`
  
- Supposed to use local data as `String` that will be loaded by network

## Features
- File cache
- Show how many bytes are being downloaded
- Show State either `downloading` or `completed`
- Show image after image file downloaded
- Click the event to show the next image
