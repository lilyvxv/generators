package me.github.lilyvxv.generators.utils.misc;

import me.github.lilyvxv.generators.utils.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PermissionUtils {
    private static final Pattern MAX_GENS_PERM_MATCHER = Pattern.compile("generators\\.max-generators\\.(\\d+)");;

    public static int getPlayerMaxGens(Player player) {
        Optional<Integer> maxGens = player.getEffectivePermissions()
                .stream()
                .filter(PermissionAttachmentInfo::getValue)
                .map(PermissionAttachmentInfo::getPermission)
                .map(MAX_GENS_PERM_MATCHER::matcher)
                .filter(Matcher::matches)
                .map(g -> Integer.parseInt(g.group(1)))
                .reduce(Integer::sum);

        return maxGens.orElse(0) + ConfigManager.maxDefaultGenerators;
    }
}
