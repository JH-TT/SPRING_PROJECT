# 로그인 세션 Redis에 저장하기

## 왜 Redis?
일단 외부 Session Storage를 사용하면 여러 이점이 있다.
1. 배포하는 과정에서 생기는 세션 관리에도 전혀 영향을 받지 않는다.
2. 여러 서버 세션 정보의 정합성을 유지할 수 있다.

## Redis의 장점
Redis는 In-memory 저장소로, key-value 방식을 이용해 고속 쓰기 및 읽기 처리가 가능하다.
세션 자체가 복잡한 연산과 계산을 필요로 하지 않으므로 적절하다.

## Redis 연동 후 사용
![redis처음상태](https://user-images.githubusercontent.com/79801565/234675336-285f7ea8-397e-4eda-ac3e-0e19d11cd08a.png)
- 처음에 key값들이 비어있다.

한번 로그인을 해보자.(소셜 로그인 사용)
![로그인후redis](https://user-images.githubusercontent.com/79801565/234675489-03c0a0be-faf1-4570-9609-82d08b942588.png)
- 로그인에 성공했고, redis에 세션들이 저장된 것을 볼 수 있다.

redis를 flushall을 해보자. 그러면 모든 키값들을 없앤다.
redis에 세션 정보가 없으니 새로고침을 하면 로그아웃되어야 한다.
![세션삭제한뒤 새로고침](https://user-images.githubusercontent.com/79801565/234675743-b1819a3d-bca9-4470-a435-103fe4c16a89.png)
- 잘 로그아웃된 것을 볼 수 있다.