package helper;

import org.json.JSONObject;

import jdo.Books;

public class JsonHelper {

    /**
     * @param bookJdo Does json Operation
     */
    public String jsonParser(Books bookJdo,String pRequestMethod) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", bookJdo.getmId());
            jsonObject.put("Title", bookJdo.getmTitle());
            jsonObject.put("Description", bookJdo.getmDescription());
            jsonObject.put("PageCount", bookJdo.getmPageCount());
            jsonObject.put("Excerpt", bookJdo.getmExcerpt());
            jsonObject.put("PublishDate", bookJdo.getmPublishDate());
            jsonObject.put("RequestMethod",pRequestMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
