package pl.xayanix.translator.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.models.TranslationLocale;
import pl.xayanix.translator.models.TranslationUser;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LanguageCommand implements CommandExecutor {

	private final Translator translator;

	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if(strings.length == 0){
			commandSender.sendMessage("Set language: /language <lang>");
			commandSender.sendMessage(Arrays.stream(TranslationLocale.values())
					.map(Enum::toString)
					.collect(Collectors.joining(", ")));
			return true;
		}

		Player player = (Player) commandSender;
		UUID uuid = player.getUniqueId();

		TranslationLocale translationLocale = TranslationLocale.valueOf(strings[0]);
		TranslationUser translationUser = translator.getUserConcurrentHashMap().get(uuid);
		translationUser.setLocale(translationLocale);

		commandSender.sendMessage("Locale set on: " + translationLocale.getName());
		return true;
	}

}
