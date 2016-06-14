package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import latmod.lib.json.LMJsonUtils;
import latmod.lib.util.LMFileUtils;

import java.io.File;

public class ConfigFile extends ConfigGroup
{
    private File file;

    public ConfigFile()
    {
    }

    @Override
    public ConfigFile asConfigFile()
    {
        return this;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File f)
    {
        file = LMFileUtils.newFile(f);
    }

    public void load()
    {
        JsonElement e = LMJsonUtils.fromJson(file);

        if(e.isJsonObject())
        {
            ConfigGroup g = new ConfigGroup();
            g.fromJson(e.getAsJsonObject());
            loadFromGroup(g, false);
        }
    }

    public void save()
    {
        if(file != null)
        {
            LMJsonUtils.toJson(file, getSerializableElement());
        }
    }

    public void addGroup(String id, Class<?> c)
    {
        add(id, new ConfigGroup().addAll(c, null));
    }
}