package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.select.GuiSoundSelection;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBard;

public class GuiNpcBard extends GuiNPCInterface2 implements ISubGuiListener
{
	private JobBard job;

    public GuiNpcBard(EntityNPCInterface npc)
    {
    	super(npc);
    	job = (JobBard) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();

    	this.addButton(new GuiNpcButton(1, guiLeft + 55, guiTop + 15,20,20, "X"));
        addLabel(new GuiNpcLabel(0,job.song, guiLeft + 80, guiTop + 20));
    	this.addButton(new GuiNpcButton(0, guiLeft + 75, guiTop + 50, "gui.selectSound"));

    	this.addButton(new GuiNpcButton(2, guiLeft + 75, guiTop + 71, new String[]{"gui.none","item.npcBanjo.name","item.npcViolin.name","item.npcGuitar.name","item.npcHarp.name","item.npcFrenchHorn.name"}, job.getInstrument().ordinal()));
    	this.addButton(new GuiNpcButton(3, guiLeft + 75, guiTop + 92, new String[]{"bard.jukebox","bard.background"}, job.isStreamer?0:1));


        addLabel(new GuiNpcLabel(2,"bard.ondistance", guiLeft + 60, guiTop + 143));
        addTextField(new GuiNpcTextField(8,this, this.fontRendererObj, guiLeft+140, guiTop + 138, 20, 20, job.minRangeX + ""));
        getTextField(8).integersOnly = true;
        getTextField(8).setMinMaxDefault(0, Integer.MAX_VALUE, 5);
        addTextField(new GuiNpcTextField(9,this, this.fontRendererObj, guiLeft+165, guiTop + 138, 20, 20, job.minRangeY + ""));
        getTextField(9).integersOnly = true;
        getTextField(9).setMinMaxDefault(0, Integer.MAX_VALUE, 5);
        addTextField(new GuiNpcTextField(10,this, this.fontRendererObj, guiLeft+190, guiTop + 138, 20, 20, job.minRangeZ + ""));
        getTextField(10).integersOnly = true;
        getTextField(10).setMinMaxDefault(0, Integer.MAX_VALUE, 5);

        addLabel(new GuiNpcLabel(4,"bard.hasoff", guiLeft + 60, guiTop + 166));
        addButton(new GuiNpcButton(4, guiLeft + 140, guiTop + 161, 60, 20, new String[]{"gui.no","gui.yes"}, job.hasOffRange?1:0));

        addLabel(new GuiNpcLabel(3,"bard.offdistance", guiLeft + 60, guiTop + 189));
        addTextField(new GuiNpcTextField(11,this, this.fontRendererObj, guiLeft+140, guiTop + 184, 20, 20, job.maxRangeX + ""));
        getTextField(11).integersOnly = true;
        getTextField(11).setMinMaxDefault(0, Integer.MAX_VALUE, 10);
        addTextField(new GuiNpcTextField(12,this, this.fontRendererObj, guiLeft+165, guiTop + 184, 20, 20, job.maxRangeY + ""));
        getTextField(12).integersOnly = true;
        getTextField(12).setMinMaxDefault(0, Integer.MAX_VALUE, 10);
        addTextField(new GuiNpcTextField(13,this, this.fontRendererObj, guiLeft+190, guiTop + 184, 20, 20, job.maxRangeZ + ""));
        getTextField(13).integersOnly = true;
        getTextField(13).setMinMaxDefault(0, Integer.MAX_VALUE, 10);
        
        addLabel(new GuiNpcLabel(5,"bard.offsetX", guiLeft + 240, guiTop + 143));
        addTextField(new GuiNpcTextField(4,this, this.fontRendererObj, guiLeft+290, guiTop + 138, 20, 20, job.offsetX + ""));
        getTextField(4).integersOnly = true;
        getTextField(4).setMinMaxDefault(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        
        addLabel(new GuiNpcLabel(6,"bard.offsetY", guiLeft + 240, guiTop + 166));
        addTextField(new GuiNpcTextField(5,this, this.fontRendererObj, guiLeft+290, guiTop + 161, 20, 20, job.offsetY + ""));
        getTextField(5).integersOnly = true;
        getTextField(5).setMinMaxDefault(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        
        addLabel(new GuiNpcLabel(7,"bard.offsetZ", guiLeft + 240, guiTop + 189));
        addTextField(new GuiNpcTextField(6,this, this.fontRendererObj, guiLeft+290, guiTop + 184, 20, 20,  job.offsetZ + ""));
        getTextField(6).integersOnly = true;
        getTextField(6).setMinMaxDefault(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        
        addLabel(new GuiNpcLabel(10,"bard.volume", guiLeft + 290, guiTop + 50));
        addTextField(new GuiNpcTextField(17,this, this.fontRendererObj, guiLeft+320, guiTop + 45, 20, 20,  job.volume + ""));
        getTextField(17).setFloatsOnly();
        getTextField(17).setMinMaxDefaultFloat(0.0F, Float.MAX_VALUE, 4.0F);
        
        addLabel(new GuiNpcLabel(11,"bard.pitch", guiLeft + 290, guiTop + 71));
        addTextField(new GuiNpcTextField(18,this, this.fontRendererObj, guiLeft+320, guiTop + 66, 20, 20,  job.pitch + ""));
        getTextField(18).setFloatsOnly();
        getTextField(18).setMinMaxDefaultFloat(0.0F, Float.MAX_VALUE, 1.0F);

    	getLabel(3).enabled = job.hasOffRange;
    	getTextField(11).enabled = job.hasOffRange;
    	getTextField(12).enabled = job.hasOffRange;
    	getTextField(13).enabled = job.hasOffRange;

    }

    protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
        if(button.id == 0)
        {
            setSubGui(new GuiSoundSelection(job.song));
            MusicController.Instance.stopMusic();
        }
        if(button.id == 1)
        {
        	job.song = "";
        	getLabel(0).label = "";
        	MusicController.Instance.stopMusic();
        }
        if(button.id == 2)
        {
        	job.setInstrument(button.getValue());
        }
        if(button.id == 3)
        {
        	job.isStreamer = button.getValue() == 0;
        	initGui();
        }
        if(button.id == 4)
        {
        	job.hasOffRange = button.getValue() == 1;
        	initGui();
        }

    }

    @Override
	public void save() {
    	job.offsetX =  getTextField(4).getInteger();
    	job.offsetY =  getTextField(5).getInteger();
    	job.offsetZ =  getTextField(6).getInteger();
    	job.minRangeX = getTextField(8).getInteger();
    	job.minRangeY = getTextField(9).getInteger();
    	job.minRangeZ = getTextField(10).getInteger();
    	job.maxRangeX = getTextField(11).getInteger();
    	job.maxRangeY = getTextField(12).getInteger();
    	job.maxRangeZ = getTextField(13).getInteger();
    	job.volume= getTextField(17).getFloat();
    	job.pitch= getTextField(18).getFloat();
    			
    	if(job.minRangeX > job.maxRangeX)
    		job.maxRangeX = job.minRangeX;
    	if(job.minRangeY > job.maxRangeY)
    		job.maxRangeY = job.minRangeY;
    	if(job.minRangeZ > job.maxRangeZ)
    		job.maxRangeZ = job.minRangeZ;

    	MusicController.Instance.stopMusic();
		Client.sendData(EnumPacketServer.JobSave, job.writeToNBT(new NBTTagCompound()));
	}


    @Override
    public void subGuiClosed(SubGuiInterface subgui) {
        GuiSoundSelection gss = (GuiSoundSelection) subgui;
        if(gss.selectedResource != null) {
            job.song = gss.selectedResource.toString();
            getLabel(0).label = job.song;
            initGui();
        }
    }
}
