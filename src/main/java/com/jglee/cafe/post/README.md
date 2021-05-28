### 포스트 기능 추가 2021.05.12.
**기능 목록**
- 포스트 추가
- 포스트 수정
- 포스트 삭제
- 포스트 조회

**포스트란?**
> 포스트는 게시판의 게시글을 의미합니다.
>
> 제목, 작성자, 내용으로 구성되어 있습니다.
>
> 각 포스트는 카테고리 ID와 작성자 ID를 한개 씩 갖게 됩니다.

**포스트 추가**
1. title, categoryId, content를 HttpMessage의 body에 넣어 '/post'에 post 요청
2. 포스트 생성에 필요한 정보가 있는지 확인 (포스트 제목)
3. spring security의 context에서 인증정보를 받아온 후 작성자 확인
4. dto에 작성자 정보를 넣고 JpaRepository(PostRepository)에 저장

**포스트 수정**
1. title, categoryId, content를 HttpMessage의 body에 넣어 '/post/${id}'에 put 요청
2. 포스트 수정에 필요한 정보가 있는지 확인 (포스트 제목)
3. spring security의 context에서 인증정보를 받아온 후 수정자 확인
4. 수정자가 작성자 본인 혹은 관리자라면 포스트 정보를 변경

**포스트 삭제**
1. '/post/${id}'에 delete 요청
2. spring security의 context에서 인증정보를 받아온 후 삭제자 확인
3. 삭제자가 작성자 본인 혹은 관리자라면 포스트 정보를 JpaRepository(PostRepository)에서 삭제

**포스트 조회**
- postID 별 포스트 조회
    1. '/post/${id}'에 get 요청
    2. 해당 id의 포스트가 존재할 경우 해당 포스트의 정보를 반환
- 포스트 전체 목록 조회
    1. '/post'에 get 요청
    2. 포스트 전체의 정보를 반환
- 유저 별 포스트 목록 조회
    1. '/post/user/${email}'에 get 요청
    2. 해당 email의 유저 존재 여부 확인
    3. PostRepository의 findAllByUser_Email 메소드를 통해 작성자 별 포스트 목록 반환
- 카테고리 별 포스트 목록 조회
    1. '/post/category/${id}'에 get 요청
    2. 해당 id의 카테고리 존재 여부 확인
    3. PostRepository의 findAllByCategory_Id 메소드를 통해 카테고리 별 포스트 목록 반환
  