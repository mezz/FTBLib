package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.item.IMaterial;
import com.feed_the_beast.ftbl.api.recipes.IRecipes;
import com.feed_the_beast.ftbl.util.EnumDyeColorHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class LMRecipes implements IRecipes
{
    public static Object getFrom(Object o)
    {
        if(o == null)
        {
            return null;
        }
        else if(o instanceof IMaterial)
        {
            return ((IMaterial) o).getStack(1);
        }
        else if(o instanceof EnumDyeColor)
        {
            return EnumDyeColorHelper.get((EnumDyeColor) o).getOreName();
        }

        return null;
    }

    public static ItemStack size(ItemStack is, int s)
    {
        ItemStack is1 = is.copy();
        is1.stackSize = s;
        return is1;
    }

    public static Object[] fixObjects(Object[] in)
    {
        for(int i = 0; i < in.length; i++)
        {
            Object o = getFrom(in[i]);
            if(o != null)
            {
                in[i] = o;
            }
        }

        return in;
    }

    @Override
    public IRecipe addIRecipe(IRecipe r)
    {
        CraftingManager.getInstance().getRecipeList().add(r);
        return r;
    }

    @Override
    public IRecipe addRecipe(ItemStack out, Object... in)
    {
        return addIRecipe(new ShapedOreRecipe(out, fixObjects(in)));
    }

    @Override
    public IRecipe addShapelessRecipe(ItemStack out, Object... in)
    {
        return addIRecipe(new ShapelessOreRecipe(out, fixObjects(in)));
    }

    @Override
    public void addSmelting(ItemStack out, ItemStack in, float xp)
    {
        FurnaceRecipes.instance().addSmeltingRecipe(in, out, xp);
    }
}