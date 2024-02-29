package pl.xayanix.translator.commands;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.models.TranslatedMessage;
import pl.xayanix.translator.models.TranslationLocale;
import pl.xayanix.translator.models.TranslationUser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TranslationCommand implements CommandExecutor {

	private final Translator translator;

	@SneakyThrows
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if(!commandSender.hasPermission("mlapi.admin")){
			commandSender.sendMessage("No permission");
			return false;
		}

		if(strings.length == 0){
			commandSender.sendMessage("Available commands: /mlapi reload, /mlapi add <key> <text>");
			return true;
		}

		if(strings[0].equalsIgnoreCase("reload")){
			translator.load();
			commandSender.sendMessage("Configuration reloaded.");
		}

		if(strings[0].equalsIgnoreCase("add")){
			String key = strings[1];
			String result = String.join(" ", Arrays.copyOfRange(strings, 2, strings.length));

			translate(commandSender, key, result);
		}

		if(strings[0].equalsIgnoreCase("holo")){
			String name = strings[1];
			int page = Integer.parseInt(strings[2]);
			int line = Integer.parseInt(strings[3]);
			String key = name + "_" + page + "_" + line;


			Hologram hologram = DecentHologramsAPI.get().getHologramManager().getHologram(name);
			HologramLine hologramLine = hologram.getPage(page).getLine(line);
			translate(commandSender, key, hologramLine.getContent());

			DHAPI.setHologramLine(hologramLine, "%mlapi_" + key + "%");
		}

		return true;
	}

	private void translate(CommandSender commandSender, String key, String result) throws IOException {
		File file = new File(translator.getDataFolder().getAbsoluteFile() + "/" + Translator.DEFAULT_LOCALE.name().toLowerCase() + ".yml");
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
		yamlConfiguration.set(key, result);
		yamlConfiguration.save(file);

		List<TranslatedMessage> translatedMessages = translator.getTranslationMap().getOrDefault(key, Lists.newArrayList());
		TranslatedMessage translated = TranslatedMessage.builder()
				.key(key)
				.translationLocale(Translator.DEFAULT_LOCALE)
				.message(result)
				.build();

		Bukkit.getScheduler().runTaskAsynchronously(translator, () -> {
			Arrays.stream(TranslationLocale.values())
					.filter(translationLocale -> translationLocale != Translator.DEFAULT_LOCALE)
					.forEach(translationLocale -> commandSender.sendMessage("Translated to " + translationLocale.getName() + ": " + translated.translateTo(translator, translationLocale)));
			Translator.getPlugin().load();
		});

		translatedMessages.add(translated);
		translator.getTranslationMap().put(key, translatedMessages);
	}

}
