package net.itsrelizc.smp.corps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.api.Business;
import net.itsrelizc.smp.corps.menus.MenuContractSign;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Contract {
	
	private static JSONObject stuff = JSON.loadDataFromDataBase("contract_tracker.json");
	public static long contractSigned = -1;
	
	public static void loadContractSigned() {
		contractSigned = (long) stuff.get("signed");
	}
	
	public static void saveContractSigned() {
		stuff.put("signed", contractSigned);
		JSON.saveDataFromDataBase("contract_tracker.json", stuff);
	}
	
	public interface ContractContent {
		
		public NBTTagCompound convertToNBT();
		public JSONObject convertToJSON();
		
	}
	
	public static class Agreement implements ContractContent {
		private String party;
		private String content;

		public Agreement(String string, String content) {
			this.party = string;
			this.content = content;
		}
		
		public NBTTagCompound convertToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			NBT.setString(tag, "party", this.party);
			NBT.setString(tag, "value", this.content);
			
			return tag;
		}
		
		public JSONObject convertToJSON() {
			JSONObject tag = new JSONObject();
			tag.put("party", this.party);
			tag.put("value", this.content);
			
			return tag;
		}

		public static String parseParties(String string, Player player, int partyCount, Map<String, CorporateBusiness> currentbusinesses,
				Map<String, Integer> order) {
			
			String result = new String(string);
			
			int max_occurence = -1;
			
			for (Entry<String, Integer> entry : order.entrySet()) {
				CorporateBusiness current = currentbusinesses.get(entry.getKey());
				if (current == null) {
//					System.out.println("$" + entry.getValue());
//					System.out.println(Locale.get(player, "contract.party" + entry.getValue()) + " (§a" + entry.getKey() + "§7) ");
					result = result.replace("$" + entry.getValue(), Locale.get(player, "contract.party" + entry.getValue()) + " (§a" + entry.getKey() + "§7) ");
				} else {
//					System.out.println("$" + entry.getValue());
//					System.out.println(Locale.get(player, "contract.party" + entry.getValue()) + " (§a" + currentbusinesses.get(entry.getKey()).getDisplayName(player) + "§7) ");
					result = result.replace("$" + entry.getValue(), Locale.get(player, "contract.party" + entry.getValue()) + " (§a" + currentbusinesses.get(entry.getKey()).getName(player) + "§7) ");
				}
				
				max_occurence = Math.max(max_occurence, entry.getValue());
				
			}

			for (int i = max_occurence; i <= partyCount; i ++) {
				result = result.replace("$" + i, Locale.get(player, "contract.party" + i) + " (§8" + Locale.get(player, "contract.party.undefined") + "§7) ");
			}
			
			return result;
			
		}
		
		
	}
	
	
	
	public static class Expire implements ContractContent {
		
		public static enum ExpireType {
			
			TIME((int) 0x00),
			EITHER_BREACH((int) 0x01);
			
			public int type;

			private ExpireType(int type) {
				this.type = type;
			}
			
		}

		public static final String EXPIRE_NEVER = "permanent";
		
		private ExpireType type;
		private String value;

		public Expire(ExpireType type, String value) {
			this.type = type;
			this.value = value;
		}
		
		public NBTTagCompound convertToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			NBT.setInteger(tag, "type", this.type.type);
			NBT.setString(tag, "value", this.value);
			
			return tag;
		}
		
		public JSONObject convertToJSON() {
			JSONObject tag = new JSONObject();
			tag.put("type", this.type.type);
			tag.put("value", this.value);
			
			return tag;
		}
		
	}
	
	public static class Party implements ContractContent {
		
		public static enum PartyType {
			
			INDIVIDUAL((int) 0x00, "business.individual"),
			CORPORATION((int) 0x01, "business.corporation"),
			GOVERNMENT((int) 0x02, "business.government");
			
			public int type;
			private String namespace;
			private boolean sealed;

			private PartyType(int type, String namespace) {
				this.type = type;
				this.namespace = namespace;
			}
			
			public String getNamespace() {
				return this.namespace;
			}
			
			
		}
		
		private PartyType type;
		private Business business;
		private boolean sealed;

		
		public Party(PartyType type, Business value, boolean sealed) {
			this.type = type;
			this.business = value;
			this.sealed = sealed;
		}
		
		public NBTTagCompound convertToNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			NBT.setInteger(tag, "type", this.type.type);
			NBT.setString(tag, "party", this.business.getRegistrationName());
			NBT.setBoolean(tag, "sealed", this.sealed);
			
			return tag;
		}
		
		public static String getLocalizedSealedString(boolean sealed, Player player, int type) {
			
			if (!sealed) return "(§8" + Locale.get(player, "contract.sealed.null") + "§7)";
			
			if (type == 0) {
				return "(§a🖐 " + Locale.get(player, "contract.sealed.individual") + "§7)";
			} else {
				return "(§a🔨 " + Locale.get(player, "contract.sealed.multiple") + "§7)";
			}
		}
		
		public Business getBusiness() {
			return this.business;
		}
		
		public JSONObject convertToJSON() {
			JSONObject tag = new JSONObject();
			tag.put("type", this.type.type);
			tag.put("party", this.business.getRegistrationName());
			tag.put("sealed", this.sealed);
			
			return tag;
		}

		public static Party getIndividual(Player player, boolean sealed) {
			return new Party(PartyType.INDIVIDUAL, CorporateBusiness.getIndividual(player), sealed);
		}
		
	}
	
	public static Contract create(int partyAmount, Party mainIssuer, Party[] parties2, Agreement[] agreements2, Expire[] expires2) {
		
		return new Contract(partyAmount, mainIssuer, parties2, agreements2, expires2);
		
	}

	private ItemStack item;
	private UUID id;
	private String id_prefix;
	private Agreement[] agreements;
	private Expire[] expires;
	private Party[] parties;
	private long issued;
	private int party_amount;
	
	public Contract(int partyAmount, Party mainIssuer, Party[] parties2, Agreement[] agreements2, Expire[] expires2) {
		
		this.id = UUID.randomUUID();
		this.generate(mainIssuer, expires2, agreements2, parties2, partyAmount);
		
		
		
		
		
		this.id_prefix = this.id.toString().substring(0, 2);
		
		this.agreements = agreements2;
		this.expires = expires2;
		this.parties = parties2;
		
		this.party_amount = partyAmount;
		
		try {
			this.save();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void generateLore(Player player) {
		ContractListener.generate(item, player);
	}
	
	public void generate(Party mainIssuer, Expire[] expires2, Agreement[] agreements2, Party[] parties2, int partyAmount) {
		
		ItemStack contract = ItemGenerator.generate(Material.ENCHANTED_BOOK, 1);
//		contract = Locale.insertSmartLocale(contract, "item.contract.name", "item.contract.signed_date", "item.contract.signed_parties");
		
		this.issued = System.currentTimeMillis();
		
		NBTTagCompound tag = NBT.getNBT(contract);
		if (tag == null) tag = new NBTTagCompound();
		
		NBT.setLong(tag, "issued", issued);
		NBT.setString(tag, "uuid", this.id.toString());
		NBT.setLong(tag, "number", Contract.contractSigned);
		
		Contract.contractSigned ++;
		
		NBT.setString(tag, "creator", mainIssuer.getBusiness().getRegistrationName());
		
		NBTTagList list = new NBTTagList();
		for (Expire e : expires2) {
			NBT.addItem(list, e.convertToNBT());
		}
		
		NBT.setCompound(tag, "expire", list);
		
		NBTTagList p = new NBTTagList();
		for (Agreement e : agreements2) {
			NBT.addItem(p, e.convertToNBT());
		}
		
		NBT.setCompound(tag, "agreement", p);
		
		NBT.setInteger(tag, "partyAmount", partyAmount);
		
		NBTTagList party = new NBTTagList();
		for (Party e : parties2) {
			NBT.addItem(party, e.convertToNBT());
		}
		
		NBT.setCompound(tag, "party", party);
		
		this.item = NBT.setCompound(contract, tag);
	}
	
	public ItemStack getItem() {return this.item;}
	
	@SuppressWarnings("unchecked")
	public void save() throws IOException {
		Path path = Paths.get(JSON.PREFIX + "contract/" + id_prefix + "/" + id.toString().replace("-", "") + ".json");
		
		if (Files.exists(path) && Files.isRegularFile(path)) {
			throw new IOException("Contract ID already exists");
		} else {
			Files.createFile(path);
			
			JSONObject contractData = new JSONObject();
			
			contractData.put("issued", issued);
			
			JSONArray expire = new JSONArray();
			for (Expire e : expires) {
				expire.add(e.convertToJSON());
			}
			contractData.put("expire", expire);
			
			JSONArray agree = new JSONArray();
			for (Agreement a : agreements) {
				agree.add(a.convertToJSON());
			}
			contractData.put("agreement", agree);
			
			JSONArray party = new JSONArray();
			for (Party p : parties) {
				party.add(p.convertToJSON());
			}
			contractData.put("party", party);
			
			contractData.put("partyAmount", party_amount);
			
			JSON.saveDataFromDataBase("contract/" + id_prefix + "/" + id.toString().replace("-", "") + ".json", contractData);
			
			System.out.println("Saved contract: " + this.id);
		}
	}

	public boolean signMenu(Player p) {
		
		Menu2 menu = new Menu2(p, 5, new MenuContractSign(Locale.get(p, "menu.contract.title"), this.getItem()));
		menu.open();
		
		return false;
		
	}

	public void signName(Player player) {
		
		ItemStack item;
		if (this.getItem() == null) item = player.getItemInHand();
		else item = this.getItem();
		
		NBTTagCompound tag = NBT.getNBT(item);
		
		
	}

}
