package core.transaction;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import core.account.Account;
import core.account.PrivateKeyAccount;
import core.account.PublicKeyAccount;
import core.crypto.Base58;
import core.crypto.Crypto;
import core.item.notes.NoteCls;
import core.item.notes.NoteFactory;
import core.item.persons.PersonCls;
import core.item.persons.PersonFactory;
import database.BalanceMap;
import database.DBSet;
import utils.Converter;

public class R_SertifyPerson extends Transaction {

	private static final byte TYPE_ID = (byte)Transaction.CERTIFY_PERSON_TRANSACTION;
	private static final String NAME_ID = "Sertify Person";
	private static final int USER_ACCOUNT_LENGTH = Transaction.CREATOR_LENGTH;
	private static final int DURATION_LENGTH = 4; // one year + 256 days max
	private static final BigDecimal MIN_ERM_BALANCE = BigDecimal.valueOf(1000).setScale(8);
	private static final BigDecimal MIN_VOTE_BALANCE = BigDecimal.valueOf(10).setScale(8);

	// how many OIL gift
	public static final BigDecimal OIL_AMOUNT = BigDecimal.valueOf(0.00005).setScale(8);
	public static final BigDecimal VOTE_AMOUNT = BigDecimal.ONE.setScale(8);

	protected long key;
	protected Integer duration; // duration in days 
	protected PublicKeyAccount userAccount1;
	protected PublicKeyAccount userAccount2;
	protected PublicKeyAccount userAccount3;
	protected byte[] userSignature1;
	protected byte[] userSignature2;
	protected byte[] userSignature3;
	private static final int SELF_LENGTH = 3 * (USER_ACCOUNT_LENGTH + SIGNATURE_LENGTH) + DURATION_LENGTH
			+ KEY_LENGTH;
	
	protected static final int BASE_LENGTH_AS_PACK = Transaction.BASE_LENGTH_AS_PACK + SELF_LENGTH;
	protected static final int BASE_LENGTH = Transaction.BASE_LENGTH + SELF_LENGTH;

	public R_SertifyPerson(byte[] typeBytes, PublicKeyAccount creator, byte feePow, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, long timestamp, byte[] reference) {
		super(typeBytes, NAME_ID, creator, feePow, timestamp, reference);		

		this.TYPE_NAME = NAME_ID;
		this.key = key;
		this.userAccount1 = userAccount1;
		this.userAccount2 = userAccount2;
		this.userAccount3 = userAccount3;
		this.duration = duration; 
	}
	public R_SertifyPerson(PublicKeyAccount creator, byte feePow, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, long timestamp, byte[] reference) {
		this(new byte[]{TYPE_ID, 0, 0, 0}, creator, feePow, key,
				userAccount1, userAccount2, userAccount3,
				duration, timestamp, reference);
	}
	public R_SertifyPerson(byte[] typeBytes, PublicKeyAccount creator, byte feePow, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, long timestamp, byte[] reference, byte[] signature,
			 byte[] userSignature1,  byte[] userSignature2,  byte[] userSignature3) {
		this(typeBytes, creator, feePow, key,
				userAccount1, userAccount2, userAccount3,
				duration, timestamp, reference);
		this.signature = signature;
		this.userSignature1 = userSignature1;
		this.userSignature2 = userSignature2;
		this.userSignature3 = userSignature3;
		this.calcFee();
	}
	// as pack
	public R_SertifyPerson(byte[] typeBytes, PublicKeyAccount creator, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, byte[] signature,
			 byte[] userSignature1,  byte[] userSignature2,  byte[] userSignature3) {
		this(typeBytes, creator, (byte)0, key,
				userAccount1, userAccount2, userAccount3,
				duration, 0l, null);
		this.signature = signature;
		this.userSignature1 = userSignature1;
		this.userSignature2 = userSignature2;
		this.userSignature3 = userSignature3;
	}
	public R_SertifyPerson(PublicKeyAccount creator, byte feePow, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, long timestamp, byte[] reference, byte[] signature,
			byte[] userSignature1, byte[] userSignature2, byte[] userSignature3) {
		this(new byte[]{TYPE_ID, 0, 0, 0}, creator, feePow, key,
				userAccount1, userAccount2, userAccount3,
				duration, timestamp, reference);
	}
	// as pack
	public R_SertifyPerson(PublicKeyAccount creator, long key,
			PublicKeyAccount userAccount1, PublicKeyAccount userAccount2, PublicKeyAccount userAccount3,
			int duration, byte[] signature,
			byte[] userSignature1, byte[] userSignature2, byte[] userSignature3) {
		this(new byte[]{TYPE_ID, 0, 0, 0}, creator, (byte)0, key,
				userAccount1, userAccount2, userAccount3,
				duration, 0l, null);
	}
	
	//GETTERS/SETTERS

	//public static String getName() { return "Send"; }

	public long getKey()
	{
		return this.key;
	}

	public PublicKeyAccount getUserAccount1() 
	{
		return this.userAccount1;
	}
	public PublicKeyAccount getUserAccount2() 
	{
		return this.userAccount2;
	}
	public PublicKeyAccount getUserAccount3() 
	{
		return this.userAccount3;
	}
	
	public byte[] getUserSignature1() 
	{
		return this.userSignature1;
	}
	public byte[] getUserSignature2() 
	{
		return this.userSignature2;
	}
	public byte[] getUserSignature3() 
	{
		return this.userSignature3;
	}
	public int getDuration() 
	{
		return this.duration;
	}
			
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJson() 
	{
		//GET BASE
		JSONObject transaction = this.getJsonBase();

		//ADD CREATOR/SERVICE/DATA
		transaction.put("key", this.key);
		transaction.put("userAccount1", this.userAccount1.getAddress());
		transaction.put("userAccount2", this.userAccount2.getAddress());
		transaction.put("userAccount3", this.userAccount3.getAddress());
		transaction.put("duration", this.duration);
		
		return transaction;	
	}

	public void signUserAccounts(PrivateKeyAccount userAccount1, PrivateKeyAccount userAccount2, PrivateKeyAccount userAccount3)
	{
		byte[] data;
		// use this.reference in any case
		data = this.toBytes( false, null );
		if ( data == null ) return;

		this.userSignature1 = Crypto.getInstance().sign(userAccount1, data);
		this.userSignature2 = Crypto.getInstance().sign(userAccount2, data);
		this.userSignature3 = Crypto.getInstance().sign(userAccount3, data);
		//this.calcFee(); // need for recal!
	}

	// releaserReference = null - not a pack
	// releaserReference = reference for releaser account - it is as pack
	public static Transaction Parse(byte[] data, byte[] releaserReference) throws Exception
	{
		boolean asPack = releaserReference != null;
		
		//CHECK IF WE MATCH BLOCK LENGTH
		if (data.length < BASE_LENGTH_AS_PACK
				| !asPack & data.length < BASE_LENGTH)
		{
			throw new Exception("Data does not match block length " + data.length);
		}
		
		// READ TYPE
		byte[] typeBytes = Arrays.copyOfRange(data, 0, TYPE_LENGTH);
		int position = TYPE_LENGTH;

		long timestamp = 0;
		if (!asPack) {
			//READ TIMESTAMP
			byte[] timestampBytes = Arrays.copyOfRange(data, position, position + TIMESTAMP_LENGTH);
			timestamp = Longs.fromByteArray(timestampBytes);	
			position += TIMESTAMP_LENGTH;
		}

		byte[] reference;
		if (!asPack) {
			//READ REFERENCE
			reference = Arrays.copyOfRange(data, position, position + REFERENCE_LENGTH);
			position += REFERENCE_LENGTH;
		} else {
			reference = releaserReference;
		}
		
		//READ CREATOR
		byte[] creatorBytes = Arrays.copyOfRange(data, position, position + CREATOR_LENGTH);
		PublicKeyAccount creator = new PublicKeyAccount(creatorBytes);
		position += CREATOR_LENGTH;
		
		byte feePow = 0;
		if (!asPack) {
			//READ FEE POWER
			byte[] feePowBytes = Arrays.copyOfRange(data, position, position + 1);
			feePow = feePowBytes[0];
			position += 1;
		}
		
		//READ SIGNATURE
		byte[] signature = Arrays.copyOfRange(data, position, position + SIGNATURE_LENGTH);
		position += SIGNATURE_LENGTH;

		/////
		//READ PERSON
		// Person parse without reference - if is = signature
		//PersonCls person = PersonFactory.getInstance().parse(Arrays.copyOfRange(data, position, data.length), false);
		//position += person.getDataLength(false);

		//READ KEY
		byte[] keyBytes = Arrays.copyOfRange(data, position, position + KEY_LENGTH);
		long key = Longs.fromByteArray(keyBytes);	
		position += KEY_LENGTH;

		//READ USER ACCOUNT1
		byte[] userAccount1Bytes = Arrays.copyOfRange(data, position, position + USER_ACCOUNT_LENGTH);
		PublicKeyAccount userAccount1 = new PublicKeyAccount(userAccount1Bytes);
		position += USER_ACCOUNT_LENGTH;

		//READ USER ACCOUNT2
		byte[] userAccount2Bytes = Arrays.copyOfRange(data, position, position + USER_ACCOUNT_LENGTH);
		PublicKeyAccount userAccount2 = new PublicKeyAccount(userAccount2Bytes);
		position += USER_ACCOUNT_LENGTH;

		//READ USER ACCOUNT1
		byte[] userAccount3Bytes = Arrays.copyOfRange(data, position, position + USER_ACCOUNT_LENGTH);
		PublicKeyAccount userAccount3 = new PublicKeyAccount(userAccount3Bytes);
		position += USER_ACCOUNT_LENGTH;

		// READ DURATION
		int duration = Ints.fromByteArray(Arrays.copyOfRange(data, position, position + DURATION_LENGTH));
		position += DURATION_LENGTH;
				
		//READ USER1 SIGNATURE
		byte[] userSignature1 = Arrays.copyOfRange(data, position, position + SIGNATURE_LENGTH);
		position += SIGNATURE_LENGTH;

		//READ USER2 SIGNATURE
		byte[] userSignature2 = Arrays.copyOfRange(data, position, position + SIGNATURE_LENGTH);
		position += SIGNATURE_LENGTH;

		//READ USER3 SIGNATURE
		byte[] userSignature3 = Arrays.copyOfRange(data, position, position + SIGNATURE_LENGTH);
		position += SIGNATURE_LENGTH;

		if (!asPack) {
			return new R_SertifyPerson(typeBytes, creator, feePow, key,
					userAccount1, userAccount2, userAccount3,
					duration, timestamp, reference, signature,
					 userSignature1,  userSignature2,  userSignature3);
		} else {
			return new R_SertifyPerson(typeBytes, creator, key,
					userAccount1, userAccount2, userAccount3,
					duration, signature,
					 userSignature1,  userSignature2,  userSignature3);
		}

	}

	//@Override
	public byte[] toBytes(boolean withSign, byte[] releaserReference) {

		byte[] data = super.toBytes(withSign, releaserReference);

		//WRITE PERSON KEY
		byte[] keyBytes = Longs.toByteArray(this.key);
		keyBytes = Bytes.ensureCapacity(keyBytes, KEY_LENGTH, 0);
		data = Bytes.concat(data, keyBytes);
		
		//WRITE USER ACCOUNT1
		data = Bytes.concat(data, this.userAccount1.getPublicKey());
		//WRITE USER ACCOUNT2
		data = Bytes.concat(data, this.userAccount2.getPublicKey());
		//WRITE USER ACCOUNT3
		data = Bytes.concat(data, this.userAccount3.getPublicKey());

		//WRITE DURATION
		data = Bytes.concat(data, Ints.toByteArray(this.duration));

		//USER SIGNATUREs
		if (withSign) {
			data = Bytes.concat(data, this.userSignature1);
			data = Bytes.concat(data, this.userSignature2);
			data = Bytes.concat(data, this.userSignature3);
		}

		return data;	
	}

	@Override
	public int getDataLength(boolean asPack)
	{
		// not include note reference
		return asPack? BASE_LENGTH_AS_PACK : BASE_LENGTH;
	}

	//VALIDATE

	@Override
	public boolean isSignatureValid() {

		if ( this.signature == null | this.signature.length != 64 | this.signature == new byte[64]
			| this.userSignature1 == null | this.userSignature1.length != 64 | this.userSignature1 == new byte[64]
			| this.userSignature2 == null | this.userSignature2.length != 64 | this.userSignature2 == new byte[64]
			| this.userSignature3 == null | this.userSignature3.length != 64 | this.userSignature3 == new byte[64]
					)
			return false;
		
		byte[] data = this.toBytes( false, null );
		if ( data == null ) return false;

		Crypto crypto = Crypto.getInstance();
		return crypto.verify(creator.getPublicKey(), signature, data)
				& crypto.verify(this.userAccount1.getPublicKey(), this.userSignature1, data)
				& crypto.verify(this.userAccount2.getPublicKey(), this.userSignature2, data)
				& crypto.verify(this.userAccount3.getPublicKey(), this.userSignature3, data);
	}

	//
	public int isValid(DBSet db, byte[] releaserReference) {
		
		//CHECK DURATION
		if(duration < 100 | duration > 777)
		{
			return INVALID_DURATION;
		}
	
		//CHECK IF RECIPIENT IS VALID ADDRESS
		if(this.userAccount1 == null | !Crypto.getInstance().isValidAddress(this.userAccount1.getAddress())
			| this.userAccount2 == null | !Crypto.getInstance().isValidAddress(this.userAccount2.getAddress())
			| this.userAccount3 == null | !Crypto.getInstance().isValidAddress(this.userAccount3.getAddress())
				)
		{
			return INVALID_ADDRESS;
		}
		
		BigDecimal balERM = this.creator.getConfirmedBalance(ERMO_KEY, db);
		if ( balERM.compareTo(MIN_ERM_BALANCE)<0 )
		{
			return Transaction.NOT_ENOUGH_ERM;
		}
		
		BigDecimal balVOTE = this.creator.getConfirmedBalance(0l, db);
		if ( balVOTE.compareTo(MIN_VOTE_BALANCE)<0 )
		{
			return Transaction.INVALID_AMOUNT;
		}

		int result = super.isValid(db, releaserReference);
		if (result != Transaction.VALIDATE_OK) return result; 
		
		// ITEM EXIST?
		if (!db.getPersonMap().contains(this.key))
			return Transaction.ITEM_DOES_NOT_EXIST;

		return Transaction.VALIDATE_OK;
	}

	//PROCESS/ORPHAN
	
	public void process(DBSet db, boolean asPack) {

		//UPDATE SENDER
		super.process(db, asPack);

		// send VOTE_KEY
		this.creator.setConfirmedBalance(DIL_KEY, this.creator.getConfirmedBalance(DIL_KEY, db).subtract(OIL_AMOUNT), db);						
		this.creator.setConfirmedBalance(LAEV_KEY, this.creator.getConfirmedBalance(LAEV_KEY, db).subtract(VOTE_AMOUNT), db);						
		//UPDATE USER
		this.userAccount1.setConfirmedBalance(Transaction.DIL_KEY, this.userAccount1.getConfirmedBalance(Transaction.DIL_KEY, db).add(OIL_AMOUNT), db);
		this.userAccount1.setConfirmedBalance(Transaction.LAEV_KEY, this.userAccount1.getConfirmedBalance(Transaction.LAEV_KEY, db).add(VOTE_AMOUNT), db);
		
		if (!asPack) {

			//UPDATE REFERENCE OF RECIPIENT - for first accept OIL need
			if(Arrays.equals(this.userAccount1.getLastReference(db), new byte[0]))
			{
				this.userAccount1.setLastReference(this.signature, db);
			}
		}

	}

	public void orphan(DBSet db, boolean asPack) {

		//UPDATE SENDER
		super.orphan(db, asPack);
		
		// BACK VOTE_KEY
		this.creator.setConfirmedBalance(Transaction.DIL_KEY, this.creator.getConfirmedBalance(Transaction.DIL_KEY, db).add(OIL_AMOUNT), db);						
		this.creator.setConfirmedBalance(Transaction.LAEV_KEY, this.creator.getConfirmedBalance(Transaction.LAEV_KEY, db).add(VOTE_AMOUNT), db);						
						
		//UPDATE RECIPIENT
		this.userAccount1.setConfirmedBalance(Transaction.DIL_KEY, this.userAccount1.getConfirmedBalance(Transaction.DIL_KEY, db).subtract(OIL_AMOUNT), db);
		this.userAccount1.setConfirmedBalance(Transaction.LAEV_KEY, this.userAccount1.getConfirmedBalance(Transaction.LAEV_KEY, db).subtract(VOTE_AMOUNT), db);
		
		if (!asPack) {
			
			//UPDATE REFERENCE OF RECIPIENT
			if(Arrays.equals(this.userAccount1.getLastReference(db), this.signature))
			{
				this.userAccount1.removeReference(db);
			}	
		}
	}

	@Override
	public HashSet<Account> getInvolvedAccounts()
	{
		HashSet<Account> accounts = new HashSet<Account>();
		accounts.add(this.creator);
		accounts.addAll(this.getRecipientAccounts());
		return accounts;
	}

	@Override
	public HashSet<Account> getRecipientAccounts() {
		HashSet<Account> accounts = new HashSet<Account>();
		
		accounts.add(this.userAccount1);
		accounts.add(this.userAccount2);
		accounts.add(this.userAccount3);
		
		return accounts;
	}
	
	@Override
	public boolean isInvolved(Account account) 
	{
		String address = account.getAddress();
		
		if(address.equals(creator.getAddress())
				|| address.equals(userAccount1.getAddress())
				|| address.equals(userAccount2.getAddress())
				|| address.equals(userAccount3.getAddress())
				)
		{
			return true;
		}
		
		return false;
	}

	public int calcBaseFee() {
		return calcCommonFee();
	}

}