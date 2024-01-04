package com.ll.medium.global.initData;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Configuration
// @Profile("!prod") // TODO : prod 환경에서는 주석 처리해제해야 함
@Slf4j
@RequiredArgsConstructor
public class NotProd {
    private final MemberService memberService;
    private final PostService postService;

    @Bean
    @Order(3)
    public ApplicationRunner initNotProd() {
        return args -> {
            createSampleUsers();
            createSamplePosts();
        };
    }

    @Transactional
    public void createSampleUsers() {
        for (int i = 1; i <= 4; i++) {
            String username = "user" + i;
            memberService.join(username, "1234");
        }
    }

    @Transactional
    public void createSamplePosts() {
        for (int i = 1; i <= 4; i++) {
            String username = "user" + i;
            Member member = memberService.findByUsername(username).orElse(null);

            if (member != null) {
                for (int j = 1; j <= 100; j++) {
                    postService.write(member, "제목 " + j, "내용 " + j, true);
                }
            }
        }
    }
}

