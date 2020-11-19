package it.gamerover.nbs.reflection.minecraft;

import it.gamerover.nbs.reflection.Reflection;
import it.gamerover.nbs.reflection.ReflectionException;

public class MCReflection extends Reflection {

    private static final String MINECRAFT_PACKAGE   = "net.minecraft.server";

    private final String minecraftPackage;

    public MCReflection(String completeServerVersion) {
        this.minecraftPackage = MINECRAFT_PACKAGE + '.' + completeServerVersion;
    }

    public Class<?> getMinecraftClass(String className) throws ReflectionException {
        return getMinecraftClass("", className);
    }

    public Class<?> getMinecraftClass(String packageName, String className) throws ReflectionException {
        return super.getClass(this.minecraftPackage + packageName, className);
    }

}