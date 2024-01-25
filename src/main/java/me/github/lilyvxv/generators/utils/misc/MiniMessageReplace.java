package me.github.lilyvxv.generators.utils.misc;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static me.github.lilyvxv.generators.Generators.miniMessage;

public class MiniMessageReplace {

    public static Component replace(Component component, String target, String replace) {
        return miniMessage.deserialize(miniMessage.serialize(component).replaceAll(target, replace));
    }
}