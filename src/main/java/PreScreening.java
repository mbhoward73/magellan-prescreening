import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreScreening {

    public List run(int reviewDetailsPerPage, List<Map> reviewsList) {

        Map<String, List<Map>> groupedByFeatures = new HashMap();
        for (Map<String, String> review : reviewsList) {
            String feature = review.get("feature");
            List<Map> reviews = groupedByFeatures.get(feature);
            if (reviews == null) {
                reviews = new ArrayList();
            }
            reviews.add(review);
            groupedByFeatures.put(feature, reviews);
        }

        List groupedAndPaged = new ArrayList();

        for (Map.Entry<String, List<Map>> entry : groupedByFeatures.entrySet()) {
            List<Map> page = new ArrayList();
            int pageCounter = 0;
            for (Map<String, Object> review : entry.getValue()) {
                page.add(review);
                if (reviewDetailsPerPage == page.size()) {
                    Map<String, Object> reviewDetail = new HashMap();
                    reviewDetail.put("feature", entry.getKey());
                    reviewDetail.put("page", pageCounter);
                    reviewDetail.put("totalRecords", entry.getValue().size());
                    reviewDetail.put("reviews", page);
                    groupedAndPaged.add(reviewDetail);
                    pageCounter++;
                    page = new ArrayList();
                }
            }
        }

        return groupedAndPaged;

    }

    public static void main(String[] args) {

        PreScreening preScreening = new PreScreening();
        List<Map> input = new ArrayList();

        Map reviewOne = new HashMap();
        reviewOne.put("reviewId", "2874274");
        reviewOne.put("feature", "can use this product");
        input.add(reviewOne);

        Map reviewTwo = new HashMap();
        reviewTwo.put("reviewId", "3535533");
        reviewTwo.put("feature", "can use this product");
        input.add(reviewTwo);

        Map reviewThree = new HashMap();
        reviewThree.put("reviewId", "5839589");
        reviewThree.put("feature", "can not use this product");
        input.add(reviewThree);

        List<Map> output = preScreening.run(1, input);
        List<Map> expectedOutput = new ArrayList();

        Map<String, Object> reviewPageOne = new HashMap();
        reviewPageOne.put("feature", "can use this product");
        reviewPageOne.put("page", 0);
        reviewPageOne.put("totalRecords", 2);

        List<Map> reviewListOne = new ArrayList();
        reviewListOne.add(reviewOne);
        reviewPageOne.put("reviews", reviewListOne);
        expectedOutput.add(reviewPageOne);

        Map<String, Object> reviewPageTwo = new HashMap();
        reviewPageTwo.put("feature", "can use this product");
        reviewPageTwo.put("page", 1);
        reviewPageTwo.put("totalRecords", 2);

        List<Map> reviewListTwo = new ArrayList();
        reviewListTwo.add(reviewTwo);
        reviewPageTwo.put("reviews", reviewListTwo);
        expectedOutput.add(reviewPageTwo);

        Map<String, Object> reviewPageThree = new HashMap();
        reviewPageThree.put("feature", "can not use this product");
        reviewPageThree.put("page", 0);
        reviewPageThree.put("totalRecords", 1);

        List<Map> reviewListThree = new ArrayList();
        reviewListThree.add(reviewThree);
        reviewPageThree.put("reviews", reviewListThree);
        expectedOutput.add(reviewPageThree);

        if (!expectedOutput.equals(output)) {
            throw new AssertionError("Expected: " + expectedOutput + " is not equals to Actual: " + output);
        } else {
            System.out.println("success!");
        }
    }

}
