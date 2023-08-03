package net.aros.portals.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.aros.portals.Config;
import net.aros.portals.Portals;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class PortalsCommand {
    // В данном случае числа легче использовать чем enum
    private static final int NETHER_PORTAL = 0;
    private static final int END_PORTAL = 1;
    private static final int END_GATEWAYS = 2;
    private static final int EDEN_RING_PORTAL = 3;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("portals").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("nether_portal")
                                .then(literal("enable").executes(context -> portal(NETHER_PORTAL, true)))
                                .then(literal("disable").executes(context -> portal(NETHER_PORTAL, false)))
                        )
                        .then(literal("end_portal")
                                .then(literal("enable").executes(context -> portal(END_PORTAL, true)))
                                .then(literal("disable").executes(context -> portal(END_PORTAL, false)))
                        )
                        .then(literal("end_gateways")
                                .then(literal("enable").executes(context -> portal(END_GATEWAYS, true)))
                                .then(literal("disable").executes(context -> portal(END_GATEWAYS, false)))
                        )
                        .then(literal("eden_ring_portal")
                                .then(literal("enable").executes(context -> portal(EDEN_RING_PORTAL, true)))
                                .then(literal("disable").executes(context -> portal(EDEN_RING_PORTAL, false)))
                        )
                        .then(literal("blocked_dimensions")
                                .then(literal("add").then(argument("dimension_id", IdentifierArgumentType.identifier()).executes(c -> addOrRemoveDimension(c, true))))
                                .then(literal("remove").then(argument("dimension_id", IdentifierArgumentType.identifier()).executes(c -> addOrRemoveDimension(c, false))))
                                .then(literal("list").executes(PortalsCommand::dimensionsList))
                        )
        );
    }

    private static int portal(int type, boolean enabled) {
        switch (type) {
            case NETHER_PORTAL -> net.aros.portals.Portals.CONFIG.main.disableNetherPortals = !enabled;
            case END_PORTAL -> net.aros.portals.Portals.CONFIG.main.disableEndPortals = !enabled;
            case END_GATEWAYS -> net.aros.portals.Portals.CONFIG.main.disableEndGateways = !enabled;
            case EDEN_RING_PORTAL -> net.aros.portals.Portals.CONFIG.main.disableEdenPortals = !enabled;
        }
        Portals.CONFIG.saveConfig(Config.PATH);

        return 0;
    }

    private static int dimensionsList(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Text.literal(Arrays.toString(Portals.CONFIG.main.blockedDimensions.toArray())), false);

        return 0;
    }

    private static int addOrRemoveDimension(CommandContext<ServerCommandSource> context, boolean add) {
        Identifier dimensionId = IdentifierArgumentType.getIdentifier(context, "dimension_id");

        if (add) Portals.CONFIG.main.blockedDimensions.add(dimensionId.toString());
        else Portals.CONFIG.main.blockedDimensions.remove(dimensionId.toString());

        Portals.CONFIG.saveConfig(Config.PATH);

        return 0;
    }
}
