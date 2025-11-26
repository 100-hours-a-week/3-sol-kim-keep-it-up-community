package com.project.community.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 스프링이 내부적으로 setter 메서드를 통해 값을 주입하기 때문에 @Setter가 필수. 넣지 않으면 field값 null이라는 오류 발생
public class PostImageUploadRequest {
    String imageUrl;
}
