package pl.xayanix.translator.placeholder;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.models.TranslatedMessage;
import pl.xayanix.translator.models.TranslationUser;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlaceholderHook  extends PlaceholderExpansion implements Listener {

	private final Translator translator;

	@Override
	public String getIdentifier() {
		return "mlapi";
	}

	@Override
	public String getAuthor() {
		return "Xayanix";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String onRequest(OfflinePlayer player, String placeholder) {
		List<TranslatedMessage> translatedMessageList = translator.getTranslationMap().get(placeholder);
		if(translatedMessageList == null){
			return "Translation " + placeholder + " not found.";
		}

		TranslationUser translationUser = translator.getUserConcurrentHashMap().get(player.getUniqueId());
		if(translationUser == null){
			return "Translation user doesn't exist.";
		}

		Optional<TranslatedMessage> translatedMessage = translatedMessageList.stream()
				.filter(t -> t.getTranslationLocale() == translationUser.getLocale())
				.findAny();

		if(translatedMessage.isEmpty()){
			return "Translation " + translationUser.getLocale() + " " + placeholder + " not found.";
		}

		return translatedMessage.get().getMessage(player.getPlayer());
	}


}
