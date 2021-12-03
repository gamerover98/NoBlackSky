package it.gamerover.nbs.reflection.craftbukkit;

import java.lang.reflect.Method;

import it.gamerover.nbs.reflection.ReflectionException;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

public final class CBCraftServer extends CBReflection {

    private static final String CRAFT_SERVER_CLASS_NAME = "CraftServer";
    private static final String GET_SERVER_METHOD_NAME  = "getServer";

    /**
     * Gets the CraftBukkit server class.
     */
    @Getter
    private final Class<?> craftServerClass;

    /**
     * Gets the CraftBukkit.getServer() method.
     */
    @Getter
    private final Method getServerMethod;

    public CBCraftServer(String completeServerVersion) throws ReflectionException {

        super(completeServerVersion);

        this.craftServerClass = super.getCraftBukkitClass(CRAFT_SERVER_CLASS_NAME);
        this.getServerMethod  = super.getMethod(craftServerClass, GET_SERVER_METHOD_NAME);

    }

    /**
     * Gets the NMS server instance.
     *
     * @return The Object of the NMS server instance.
     * @throws ReflectionException Invoked due to a reflection error.
     */
    public Object getMinecraftServerInstance() throws ReflectionException {

        Server server = Bukkit.getServer();
        return getMinecraftServerInstance(server);

    }

    /**
     * Gets the NMS server instance.
     *
     * @param server The not null Bukkit server instance.
     * @return The Object of the NMS server instance.
     * @throws ReflectionException Invoked due to a reflection error.
     */
    @NotNull
    public Object getMinecraftServerInstance(@NotNull Server server) throws ReflectionException {

        try {
            return getServerMethod.invoke(server);
        } catch (Exception ex) {
            throw new ReflectionException("Cannot invoke method " + getServerMethod.getName(), ex);
        }

    }

}