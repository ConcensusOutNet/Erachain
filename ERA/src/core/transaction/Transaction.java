package core.transaction;

// import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.math.BigInteger;
//import java.math.RoundingMode;
//import java.math.MathContext;
import java.util.Arrays;
//import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
//import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

//import javax.swing.JFrame;
//import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.mapdb.Fun.Tuple2;
import org.mapdb.Fun.Tuple3;
import org.mapdb.Fun.Tuple4;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import controller.Controller;
import core.BlockChain;
import core.account.Account;
import core.account.PrivateKeyAccount;
import core.account.PublicKeyAccount;
import core.block.Block;
import core.crypto.Base58;
import core.crypto.Crypto;
import core.item.ItemCls;
import core.item.assets.AssetCls;
import core.item.assets.Order;
import database.AddressTime_SignatureMap;
import database.DBSet;
import settings.Settings;
//import lang.Lang;
//import settings.Settings;
import utils.DateTimeFormat;

public abstract class Transaction {
	
	protected static final int FREEZE_FROM = BlockChain.DEVELOP_USE?9999999:41800;
	protected static final String[] TRUE_ADDRESSES = new String[]{
			"78JFPWVVAVP3WW7S8HPgSkt24QF2vsGiS5",
			"7S8qgSTdzDiBmyw7j3xgvXbVWdKSJVFyZv",
			"7R2WUFaS7DF2As6NKz13Pgn9ij4sFw6ymZ",
			"7H5GTyxv4h5GKeDUiSPBMKAgpFb8EKTbvj",
		};

	//VALIDATION CODE
	public static final int VALIDATE_OK = 1;
	public static final int INVALID_MAKER_ADDRESS = 5;
	public static final int INVALID_REFERENCE = 6;
	public static final int INVALID_TIMESTAMP = 7;
	public static final int INVALID_ADDRESS = 8;
	public static final int INVALID_FEE_POWER = 9;
	public static final int NOT_ENOUGH_FEE = 10;
	public static final int NO_BALANCE = 11;
	public static final int INVALID_PUBLIC_KEY = 12;
	public static final int INVALID_RAW_DATA = 13;
	public static final int INVALID_DATE = 14;
	public static final int INVALID_CREATOR = 15; // for some reasons that creator is invalid (same as trade order)
	public static final int INVALID_SIGNATURE = 16;
	public static final int NO_DEBT_BALANCE = 17;
	public static final int NO_HOLD_BALANCE = 18;
	
	public static final int NOT_ENOUGH_RIGHTS = 20;
	public static final int OWNER_NOT_PERSONALIZED = 21;
	public static final int ACCOUNT_ALREADY_PERSONALIZED = 23;
	public static final int TRANSACTION_DOES_NOT_EXIST = 24;
	public static final int CREATOR_NOT_PERSONALIZED = 25;
	public static final int RECEIVER_NOT_PERSONALIZED = 26;

	// ASSETS
	public static final int INVALID_QUANTITY = 30;
	//public static final int ASSET_DOES_NOT_EXIST = 31;
	public static final int NEGATIVE_AMOUNT = 32;
	public static final int INVALID_AMOUNT = 33;
	public static final int INVALID_RETURN = 34;
	public static final int HAVE_EQUALS_WANT = 35;
	public static final int ORDER_DOES_NOT_EXIST = 36;
	public static final int INVALID_ORDER_CREATOR = 37;
	public static final int INVALID_PAYMENTS_LENGTH = 38;
	public static final int NEGATIVE_PRICE = 39;
	public static final int INVALID_PRICE = 40;
	public static final int INVALID_CREATION_BYTES = 41;
	public static final int INVALID_TAGS_LENGTH = 42;
	public static final int INVALID_TYPE_LENGTH = 43;
	public static final int NOT_MOVABLE_ASSET = 44;
	public static final int NOT_DEBT_ASSET = 45;

	public static final int INVALID_NAME_LENGTH = 50;
	public static final int INVALID_ICON_LENGTH = 51;
	public static final int INVALID_IMAGE_LENGTH = 52;
	public static final int INVALID_DESCRIPTION_LENGTH = 53;

	public static final int INVALID_VALUE_LENGTH = 55;

	// NAMES
	public static final int NAME_DOES_NOT_EXIST = 60;
	public static final int NAME_ALREADY_REGISTRED = 61;
	public static final int NAME_ALREADY_ON_SALE = 62;
	public static final int NAME_NOT_FOR_SALE = 63;
	public static final int BUYER_ALREADY_OWNER = 64;	
	public static final int NAME_NOT_LOWER_CASE = 65;
	public static final int NAME_WITH_SPACE = 66;
	public static final int CREATOR_NOT_OWNER = 67;
	public static final int NAME_KEY_ALREADY_EXISTS = 68;
	public static final int NAME_KEY_NOT_EXISTS = 69;
	public static final int LAST_KEY_IS_DEFAULT_KEY = 70;


	// POLL
	public static final int INVALID_OPTIONS_LENGTH = 80;
	public static final int INVALID_OPTION_LENGTH = 81;
	public static final int DUPLICATE_OPTION = 82;
	public static final int POLL_ALREADY_CREATED = 83;
	public static final int POLL_ALREADY_HAS_VOTES = 84;
	public static final int POLL_NOT_EXISTS = 85;
	public static final int POLL_OPTION_NOT_EXISTS = 86;
	public static final int ALREADY_VOTED_FOR_THAT_OPTION = 87;

	public static final int INVALID_DATA_LENGTH = 88;
	public static final int INVALID_DATA = 89;
	public static final int INVALID_PARAMS_LENGTH = 90;
	public static final int INVALID_URL_LENGTH = 91;
	public static final int INVALID_HEAD_LENGTH = 92;

	// ITEMS
	public static final int INVALID_ITEM_VALUE = 100;
	public static final int ITEM_DOES_NOT_EXIST = 101;
	public static final int ITEM_ASSET_NOT_EXIST = 102;
	public static final int ITEM_IMPRINT_DOES_NOT_EXIST = 103;
	public static final int ITEM_NOTE_NOT_EXIST = 104;
	public static final int ITEM_PERSON_NOT_EXIST = 105;
	public static final int ITEM_STATUS_NOT_EXIST = 106;
	public static final int ITEM_UNION_NOT_EXIST = 107;
	public static final int ITEM_DOES_NOT_STATUSED = 108;
	public static final int ITEM_DOES_NOT_UNITED = 109;
	public static final int ITEM_DUPLICATE_KEY = 110;
	public static final int ITEM_DUPLICATE = 111;

	public static final int AMOUNT_DIVISIBLE = 115;

	public static final int ITEM_PERSON_LATITUDE_ERROR = 120;
	public static final int ITEM_PERSON_LONGITUDE_ERROR = 121;
	public static final int ITEM_PERSON_RACE_ERROR = 122;
	public static final int ITEM_PERSON_GENDER_ERROR = 123;
	public static final int ITEM_PERSON_SKIN_COLOR_ERROR = 124;
	public static final int ITEM_PERSON_EYE_COLOR_ERROR = 125;
	public static final int ITEM_PERSON_HAIR_COLOR_ERROR = 126;
	public static final int ITEM_PERSON_HEIGHT_ERROR = 127;
	public static final int ITEM_PERSON_OWNER_SIGNATURE_INVALID = 128;

	public static final int INVALID_UPDATE_VALUE = 140;

	public static final int INVALID_TRANSACTION_TYPE = 150;
	
	public static final int INVALID_BLOCK_HEIGHT = 200;
	public static final int INVALID_BLOCK_TRANS_SEQ_ERROR = 201;

	public static final int NOT_YET_RELEASED = 299;
	public static final int AT_ERROR = 300;
	
	//TYPES *******
	// universal
	public static final int EXTENDED = 0;
	// genesis
	public static final int GENESIS_ISSUE_ASSET_TRANSACTION = 1;
	public static final int GENESIS_ISSUE_NOTE_TRANSACTION = 2;
	public static final int GENESIS_ISSUE_PERSON_TRANSACTION = 3;
	public static final int GENESIS_ISSUE_STATUS_TRANSACTION = 4;
	public static final int GENESIS_ISSUE_UNION_TRANSACTION = 5; //
	public static final int GENESIS_SEND_ASSET_TRANSACTION = 6;
	public static final int GENESIS_SIGN_NOTE_TRANSACTION = 7; //
	public static final int GENESIS_CERTIFY_PERSON_TRANSACTION = 8; // нет в гуи
	public static final int GENESIS_ASSIGN_STATUS_TRANSACTION = 9;//
	public static final int GENESIS_ADOPT_UNION_TRANSACTION = 10;//
	// ISSUE ITEMS
	public static final int ISSUE_ASSET_TRANSACTION = 21;
	public static final int ISSUE_IMPRINT_TRANSACTION = 22;
	public static final int ISSUE_NOTE_TRANSACTION = 23;
	public static final int ISSUE_PERSON_TRANSACTION = 24;
	public static final int ISSUE_STATUS_TRANSACTION = 25;
	public static final int ISSUE_UNION_TRANSACTION = 26;
	public static final int ISSUE_STATEMENT_TRANSACTION = 27; // not in gui

	// SEND ASSET
	public static final int SEND_ASSET_TRANSACTION = 31;
	// RENT ASSET
	public static final int RENT_ASSET_TRANSACTION = 32; //
	// HOLD ASSET
	public static final int HOLD_ASSET_TRANSACTION = 33; // not in gui
	
	// OTHER
	public static final int SIGN_NOTE2_TRANSACTION = 34;
	public static final int SIGN_NOTE_TRANSACTION = 35;
	public static final int CERTIFY_PUB_KEYS_TRANSACTION = 36;
	public static final int SET_STATUS_TO_ITEM_TRANSACTION = 37;
	public static final int SET_UNION_TO_ITEM_TRANSACTION = 38;
	public static final int SET_UNION_STATUS_TO_ITEM_TRANSACTION = 39; // not in gui
	
	// confirms other transactions
	// NOT EDIT - fkr CONCORCIUM = 40 !!!
	public static final int VOUCH_TRANSACTION = 40;

	public static final int HASHES_RECORD = 41;
	
	// exchange of assets
	public static final int CREATE_ORDER_TRANSACTION = 50;
	public static final int CANCEL_ORDER_TRANSACTION = 51;
	// voting
	public static final int CREATE_POLL_TRANSACTION = 61;
	public static final int VOTE_ON_POLL_TRANSACTION = 62;
	
	public static final int RELEASE_PACK = 70;

	// old
	public static final int REGISTER_NAME_TRANSACTION = 6 + 130;
	public static final int UPDATE_NAME_TRANSACTION = 7 + 130;
	public static final int SELL_NAME_TRANSACTION = 8 + 130;
	public static final int CANCEL_SELL_NAME_TRANSACTION = 9 + 130;
	public static final int BUY_NAME_TRANSACTION = 10 + 130;
	public static final int ARBITRARY_TRANSACTION = 12 + 130;
	public static final int MULTI_PAYMENT_TRANSACTION = 13 + 130;
	public static final int DEPLOY_AT_TRANSACTION = 14 + 130;
	
	//public static final int ACCOUNTING_TRANSACTION = 26;
	//public static final int JSON_TRANSACTION = 27;

	// FEE PARAMETERS
	public static final long RIGHTS_KEY = AssetCls.ERA_KEY;

	// FEE PARAMETERS	public static final int FEE_PER_BYTE = 1;

	public static final long FEE_KEY = AssetCls.FEE_KEY;

	//RELEASES
	//private static final long ASSETS_RELEASE = 0l;
	//private static final long POWFIX_RELEASE = 0L; // Block Version 3 // 2016-02-25T19:00:00+00:00
							
	static Logger LOGGER = Logger.getLogger(Transaction.class.getName());

	/*/
	public static long getVOTING_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return Settings.getInstance().getGenesisStamp();
		}
		return VOTING_RELEASE;
	}

	public static long getARBITRARY_TRANSACTIONS_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return Settings.getInstance().getGenesisStamp();
		}
		return ARBITRARY_TRANSACTIONS_RELEASE;
	}

	public static int getAT_BLOCK_HEIGHT_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return 1;
		}
		return AT_BLOCK_HEIGHT_RELEASE;
	}
	
	public static int getMESSAGE_BLOCK_HEIGHT_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return 1;
		}
		return MESSAGE_BLOCK_HEIGHT_RELEASE;
	}
	
	public static long getASSETS_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return Settings.getInstance().getGenesisStamp();
		}
		return ASSETS_RELEASE;
	}
	
	public static long getPOWFIX_RELEASE() {
		if(Settings.getInstance().isTestnet()) {
			return Settings.getInstance().getGenesisStamp();
		}
		return POWFIX_RELEASE;
	}
	*/
	
	//PROPERTIES LENGTH
	protected static final int SIMPLE_TYPE_LENGTH = 1;
	protected static final int TYPE_LENGTH = 4;
	protected static final int HEIGHT_LENGTH = 4;
	protected static final int DATA_JSON_PART_LENGTH = 4;
	protected static final int DATA_VERSION_PART_LENGTH = 6;
	protected static final int DATA_TITLE_PART_LENGTH = 4;
	protected static final int DATA_NUM_FILE_LENGTH = 4;
	protected static final int SEQ_LENGTH = 4;
	//protected static final int PROP_LENGTH = 2; // properties
	public static final int TIMESTAMP_LENGTH = 8;
	//public static final int REFERENCE_LENGTH = Crypto.SIGNATURE_LENGTH;
	public static final int REFERENCE_LENGTH = TIMESTAMP_LENGTH;
	protected static final int DATA_SIZE_LENGTH = 4;
	protected static final int ENCRYPTED_LENGTH = 1;
	protected static final int IS_TEXT_LENGTH = 1;
	public static final int KEY_LENGTH = 8;
	protected static final int FEE_POWER_LENGTH = 1;
	//protected static final int HKEY_LENGTH = 20;
	protected static final int CREATOR_LENGTH = PublicKeyAccount.PUBLIC_KEY_LENGTH;
	// not need now protected static final int FEE_LENGTH = 8;
	public static final int SIGNATURE_LENGTH = Crypto.SIGNATURE_LENGTH;
	protected static final int BASE_LENGTH = TYPE_LENGTH + FEE_POWER_LENGTH + REFERENCE_LENGTH + TIMESTAMP_LENGTH + CREATOR_LENGTH + SIGNATURE_LENGTH;
	// in pack toByte and Parse - reference not included
	protected static final int BASE_LENGTH_AS_PACK = TYPE_LENGTH + CREATOR_LENGTH + /*REFERENCE_LENGTH*/ + SIGNATURE_LENGTH;

		
	protected String TYPE_NAME = "unknown";
	//protected int type;
	protected byte[] typeBytes;
	protected Block block; // parent block
	// TODO REMOVE REFERENCE - use TIMESTAMP as reference
	protected Long reference = 0l;
	protected BigDecimal fee  = BigDecimal.ZERO.setScale(8); // - for genesis transactions
	//protected BigDecimal fee  = new BigDecimal.valueOf(999000).setScale(8);
	protected byte feePow = 0;
	protected byte[] signature;
	protected long timestamp;
	protected PublicKeyAccount creator;

	// need for genesis
	protected Transaction(byte type, String type_name)
	{
		this.typeBytes = new byte[]{type,0,0,0}; // for GENESIS
		this.TYPE_NAME = type_name;
	}
	protected Transaction(byte[] typeBytes, String type_name, PublicKeyAccount creator, byte feePow, long timestamp, Long reference)
	{
		this.typeBytes = typeBytes;
		this.TYPE_NAME = type_name;
		this.creator = creator;
		//this.props = props;
		this.timestamp = timestamp;
		this.reference = reference;
		if (feePow < 0 ) feePow = 0;
		else if (feePow > BlockChain.FEE_POW_MAX ) feePow = BlockChain.FEE_POW_MAX;
		this.feePow = feePow;
	}
	protected Transaction(byte[] typeBytes, String type_name, PublicKeyAccount creator, byte feePow, long timestamp, Long reference, byte[] signature)
	{
		this(typeBytes, type_name, creator, feePow, timestamp, reference);
		this.signature = signature;
	}

	//GETTERS/SETTERS
	
	
	public int getType()
	{
		return Byte.toUnsignedInt(this.typeBytes[0]);
	}
	public int getVersion()
	{
		return Byte.toUnsignedInt(this.typeBytes[1]);
	}
	public static int getVersion(byte[] typeBytes)
	{
		return Byte.toUnsignedInt(typeBytes[1]);
	}
	public byte[] getTypeBytes()
	{
		return this.typeBytes;
	}
		
	public PublicKeyAccount getCreator() 
	{
		return this.creator;
	}

	public Long getTimestamp()
	{
		return this.timestamp;
	}
	// for test signature only!!!
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public long getDeadline()
	{
		//24HOUR DEADLINE TO INCLUDE TRANSACTION IN BLOCK
		return this.timestamp + (1000*60*60*24);
	}

	/// tyutuy jhg jhg jg j
	/*
	// TIME
	public Long viewTime() {
		return 0L;
	}
	public Long getTime() {
		return this.viewTime();
	}
	public Long viewTime(Account account)
	{
		return 0L;
	}
	public Long getTime(Account account) {
		return this.viewTime(account);
	}
	*/
	public long getKey() {
		return 0l;
	}
	public long getAbsKey()
	{
		long key = this.getKey();
		if (key < 0)
			return -key;
		return key;
	}

	public BigDecimal getAmount() {
		return BigDecimal.ZERO;
	}

	public BigDecimal getAmount(Account account)
	{
		return BigDecimal.ZERO;
	}
	public BigDecimal getAmount(String account)
	{
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getFee(String address)
	{
		if (this.creator != null)
			if (this.creator.getAddress().equals(address))
				return this.fee;
		return BigDecimal.ZERO;
	}
	public BigDecimal getFee(Account account)
	{
		return this.getFee(account.getAddress());
	}

	public BigDecimal getFee()
	{
		return this.fee;
	}
	public long getFeeLong()
	{
		return this.fee.unscaledValue().longValue();
	}
	
	public byte getFeePow()
	{
		return this.feePow;
	}	

	public long getAssetKey()
	{
		return 0l;
	}
	
	public byte[] getSignature()
	{
		return this.signature;
	}
	
	public Long getReference()
	{
		return this.reference;
	}			
	// for isValid - check reference value
	public boolean isReferenced()
	{
		return true;
	}			

	public abstract boolean hasPublicText();

	
	public int calcCommonFee()
	{		
		int len = this.getDataLength(false);

		// FEE_FOR_ANONIMOUSE !
		///if (this.getBlockHeightByParent(db))
		// TODO FEE_FOR_ANONIMOUSE + is PERSON + DB
		int anonimous = 0;		
		// TODO DBSet get from CHAIN
		/*
		Controller cnt = Controller.getInstance();
		BlockChain bchain = cnt.getBlockChain();
		for ( Account acc : this.getRecipientAccounts())
		{
			//byte[] publicKey = cnt.getPublicKeyByAddress(acc.getAddress());
			if (!acc.isPerson(bchain.getDB())) {
				anonimuus += BlockChain.FEE_FOR_ANONIMOUSE;
			}
		}
		
		if ( anonimuus == 0 && this.creator != null) {
			byte[] publicKey = cnt.getPublicKeyByAddress(this.creator.getAddress());
			if (publicKey == null) {
				anonimuus += BlockChain.FEE_FOR_ANONIMOUSE;
			}
		}
		*/
		
		if ( anonimous > 0) {
			len *= anonimous;
		}
		
		return (int) len * BlockChain.FEE_PER_BYTE;
	}
	
	// get fee
	public int calcBaseFee() {
		return calcCommonFee();
	}
	
	// calc FEE by recommended and feePOW
	public void calcFee()
	{	
		
		BigDecimal fee = new BigDecimal(calcBaseFee())
				.multiply(BlockChain.FEE_RATE)
				.setScale(8, BigDecimal.ROUND_UP);

		if (this.feePow > 0) {
			this.fee = fee.multiply(new BigDecimal(BlockChain.FEE_POW_BASE).pow((int)this.feePow))
					.setScale(8, BigDecimal.ROUND_UP);
		} else {
			this.fee = fee;
		}
	}

	// GET forged FEE without invited FEE
	public int getForgedFee()
	{
		int fee = this.fee.unscaledValue().intValue();
		int fee_invited = fee>>BlockChain.FEE_INVITED_SHIFT;
		return fee - fee_invited;
	}
	
	// GET only INVITED FEE
	public int getInvitedFee()
	{
		int fee = this.fee.unscaledValue().intValue();
		return fee>>BlockChain.FEE_INVITED_SHIFT;		
	}
	
	public BigDecimal feeToBD(int fee)
	{
		return BigDecimal.valueOf(fee, 8);		
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Block getBlock(DBSet db) {
		
		if (block != null)
			return block;
		
		block = db.getTransactionRef_BlockRef_Map().getParent(this.signature);

		return block;
	}
	
	public Tuple2<Integer, Integer> getHeightSeqNo(DBSet db, Block block) {
		int transactionIndex = -1;
		int blockIndex = -1;
		if (block == null) {
			blockIndex = db.getBlockMap().getLastBlock().getHeight(db);
		} else {
			blockIndex = block.getHeight(db);
			if (blockIndex < 1 ) {
				// if block not is confirmed - get last block + 1
				blockIndex = db.getBlockMap().getLastBlock().getHeight(db) + 1;
			}
			//transactionIndex = this.getSeqNo(db);
			transactionIndex = block.getTransactionSeq(signature);
		}
		
		return new Tuple2<Integer, Integer>(blockIndex, transactionIndex);

	}
	
	public int getBlockHeight(DBSet db)
	{
		if(this.isConfirmed(db))
		{
			return this.getBlock(db).getHeight(db);
		}
		return -1;
	}
	
	// get current or -1
	public int getBlockHeightByParent(DBSet db)
	{

		if (block != null)
			return block.getHeightByParent(db);
		
		return getBlockHeight(db);
	}

	// get current or last
	public int getBlockHeightByParentOrLast(DBSet db)
	{

		if (block != null)
			return block.getHeightByParent(db);
		
		return Controller.getInstance().getMyHeight() + 1;
	}

	public int getSeqNo(DBSet db)
	{
		Block block = this.getBlock(db);
		if (block == null)
			return -1;
		
		return block.getTransactionSeq(this.signature);
		
	}
	
	// reference in Map - or as signatire or as BlockHeight + seqNo
	public byte[] getDBRef(DBSet db)
	{
		if(this.getConfirmations(db) < BlockChain.MAX_ORPHAN)
		{
			// soft or hard confirmations
			return this.signature;
		}
		
		int bh = this.getBlockHeight(db);
		if (bh < 1)
			// not in chain
			return null;
		
		byte[] ref = Ints.toByteArray(bh);
		Bytes.concat(ref, Ints.toByteArray(this.getSeqNo(db)));
		return ref;
		
	}
	
	public static Transaction findByHeightSeqNo(DBSet db, int height, int seq) {
		return db.getTransactionFinalMap().getTransaction(height, seq);
	}

	// reference in Map - or as signatire or as BlockHeight + seqNo
	public static Transaction findByDBRef(DBSet db, byte[] dbRef)
	{
		
		if (dbRef == null) return null;
		
		Tuple2<Integer, Integer> key;
		if(dbRef.length > 20)
		{
			// soft or hard confirmations
			key = db.getTransactionFinalMapSigns().get(dbRef);
			if (key == null) {
				return db.getTransactionMap().get(dbRef);
			}
		} else {
			int blockHeight = Ints.fromByteArray(Arrays.copyOfRange(dbRef, 0, 4));
			int seqNo = Ints.fromByteArray(Arrays.copyOfRange(dbRef, 4, 8));
			key = new Tuple2<Integer,Integer>(blockHeight, seqNo);
		}
		
		return db.getTransactionFinalMap().get(key);
		
	}

	////
	// VIEW
	public String viewType() {
		return Byte.toUnsignedInt(typeBytes[0])+"."+Byte.toUnsignedInt(typeBytes[1]);
	}
	public String viewTypeName() {
		return TYPE_NAME;
	}
	public String viewProperies() {
		return Byte.toUnsignedInt(typeBytes[2])+"."+Byte.toUnsignedInt(typeBytes[3]);
	}
	public String viewSubTypeName() {
		return "";
	}
	public String viewFullTypeName() {
		String sub = viewSubTypeName();
		return sub.length() > 0? viewTypeName() + ":" + sub: viewTypeName();
	}
	public String viewHeightSeq(DBSet db) {
		int seq = this.getSeqNo(db);
		if (seq <1)
			return "???";
		
		return this.getBlockHeight(db) + "-" + seq;
	}
	public String viewAmount(Account account) {
		return account==null?"": viewAmount(account.getAddress());
	}
	public String viewAmount(String address) {
		return "";
	}

	public String viewCreator() {
		return creator==null?"GENESIS":creator.getPersonAsString();
	}
	public String viewRecipient() {
		return "";
	}
	public String viewReference() {
		//return reference==null?"null":Base58.encode(reference);
		return reference==null?"null":"" + reference;
	}
	public String viewSignature() {
		return signature==null?"null":Base58.encode(signature);
	}
	public String viewTimestamp() {
		return timestamp<1000?"null":DateTimeFormat.timestamptoString(timestamp);
	}
	public int viewSize(boolean asPack) {
		return getDataLength(asPack);
	}
	public String viewFee() {
		return feePow +":" + this.fee.unscaledValue().longValue();
	}

	public String viewItemName() {
		return "";
	}
	public String viewAmount() {
		return "";
	}

	//PARSE/CONVERT
	
	@SuppressWarnings("unchecked")
	protected JSONObject getJsonBase()
	{
		JSONObject transaction = new JSONObject();
		
		transaction.put("type", Byte.toUnsignedInt(this.typeBytes[0]));
		transaction.put("record_type", this.viewTypeName());
		transaction.put("confirmations", this.getConfirmations(DBSet.getInstance()));
		transaction.put("type_name", this.viewTypeName());
		transaction.put("sub_type_name", this.viewSubTypeName());
		
		int height;
		if (this.creator == null )
		{
			transaction.put("creator", "genesis");
			transaction.put("reference", "genesis");
			transaction.put("signature", "genesis");
			height = 1;
		} else {
			height = this.getBlockHeight(DBSet.getInstance());
			transaction.put("reference", this.reference==null?"null":"" + this.reference);
			transaction.put("signature", this.signature==null?"null":Base58.encode(this.signature));
			transaction.put("fee", this.fee.toPlainString());
			transaction.put("timestamp", this.timestamp<1000?"null":this.timestamp);
			transaction.put("creator", this.creator.getAddress());
			transaction.put("version", Byte.toUnsignedInt(this.typeBytes[1]));
			transaction.put("property1", Byte.toUnsignedInt(this.typeBytes[2]));
			transaction.put("property2", Byte.toUnsignedInt(this.typeBytes[3]));
		}
		
		transaction.put("height", height);
		if (height > 0)
			transaction.put("sequence", this.getSeqNo(DBSet.getInstance()));
		
		return transaction;
	}
	
	public abstract JSONObject toJson();

	@SuppressWarnings("unchecked")
	public JSONObject rawToJson() {
		
		JSONObject transaction = new JSONObject();
		
		transaction.put("confirmations", this.getConfirmations(DBSet.getInstance()));
		
		int height;
		if (this.creator == null )
		{
			height = 1;
		} else {
			height = this.getBlockHeight(DBSet.getInstance());
			transaction.put("publickey", Base58.encode(this.creator.getPublicKey()));
		}

		transaction.put("height", height);
		if (height > 0)
			transaction.put("sequence", this.getSeqNo(DBSet.getInstance()));
		
		boolean isSigned = this.signature!=null;
		transaction.put("signature", isSigned?Base58.encode(this.signature):"null");
		
		transaction.put("raw", Base58.encode(this.toBytes(isSigned, null)));
		
		return transaction;
	}

	public void sign(PrivateKeyAccount creator, boolean asPack)
	{
		
		// use this.reference in any case and for Pack too
		// but not with SIGN
		boolean withSign = false;
		byte[] data = this.toBytes( withSign, null );
		if ( data == null ) return;
				
		// all test a not valid for main test
		// all other network must be invalid here!
		int port = Controller.getInstance().getNetworkPort();
		data = Bytes.concat(data, Ints.toByteArray(port));

		this.signature = Crypto.getInstance().sign(creator, data);
		if (!asPack)
			 // need for recalc! if not as a pack
			this.calcFee();
	}

	// releaserReference == null - not as pack
	// releaserReference = reference of releaser - as pack
	public byte[] toBytes(boolean withSign, Long releaserReference) {

		boolean asPack = releaserReference != null;

		byte[] data = new byte[0];

		//WRITE TYPE
		data = Bytes.concat(data, this.typeBytes);

		if (!asPack) {
			//WRITE TIMESTAMP
			byte[] timestampBytes = Longs.toByteArray(this.timestamp);
			timestampBytes = Bytes.ensureCapacity(timestampBytes, TIMESTAMP_LENGTH, 0);
			data = Bytes.concat(data, timestampBytes);
		}
		
		//WRITE REFERENCE - in any case as Pack or not
		if (this.reference != null) {
			// NULL in imprints
			byte[] referenceBytes = Longs.toByteArray(this.reference);
			referenceBytes = Bytes.ensureCapacity(referenceBytes, REFERENCE_LENGTH, 0);
			data = Bytes.concat(data, referenceBytes);
		}

		//WRITE CREATOR
		data = Bytes.concat(data, this.creator.getPublicKey());

		if (!asPack) {
			//WRITE FEE POWER
			byte[] feePowBytes = new byte[1];
			feePowBytes[0] = this.feePow;
			data = Bytes.concat(data, feePowBytes);
		}

		//SIGNATURE
		if (withSign)
			data = Bytes.concat(data, this.signature);
		
		return data;
		
	}

	public abstract int getDataLength(boolean asPack);

	public boolean isWiped() {
		for ( byte[] wiped: BlockChain.WIPED_RECORDS) {
			if (Arrays.equals(this.signature, wiped)) {
				return true;
			}
		}
		return false;
	}

	/*
	public boolean isValidated() {
		for ( byte[] wiped: BlockChain.VALID_RECORDS) {
			byte[] sign = wiped;
			if (Arrays.equals(this.signature, sign)) {
				return true;
			}
		}
		return false;
	}
	*/

	//VALIDATE
	
	public static boolean isSignatureValid(PublicKeyAccount creator, byte[] data, byte[] signature) {

		if ( signature == null | signature.length != Crypto.SIGNATURE_LENGTH
				| signature == new byte[Crypto.SIGNATURE_LENGTH]) return false;
		
		if ( data == null ) return false;

		return Crypto.getInstance().verify(creator.getPublicKey(), signature, data);

	}
	
	public boolean isSignatureValid() {

		if ( this.signature == null || this.signature.length != Crypto.SIGNATURE_LENGTH
				|| Arrays.equals(this.signature, new byte[Crypto.SIGNATURE_LENGTH]))
			return false;
		
		// validation with reference - not as a pack in toBytes - in any case!
		byte[] data = this.toBytes( false, null );
		if ( data == null ) return false;
		
		// all test a not valid for main test
		// all other network must be invalid here!
		int port = Controller.getInstance().getNetworkPort();
		data = Bytes.concat(data, Ints.toByteArray(port));

		return Crypto.getInstance().verify(this.creator.getPublicKey(), this.signature, data);
	}
	
	public int isValid(Long releaserReference)
	{
		return isValid(DBSet.getInstance(), releaserReference);
	}
	
	public int isValid(DBSet db, Long releaserReference)
	{
	
		//CHECK IF REFERENCE IS OK
		Long reference = releaserReference==null ? this.creator.getLastReference(db) : releaserReference;
		if (reference != null && this.isReferenced()) {
			if (reference.compareTo(this.reference) != 0)
				return INVALID_REFERENCE;
			else if (reference.compareTo(this.timestamp) >= 0)
				return INVALID_TIMESTAMP;
		}		

		//CHECK CREATOR
		if(!Crypto.getInstance().isValidAddress(this.creator.getAddress()))
		{
			return INVALID_ADDRESS;
		}
				
		if (this.hasPublicText() && !this.creator.isPerson(db)) {
			if (BlockChain.DEVELOP_USE) {	
				for ( String admin: BlockChain.GENESIS_ADMINS) {
					if (this.creator.equals(admin)) {
						return VALIDATE_OK;
					}
				}
			}
			return CREATOR_NOT_PERSONALIZED;
		}

		// CHECK IT AFTER isPERSON ! because in ignored in IssuePerson
		//CHECK IF CREATOR HAS ENOUGH FEE MONEY
		if(this.creator.getBalance(db, FEE_KEY).a.compareTo(this.fee) < 0)
		{
			return NOT_ENOUGH_FEE;
		}

		return VALIDATE_OK;

	}

	//PROCESS/ORPHAN
	
	public void process_gifts(DBSet db, int level, int fee_gift, Account creator, boolean asOrphan) {
		Tuple4<Long, Integer, Integer, Integer> personDuration = creator.getPersonDuration(db);
		//byte[] recordSignature = record.getSignature();
		// TODO if PERSON die - skip it steep
		if (personDuration == null) {
			// USE all GIFT for current ACCOUNT
			//creator.addBalanceOWN(FEE_KEY,BigDecimal.valueOf(asOrphan?-fee_gift:fee_gift, BlockChain.FEE_SCALE), db);
			creator.changeBalance(db, asOrphan, FEE_KEY, BigDecimal.valueOf(fee_gift, BlockChain.FEE_SCALE));
			return;
		}
		
		// CREATOR is PERSON
		// FIND person
		long personKey = personDuration.a;
		//ItemCls person = ItemCls.getItem(db, ItemCls.PERSON_TYPE, personKey);
		ItemCls person = db.getItemPersonMap().get(personKey);
		Account invitedAccount = person.getOwner();
		if (creator.equals(invitedAccount)) {
			// IT IS ME - all fee!
			creator.changeBalance(db, asOrphan, FEE_KEY, BigDecimal.valueOf(fee_gift, BlockChain.FEE_SCALE));
			return;			
		}

		int fee_gift_next;
		if (fee_gift > 2)
			fee_gift_next = fee_gift>>BlockChain.FEE_INVITED_SHIFT_IN_LEVEL;
		else
			fee_gift_next = fee_gift - 1;
		
		int fee_gift_get =  fee_gift - fee_gift_next;
		//invitedAccount.addBalanceOWN(FEE_KEY, fee_gift_get_BD, db);
		invitedAccount.changeBalance(db, asOrphan, FEE_KEY, BigDecimal.valueOf(fee_gift_get, BlockChain.FEE_SCALE));
		
		if (level < BlockChain.FEE_INVITED_DEEP && fee_gift_next > 0) {
			process_gifts(db, ++level, fee_gift_next, invitedAccount, asOrphan);
		}
	}

	//public abstract void process(DBSet db);
	public void process(DBSet db, Block block, boolean asPack)
	{
	
		this.block = block;
		
		if (!asPack) {
			this.calcFee();
	
			if (this.fee != null && this.fee.compareTo(BigDecimal.ZERO) != 0) {
				//this.creator.setBalance(FEE_KEY, this.creator.getBalance(db, FEE_KEY).subtract(this.fee), db);
				this.creator.changeBalance(db, true, FEE_KEY, this.fee);

			}
						
			if (!db.isFork()) {
				// calc INVITED FEE if its not a FORK
				process_gifts(db, 0, getInvitedFee(), this.creator, false);
			}

			String creatorAddress = this.creator.getAddress();
			AddressTime_SignatureMap dbASmap = db.getAddressTime_SignatureMap();
			if (!dbASmap.contains(creatorAddress)) {
				dbASmap.set(creatorAddress, signature); // for quick search public keys
			}
			dbASmap.set(creatorAddress, timestamp, signature); // for search records by time

			//UPDATE REFERENCE OF SENDER
			if (this.isReferenced() )
				// IT IS REFERENCED RECORD?
				this.creator.setLastReference(this.timestamp, db);
		}

	}
	
	public void orphan(DBSet db, boolean asPack)
	{
		
		if (!asPack) {
			if (this.fee != null && this.fee.compareTo(BigDecimal.ZERO) != 0) {
				//this.creator.setBalance(FEE_KEY, this.creator.getBalance(db, FEE_KEY).add(this.fee), db);
				this.creator.changeBalance(db, false, FEE_KEY, this.fee);

			}
			
			if (!db.isFork()) {
				// calc INVITED FEE if its not a FORK
				process_gifts(db, 0, getInvitedFee(), this.creator, true);
			}

			//UPDATE REFERENCE OF SENDER
			if (this.isReferenced() ) {
				// IT IS REFERENCED RECORD?
				this.creator.setLastReference(this.reference, db);
				// set last transaction signature for this ACCOUNT
			}

			db.getAddressTime_SignatureMap().delete(creator, timestamp);

		}
	}

	public Transaction copy() 
	{	
		try 
		{
			return TransactionFactory.getInstance().parse(this.toBytes(false, null), null);
		}
		catch (Exception e) 
		{
			return null;
		}	
	}
	
	//REST
		
	public abstract HashSet<Account> getInvolvedAccounts();
	
	public abstract HashSet<Account> getRecipientAccounts();
		
	public abstract boolean isInvolved(Account account);
 

	@Override 
	public boolean equals(Object object)
	{
		if(object instanceof Transaction)
		{
			Transaction transaction = (Transaction) object;
			
			return Arrays.equals(this.getSignature(), transaction.getSignature());
		}
		
		return false;
	}

	/*
	public boolean isConfirmed()
	{
		return this.isConfirmed(DBSet.getInstance());
	}
	*/
	
	public boolean isConfirmed(DBSet db)
	{
		return db.getTransactionRef_BlockRef_Map().contains(this.getSignature());
	}
	
	public int getConfirmations(DBSet db)
	{
		
		try
		{
		//CHECK IF IN UNCONFIRMED TRANSACTION
		if(db.getTransactionMap().contains(this))
		{
			return -db.getTransactionMap().getBroadcasts(this);
		}
		
		//CALCULATE CONFIRMATIONS
		int lastBlockHeight = db.getBlockSignsMap().getHeight(db.getBlockMap().getLastBlockSignature());
		//Block block = DBSet.getInstance().getTransactionRef_BlockRef_Map().getParent(this.signature);
		Block block = this.getBlock(db);
		
		if (block == null)return 0;
		
		int transactionBlockHeight = db.getBlockSignsMap().getHeight(block);
		
		//RETURN
		return 1 + lastBlockHeight - transactionBlockHeight;

		} catch(Exception e) {
			LOGGER.error(e.getMessage(),e);
			return 0;
		}
	}

	public int getBlockVersion(DBSet db)
	{
		// IF ALREADY IN THE BLOCK. CONFIRMED 
		if(this.isConfirmed(db))
		{
			//return DBSet.getInstance().getTransactionRef_BlockRef_Map().getParent(this.getSignature()).getVersion();
			return this.getBlock(db).getVersion();
		}
		
		// IF UNCONFIRMED
		return Controller.getInstance().getLastBlock().getNextBlockVersion(DBSet.getInstance());	
	}

	public static Map<String, Map<Long, BigDecimal>> subAssetAmount(Map<String, Map<Long, BigDecimal>> allAssetAmount, String address, Long assetKey, BigDecimal amount) 
	{
		return addAssetAmount(allAssetAmount, address, assetKey, BigDecimal.ZERO.setScale(8).subtract(amount));
	}

	public static Map<String, Map<Long, BigDecimal>> addAssetAmount(Map<String, Map<Long, BigDecimal>> allAssetAmount, String address, Long assetKey, BigDecimal amount) 
	{
		Map<String, Map<Long, BigDecimal>> newAllAssetAmount;
		if(allAssetAmount != null) {
			newAllAssetAmount = new LinkedHashMap<String, Map<Long, BigDecimal>>(allAssetAmount);
		} else {
			newAllAssetAmount = new LinkedHashMap<String, Map<Long, BigDecimal>>();
		}

		Map<Long, BigDecimal> newAssetAmountOfAddress;
		
		if(!newAllAssetAmount.containsKey(address)){
			newAssetAmountOfAddress = new LinkedHashMap<Long, BigDecimal>();
			newAssetAmountOfAddress.put(assetKey, amount);
			
			newAllAssetAmount.put(address, newAssetAmountOfAddress);
		} else {
			if(!newAllAssetAmount.get(address).containsKey(assetKey)) {
				newAssetAmountOfAddress = new LinkedHashMap<Long, BigDecimal>(newAllAssetAmount.get(address));
				newAssetAmountOfAddress.put(assetKey, amount);

				newAllAssetAmount.put(address, newAssetAmountOfAddress);
			} else {
				newAssetAmountOfAddress = new LinkedHashMap<Long, BigDecimal>(newAllAssetAmount.get(address));
				BigDecimal newAmount = newAllAssetAmount.get(address).get(assetKey).add(amount);
				newAssetAmountOfAddress.put(assetKey, newAmount);
				
				newAllAssetAmount.put(address, newAssetAmountOfAddress);
			}
		}
		
		return newAllAssetAmount;
	}
	public static Map<String, Map<Long, Long>> addStatusTime(Map<String, Map<Long, Long>> allStatusTime, String address, Long assetKey, Long time) 
	{
		Map<String, Map<Long, Long>> newAllStatusTime;
		if(allStatusTime != null) {
			newAllStatusTime = new LinkedHashMap<String, Map<Long, Long>>(allStatusTime);
		} else {
			newAllStatusTime = new LinkedHashMap<String, Map<Long, Long>>();
		}

		Map<Long, Long> newStatusTimetOfAddress;
		
		if(!newAllStatusTime.containsKey(address)){
			newStatusTimetOfAddress = new LinkedHashMap<Long, Long>();
			newStatusTimetOfAddress.put(assetKey, time);
			
			newAllStatusTime.put(address, newStatusTimetOfAddress);
		} else {
			if(!newAllStatusTime.get(address).containsKey(assetKey)) {
				newStatusTimetOfAddress = new LinkedHashMap<Long, Long>(newAllStatusTime.get(address));
				newStatusTimetOfAddress.put(assetKey, time);

				newAllStatusTime.put(address, newStatusTimetOfAddress);
			} else {
				newStatusTimetOfAddress = new LinkedHashMap<Long, Long>(newAllStatusTime.get(address));
				Long newTime = newAllStatusTime.get(address).get(assetKey) + time;
				newStatusTimetOfAddress.put(assetKey, newTime);
				
				newAllStatusTime.put(address, newStatusTimetOfAddress);
			}
		}
		
		return newAllStatusTime;
	}

}
