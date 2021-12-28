import com.google.gson.JsonArray;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;


public class VK_Parser {

    private final Integer USER_ID = Config.USER_ID;
    private final String TOKEN = Config.TOKEN;

    private final TransportClient transportClient = HttpTransportClient.getInstance();
    private final VkApiClient vk = new VkApiClient(transportClient);
    private final UserActor actor = new UserActor(USER_ID, TOKEN);

    public JsonArray getJsonMembersOfGroup(Integer GROUP_ID) throws ClientException, ApiException, InterruptedException {

        JsonArray members = new JsonArray();
        int page = 0;
        int limit = 1000;
        int offset = 0;
        //Запрос к API для получения количества участников в сообществе
        var countOfGroupMembers = vk.execute().code(actor,
                        "return API.groups.getMembers(" +
                                "{\"group_id\": " + GROUP_ID + ", " +
                                "\"v\": \"5.131\", " +
                                "})" +
                                ".count;")

                .execute().getAsInt();

        //Получаем всех участников сообщества
        while (offset + limit < countOfGroupMembers) {
            offset = page * limit;

            var response = vk.execute().code(actor,
                            "return API.groups.getMembers(" +
                                    "{\"group_id\": " + GROUP_ID + ", " +
                                    "\"v\": \"5.131\", " +
                                    "\"sort\": \"id_asc\", " +
                                    "\"count\": \"1000\"," +
                                    "\"fields\" : \"bdate,city,country,sex\"," +
                                    "\"offset\" :" + offset +
                                    "})" +
                                    ".items;")
                    .execute().getAsJsonArray();
            members.addAll(response);
            //У API есть ограничение количества заапросов в одну секунду, поэтому:
            Thread.sleep(333);
            page++;
        }
        return members;
    }
}

