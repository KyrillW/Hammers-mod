package ciedorp.hammers.tags;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModBlockTags {
    public static final TagKey<Block> HAMMER_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("hammers", "hammer_mineable"));
}
