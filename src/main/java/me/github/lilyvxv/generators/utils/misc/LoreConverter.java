package me.github.lilyvxv.generators.utils.misc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class LoreConverter {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static List<Component> deserialize(String input) {
        String[] stringArray = input.split("\n");
        List<Component> componentList = new ArrayList<>();

        for (String string : stringArray) {
            componentList.add(miniMessage.deserialize(string));
        }

        return componentList;
    }
}