package net.aros.portals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Formatting;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final File PATH = new File(FabricLoader.getInstance().getConfigDir() + "/get-out-of-here-portals.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public final Config.Main main = new Main();
    public final Config.Messages messages = new Messages();

    public static Config loadConfig(File configFile) {
        Config config;
        if (configFile.exists() && configFile.isFile()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(configFile);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                config = GSON.fromJson(bufferedReader, Config.class);
            } catch (IOException e) {
                throw new RuntimeException("[Portals] Problem occurred when trying to load config: ", e);
            }
        } else {
            config = new Config();
        }
        config.saveConfig(configFile);

        return config;
    }

    public void saveConfig(File configFile) {
        try (
                FileOutputStream stream = new FileOutputStream(configFile);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)
        ) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            Portals.LOGGER.error("[Portals] Problem occurred when saving config: " + e.getMessage());
        }
    }

    // Отключение порталов и измерений
    public static class Main {
        public boolean disableNetherPortals = false;
        public boolean disableEndPortals = false;
        public boolean disableEndGateways = false;
        public boolean disableEdenPortals = false;

        public List<String> blockedDimensions = new ArrayList<>();
    }

    // Сообщения (можно использовать параграф)
    public static class Messages {
        public String netherPortalMessage = Formatting.BOLD.toString() + Formatting.GOLD + "[>>] " + Formatting.RESET + "В силу сюжета, портал в ад отключён!";
        public String endPortalMessage = Formatting.BOLD.toString() + Formatting.GOLD + "[>>] " + Formatting.RESET + "В силу сюжета, портал в энд отключён!";
        public String endGatewayMessage = Formatting.BOLD.toString() + Formatting.GOLD + "[>>] " + Formatting.RESET + "В силу сюжета, гейты энда отключены!";
        public String edenPortalMessage = Formatting.BOLD.toString() + Formatting.GOLD + "[>>] " + Formatting.RESET + "В силу сюжета, портал Эдена отключён!";

        public String blockedDimension = Formatting.BOLD.toString() + Formatting.GOLD + "[>>] " + Formatting.RESET + "В силу сюжета, данное измерение заблокировано";
    }
}
