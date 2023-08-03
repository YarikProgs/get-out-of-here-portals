package net.aros.portals;

import net.aros.portals.commands.PortalsCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Portals implements ModInitializer {
    public static final String MOD_ID = "portals";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Config CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = Config.loadConfig(Config.PATH);

        CommandRegistrationCallback.EVENT.register((dispatcher, r, e) -> PortalsCommand.register(dispatcher));

        LOGGER.info("Get out of here, portals is initializing!");
    }
}
