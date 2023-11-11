# Team5_BE

![순수웨딩 소개](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/ebb9b772-69cd-413d-9d17-cb10fefdf714)

> 목차 
> - [📌 프로젝트 소개](#프로젝트-소개)
> - [👩‍👩‍👧‍👧 팀원 소개](#팀원-소개)
> - [✏️ 주요 기능](#주요-기능)
> - [🔗 링크 모음(배포 주소 포함)](#링크-모음)
> - [📜 ERD](#erd)
> - [📄 API 모아보기](#api-모아보기)
> - [📁 파일 구조](#파일-구조)
> - [🚩 시작 가이드](#시작-가이드)
> - [🖥️ 기술 스택](#기술-스택)
> - [©️ License: The MIT License (MIT)](#license)

<br>

## 프로젝트 소개

### 개발 동기 및 목적
 예비 부부의 경우 결혼과 관련된 정보를 찾기 위해서는 웨딩 박람회, 온라인 검색, SNS 등 여러 방법을 사용하지만, 어디를 알아봐도 복잡하기만 하고 가격도 천차만별이라 선택하기 어렵습니다. 또한, 결혼 시장의 경우 일회성 고객이 대부분이다 보니 소비자보다 판매자 위주의 시장이 형성되어 왔습니다. 이에 따라 결혼 비용을 공개하지 않아 지인 소개 등으로 가면 훨씬 싸게 해주는 등 소비자 입장에선 모르고 당하기 십상입니다.

 웨딩 플래너의 경우 무급으로 시작해서 실력이 아닌 연차가 쌓일 때까지는 최저시급에도 미치지 못하는 기본급을 받으며 열정 페이를 강요받아 어려움을 겪고 있었습니다.

✨ 저희 `순수웨딩` 팀은 앞선 웨딩플래너의 결혼 준비 비용 불투명성 문제와 웨딩 플래너의 급여 공정성 문제를 해결하기 위해 투명한 가격으로 이 둘을 매칭 해주는 서비스를 기획하게 되었습니다. ✨

<br>

### 서비스 소개
> 투명한 가격으로 웨딩 플래너와 예비 부부를 매칭하다, '순수웨딩'

1. ✏️ 투명한 웨딩 플래너의 **포트폴리오**
   - 웨딩 플래너가 직접 등록한 자기 소개, 예상 가격, 사진, 사용자 리뷰등 볼 수 있어요
   - `순수 웨딩 멤버십`에 가입하면 플래너의 실제 이전 계약 기록(업체, 가격, 날짜 등)을 볼 수 있어요
   - 마음에 드는 플래너를 찜할 수 있어요!
     
2. 🗨️ 내 PICK! 플래너에게 **채팅 상담받기**
   - 원하는 플래너들과 채팅으로 상담받으며 결혼 준비를 할 수 있어요
     
3. 📜 **견적서**로 결혼 진행 과정을 서로 공유해요
   - 채팅방에서 견적서를 만들어서 업체 이름, 가격 등을 확인하고 진행 상황을 한눈에 확인할 수 있어요
   - 전체 견적서를 확정하면 해당 플래너에 대한 리뷰를 작성할 수 있어요
    
4. 💯 **리뷰**를 통해 생생한 결혼 진행 후기 확인하기
   - 다른 사람이 실제로 플래너와 진행한 후기를 들려줘요 이를 기반으로 더 좋은 플래너를 선택할 수 있어요
   - 해당 내용은 포트폴리오 상세 페이지에서 확인할 수 있어요
  
<br>

### 개발 기간
2023.09-11 (카카오 테크 캠퍼스 1기 - 3단계 진행 기간)

<br>

## 팀원 소개
| | <img src="https://github.com/1jeongg.png" width="100"> | <img src="https://github.com/seokwns.png" width="100"> | <img src="https://github.com/chaee813.png" width="100"> | <img src="https://github.com/Rizingblare.png" width="100"> |
|:--:|:----------:|:----------:|:----------:|:----------:|
| 이름 | [남원정](https://github.com/1jeongg) | [문석준](https://github.com/seokwns) | [천영채](https://github.com/chaee813)|[김정도](https://github.com/Rizingblare) |
| 개발 범위 | 회원, 결제, 찜하기 | 토큰, 이메일, 포트폴리오, 견적서 | 채팅, 매칭, 견적서, 리뷰 | 포트폴리오 |

<br>


## 주요 기능
> - ✏️ 포트폴리오
> - 🗨️ 채팅 상담
> - 📜 견적서
> - 💯 리뷰

|분류|기능1|기능2|
|------|------|---|
|포트폴리오| ![포트폴리오 조회](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/add60899-8a8f-433f-9ca0-dd68d22970cb) | ![포트폴리오 등록](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/65bad53a-0916-40fe-9e3b-7f001034c890) |
|         | • 플래너가 등록한 정보, 이미지, 리뷰 등 조회 <br> • 검색과 필터링 <br> • 멤버십 사용자 - 지난 계약 정보(진행 가격, 업체 등) 조회  <br> • 찜하기  | • 플래너의 소개, 가격 등의 정보를 등록 <br> • 수정 및 삭제 |
|채팅 상담| ![채팅 상담](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/5741cb65-a3ff-4260-954d-be354dc25964)| | 
|견적서| ![예비 부부 견적서](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/20fbc800-3808-4e73-a545-63356b9ff47c) | ![플래너 견적서](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/764d33a5-e988-4667-9329-ccbc245169df) |
|  |  • 견적서 전체 리스트 조회 <br>  • 견적서 매칭별 리스트 조회 <br>  • 매칭 확정 |  • 견적서 등록, 수정, 삭제 <br> • 견적서 1개 확정 |
| 리뷰 | ![리뷰](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/7e4d7927-9f1d-4468-b062-b598f80cb84b) | |
| | • 리뷰 전체 리스트 조회 <br> • 리뷰 플래너별 리스트 조회 <br> • 리뷰 상세 조회 <br> • 등록 수정 삭제  | |


<br>


## 링크 모음
| 기획 | 디자인 | 개발 | 배포 |
|------|-------|------|------|
| [노션](https://unmarred-belief-362.notion.site/f11783c42731479ca1f6c9a97a738324?pvs=4) | [와이어프레임](https://www.figma.com/file/Wcz9d59GIQqwTEAfIEmRly/SUNSU-WEDDING?type=design&node-id=0%3A1&mode=design&t=5PnD2GPs3WktbNNy-1) | [프론트 깃허브](https://github.com/Step3-kakao-tech-campus/Team5_FE) | [풀스택 배포 주소](https://k6f3d3b1a0696a.user-app.krampoline.com) |
| [기획안 PPT](https://www.figma.com/file/A81sCNiwoLUfSe5aqXEHXr/%EC%B9%B4%ED%85%8C%EC%BA%A0-5%EC%A1%B0---%EA%B8%B0%ED%9A%8D%EC%95%88?type=design&node-id=972-145&mode=design&t=QdcoBgtPZi631myk-0) |  | [ERD](https://www.erdcloud.com/d/fww6HRj7oXL9cdp5t) | [프론트 테스트용 배포 주소](https://k5c1813d97f50a.user-app.krampoline.com/)
| [Survey](https://unmarred-belief-362.notion.site/Survey-a4c9ba9b8114456f88faa61b33f232c9) | | [API 문서](https://unmarred-belief-362.notion.site/6fd74038970941a2ad02df0045705095?v=545b8da990c74661b2b6b560009766ff) | |
| | | [테스트 시나리오 명세서](https://unmarred-belief-362.notion.site/BE-3d4e69e38d6a498d8f9e790945660790?pvs=4) | |
| | | [테스트 결과 보고서](https://unmarred-belief-362.notion.site/BE-d5eade6a1d6f4bed9933eebd5899b6a8?pvs=4) | |

<br>

## ERD
![ERD](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/2f6cf5f5-64d5-4a89-9b84-a9f552b9cc6d)

## API 모아보기
<img width="600" alt="API 모아보기" src="https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/4d1b3f33-9411-438d-b78e-756db7fa121d">

## 파일 구조
```
└───📂src
    ├───📂main
    │   ├───📂generated
    │   ├───📂java.com.kakao.sunsuwedding
    │   │               ├───📁chat
    │   │               ├───📁favorite
    │   │               ├───📁match
    │   │               ├───📁payment
    │   │               ├───📁portfolio
    │   │               │   ├───📁cursor
    │   │               │   ├───📁image
    │   │               │   └───📁price
    │   │               ├───📁quotation
    │   │               ├───📁review
    │   │               │   └───📁image
    │   │               ├───📁user
    │   │               │   ├───📁base_user
    │   │               │   ├───📁constant
    │   │               │   ├───📁couple
    │   │               │   ├───📁mail
    │   │               │   ├───📁planner
    │   │               │   └───📁token
    │   │               └───📁_core
    │   │                   ├───📁constants
    │   │                   ├───📁errors
    │   │                   │   └───📁exception
    │   │                   ├───📁security
    │   │                   └───📁utils
    │   └───📂resources
    │       ├───📁db
    │       └───📁env
    └───📂test
        └───📂java.com.kakao.sunsuwedding
            ├───📁chat
            ├───📁favorite
            ├───📁match
            ├───📁payment
            ├───📁portfolio
            │   ├───📁image
            │   └───📁price
            ├───📁quotation
            ├───📁review
            ├───📁user
            ├───📁util
            └───📁_core
```

<br>

## 시작 가이드

> Requirements: Java 17, Spring 3.1.4
> 
> 순수웨딩 환경 변수 설정이 완료되어야 프로젝트가 실행됩니다.

1. 프로젝트 클론
```
git clone https://github.com/Step3-kakao-tech-campus/Team5_BE.git
cd Team5_BE
cd sunsu-wedding
```

2. 실행
```
./gradlew build
cd build
cd libs
java -jar sunsu-wedding-0.0.1-SNAPSHOT.jar
```

<br>

## 기술 스택

![기술 스택)](https://github.com/Step3-kakao-tech-campus/Team5_BE/assets/84652886/1710fc38-9fc3-44d1-b9f9-e2893129eda1)

<br>

## License

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

