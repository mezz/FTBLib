package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import latmod.lib.FinalIDObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientNotifications
{
    private static Temp current = null;

    public static class Temp extends FinalIDObject
    {
        public static final List<Temp> list = new ArrayList<>();

        private long time;
        private Notification notification;
        private List<String> text;
        private int width;

        private Temp(Notification n)
        {
            super(n.getID());
            notification = n;
            time = -1L;
        }

        public boolean render()
        {
            if(time == -1L)
            {
                time = Minecraft.getSystemTime();
            }

            FontRenderer font = FTBLibClient.mc().fontRendererObj;

            if(text == null)
            {
                text = new ArrayList<>();
                width = 0;

                for(ITextComponent t : notification.text)
                {
                    String s = t.getFormattedText();
                    text.add(s);

                    width = Math.max(width, font.getStringWidth(s));
                }

                width += 20;

                if(notification.hasItem())
                {
                    width += 20;
                }
            }

            if(time > 0L)
            {
                double d0 = (double) (Minecraft.getSystemTime() - time) / (double) notification.getTimer();

                if(d0 < 0D || d0 > 1D)
                {
                    time = 0L;
                    return true;
                }

                double d1 = d0 * 2D;

                if(d1 > 1D)
                {
                    d1 = 2D - d1;
                }
                d1 *= 4D;
                d1 = 1D - d1;

                if(d1 < 0D)
                {
                    d1 = 0D;
                }

                d1 *= d1;
                d1 *= d1;

                int displayW = new ScaledResolution(FTBLibClient.mc()).getScaledWidth();

                GlStateManager.disableDepth();
                GlStateManager.pushMatrix();
                GlStateManager.depthMask(false);
                GlStateManager.translate(displayW - width, -d1 * 36D, 0F);

                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                FTBLibClient.setGLColor(notification.getColor(), 230);
                GuiLM.drawBlankRect(0D, 0D, displayW, 32D);
                GlStateManager.enableTexture2D();
                GlStateManager.color(1F, 1F, 1F, 1F);

                int w = notification.hasItem() ? 10 : 30;

                if(text.size() == 1)
                {
                    font.drawString(text.get(0), w, 12, 0xFFFFFFFF);
                }
                else if(text.size() == 2)
                {
                    font.drawString(text.get(0), w, 7, 0xFFFFFFFF);
                    font.drawString(text.get(1), w, 18, 0xFFFFFFFF);
                }
                else
                {
                    for(int i = 0; i < text.size(); i++)
                    {
                        font.drawString(text.get(i), w, 4 + i * 12, 0xFFFFFFFF);
                    }
                }

                if(notification.hasItem())
                {
                    FTBLibClient.renderGuiItem(notification.getItem(), 8D, 8D);
                }

                GlStateManager.depthMask(true);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
            }

            return time == 0L;
        }
    }

    public static class Perm extends FinalIDObject implements Comparable<Perm>
    {
        public static final LinkedHashMap<String, Perm> map = new LinkedHashMap<>();

        public final Notification notification;
        public final long timeAdded;

        private Perm(Notification n)
        {
            super(n.getID());
            notification = n;
            timeAdded = System.currentTimeMillis();
        }

        @Override
        public int compareTo(@Nonnull Perm o)
        {
            return Long.compare(o.timeAdded, timeAdded);
        }

        public void onClicked(MouseButton button)
        {
            if(notification.getClickAction() != null)
            {
                notification.getClickAction().onClicked(button);
            }
        }
    }

    public static void renderTemp()
    {
        if(current != null)
        {
            if(current.render())
            {
                current = null;
            }
        }
        else if(!Temp.list.isEmpty())
        {
            current = Temp.list.get(0);
            Temp.list.remove(0);
        }
    }

    public static void add(Notification n)
    {
        if(n == null)
        {
            return;
        }
        Temp.list.remove(n);
        Perm.map.remove(n);
        if(current != null && current.equals(n))
        {
            current = null;
        }

        Temp.list.add(new Temp(n));
        if(!n.isTemp())
        {
            Perm.map.put(n.getID(), new Perm(n));
        }
    }

    public static void init()
    {
        current = null;
        Perm.map.clear();
        Temp.list.clear();
    }
}