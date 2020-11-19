package it.gamerover.nbs.reflection.craftbukkit;

import it.gamerover.nbs.reflection.Reflection;
import it.gamerover.nbs.reflection.ReflectionException;

public class CBReflection extends Reflection {

    private static final String CRAFTBUKKIT_PACKAGE = "org.bukkit.craftbukkit";

    private final String craftBukkitPackage;

    public CBReflection(String completeServerVersion) {
        this.craftBukkitPackage = CRAFTBUKKIT_PACKAGE + '.' + completeServerVersion;
    }

    public Class<?> getCraftBukkitClass(String className) throws ReflectionException {
        return getCraftBukkitClass("", className);
    }

    public Class<?> getCraftBukkitClass(String packageName, String className) throws ReflectionException {
        return super.getClass(this.craftBukkitPackage + packageName, className);
    }

}

