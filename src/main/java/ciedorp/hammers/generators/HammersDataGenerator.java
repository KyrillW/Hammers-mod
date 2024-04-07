package ciedorp.hammers.generators;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import ciedorp.hammers.Hammers;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.BlockTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class HammersDataGenerator implements DataGeneratorEntrypoint {
    
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
 
        // Adding a provider example:
        // 
        pack.addProvider(BlockTagGenerator::new);
        pack.addProvider(HammersEnglishLangProvider::new);
    }

    private static class BlockTagGenerator extends BlockTagProvider {

        public BlockTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        private static final TagKey<Block> HAMMER_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("hammers:hammer_mineable"));

        @Override
        protected void configure(WrapperLookup arg) {
            getOrCreateTagBuilder(HAMMER_MINEABLE)
                            .forceAddTag(BlockTags.PICKAXE_MINEABLE)
                            .forceAddTag(BlockTags.SHOVEL_MINEABLE);
        }     
    }

    private static class HammersEnglishLangProvider extends FabricLanguageProvider {

        protected HammersEnglishLangProvider(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            translationBuilder.add(Hammers.DIAMOND_HAMMER, "Diamond Hammer");

            // Load an existing language file.
            try {
                Path existingFilePath = dataOutput.getModContainer().findPath("assets/hammers/lang/en_us.json").get();
                translationBuilder.add(existingFilePath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to add existing language file!", e);
            }
        }
    
        
    }
}
