package pl.xayanix.translator.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.models.TranslationUser;

@RequiredArgsConstructor
public class AsyncPlayerPreLoginListener implements Listener {

	private final Translator translator;

	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent event){
		TranslationUser translationUser = translator.getUserConcurrentHashMap().getOrDefault(event.getUniqueId(), new TranslationUser(event.getUniqueId(), Translator.DEFAULT_LOCALE));
		translator.getUserConcurrentHashMap().put(event.getUniqueId(), translationUser);
	}

}
