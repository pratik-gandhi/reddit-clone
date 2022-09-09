package com.gandhi.reddit.backend.controller.v1;

import com.gandhi.reddit.backend.dto.SubredditDto;
import com.gandhi.reddit.backend.service.SubredditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subreddit")
@RequiredArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.ok(subredditService.getAllSubreddit());
    }

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.create(subredditDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable("id") final long id) {
        return ResponseEntity.ok(subredditService.getSubreddit(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSubreddit(@PathVariable("id") final long id) {
        subredditService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubredditDto> patchSubreddit(@PathVariable("id") final long id, @RequestBody SubredditDto subredditDto) {
        return ResponseEntity.ok(subredditService.patch(id, subredditDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubredditDto> updateSubreddit(@PathVariable("id") final long id, @RequestBody SubredditDto subredditDto) {
        return ResponseEntity.ok(subredditService.update(id, subredditDto));
    }
}
