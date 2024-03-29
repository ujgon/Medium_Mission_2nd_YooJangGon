package com.ll.medium.domain.post.post.controller;

import ch.qos.logback.core.model.Model;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "PostController", description = "글 CRUD 컨트롤러")
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{id}")
    @Operation(summary = "글 상세")
    public String showDetail(@PathVariable long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isUserAuthenticated = auth.isAuthenticated();

        boolean hasUserRole = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

        String postContent = postService.findPostContentById(id, isUserAuthenticated);

        rq.setAttribute("content", postContent);

        Optional<Post> foundPost = postService.findById(id);
        model.addText("post");

        return "domain/post/post/detail";
    }

    @GetMapping("/list")
    @Operation(summary = "글 목록")
    public String showList(
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Page<Post> postPage = postService.search(kw, pageable);
        rq.setAttribute("postPage", postPage);
        rq.setAttribute("page", page);

        return "domain/post/post/list";
    }
}

