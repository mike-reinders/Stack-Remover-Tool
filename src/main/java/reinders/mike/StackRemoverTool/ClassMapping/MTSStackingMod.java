package reinders.mike.StackRemoverTool.ClassMapping;

import java.util.HashMap;
import java.util.Map;

public class MTSStackingMod implements MappingInterface {

    private static Map<String, String> classMapping = new HashMap<>();

    static {
        classMapping.put("PrimalItemAmmo_AdvancedBullet_C", "PrimalItemAmmo_AdvancedBullet_Child_C");
        classMapping.put("PrimalItemAmmo_AdvancedSniperBullet_C", "PrimalItemAmmo_AdvancedSniperBullet_Child_C");
        classMapping.put("PrimalItemAmmo_AggroTranqDart_C", "PrimalItemAmmo_AggroTranqDart_Child_C");
        classMapping.put("PrimalItemAmmo_ArrowFlame_C", "PrimalItemAmmo_ArrowFlame_Child_C");
        classMapping.put("PrimalItemAmmo_ArrowStone_C", "PrimalItemAmmo_ArrowStone_Child_C");
        classMapping.put("PrimalItemAmmo_ArrowTranq_C", "PrimalItemAmmo_ArrowTranq_Child_C");
        classMapping.put("PrimalItemAmmo_BallistaArrow_C", "PrimalItemAmmo_BallistaArrow_Child_C");
        classMapping.put("PrimalItemAmmo_CompoundBowArrow_C", "PrimalItemAmmo_CompoundBowArrow_Child_C");
        classMapping.put("PrimalItemAmmo_Flamethrower_C", "PrimalItemAmmo_Flamethrower_Child_C");
        classMapping.put("PrimalItemAmmo_GrapplingHook_C", "PrimalItemAmmo_GrapplingHook_Child_C");
        classMapping.put("PrimalItemAmmo_RefinedTranqDart_C", "PrimalItemAmmo_RefinedTranqDart_Child_C");
        classMapping.put("PrimalItemAmmo_RocketHomingMissile_C", "PrimalItemAmmo_RocketHomingMissile_Child_C");
        classMapping.put("PrimalItemAmmo_Rocket_C", "PrimalItemAmmo_Rocket_Child_C");
        classMapping.put("PrimalItemAmmo_SimpleBullet_C", "PrimalItemAmmo_SimpleBullet_Child_C");
        classMapping.put("PrimalItemAmmo_SimpleRifleBullet_C", "PrimalItemAmmo_SimpleRifleBullet_Child_C");
        classMapping.put("PrimalItemAmmo_SimpleShotgunBullet_C", "PrimalItemAmmo_SimpleShotgunBullet_Child_C");
        classMapping.put("PrimalItemAmmo_TranqDart_C", "PrimalItemAmmo_TranqDart_Child_C");
        classMapping.put("PrimalItemAmmo_TranqSpearBolt_C", "PrimalItemAmmo_TranqSpearBolt_Child_C");
        classMapping.put("PrimalItemC4Ammo_C", "PrimalItemC4Ammo_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Amarberry_C", "PrimalItemConsumable_Berry_Amarberry_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Azulberry_C", "PrimalItemConsumable_Berry_Azulberry_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Mejoberry_C", "PrimalItemConsumable_Berry_Mejoberry_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Narcoberry_C", "PrimalItemConsumable_Berry_Narcoberry_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Stimberry_C", "PrimalItemConsumable_Berry_Stimberry_Child_C");
        classMapping.put("PrimalItemConsumable_Berry_Tintoberry_C", "PrimalItemConsumable_Berry_Tintoberry_Child_C");
        classMapping.put("PrimalItemConsumable_BugRepellant_C", "PrimalItemConsumable_BugRepellant_Child_C");
        classMapping.put("PrimalItemConsumable_CactusSap_C", "PrimalItemConsumable_CactusSap_Child_C");
        classMapping.put("PrimalItemConsumable_CookedLambChop_C", "PrimalItemConsumable_CookedLambChop_Child_C");
        classMapping.put("PrimalItemConsumable_CookedMeat_C", "PrimalItemConsumable_CookedMeat_Child_C");
        classMapping.put("PrimalItemConsumable_CookedMeat_Fish_C", "PrimalItemConsumable_CookedMeat_Fish_Child_C");
        classMapping.put("PrimalItemConsumable_CookedMeat_Jerky_C", "PrimalItemConsumable_CookedMeat_Jerky_Child_C");
        classMapping.put("PrimalItemConsumable_CookedPrimeMeat_C", "PrimalItemConsumable_CookedPrimeMeat_Child_C");
        classMapping.put("PrimalItemConsumable_CookedPrimeMeat_Fish_C", "PrimalItemConsumable_CookedPrimeMeat_Fish_Child_C");
        classMapping.put("PrimalItemConsumable_CookedPrimeMeat_Jerky_C", "PrimalItemConsumable_CookedPrimeMeat_Jerky_Child_C");
        classMapping.put("PrimalItemConsumable_Honey_C", "PrimalItemConsumable_Honey_Child_C");
        classMapping.put("PrimalItemConsumable_JellyVenom_C", "PrimalItemConsumable_JellyVenom_Child_C");
        classMapping.put("PrimalItemConsumable_Mushroom_Aquatic_C", "PrimalItemConsumable_Mushroom_Aquatic_Child_C");
        classMapping.put("PrimalItemConsumable_Mushroom_Ascerbic_C", "PrimalItemConsumable_Mushroom_Ascerbic_Child_C");
        classMapping.put("PrimalItemConsumable_Mushroom_Auric_C", "PrimalItemConsumable_Mushroom_Auric_Child_C");
        classMapping.put("PrimalItemConsumable_NamelessVenom_C", "PrimalItemConsumable_NamelessVenom_Child_C");
        classMapping.put("PrimalItemConsumable_Narcotic_C", "PrimalItemConsumable_Narcotic_Child_C");
        classMapping.put("PrimalItemConsumable_RawMeat_C", "PrimalItemConsumable_RawMeat_Child_C");
        classMapping.put("PrimalItemConsumable_RawMeat_Fish_C", "PrimalItemConsumable_RawMeat_Fish_Child_C");
        classMapping.put("PrimalItemConsumable_RawMutton_C", "PrimalItemConsumable_RawMutton_Child_C");
        classMapping.put("PrimalItemConsumable_RawPrimeMeat_C", "PrimalItemConsumable_RawPrimeMeat_Child_C");
        classMapping.put("PrimalItemConsumable_RawPrimeMeat_Fish_C", "PrimalItemConsumable_RawPrimeMeat_Fish_Child_C");
        classMapping.put("PrimalItemConsumable_SpoiledMeat_C", "PrimalItemConsumable_SpoiledMeat_Child_C");
        classMapping.put("PrimalItemConsumable_Stimulant_C", "PrimalItemConsumable_Stimulant_Child_C");
        classMapping.put("PrimalItemConsumable_Veggie_Citronal_C", "PrimalItemConsumable_Veggie_Citronal_Child_C");
        classMapping.put("PrimalItemConsumable_Veggie_Longrass_C", "PrimalItemConsumable_Veggie_Longrass_Child_C");
        classMapping.put("PrimalItemConsumable_Veggie_Rockarrot_C", "PrimalItemConsumable_Veggie_Rockarrot_Child_C");
        classMapping.put("PrimalItemConsumable_Veggie_Savoroot_C", "PrimalItemConsumable_Veggie_Savoroot_Child_C");
        classMapping.put("PrimalItemConsumable_WyvernMilk_C", "PrimalItemConsumable_WyvernMilk_Child_C");
        classMapping.put("PrimalItemResource_CommonMushroom_C", "PrimalItemResource_CommonMushroom_Child_C");
        classMapping.put("PrimalItemResource_Ambergris_C", "PrimalItemResource_Ambergris_Child_C");
        classMapping.put("PrimalItemResource_AmmoniteBlood_C", "PrimalItemResource_AmmoniteBlood_Child_C");
        classMapping.put("PrimalItemResource_AnglerGel_C", "PrimalItemResource_AnglerGel_Child_C");
        classMapping.put("PrimalItemResource_BlackPearl_C", "PrimalItemResource_BlackPearl_Child_C");
        classMapping.put("PrimalItemResource_BlueSap_C", "PrimalItemResource_BlueSap_Child_C");
        classMapping.put("PrimalItemResource_Charcoal_C", "PrimalItemResource_Charcoal_Child_C");
        classMapping.put("PrimalItemResource_ChitinOrKeratin_C", "PrimalItemResource_ChitinOrKeratin_Child_C");
        classMapping.put("PrimalItemResource_ChitinPaste_C", "PrimalItemResource_ChitinPaste_Child_C");
        classMapping.put("PrimalItemResource_Chitin_C", "PrimalItemResource_Chitin_Child_C");
        classMapping.put("PrimalItemResource_Clay_C", "PrimalItemResource_Clay_Child_C");
        classMapping.put("PrimalItemResource_CondensedGas_C", "PrimalItemResource_CondensedGas_Child_C");
        classMapping.put("PrimalItemResource_CorruptedPolymer_C", "PrimalItemResource_CorruptedPolymer_Child_C");
        classMapping.put("PrimalItemResource_CorruptedWood_C", "PrimalItemResource_CorruptedWood_Child_C");
        classMapping.put("PrimalItemResource_Crystal_C", "PrimalItemResource_Crystal_Child_C");
        classMapping.put("PrimalItemResource_Crystal_IslesPrimal_C", "PrimalItemResource_Crystal_IslesPrimal_Child_C");
        classMapping.put("PrimalItemResource_Electronics_C", "PrimalItemResource_Electronics_Child_C");
        classMapping.put("PrimalItemResource_ElementOre_C", "PrimalItemResource_ElementOre_Child_C");
        classMapping.put("PrimalItemResource_Element_C", "PrimalItemResource_Element_Child_C");
        classMapping.put("PrimalItemResource_Fibers_C", "PrimalItemResource_Fibers_Child_C");
        classMapping.put("PrimalItemResource_Flint_C", "PrimalItemResource_Flint_Child_C");
        classMapping.put("PrimalItemResource_FracturedGem_C", "PrimalItemResource_FracturedGem_Child_C");
        classMapping.put("PrimalItemResource_FungalWood_C", "PrimalItemResource_FungalWood_Child_C");
        classMapping.put("PrimalItemResource_Gasoline_C", "PrimalItemResource_Gasoline_Child_C");
        classMapping.put("PrimalItemResource_GasRefined_C", "PrimalItemResource_GasRefined_Child_C");
        classMapping.put("PrimalItemResource_Gas_C", "PrimalItemResource_Gas_Child_C");
        classMapping.put("PrimalItemResource_Gem_BioLum_C", "PrimalItemResource_Gem_BioLum_Child_C");
        classMapping.put("PrimalItemResource_Gem_Element_C", "PrimalItemResource_Gem_Element_Child_C");
        classMapping.put("PrimalItemResource_Gem_Fertile_C", "PrimalItemResource_Gem_Fertile_Child_C");
        classMapping.put("PrimalItemResource_Gunpowder_C", "PrimalItemResource_Gunpowder_Child_C");
        classMapping.put("PrimalItemResource_Hair_C", "PrimalItemResource_Hair_Child_C");
        classMapping.put("PrimalItemResource_Hide_C", "PrimalItemResource_Hide_Child_C");
        classMapping.put("PrimalItemResource_Horn_C", "PrimalItemResource_Horn_Child_C");
        classMapping.put("PrimalItemResource_Keratin_C", "PrimalItemResource_Keratin_Child_C");
        classMapping.put("PrimalItemResource_LeechBlood_C", "PrimalItemResource_LeechBlood_Child_C");
        classMapping.put("PrimalItemResource_MetalIngot_C", "PrimalItemResource_MetalIngot_Child_C");
        classMapping.put("PrimalItemResource_Metal_C", "PrimalItemResource_Metal_Child_C");
        classMapping.put("PrimalItemResource_Obsidian_C", "PrimalItemResource_Obsidian_Child_C");
        classMapping.put("PrimalItemResource_Oil_C", "PrimalItemResource_Oil_Child_C");
        classMapping.put("PrimalItemResource_Pelt_C", "PrimalItemResource_Pelt_Child_C");
        classMapping.put("PrimalItemResource_Polymer_C", "PrimalItemResource_Polymer_Child_C");
        classMapping.put("PrimalItemResource_Polymer_Organic_C", "PrimalItemResource_Polymer_Organic_Child_C");
        classMapping.put("PrimalItemResource_PreservingSalt_C", "PrimalItemResource_PreservingSalt_Child_C");
        classMapping.put("PrimalItemResource_Propellant_C", "PrimalItemResource_Propellant_Child_C");
        classMapping.put("PrimalItemResource_RareFlower_C", "PrimalItemResource_RareFlower_Child_C");
        classMapping.put("PrimalItemResource_RareMushroom_C", "PrimalItemResource_RareMushroom_Child_C");
        classMapping.put("PrimalItemResource_RawSalt_C", "PrimalItemResource_RawSalt_Child_C");
        classMapping.put("PrimalItemResource_RedSap_C", "PrimalItemResource_RedSap_Child_C");
        classMapping.put("PrimalItemResource_Sand_C", "PrimalItemResource_Sand_Child_C");
        classMapping.put("PrimalItemResource_Sap_C", "PrimalItemResource_Sap_Child_C");
        classMapping.put("PrimalItemResource_ScrapMetalIngot_C", "PrimalItemResource_ScrapMetalIngot_Child_C");
        classMapping.put("PrimalItemResource_ScrapMetal_C", "PrimalItemResource_ScrapMetal_Child_C");
        classMapping.put("PrimalItemResource_Silicate_C", "PrimalItemResource_Silicate_Child_C");
        classMapping.put("PrimalItemResource_Silicon_C", "PrimalItemResource_Silicon_Child_C");
        classMapping.put("PrimalItemResource_Silk_C", "PrimalItemResource_Silk_Child_C");
        classMapping.put("PrimalItemResource_SnailPaste_C", "PrimalItemResource_SnailPaste_Child_C");
        classMapping.put("PrimalItemResource_Sparkpowder_C", "PrimalItemResource_Sparkpowder_Child_C");
        classMapping.put("PrimalItemResource_SquidOil_C", "PrimalItemResource_SquidOil_Child_C");
        classMapping.put("PrimalItemResource_Stone_C", "PrimalItemResource_Stone_Child_C");
        classMapping.put("PrimalItemResource_SubstrateAbsorbent_C", "PrimalItemResource_SubstrateAbsorbent_Child_C");
        classMapping.put("PrimalItemResource_Sulfur_C", "PrimalItemResource_Sulfur_Child_C");
        classMapping.put("PrimalItemResource_Thatch_C", "PrimalItemResource_Thatch_Child_C");
        classMapping.put("PrimalItemResource_TurtleShell_C", "PrimalItemResource_TurtleShell_Child_C");
        classMapping.put("PrimalItemResource_Wood_C", "PrimalItemResource_Wood_Child_C");
        classMapping.put("PrimalItemResource_Wool_C", "PrimalItemResource_Wool_Child_C");
        classMapping.put("PrimalItemResource_XenomorphPheromoneGland_C", "PrimalItemResource_XenomorphPheromoneGland_Child_C");
    }

    public String getName() {
        return "MTSStackingMod";
    }

    public Map<String, String> getMapping() {
        return MTSStackingMod.classMapping;
    }

}
