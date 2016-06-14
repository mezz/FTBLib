package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class ConfigEntryString extends ConfigEntry
{
    public String defValue;
    private String value;

    public ConfigEntryString(String def)
    {
        set(def);
        defValue = def == null ? "" : def;
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.STRING;
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    public void set(String v)
    {
        value = v == null ? "" : v;
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsString());
    }

    @Nonnull
    @Override
    public final JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsString());
    }

    @Override
    public String getAsString()
    {
        return value;
    }

    @Override
    public boolean getAsBoolean()
    {
        return getAsString().equals("true");
    }

    @Override
    public int getAsInt()
    {
        return Integer.parseInt(getAsString());
    }

    @Override
    public double getAsDouble()
    {
        return Double.parseDouble(getAsString());
    }

    @Override
    public String getDefValueString()
    {
        return defValue;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        String s = getAsString();
        if(!s.isEmpty())
        {
            tag.setString("V", s);
        }

        if(extended && !defValue.isEmpty())
        {
            tag.setString("D", defValue);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);
        set(tag.getString("V"));

        if(extended)
        {
            defValue = tag.getString("D");
        }
    }
}