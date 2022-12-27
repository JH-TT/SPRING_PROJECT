# SPRING_PROJECT
스프링 개인 플젝

- 점프투 스프링부트 참고.
- 점프투 스프링부트를 전부 따라하진않고(Builder를 사용하거나 DTO 생성 등 조금 다른 부분도 있음.) 진행방향만 비슷.
- 전부 끝낸 뒤 UI 수정, 기능추가 등 개선시켜볼 계획.

## 점프 투 스프링 부트와 다른 부분

- 질문 혹은 댓글을 작성하거나 수정하면 자동으로 적용되도록 구현.
  - EnableJpaAuditing을 이용.
- 엔티티와 DTO를 사용하려고 노력중.
  - Question, Answer, Comment를 DTO로 변경완료했다.
  - View, Controller, Service끼리는 DTO로 주고받고 Service와 Repository사이는 Entity로 주고받는다.
  - 서로 관계있는 상태에도 DTO로 가능한지는 의문 예를들면, QuestionDTO.getAnswerList할때 answerList가 DTO로 구성되어야 하는지 엔티티인지 의문.
    -일단은 엔티티로 함. Test할때는 DTO로 하니까 해당 엔티티를 못찾는거 같아서(아닐 확률 높음. 다시 시도해볼거임.) 엔티티로 저장하기로 결정.
- DB는 H2가 아닌 MariaDB를 사용.

## 오류가 났던 부분과 어떻게 해결했는지

- 렌더링 오류
  - 보통 SpEL문제거나 데이터가 null로 넘어올때 생겼음.
    - 만약 템플릿을 딱히 건든게 없었는데 오류가 난다면 바로 컨트롤러부분에서 데이터가 잘 넘어가는지 확인할것.
    - 대부분은 오류로그에 어떤 부분이 null인지 알려주지만, 나는 잘 모르겠다면 그냥 무지성 주석처리하면서 어디가 오류인지 파악했다.(아니면 디버깅을 해도 괜찮다) 
- N+1오류
  - 내가 대댓글을 구현했는데, 대댓글을 작성할때마다 댓글이 증식되는 일이 있었음.
  - repository에서 join fetch를 통해 해결했다.
- join fetch 오류
  - 그냥 join fetch를 하니까 디폴트로 inner join이 되다보니 댓글이 없는 질문에 들어가면 오류발생함.
  - left join fetch로 해결했다.

## 기능들

### 로그인창
![image](https://user-images.githubusercontent.com/79801565/208492281-47873c1f-3ca5-4537-a404-b7c94da3f3c4.png)

- 소셜로그인 미구현
- 비밀번호 보이기 구현완료
- 아이디 기억하기 미구현

### 회원가입
![회원가입](https://user-images.githubusercontent.com/79801565/208492552-1d40b3f6-d686-4c4c-8fdc-e53d6e63b12f.png)

- 아직 꾸미기는 안함
- 회원가입을 누르면 잘 입력했는지 확인하고 잘못입력했거나 빈곳이 있으면 경고창 발생하도록 구현.
- 아이디 중복확인 구현예정
- 더 정확한 이메일형식 확인을 위한 정규식 수정예정
- 비밀번호 규정 추가예정

### 질문목록
![질문목록](https://user-images.githubusercontent.com/79801565/208492956-2c8e34aa-de72-4bca-b12c-0aa09ddb322d.png)

- 검색기능, 질문등록기능, 페이징기능 구현완료
- 페이징이 한번에 5개에서 최대 7개까지 늘어나서 그냥 5개 혹은 10개로 고정할 예정.

### 질문상세
![질문상세](https://user-images.githubusercontent.com/79801565/208493337-2f95937e-54ab-45ee-a04f-0eb14edb2dc2.png)

- 제목, 내용, 글쓴이, 작성날짜, 수정날짜, 추천, 수정, 삭제 까지 구현완료
- 댓글, 대댓글기능도 구현
  - 댓글은 로그인이 돼있어야 작성가능하도록 구현
  - 대댓글도 로그인이 돼있어야 댓글에서 작성가능하도록 구현
- 좀 더 깔끔하게 보이도록 CSS수정예정
- 조회수 추가예정

### 댓글, 대댓글
![댓글대댓글](https://user-images.githubusercontent.com/79801565/208493949-7f93363c-d1af-4207-90c0-ff1ce7b40b38.png)
![image](https://user-images.githubusercontent.com/79801565/208494132-77550dcf-3be6-45da-867c-cf6ad18e745b.png)

- 유저아이디를 확인하고 맞으면 수정, 삭제버튼 활성화
- 로그인만 되면 답변등록버튼 활성화되도록 구현

### 전체적으로 추가, 수정할 내용

- 지금 엔티티로만 확인하고 있다보니 아이디를 삭제하고 같은 아이디로 만들어도 이전에 썼던 질문이나 댓글이 그대로 이어질것임.
  - 만약 아이디를 삭제하고 다시 같은 아이디로 가입을 하면 기존에 있던 기록들이 없어져야함.
  - 방법 1. 아이디를 삭제할때 DB에 아예삭제하지않고 null값으로 처리한다. 그러고 같은 아이디로 가입하면 임의로 아이디에 숫자같은걸 넣어서 서로 다른 아이디로 실행되도록 한다.
  - 방법 2. 아이디를 삭제할때 해당 아이디의 기록들(질문, 댓글, 대댓글)을 삭제한다.(그런데 커뮤니티같은곳을 보면 질문글 같은건 남기는듯. 이방법은 안할거 같다.)
- 마이페이지 추가예정
  - 기본정보, 질문, 댓글, 대댓글 작성기록 보여주도록 구현할 예정
- 등급시스템도 구현할 예정(안할수도 있음)