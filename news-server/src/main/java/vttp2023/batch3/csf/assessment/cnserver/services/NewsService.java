package vttp2023.batch3.csf.assessment.cnserver.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.ImageRepository;
import vttp2023.batch3.csf.assessment.cnserver.repositories.NewsRepository;

@Service
public class NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	// TODO: Task 1
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns the news id
	public String postNews(MultipartFile file, String title, String description, String tags) throws IOException {

		String image_id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String imgUrl = imageRepository.saveImage(file, image_id);
		News newPost = new News();
		if (tags != null){
			String[] tagsArray = tags.trim().split(" ");
			newPost.setTags(Arrays.asList(tagsArray));
		}

		newPost.setPostDate(System.currentTimeMillis());
		newPost.setTitle(title);
		newPost.setDescription(description);
		newPost.setImage(imgUrl);

		System.out.println("NEWS TO BE SAVED IN MONGO" + newPost);

        return newsRepository.save(newPost);
	}
	 
	// TODO: Task 2
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of tags and their associated count
	public List<TagCount> getTags(int minutes) {

		List<TagCount> tagList = new ArrayList<>();
		List<Document> tagDocument = newsRepository.getTags(minutes);

		for (Document d : tagDocument) {
			Integer count = (Integer) d.get("count");
			String tag = (String) d.get("tag");

			TagCount newTag = new TagCount(tag, count);

			tagList.add(newTag);
		}

		System.out.println("FULL LIST OF TAGCOUNTS>>>>>" + tagList);

		return tagList;
	}

	// TODO: Task 3
	// Do not change the method name and the return type
	// You may add any number of parameters
	// Returns a list of news
	public List<News> getNewsByTag(String tag, int minutes) {

        return newsRepository.getFilteredPosts(tag, minutes);
	}
	
}
