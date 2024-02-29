package pl.xayanix.translator.models;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.xayanix.translator.Translator;
import pl.xayanix.translator.utils.GPTUtil;

import java.io.File;
import java.util.List;


@Getter
@Builder
public class TranslatedMessage {

	private String key;
	private TranslationLocale translationLocale;
	private String message;

	public String getMessage(Player player){
		return PlaceholderAPI.setPlaceholders(player, this.message);
	}

	@SneakyThrows
	public String translateTo(Translator translator, TranslationLocale newLocale){
		String translated = GPTUtil.gptTranslated(newLocale, this.message);
		File file = new File(translator.getDataFolder().getAbsoluteFile() + "/" + newLocale.name().toLowerCase() + ".yml");

		if(!file.exists())
			file.createNewFile();

		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
		yamlConfiguration.set(this.key, translated);
		yamlConfiguration.save(file);

		return translated;
	}

}
