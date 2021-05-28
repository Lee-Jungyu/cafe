### 코멘트 기능 추가 2021.05.12.
**기능 목록**
- 코멘트 추가
- 코멘트 수정
- 코멘트 삭제
- 코멘트 조회

**코멘트란?**
> 코멘트는 게시글의 댓글을 의미합니다.
>
> 작성자, 내용으로 구성되어 있습니다.
>
> 각 코멘트는 Post ID와 작성자 ID를 1개 씩 갖게 됩니다.

**코멘트 추가**
1. postId, content를 HttpMessage의 body에 넣어 '/comment'에 post 요청
2. 코멘트 생성에 필요한 정보가 있는지 확인 (코멘트 내용)
3. spring security의 context에서 인증정보를 받아온 후 작성자 확인
4. dto에 작성자 정보를 넣고 JpaRepository(CommentRepository)에 저장

**코멘트 수정**
1. postId, content를 HttpMessage의 body에 넣어 '/comment/${id}'에 put 요청
2. 코멘트 수정에 필요한 정보가 있는지 확인 (코멘트 내용)
3. spring security의 context에서 인증정보를 받아온 후 수정자 확인
4. 수정자가 작성자 본인이라면 코멘트 정보를 변경
> UI적인 부분은 구현하지 않았으나 기능적인 부분은 개발함

**코멘트 삭제**
1. '/comment/${id}'에 delete 요청
2. spring security의 context에서 인증정보를 받아온 후 삭제자 확인
3. 삭제자가 작성자 본인 혹은 관리자라면 코멘트 정보를 JpaRepository(CommentRepository)에서 삭제

**코멘트 조회**
- commentID 별 코멘트 조회
    1. '/comment/${id}'에 get 요청
    2. 해당 id의 코멘트가 존재할 경우 해당 코멘트의 정보를 반환
- 코멘트 전체 목록 조회
    1. '/comment'에 get 요청
    2. 코멘트 전체의 정보를 반환