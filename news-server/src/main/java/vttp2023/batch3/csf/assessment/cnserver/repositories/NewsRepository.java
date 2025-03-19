package vttp2023.batch3.csf.assessment.cnserver.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;

import java.util.List;

@Repository
public class NewsRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    // TODO: Task 1
    // Write the native Mongo query in the comment above the method
    // db.news.insertOne(
    // {
    // postDate: << date >>,
    // title: << title >>,
    // description: << description >>,
    // image: << image URL >>,
    // tags: << tags, if applicable >>,
    // }
    public String save(News post) {
        Document postToSave = new Document();
        postToSave.append("postDate", post.getPostDate());
        postToSave.append("title", post.getTitle());
        postToSave.append("description", post.getDescription());
        postToSave.append("image", post.getImage());

        if (post.getTags() != null) {
            postToSave.append("tags", post.getTags());
        }

        mongoTemplate.save(postToSave, "news");
        return postToSave.getObjectId("_id").toString();

    }

    // TODO: Task 2
    // Write the native Mongo query in the comment above the method

    /*
db.news.aggregate([
  {
    $match: {
      "postDate": {
        $gte: new Date().getTime() - (1000 * 60 * 1000)
      },
      "tags": { "$exists": true, $ne: [] }
    }
  },
  { $unwind: "$tags" },
  {
    $group: {
      _id: "$tags",
      count: { $sum: 1 }
    }
  },
  {
    $sort: {
      count: -1,
      _id: 1
    }
  },
  {
    $limit: 10
  },
  {
    $project: {
      _id: 0,
      tag: "$_id",
      count: 1
    }
  }
]);
	 */

    public List<Document> getTags(int minutes) {

        MatchOperation matchOperation = Aggregation.match(Criteria.where("postDate").gte(System.currentTimeMillis() - ((long) minutes * 60 * 1000)).and("tags").exists(true));
        UnwindOperation unwind = Aggregation.unwind("tags");

        MatchOperation matchOperation1 = Aggregation.match(Criteria.where("tags").ne(" "));
        GroupOperation group = Aggregation.group("tags").count().as("count");

        SortOperation sort = Aggregation.sort(Sort.by(Sort.Order.desc("count"), Sort.Order.asc("_id")));

        LimitOperation limit = Aggregation.limit(10);

        ProjectionOperation project = Aggregation.project()
                .andExclude("_id")
                .and("_id").as("tag")
                .and("count").as("count");

        Aggregation pipeline = Aggregation.newAggregation(matchOperation, unwind, matchOperation1, group, sort, limit, project);
        return mongoTemplate.aggregate(pipeline, "news", Document.class).getMappedResults();

    }

    // TODO: Task 3
    // Write the native Mongo query in the comment above the method



    public List<News> getFilteredPosts(String tag, int minutes) {

        Query query = Query.query(Criteria.where("postDate")
                .gte(System.currentTimeMillis() - (minutes * 60 * 1000))
                .and("tags").is(tag));
        return mongoTemplate.find(query, News.class, "news");
    }

}
