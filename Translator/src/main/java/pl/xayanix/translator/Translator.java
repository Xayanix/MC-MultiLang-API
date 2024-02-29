package pl.xayanix.translator;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.xayanix.translator.commands.LanguageCommand;
import pl.xayanix.translator.commands.TranslationCommand;
import pl.xayanix.translator.listeners.AsyncPlayerPreLoginListener;
import pl.xayanix.translator.models.TranslatedMessage;
import pl.xayanix.translator.models.TranslationLocale;
import pl.xayanix.translator.models.TranslationUser;
import pl.xayanix.translator.placeholder.PlaceholderHook;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class Translator extends JavaPlugin {

	public static final TranslationLocale DEFAULT_LOCALE = TranslationLocale.EN;
	private final ConcurrentHashMap<UUID, TranslationUser> userConcurrentHashMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, List<TranslatedMessage>> translationMap = new ConcurrentHashMap<>();
	public static String GPT_API_KEY;
	public static String GPT_MODEL;

	@Getter
	private static Translator plugin;

	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		this.saveResource(DEFAULT_LOCALE.name().toLowerCase() + ".yml", false);

		GPT_API_KEY = this.getConfig().getString("gptApiKey");
		GPT_MODEL = this.getConfig().getString("gptModel");

		Bukkit.getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);

		getCommand("language").setExecutor(new LanguageCommand(this));
		getCommand("mlapi").setExecutor(new TranslationCommand(this));

		new PlaceholderHook(this).register();
		this.load();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	public void load(){
		this.translationMap.clear();

		Arrays.stream(this.getDataFolder().listFiles()).forEach(file -> {
			try {
				TranslationLocale translationLocale = TranslationLocale.valueOf(file.getName().replace(".yml", "").toUpperCase());
				YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
				yamlConfiguration.getKeys(false).forEach(key -> {
					List<TranslatedMessage> translatedMessages = this.translationMap.getOrDefault(key, Lists.newArrayList());
					translatedMessages.add(TranslatedMessage.builder()
							.key(key)
							.translationLocale(translationLocale)
							.message(yamlConfiguration.getString(key))
							.build());

					this.translationMap.put(key, translatedMessages);
				});

				this.getLogger().info("Loaded " + translationLocale.getName() + " language.");
			} catch (IllegalArgumentException illegalArgumentException){
				this.getLogger().warning("Language " + file.getName() + " is not supported yet.");
			}
		});
	}

}
