package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleTrader;

public class ContainerNPCTrader extends ContainerNpcInterface {
	public RoleTrader role;

	public ContainerNPCTrader(EntityNPCInterface npc, EntityPlayer player) {
		super(player);
		role = (RoleTrader) npc.roleInterface;

		for (int i = 0; i < 18; i++) {
			int x = 53;
			x += i % 3 * 72;
			int y = 7;
			y += i / 3 * 21;

			ItemStack item = role.inventoryCurrency.items.get(i);
			ItemStack item2 = role.inventoryCurrency.items.get(i + 18);
			if (item == null) {
				item = item2;
				item2 = null;
			}
			addSlotToContainer(new Slot(role.inventorySold, i, x, y));
		}

		for (int i1 = 0; i1 < 3; i1++) {
			for (int l1 = 0; l1 < 9; l1++) {
				addSlotToContainer(new Slot(player.inventory, l1 + i1 * 9 + 9, 32 + l1 * 18, 140 + i1 * 18));
			}

		}

		for (int j1 = 0; j1 < 9; j1++) {
			addSlotToContainer(new Slot(player.inventory, j1, 32 + j1 * 18, 198));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 18 && index < 44) {
				if (!this.mergeItemStack(itemstack1, 44, 53, false)) {
					return null;
				}
			} else if (index >= 44 && index < 53) {
				if (!this.mergeItemStack(itemstack1, 18, 44, false)) {
					return null;
				}
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);

		}
		return itemstack;
	}

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer entityplayer) {
		if (mode == 6)
			mode = 0;
		if (slotId < 0 || slotId >= 18)
			return super.slotClick(slotId, clickedButton, mode, entityplayer);
		if (clickedButton == 1)
			return null;
		Slot slot = (Slot) inventorySlots.get(slotId);
		if (slot == null || slot.getStack() == null)
			return null;
		ItemStack item = slot.getStack();
		if (!canBuy(slotId, entityplayer))
			return null;
		if (!isSlotEnabled(slotId, entityplayer))
			return null;

		ItemStack currency = role.inventoryCurrency.getStackInSlot(slotId);
		ItemStack currency2 = role.inventoryCurrency.getStackInSlot(slotId + 18);
		ItemStack soldItem = item.copy();
		boolean loop = NoppesUtilPlayer.compareItems(currency, soldItem, role.ignoreDamage, role.ignoreNBT)
				|| NoppesUtilPlayer.compareItems(currency2, soldItem, role.ignoreDamage, role.ignoreNBT);
		handlePurchase(entityplayer, slotId, soldItem, mode, loop);
		return soldItem;

	}

	public boolean isSlotEnabled(int slot, EntityPlayer player) {
		return role.isSlotEnabled(slot, player.getDisplayName());
	}

	public boolean canBuy(int slot, EntityPlayer player) {
		ItemStack currency = role.inventoryCurrency.getStackInSlot(slot);
		ItemStack currency2 = role.inventoryCurrency.getStackInSlot(slot + 18);
		if (currency == null && currency2 == null)
			return true;
		if (currency == null) {
			currency = currency2;
			currency2 = null;
		}
		if (NoppesUtilPlayer.compareItems(currency, currency2, role.ignoreDamage, role.ignoreNBT)) {
			currency = currency.copy();
			currency.stackSize += currency2.stackSize;
			currency2 = null;
		}
		if (currency2 == null)
			return NoppesUtilPlayer.compareItems(player, currency, role.ignoreDamage, role.ignoreNBT);
		return NoppesUtilPlayer.compareItems(player, currency, role.ignoreDamage, role.ignoreNBT)
				&& NoppesUtilPlayer.compareItems(player, currency2, role.ignoreDamage, role.ignoreNBT);

	}

	private boolean canGivePlayer(ItemStack item, EntityPlayer entityplayer) {// check Item being held with the mouse
		ItemStack itemstack3 = entityplayer.inventory.getItemStack();
		if (itemstack3 == null) {
			return true;
		} else if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {
			int k1 = item.stackSize;
			if (k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize()) {
				return true;
			}
		}
		return false;
	}

	private void givePlayer(ItemStack item, EntityPlayer entityplayer) {// set item bought to the held mouse item
		ItemStack itemstack3 = entityplayer.inventory.getItemStack();
		if (itemstack3 == null) {
			entityplayer.inventory.setItemStack(item);
		} else if (NoppesUtilPlayer.compareItems(itemstack3, item, false, false)) {

			int k1 = item.stackSize;
			if (k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize()) {
				itemstack3.stackSize += k1;
			}
		}
	}

	public void handlePurchase(EntityPlayer entityplayer, int slotId, ItemStack soldItem, int mode, boolean loop) {
		boolean shiftClick = (mode == 1);
		processPurchase(entityplayer, slotId, soldItem, loop, shiftClick);
	}

	private void processPurchase(EntityPlayer entityplayer, int slotId, ItemStack soldItem, boolean loop,
			boolean shiftClick) {
		if (shiftClick) {
			while (canBuy(slotId, entityplayer) && (loop ? canGivePlayer(soldItem, entityplayer)
					: entityplayer.inventory.getFirstEmptyStack() != -1)) {
				processTransaction(entityplayer, slotId, soldItem, loop);
			}
		} else {
			if ((loop && !canGivePlayer(soldItem, entityplayer))
					|| (!loop && entityplayer.inventory.getFirstEmptyStack() == -1)) {
				return;
			}
			processTransaction(entityplayer, slotId, soldItem, loop);
		}
	}

	private void processTransaction(EntityPlayer entityplayer, int slotId, ItemStack soldItem, boolean loop) {
		NoppesUtilPlayer.consumeItem(entityplayer, role.inventoryCurrency.getStackInSlot(slotId), role.ignoreDamage,
				role.ignoreNBT);
		NoppesUtilPlayer.consumeItem(entityplayer, role.inventoryCurrency.getStackInSlot(slotId + 18),
				role.ignoreDamage, role.ignoreNBT);

		if (loop) {
			givePlayer(soldItem.copy(), entityplayer);
		} else {
			entityplayer.inventory.addItemStackToInventory(soldItem.copy());
		}

		role.addPurchase(slotId, entityplayer.getDisplayName());
	}
}
