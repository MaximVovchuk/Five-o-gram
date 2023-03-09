package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status444NotYourStoryException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.serviceInterfaces.ReportService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import com.fivesysdev.Fiveogram.util.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/story")
// TODO: 9/3/23 rename all endpoint avoiding CRUD namings
public class StoryController {
    private final StoryService storyService;
    private final ReportService reportService;
    private final JWTUtil jwtUtil;

    @PostMapping("/new")
    public Response<Story> addNewStory(@ModelAttribute MultipartFile multipartFile,
                                       @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException {
        return new Response<>(storyService.createNewStory(jwtUtil.getUsername(token), multipartFile));
    }

    @DeleteMapping("{id}")
    public void deleteStory(@PathVariable Long id,
                            @RequestHeader(value = "Authorization") String token)
            throws Status444NotYourStoryException, Status445StoryNotFoundException {
        storyService.deleteById(jwtUtil.getUsername(token), id);
    }

    @GetMapping("/{id}")
    public Response<Story> getStory(@PathVariable Long id) throws Status445StoryNotFoundException {
        return new Response<>(storyService.getStoryById(id));
    }

    @GetMapping()
    public Response<List<Story>> getStories(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(storyService.getStoriesList(jwtUtil.getUsername(token)));
    }

    @GetMapping("/archive")
    public Response<List<Story>> getMyArchive(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(storyService.getMyStoriesArchive(jwtUtil.getUsername(token)));
    }

    @PostMapping("/{id}/report")
    public Response<Story> report(@PathVariable long id, @RequestParam String text)
            throws Status445StoryNotFoundException {
        return new Response<>(reportService.reportStory(id, text));
    }
}