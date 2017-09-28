package controller;

import com.google.gson.*;
import model.Credentials;
import model.Filter;
import model.Response;
import service.ActService;
import spark.Request;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static spark.Spark.get;

public class ActController {

	public ActController(){

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ZonedDateTime.class, new LocalDateAdapter())
				.create();


		get("/api/v0/acts", (Request req, spark.Response res) -> {
			if(!req.queryMap().hasKey("date")){
				return new Response(400, "Query parameter date is mandatory.");
			}

			try{
				List actList = new ActService().getAct(new Filter(req.queryMap()), new Credentials(req.cookies()));
				return new Response(200, actList);
			}catch (Exception e){
				return new Response(500, e.toString());
			}
		}, gson::toJson);

	}
}

class LocalDateAdapter implements JsonSerializer<ZonedDateTime> {

	public JsonElement serialize(ZonedDateTime date, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(date.format(DateTimeFormatter.ISO_DATE_TIME));
	}
}
