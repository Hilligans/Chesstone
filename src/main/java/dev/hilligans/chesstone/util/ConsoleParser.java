package dev.hilligans.chesstone.util;

import java.util.HashMap;
import java.util.function.Consumer;

public class ConsoleParser {

    public HashMap<String, ConsoleArgument> messages = new HashMap<>();

    public ConsoleReader consoleReader = new ConsoleReader(s -> {
        String[] args = s.split(" ", 2);
        ConsoleArgument arg = messages.get(args[0]);
        if(args.length > 1) {
            arg.consumer.accept(arg.arguments <= 1 ? new String[] {args[1].trim()} : args[1].split(" ", arg.arguments));
        } else {
            arg.consumer.accept(null);
        }
    });

    public ConsoleParser() {
        addCommand("help", "gets this list", 0, s1 -> {
            for(String message : messages.keySet()) {
                System.out.println(message + " - " + messages.get(message).description);
            }
        });
    }

    public ConsoleParser addCommand(String command, String description, int arguments, Consumer<String[]> consumer) {
        this.messages.put(command, new ConsoleArgument(description, arguments, consumer));
        return this;
    }

    record ConsoleArgument(String description, int arguments, Consumer<String[]> consumer) {}
}
