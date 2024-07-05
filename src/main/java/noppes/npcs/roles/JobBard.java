package noppes.npcs.roles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.constants.EnumBardInstrument;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JobBard extends JobInterface {
	@Deprecated
	public int minRange = 0;
	@Deprecated
	public int maxRange = 0;

	public int minRangeX = 5;
	public int minRangeY = 5;
	public int minRangeZ = 5;
	public int maxRangeX = 10;
	public int maxRangeY = 10;
	public int maxRangeZ = 10;

	public int offsetX = 0;
	public int offsetY = 0;
	public int offsetZ = 0;
	
	public int fadeOutTimeMs = 0;

	public float volume = 4.0F;
	public float pitch = 1.0F;

	public boolean isStreamer = true;
	public boolean hasOffRange = true;

	public String song = "";

	private EnumBardInstrument instrument = EnumBardInstrument.Banjo;
	private static final Logger LOGGER = LogManager.getLogger();

	public JobBard(EntityNPCInterface npc) {
		super(npc);

		if (this.minRange != 0) {
			this.minRangeX = this.minRangeY = this.minRangeZ = this.minRange;
			this.minRange = 0;
		}

		if (this.maxRange != 0) {
			this.maxRangeX = this.maxRangeY = this.maxRangeZ = this.maxRange;
			this.maxRange = 0;
		}

		if (CustomItems.banjo != null) {
			mainhand = new ItemStack(CustomItems.banjo);
			overrideMainHand = overrideOffHand = true;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("BardSong", song);
		nbttagcompound.setInteger("BardMinRange", minRange);
		nbttagcompound.setInteger("BardMinRangeX", minRangeX);
		nbttagcompound.setInteger("BardMinRangeY", minRangeY);
		nbttagcompound.setInteger("BardMinRangeZ", minRangeZ);
		nbttagcompound.setInteger("BardMaxRange", maxRange);
		nbttagcompound.setInteger("BardMaxRangeX", maxRangeX);
		nbttagcompound.setInteger("BardMaxRangeY", maxRangeY);
		nbttagcompound.setInteger("BardMaxRangeZ", maxRangeZ);
		nbttagcompound.setInteger("BardOffsetX", offsetX);
		nbttagcompound.setInteger("BardOffsetY", offsetY);
		nbttagcompound.setInteger("BardOffsetZ", offsetZ);
		nbttagcompound.setInteger("BardFadeOutTimeMs", fadeOutTimeMs);
		nbttagcompound.setFloat("BardVolume", volume);
		nbttagcompound.setFloat("BardPitch", pitch);
		nbttagcompound.setInteger("BardInstrument", instrument.ordinal());
		nbttagcompound.setBoolean("BardStreamer", isStreamer);
		nbttagcompound.setBoolean("BardHasOff", hasOffRange);

		return nbttagcompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		song = nbttagcompound.getString("BardSong");
		minRange = nbttagcompound.getInteger("BardMinRange");
		minRangeX = nbttagcompound.getInteger("BardMinRangeX");
		minRangeY = nbttagcompound.getInteger("BardMinRangeY");
		minRangeZ = nbttagcompound.getInteger("BardMinRangeZ");
		maxRange = nbttagcompound.getInteger("BardMaxRange");
		maxRangeX = nbttagcompound.getInteger("BardMaxRangeX");
		maxRangeY = nbttagcompound.getInteger("BardMaxRangeY");
		maxRangeZ = nbttagcompound.getInteger("BardMaxRangeZ");
		offsetX = nbttagcompound.getInteger("BardOffsetX");
		offsetY = nbttagcompound.getInteger("BardOffsetY");
		offsetZ = nbttagcompound.getInteger("BardOffsetZ");
		fadeOutTimeMs = nbttagcompound.getInteger("BardFadeOutTimeMs");
		volume = nbttagcompound.getFloat("BardVolume");
		pitch = nbttagcompound.getFloat("BardPitch");
		setInstrument(nbttagcompound.getInteger("BardInstrument"));
		isStreamer = nbttagcompound.getBoolean("BardStreamer");
		hasOffRange = nbttagcompound.getBoolean("BardHasOff");
	}

	public void setInstrument(int i) {
		if (CustomItems.banjo == null)
			return;
		instrument = EnumBardInstrument.values()[i];
		overrideMainHand = overrideOffHand = instrument != EnumBardInstrument.None;
		switch (instrument) {
		case None:
			this.mainhand = null;
			this.offhand = null;
			break;
		case Banjo:
			this.mainhand = new ItemStack(CustomItems.banjo);
			this.offhand = null;
			break;
		case Violin:
			this.mainhand = new ItemStack(CustomItems.violin);
			this.offhand = new ItemStack(CustomItems.violinbow);
			break;
		case Guitar:
			this.mainhand = new ItemStack(CustomItems.guitar);
			this.offhand = null;
			break;
		case Harp:
			this.mainhand = new ItemStack(CustomItems.harp);
			this.offhand = null;
			break;
		case FrenchHorn:
			this.mainhand = new ItemStack(CustomItems.frenchHorn);
			this.offhand = null;
			break;
		}
	}

	public EnumBardInstrument getInstrument() {
		return instrument;
	}

	@SuppressWarnings("unchecked")
	public void onLivingUpdate() {
		if (!npc.isRemote() || song.isEmpty())
			return;
		//LOGGER.info("onLivingUpdate1 call: ");
		if (!MusicController.Instance.isPlaying(song)) {
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox
					.expand(minRangeX, minRangeY, minRangeZ).getOffsetBoundingBox(offsetX, offsetY, offsetZ));
			if (!list.contains(CustomNpcs.proxy.getPlayer()))
				return;
			if (isStreamer) {
				MusicController.Instance.playStreaming(song, npc, volume, pitch, offsetX, offsetY, offsetZ, fadeOutTimeMs);
			} else {
				MusicController.Instance.playMusic(song, npc, volume, pitch, fadeOutTimeMs);
			}
		} else if (MusicController.Instance.playingEntity != npc) {
			/*
			 * 这个功能可以“接力播放” 
			 * 设置要求：触发范围不能重叠，关闭范围重叠 
			 * 小bug：在关闭范围重叠的那一小层进入则不会触发音乐
			 */
			EntityPlayer player = CustomNpcs.proxy.getPlayer();
			if (npc.getDistanceSqToEntity(player) < MusicController.Instance.playingEntity
					.getDistanceSqToEntity(player)) {
				MusicController.Instance.playingEntity = npc;
			}

		} else if (hasOffRange) {
			List<EntityPlayer> list = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox
					.expand(maxRangeX, maxRangeY, maxRangeZ).getOffsetBoundingBox(offsetX, offsetY, offsetZ));
			if (!list.contains(CustomNpcs.proxy.getPlayer()))
				{
					MusicController.Instance.fadeOutPlaying(null, fadeOutTimeMs);
				}
		}
		//LOGGER.info("onLivingUpdate2 call: ");
		if (MusicController.Instance.isPlaying(song)) {
			Minecraft.getMinecraft().mcMusicTicker.field_147676_d = 12000;
		}
	}

	@Override
	public void killed() {
		delete();
	}

	@Override
	public void delete() {
		if (npc.worldObj.isRemote) {
			if (MusicController.Instance.isPlaying(song)) {
				MusicController.Instance.stopPlaying();
			}
		}
	}
}
