# SPRING_PROJECT
스프링 개인 플젝

## 사용
- Spring boot 2.7.4
- MariaDB
- Thymeleaf
- Redis

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
- _csrf토큰 오류
  - 질문, 댓글폼에 가면 _csrf토큰 오류가 발생했다. 왜 그런가 봤더니 redis를 테스트한다고 post를 호출하는데 csrf를 잠시 disable했던것 때문에 관련 기능을 제대로 하지 못했던 것이다.
  ```html
  <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
  ```
  - 여기서 고민이 됐던것은, csrf를 disable상태로 계속 두고 수정창이나 생성창을 따로 만들까 혹은 csrf를 활성화해서 유연하게 할까 고민을 헀다.
  - 일단은 후자를 선택해서 유연하게 둔 뒤에, 나중에 바꿀 일이 생기면 바꾸기로 결정.
- Redis Serializable 오류
  - 일반 로그인을 했는데 직렬화 문제가 발생.
  - redis에 제대로 직렬화가 이루어지지 않아서 저장하지 못하는 현상이 일어났다.
  - 기존 코드
  ```java
    public class SiteUser extends BaseTimeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "siteuser_id")
        private Long id;
        ...
    }
  ```
  - 수정한 코드(Serializable을 추가해줬다)
  ```java
    public class SiteUser extends BaseTimeEntity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "siteuser_id")
        private Long id;
        ...
    }
  ```

## 기능들

### 로그인창
![image](https://user-images.githubusercontent.com/79801565/208492281-47873c1f-3ca5-4537-a404-b7c94da3f3c4.png)

- 소셜로그인 구현
  - 구글, 네이버 구현 완료. 카카오는 이메일 정보 받아오기가 선택밖에 없어서 일단 보류.
    - 소셜 로그인으로 로그인 하면 닉네임을 새로 만들도록 구현.
- 비밀번호 보이기 구현완료
- 아이디 기억하기 미구현
- **추가** 깃허브 로그인 구현

### 회원가입
![회원가입](https://user-images.githubusercontent.com/79801565/208492552-1d40b3f6-d686-4c4c-8fdc-e53d6e63b12f.png)

- 회원가입을 누르면 잘 입력했는지 확인하고 잘못입력했거나 빈곳이 있으면 경고창 발생하도록 구현.
  - 원래는 글로벌 오류로 한번에 처리했는데, 타임리프기능을 이용해서 오류가 발생한 칸은 빨간색 테두리로 알려주도록 변경.
- 아이디 중복확인 구현
  - 글로벌 오류로 한번에 묶어서 처리
- 비밀번호 규정 추가예정
  - 영어와 특수문자를 포함하도록 추가함
- 일반 회원가입은 SMTP를 이용해서 인증 메일을 보내도록 구현

### 질문목록
![질문목록](https://user-images.githubusercontent.com/79801565/234372785-f7ace4b2-e722-4b20-a594-94a6a9fca000.png)

- 검색기능, 질문등록기능, 페이징기능 구현완료
- 페이징이 한번에 5개에서 최대 7개까지 늘어나서 그냥 5개 혹은 10개로 고정할 예정.
- 추천수 추가

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

- 지금 엔티티로만 확인하고 있다보니 아이디를 삭제하고 같은 아이디로 만들어도 이전에 썼던 질문이나 추천, 댓글이 그대로 이어질것임.
  - 만약 아이디를 삭제하고 다시 같은 아이디로 가입을 하면 기존에 있던 기록들이 없어져야함.
  - 방법 1. 아이디를 삭제할때 DB에 아예삭제하지않고 null값으로 처리한다. 그러고 같은 아이디로 가입하면 임의로 아이디에 숫자같은걸 넣어서 서로 다른 아이디로 실행되도록 한다.
  - 방법 2. 아이디를 삭제할때 해당 아이디의 기록들(질문, 댓글, 대댓글)을 삭제한다.(그런데 커뮤니티같은곳을 보면 질문글 같은건 남기는듯. 이방법은 안할거 같다.)
- 마이페이지 추가예정
  - 기본정보, 질문, 댓글, 대댓글 작성기록 보여주도록 구현할 예정
- 등급, 랭크시스템도 구현할 예정(안할수도 있음)
- 댓글 고정 기능 추가예정
- 현재 마크다운 형식으로 이용하는데, 미리보기 기능도 시간이 되면 구현해 볼 예정
- 엔티티 연관관계들을 보면 FetchType이 EAGER로 돼있는게 많은데, 가급적 LAZY로 하는것이 좋다고 해서 LAZY로 바꾸는중
  - 즉시로딩은 예상치 못한 쿼리가 나갈 수 있고, N+1 문제를 일으킨다.
  - 그런데 고치는 과정중에 LazyInitializationException이 발생하는데 이는 Service단에서 Transaction이 끝나기 전에 DTO에 설정해서 넘기는 방식을 한다.
- 게시글, 댓글, 대댓글 등 삭제시 완전히 삭제하지 않는 Soft Delete를 구현할 계획
  - Soft Delete로 구현할 시  DB에 데이터를 남겨둠으로써 재사용할 수 있도록 하기 위함이다.
  - 물론 이러면 데이터가 계속 쌓이게되어 DB에 무리가 가겠지만 그 부분은 다음에 생각해볼 예정이다.

### 현재 다이어그램

![image](https://user-images.githubusercontent.com/79801565/224798827-02fa237f-6b7b-447f-92fd-b819d575fee8.png)

## 고칠 부분
- [x] 모든 FetchType을 Lazy로 고치기.(가장 중요)
- [x] 가능한 엔티티말고 DTO를 리턴하도록 구현
  - 연관관계 있는 엔티티도 전부 DTO로 변경해서 리턴시킨다.
  - 왜나하면 엔티티로 전부 처리해 버리면, 엔티티의 스펙이 변하게 되면 연쇄적으로 오류가 생길 수 있기 때문이다.
- 연관관계 메서드도 추가할 예정
- 지금 검색 코드가 많이 더러운데, Querydsl로 변경할 예정
- 현재 Builder 패턴을 사용중인데, Builder 패턴을 엔티티 최상단에 선언하면 AllArgsConstructor를 선언해야하는데 이는 인스턴스 멤버의 선언 순선에 영향을 받기 때문에 변수의 순서를 바꾸면 생성자의 입력 값 순서도 바뀌게 되어 검출되지 않는 치명적인 오류를 발생시킬 수 있다.
  - 생성자에 Builder를 붙이고 AllArgsConstructor를 지운다.
- 고칠 부분은 아니지만, 데이터를 변경하는 경우에는 merge를 사용하기 보다는 변경감지를 사용한다.