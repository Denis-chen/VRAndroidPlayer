# VR Player DataBase Model

#### 회원 테이블

- mem_id(varchar, max_length=50, PK)
- mem_nickname(varchar, max_length = 30)
- mem_profile_image(varchar, max_length = 30)



#### 비디오 테이블

- video_id(int, max_length = 30, auto_increment, PK)
- video_title(varchar, max_length = 30,  Not Null))
- video_des(varchar, max_length = 100, Null)
- video_type(varchar, max_length = 30, Not Null)
- video_filename(varchar, max_length = 100)  Random_generator
- video_thumbnail(varchar, max_length = 50)
- video_duration(int, max_length = 50)
- video_size(int, max_length = 50)
- video_hits(int, max_length = 50)
- video_date(date)
- video_valid(boolean)
- mem_id(FK)



#### 댓글 테이블

- comment_id(int, max_length = 30, auto_increment, PK)
- mem_id(FK)
- video_id(FK)
- comment_des(varchar, max_length = 100)
- comment_date(date)

