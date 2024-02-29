package pl.xayanix.translator.utils;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.models.TranslationLocale;

public class GPTUtil {

	public static String gptTranslated(TranslationLocale translationLocale, String in){

		try {
			String prompt = "Translate " + Translator.DEFAULT_LOCALE.getName() + " to " + translationLocale.getName() + "." +
					"Translate: " + in + "\n" +
					"Translation:";

			HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/completions")
					.header("Content-Type", "application/json")
					.header("Authorization", "Bearer " + Translator.GPT_API_KEY)
					.body(new JSONObject()
							.put("model", Translator.GPT_MODEL)
							.put("prompt", prompt)
							.put("temperature", 0)
							.put("max_tokens", 2048)
							.put("stop", "\n")
					)
					.asJson();

			if (response.getStatus() == 200) {
				JSONObject jsonObject = response.getBody().getObject();
				String text = jsonObject.getJSONArray("choices").getJSONObject(0).getString("text");
				return text.replaceFirst(" ", "");
			} else {
				System.err.println("Request failed. Status code: " + response.getStatus());
				System.err.println(response.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return in;
	}

}
