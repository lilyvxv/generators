package me.github.lilyvxv.generators.utils.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

import static me.github.lilyvxv.generators.Generators.miniMessage;

public class LoreConverter {

    public static List<Component> deserialize(String input) {
        String[] stringArray = input.split("\n");
        List<Component> componentList = new ArrayList<>();

        for (String string : stringArray) {
            componentList.add(miniMessage.deserialize(string));
        }

        return componentList;
    }
}