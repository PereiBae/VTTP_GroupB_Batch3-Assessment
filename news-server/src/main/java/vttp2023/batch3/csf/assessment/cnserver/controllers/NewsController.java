package vttp2023.batch3.csf.assessment.cnserver.controllers;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.services.NewsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsController {

    @Autowired
    private NewsService newsService;

	// TODO: Task 1
    @PostMapping(path = "/process", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> processNews(@RequestPart MultipartFile imageFile, @RequestPart String title,
                                              @RequestPart String description, @RequestPart(required = false) String tags) throws IOException {

        System.out.println(">>>>>title" + title);
        System.out.println(">>>>>description" + description);
        System.out.println(">>>>>text" + tags);

        System.out.printf(">>>> name: %s\n", imageFile.getName());
        System.out.printf(">>>> original file name: %s\n", imageFile.getOriginalFilename());
        System.out.printf(">>>> size: %d\n", imageFile.getSize());
        System.out.printf(">>>> content type: %s\n", imageFile.getContentType());

        String newsId = null;
        try {
            newsId = newsService.postNews(imageFile, title, description, tags);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("newsId", newsId)
                .build();

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jsonObject.toString());
    }

	// TODO: Task 2
    @GetMapping(path = "/get-tags/{minutes}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTags(@PathVariable int minutes) {

        List<TagCount> tagList = newsService.getTags(minutes);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (TagCount tagCount : tagList) {
            JsonObject obj = Json.createObjectBuilder()
                    .add("tag", tagCount.tag())
                    .add("count", tagCount.count())
                    .build();
            arrayBuilder.add(obj);
        }
        JsonArray jsonArray = arrayBuilder.build();

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jsonArray.toString());
    }

	// TODO: Task 3
    @GetMapping(path = "posts/{tag}/{minutes}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<News>> postNews(@PathVariable String tag, @PathVariable int minutes) {

        List<News> newsList = newsService.getNewsByTag(tag, minutes);

//        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
//        for (News post : newsList) {
//            JsonObject obj = Json.createObjectBuilder()
//                    .add("id", post.getId())
//                    .add("postDate", post.getPostDate())
//                    .add("title", post.getTitle())
//                    .add("description", post.getDescription())
//                    .add("image", post.getImage())
//                    .add("tags",post.getTags())
//                    .build();
//            arrayBuilder.add(obj);
//        }
//        JsonArray jsonArray = arrayBuilder.build();

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(newsList);
    }

}
