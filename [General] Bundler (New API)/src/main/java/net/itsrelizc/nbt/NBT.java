package net.itsrelizc.nbt;

import java.util.Set;

import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/*
 * net.minecraft.nbt.CompoundTag -> qr:
    com.mojang.serialization.Codec CODEC -> a
    int SELF_SIZE_IN_BYTES -> c
    int MAP_ENTRY_SIZE_IN_BYTES -> w
    net.minecraft.nbt.TagType TYPE -> b
    java.util.Map tags -> x
    153:155:void <init>(java.util.Map) -> <init>
    158:159:void <init>() -> <init>
    163:168:void write(java.io.DataOutput) -> a
    172:178:int sizeInBytes() -> a
    182:182:java.util.Set getAllKeys() -> e
    187:187:byte getId() -> b
    192:192:net.minecraft.nbt.TagType getType() -> c
    196:196:int size() -> f
    201:201:net.minecraft.nbt.Tag put(java.lang.String,net.minecraft.nbt.Tag) -> a
    205:206:void putByte(java.lang.String,byte) -> a
    209:210:void putShort(java.lang.String,short) -> a
    213:214:void putInt(java.lang.String,int) -> a
    217:218:void putLong(java.lang.String,long) -> a
    221:222:void putUUID(java.lang.String,java.util.UUID) -> a
    225:225:java.util.UUID getUUID(java.lang.String) -> a
    229:230:boolean hasUUID(java.lang.String) -> b
    234:235:void putFloat(java.lang.String,float) -> a
    238:239:void putDouble(java.lang.String,double) -> a
    242:243:void putString(java.lang.String,java.lang.String) -> a
    246:247:void putByteArray(java.lang.String,byte[]) -> a
    250:251:void putByteArray(java.lang.String,java.util.List) -> a
    254:255:void putIntArray(java.lang.String,int[]) -> a
    258:259:void putIntArray(java.lang.String,java.util.List) -> b
    262:263:void putLongArray(java.lang.String,long[]) -> a
    266:267:void putLongArray(java.lang.String,java.util.List) -> c
    270:271:void putBoolean(java.lang.String,boolean) -> a
    275:275:net.minecraft.nbt.Tag get(java.lang.String) -> c
    279:283:byte getTagType(java.lang.String) -> d
    287:287:boolean contains(java.lang.String) -> e
    291:299:boolean contains(java.lang.String,int) -> b
    304:309:byte getByte(java.lang.String) -> f
    314:319:short getShort(java.lang.String) -> g
    324:329:int getInt(java.lang.String) -> h
    334:339:long getLong(java.lang.String) -> i
    344:349:float getFloat(java.lang.String) -> j
    354:359:double getDouble(java.lang.String) -> k
    364:369:java.lang.String getString(java.lang.String) -> l
    374:380:byte[] getByteArray(java.lang.String) -> m
    385:391:int[] getIntArray(java.lang.String) -> n
    396:402:long[] getLongArray(java.lang.String) -> o
    407:413:net.minecraft.nbt.CompoundTag getCompound(java.lang.String) -> p
    418:428:net.minecraft.nbt.ListTag getList(java.lang.String,int) -> c
    432:432:boolean getBoolean(java.lang.String) -> q
    436:437:void remove(java.lang.String) -> r
    441:441:java.lang.String toString() -> toString
    445:445:boolean isEmpty() -> g
    449:456:net.minecraft.CrashReport createReport(java.lang.String,net.minecraft.nbt.TagType,java.lang.ClassCastException) -> a
    461:462:net.minecraft.nbt.CompoundTag copy() -> h
    467:471:boolean equals(java.lang.Object) -> equals
    476:476:int hashCode() -> hashCode
    480:488:void writeNamedTag(java.lang.String,net.minecraft.nbt.Tag,java.io.DataOutput) -> a
    491:491:byte readNamedTagType(java.io.DataInput,net.minecraft.nbt.NbtAccounter) -> a
    495:495:java.lang.String readNamedTagName(java.io.DataInput,net.minecraft.nbt.NbtAccounter) -> b
    500:506:net.minecraft.nbt.Tag readNamedTagData(net.minecraft.nbt.TagType,java.lang.String,java.io.DataInput,int,net.minecraft.nbt.NbtAccounter) -> a
    524:539:net.minecraft.nbt.CompoundTag merge(net.minecraft.nbt.CompoundTag) -> a
    544:545:void accept(net.minecraft.nbt.TagVisitor) -> a
    548:548:java.util.Map entries() -> i
    553:584:net.minecraft.nbt.StreamTagVisitor$ValueResult accept(net.minecraft.nbt.StreamTagVisitor) -> a
    22:22:net.minecraft.nbt.Tag copy() -> d
    452:452:java.lang.String lambda$createReport$3(java.lang.String) -> s
    31:31:com.mojang.serialization.Dynamic lambda$static$2(net.minecraft.nbt.CompoundTag) -> b
    25:29:com.mojang.serialization.DataResult lambda$static$1(com.mojang.serialization.Dynamic) -> a
    29:29:java.lang.String lambda$static$0(net.minecraft.nbt.Tag) -> a
    23:57:void <clinit>() -> <clinit>
    
    
    
    
    
    
    
net.minecraft.world.item.ItemStack -> cfz:
    com.mojang.serialization.Codec CODEC -> a
    org.slf4j.Logger LOGGER -> j
    net.minecraft.world.item.ItemStack EMPTY -> b
    java.text.DecimalFormat ATTRIBUTE_MODIFIER_FORMAT -> c
    java.lang.String TAG_ENCH -> d
    java.lang.String TAG_DISPLAY -> e
    java.lang.String TAG_DISPLAY_NAME -> f
    java.lang.String TAG_LORE -> g
    java.lang.String TAG_DAMAGE -> h
    java.lang.String TAG_COLOR -> i
    java.lang.String TAG_UNBREAKABLE -> k
    java.lang.String TAG_REPAIR_COST -> l
    java.lang.String TAG_CAN_DESTROY_BLOCK_LIST -> m
    java.lang.String TAG_CAN_PLACE_ON_BLOCK_LIST -> n
    java.lang.String TAG_HIDE_FLAGS -> o
    net.minecraft.network.chat.Component DISABLED_ITEM_TOOLTIP -> p
    int DONT_HIDE_TOOLTIP -> q
    net.minecraft.network.chat.Style LORE_STYLE -> r
    int count -> s
    int popTime -> t
    net.minecraft.world.item.Item item -> u
    net.minecraft.nbt.CompoundTag tag -> v
    net.minecraft.world.entity.Entity entityRepresentation -> w
    net.minecraft.world.item.AdventureModeCheck adventureBreakCheck -> x
    net.minecraft.world.item.AdventureModeCheck adventurePlaceCheck -> y
    110:110:java.util.Optional getTooltipImage() -> a
    152:153:void <init>(net.minecraft.world.level.ItemLike) -> <init>
    156:157:void <init>(net.minecraft.core.Holder) -> <init>
    160:162:void <init>(net.minecraft.world.level.ItemLike,int,java.util.Optional) -> <init>
    165:166:void <init>(net.minecraft.core.Holder,int) -> <init>
    168:175:void <init>(net.minecraft.world.level.ItemLike,int) -> <init>
    177:179:void <init>(java.lang.Void) -> <init>
    181:193:void <init>(net.minecraft.nbt.CompoundTag) -> <init>
    197:200:net.minecraft.world.item.ItemStack of(net.minecraft.nbt.CompoundTag) -> a
    205:205:boolean isEmpty() -> b
    209:209:boolean isItemEnabled(net.minecraft.world.flag.FeatureFlagSet) -> a
    217:222:net.minecraft.world.item.ItemStack split(int) -> a
    226:231:net.minecraft.world.item.ItemStack copyAndClear() -> c
    236:236:net.minecraft.world.item.Item getItem() -> d
    240:240:net.minecraft.core.Holder getItemHolder() -> e
    244:244:boolean is(net.minecraft.tags.TagKey) -> a
    248:248:boolean is(net.minecraft.world.item.Item) -> a
    252:252:boolean is(java.util.function.Predicate) -> a
    256:256:boolean is(net.minecraft.core.Holder) -> a
    260:260:java.util.stream.Stream getTags() -> f
    264:276:net.minecraft.world.InteractionResult useOn(net.minecraft.world.item.context.UseOnContext) -> a
    280:280:float getDestroySpeed(net.minecraft.world.level.block.state.BlockState) -> a
    284:284:net.minecraft.world.InteractionResultHolder use(net.minecraft.world.level.Level,net.minecraft.world.entity.player.Player,net.minecraft.world.InteractionHand) -> a
    288:288:net.minecraft.world.item.ItemStack finishUsingItem(net.minecraft.world.level.Level,net.minecraft.world.entity.LivingEntity) -> a
    292:298:net.minecraft.nbt.CompoundTag save(net.minecraft.nbt.CompoundTag) -> b
    302:302:int getMaxStackSize() -> g
    306:306:boolean isStackable() -> h
    310:314:boolean isDamageableItem() -> i
    318:318:boolean isDamaged() -> j
    322:322:int getDamageValue() -> k
    326:327:void setDamageValue(int) -> b
    330:330:int getMaxDamage() -> l
    334:362:boolean hurt(int,net.minecraft.util.RandomSource,net.minecraft.server.level.ServerPlayer) -> a
    368:387:void hurtAndBreak(int,net.minecraft.world.entity.LivingEntity,java.util.function.Consumer) -> a
    390:390:boolean isBarVisible() -> m
    394:394:int getBarWidth() -> n
    398:398:int getBarColor() -> o
    402:402:boolean overrideStackedOnOther(net.minecraft.world.inventory.Slot,net.minecraft.world.inventory.ClickAction,net.minecraft.world.entity.player.Player) -> a
    406:406:boolean overrideOtherStackedOnMe(net.minecraft.world.item.ItemStack,net.minecraft.world.inventory.Slot,net.minecraft.world.inventory.ClickAction,net.minecraft.world.entity.player.Player,net.minecraft.world.entity.SlotAccess) -> a
    410:414:void hurtEnemy(net.minecraft.world.entity.LivingEntity,net.minecraft.world.entity.player.Player) -> a
    417:421:void mineBlock(net.minecraft.world.level.Level,net.minecraft.world.level.block.state.BlockState,net.minecraft.core.BlockPos,net.minecraft.world.entity.player.Player) -> a
    424:424:boolean isCorrectToolForDrops(net.minecraft.world.level.block.state.BlockState) -> b
    428:428:net.minecraft.world.InteractionResult interactLivingEntity(net.minecraft.world.entity.player.Player,net.minecraft.world.entity.LivingEntity,net.minecraft.world.InteractionHand) -> a
    432:440:net.minecraft.world.item.ItemStack copy() -> p
    444:449:net.minecraft.world.item.ItemStack copyWithCount(int) -> c
    453:459:boolean matches(net.minecraft.world.item.ItemStack,net.minecraft.world.item.ItemStack) -> a
    463:463:boolean isSameItem(net.minecraft.world.item.ItemStack,net.minecraft.world.item.ItemStack) -> b
    467:473:boolean isSameItemSameTags(net.minecraft.world.item.ItemStack,net.minecraft.world.item.ItemStack) -> c
    477:477:java.lang.String getDescriptionId() -> q
    482:482:java.lang.String toString() -> toString
    486:492:void inventoryTick(net.minecraft.world.level.Level,net.minecraft.world.entity.Entity,int,boolean) -> a
    495:497:void onCraftedBy(net.minecraft.world.level.Level,net.minecraft.world.entity.player.Player,int) -> a
    500:500:int getUseDuration() -> r
    504:504:net.minecraft.world.item.UseAnim getUseAnimation() -> s
    508:509:void releaseUsing(net.minecraft.world.level.Level,net.minecraft.world.entity.LivingEntity,int) -> a
    512:512:boolean useOnRelease() -> t
    516:516:boolean hasTag() -> u
    521:521:net.minecraft.nbt.CompoundTag getTag() -> v
    525:529:net.minecraft.nbt.CompoundTag getOrCreateTag() -> w
    533:538:net.minecraft.nbt.CompoundTag getOrCreateTagElement(java.lang.String) -> a
    543:546:net.minecraft.nbt.CompoundTag getTagElement(java.lang.String) -> b
    550:556:void removeTagKey(java.lang.String) -> c
    559:562:net.minecraft.nbt.ListTag getEnchantmentTags() -> x
    566:575:void setTag(net.minecraft.nbt.CompoundTag) -> c
    578:594:net.minecraft.network.chat.Component getHoverName() -> y
    598:604:net.minecraft.world.item.ItemStack setHoverName(net.minecraft.network.chat.Component) -> a
    608:620:void resetHoverName() -> z
    623:624:boolean hasCustomHoverName() -> A
    629:791:java.util.List getTooltipLines(net.minecraft.world.entity.player.Player,net.minecraft.world.item.TooltipFlag) -> a
    795:795:boolean shouldShowInTooltip(int,net.minecraft.world.item.ItemStack$TooltipPart) -> a
    799:802:int getHideFlags() -> P
    806:808:void hideTooltipPart(net.minecraft.world.item.ItemStack$TooltipPart) -> a
    811:817:void appendEnchantmentNames(java.util.List,net.minecraft.nbt.ListTag) -> a
    821:827:java.util.Collection expandBlockState(java.lang.String) -> d
    831:831:boolean hasFoil() -> B
    835:835:net.minecraft.world.item.Rarity getRarity() -> C
    839:845:boolean isEnchantable() -> D
    849:855:void enchant(net.minecraft.world.item.enchantment.Enchantment,int) -> a
    858:861:boolean isEnchanted() -> E
    865:866:void addTagElement(java.lang.String,net.minecraft.nbt.Tag) -> a
    869:869:boolean isFramed() -> F
    873:874:void setEntityRepresentation(net.minecraft.world.entity.Entity) -> a
    878:878:net.minecraft.world.entity.decoration.ItemFrame getFrame() -> G
    883:883:net.minecraft.world.entity.Entity getEntityRepresentation() -> H
    887:890:int getBaseRepairCost() -> I
    894:895:void setRepairCost(int) -> d
    900:930:com.google.common.collect.Multimap getAttributeModifiers(net.minecraft.world.entity.EquipmentSlot) -> a
    934:945:void addAttributeModifier(net.minecraft.world.entity.ai.attributes.Attribute,net.minecraft.world.entity.ai.attributes.AttributeModifier,net.minecraft.world.entity.EquipmentSlot) -> a
    948:959:net.minecraft.network.chat.Component getDisplayName() -> J
    970:973:boolean hasAdventureModePlaceTagForBlock(net.minecraft.core.Registry,net.minecraft.world.level.block.state.pattern.BlockInWorld) -> a
    977:980:boolean hasAdventureModeBreakTagForBlock(net.minecraft.core.Registry,net.minecraft.world.level.block.state.pattern.BlockInWorld) -> b
    984:984:int getPopTime() -> K
    988:989:void setPopTime(int) -> e
    992:992:int getCount() -> L
    996:997:void setCount(int) -> f
    1000:1001:void grow(int) -> g
    1004:1005:void shrink(int) -> h
    1008:1009:void onUseTick(net.minecraft.world.level.Level,net.minecraft.world.entity.LivingEntity,int) -> b
    1012:1013:void onDestroyed(net.minecraft.world.entity.item.ItemEntity) -> a
    1016:1016:boolean isEdible() -> M
    1020:1020:net.minecraft.sounds.SoundEvent getDrinkingSound() -> N
    1024:1024:net.minecraft.sounds.SoundEvent getEatingSound() -> O
    956:956:net.minecraft.network.chat.Style lambda$getDisplayName$7(net.minecraft.network.chat.Style) -> a
    823:823:java.util.List lambda$expandBlockState$6(net.minecraft.commands.arguments.blocks.BlockStateParser$TagResult) -> a
    823:823:net.minecraft.network.chat.MutableComponent lambda$expandBlockState$5(net.minecraft.core.Holder) -> b
    822:822:java.util.List lambda$expandBlockState$4(net.minecraft.commands.arguments.blocks.BlockStateParser$BlockResult) -> a
    815:815:void lambda$appendEnchantmentNames$3(java.util.List,net.minecraft.nbt.CompoundTag,net.minecraft.world.item.enchantment.Enchantment) -> a
    90:90:void lambda$static$2(java.text.DecimalFormat) -> a
    80:84:com.mojang.datafixers.kinds.App lambda$static$1(com.mojang.serialization.codecs.RecordCodecBuilder$Instance) -> a
    83:83:java.util.Optional lambda$static$0(net.minecraft.world.item.ItemStack) -> a
    80:133:void <clinit>() -> <clinit>
    
    
net.minecraft.nbt.ListTag -> qx:
    int SELF_SIZE_IN_BYTES -> b
    net.minecraft.nbt.TagType TYPE -> a
    java.util.List list -> c
    byte type -> w
    109:112:void <init>(java.util.List,byte) -> <init>
    115:116:void <init>() -> <init>
    120:131:void write(java.io.DataOutput) -> a
    135:140:int sizeInBytes() -> a
    145:145:byte getId() -> b
    150:150:net.minecraft.nbt.TagType getType() -> c
    155:155:java.lang.String toString() -> toString
    159:162:void updateTypeAfterRemove() -> g
    166:168:net.minecraft.nbt.Tag remove(int) -> c
    173:173:boolean isEmpty() -> isEmpty
    177:183:net.minecraft.nbt.CompoundTag getCompound(int) -> a
    187:193:net.minecraft.nbt.ListTag getList(int) -> b
    197:203:short getShort(int) -> d
    207:213:int getInt(int) -> e
    217:223:int[] getIntArray(int) -> f
    227:233:long[] getLongArray(int) -> g
    237:243:double getDouble(int) -> h
    247:253:float getFloat(int) -> i
    257:264:java.lang.String getString(int) -> j
    269:269:int size() -> size
    274:274:net.minecraft.nbt.Tag get(int) -> k
    279:283:net.minecraft.nbt.Tag set(int,net.minecraft.nbt.Tag) -> d
    288:291:void add(int,net.minecraft.nbt.Tag) -> c
    295:299:boolean setTag(int,net.minecraft.nbt.Tag) -> a
    304:308:boolean addTag(int,net.minecraft.nbt.Tag) -> b
    312:319:boolean updateType(net.minecraft.nbt.Tag) -> a
    324:326:net.minecraft.nbt.ListTag copy() -> e
    331:335:boolean equals(java.lang.Object) -> equals
    340:340:int hashCode() -> hashCode
    345:346:void accept(net.minecraft.nbt.TagVisitor) -> a
    350:350:byte getElementType() -> f
    355:357:void clear() -> clear
    361:384:net.minecraft.nbt.StreamTagVisitor$ValueResult accept(net.minecraft.nbt.StreamTagVisitor) -> a
    13:13:net.minecraft.nbt.Tag copy() -> d
    13:13:java.lang.Object remove(int) -> remove
    13:13:void add(int,java.lang.Object) -> add
    13:13:java.lang.Object set(int,java.lang.Object) -> set
    13:13:java.lang.Object get(int) -> get
    26:26:void <clinit>() -> <clinit>
 * 
 * */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBT {
	
	public static enum NBTTagType {
	    TAG_End(0),
	    TAG_Byte(1),
	    TAG_Short(2),
	    TAG_Int(3),
	    TAG_Long(4),
	    TAG_Float(5),
	    TAG_Double(6),
	    TAG_Byte_Array(7),
	    TAG_String(8),
	    TAG_List(9),
	    TAG_Compound(10),
	    TAG_Int_Array(11),
	    TAG_Long_Array(12);

	    private final int id;

	    NBTTagType(int id) {
	        this.id = id;
	    }

	    public int getId() {
	        return id;
	    }

	    public static NBTTagType fromId(int id) {
	        for (NBTTagType type : values()) {
	            if (type.id == id) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Unknown NBT tag type ID: " + id);
	    }
	}
	
	public static NBTTagCompound createNew() {
		
		return new NBTTagCompound();
		
	}
	
	public static NBTTagCompound getNBT(ItemStack item) {
		net.minecraft.world.item.ItemStack im = CraftItemStack.asNMSCopy(item);
		
		if (!im.u()) return null; //516:516:boolean hasTag() -> u
		
		NBTTagCompound tag = im.v(); // 521:521:net.minecraft.nbt.CompoundTag getTag() -> v
		
		return tag;
	}
	
	public static NBTTagCompound getCompound(ItemStack item, String key) {
		net.minecraft.world.item.ItemStack im = CraftItemStack.asNMSCopy(item);
		
		if (!im.u()) return null; //516:516:boolean hasTag() -> u
		
		NBTTagCompound tag = im.v(); // 521:521:net.minecraft.nbt.CompoundTag getTag() -> v
		
		return tag.p(key); // 407:413:net.minecraft.nbt.CompoundTag getCompound(java.lang.String) -> p
		
	}
	
	public static NBTTagCompound getCompound(NBTTagCompound nbt, String key) {
		
		return nbt.p(key);
	}

	public static String getString(ItemStack item, String string) {
		
		NBTTagCompound tag = getNBT(item);
		return tag.l(string);
		

	}
	
	public static NBTTagList getNBTArray(NBTTagCompound n, String key, NBTTagType type) {
		return n.c(key, type.id);
	}

	public static void setString(NBTTagCompound n, String string, String string2) {
		n.a(string, string2);
	}

	public static ItemStack setCompound(ItemStack item, NBTTagCompound n) {
		
		net.minecraft.world.item.ItemStack im = CraftItemStack.asNMSCopy(item);
		
		im.c(n);
		
		return CraftItemStack.asBukkitCopy(im);
		
	}

	public static NBTTagCompound getCompound(NBTTagList list, int i) {
		return list.a(i);
	}

	public static byte getByte(NBTTagCompound tag, String string) {
		return tag.f(string);
	}

	public static String getString(NBTTagCompound tag, String string) {
		return tag.l(string);
	}

	public static void setInteger(NBTTagCompound tag2, String string, int c) {
		
		tag2.a(string, c);
		
	}

	public static Set<String> getKeys(NBTTagCompound tag2) {
		return tag2.e();
	}

	public static Integer getInteger(NBTTagCompound a, String string) {
		return a.h(string);
	}
	
	public static void addItem(NBTTagList list, NBTTagCompound compound) {
		list.add(compound);
	}

	public static void setCompound(NBTTagCompound tag2, String string, NBTTagCompound tag) {
		tag2.a(string, tag);
		
	}

	public static void setCompound(NBTTagCompound tag2, String string, NBTTagList list) {
		tag2.a(string, list);
		
	}
	
	public static boolean getBoolean(NBTTagCompound tag, String key) {
		return tag.q(key);
	}
	
	public static void setBoolean(NBTTagCompound tag, String key, boolean value) {
		tag.a(key, value);
	}

	public static NBTTagCompound getCompound(Entity entity) {
		net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		
		NBTTagCompound cur = new NBTTagCompound();
		nmsEntity.f(cur);
		
		return cur;
	}
	
	public static void setCompound(Entity entity, NBTTagCompound compound) {
		
		net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		nmsEntity.g(compound);
		
		
		
	}


}
