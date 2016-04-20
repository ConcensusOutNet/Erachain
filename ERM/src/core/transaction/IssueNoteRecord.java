package core.transaction;

import static org.junit.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
//import java.util.Map;
// import org.apache.log4j.Logger;

//import ntp.NTP;

import org.json.simple.JSONObject;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Longs;

import core.account.Account;
import core.account.PrivateKeyAccount;
import core.account.PublicKeyAccount;
import core.crypto.Crypto;
import core.item.ItemCls;
import core.item.notes.NoteCls;
import core.item.notes.NoteFactory;
//import database.ItemMap;
import database.DBSet;

public class IssueNoteRecord extends IssueItemRecord 
{
	private static final byte TYPE_ID = (byte)ISSUE_NOTE_TRANSACTION;
	private static final String NAME_ID = "Issue Note";
	
	public IssueNoteRecord(byte[] typeBytes, PublicKeyAccount creator, NoteCls note, byte feePow, long timestamp, byte[] reference) 
	{
		super(typeBytes, NAME_ID, creator, note, feePow, timestamp, reference);		
	}
	public IssueNoteRecord(byte[] typeBytes, PublicKeyAccount creator, NoteCls note, byte feePow, long timestamp, byte[] reference, byte[] signature) 
	{
		this(typeBytes, creator, note, feePow, timestamp, reference);		
		this.signature = signature;
		this.calcFee();
	}
	public IssueNoteRecord(byte[] typeBytes, PublicKeyAccount creator, NoteCls note, byte[] signature) 
	{
		this(typeBytes, creator, note, (byte)0, 0l, null);		
		this.signature = signature;
	}
	public IssueNoteRecord(PublicKeyAccount creator, NoteCls note, byte feePow, long timestamp, byte[] reference, byte[] signature) 
	{
		this(new byte[]{TYPE_ID,0,0,0}, creator, note, feePow, timestamp, reference, signature);
	}
	public IssueNoteRecord(PublicKeyAccount creator, NoteCls note, byte[] signature) 
	{
		this(new byte[]{TYPE_ID,0,0,0}, creator, note, (byte)0, 0l, null, signature);
	}
	public IssueNoteRecord(PublicKeyAccount creator, NoteCls note, byte feePow, long timestamp, byte[] reference) 
	{
		this(new byte[]{TYPE_ID,0,0,0}, creator, note, feePow, timestamp, reference);
	}
	public IssueNoteRecord(PublicKeyAccount creator, NoteCls note) 
	{
		this(new byte[]{TYPE_ID,0,0,0}, creator, note, (byte)0, 0l, null);
	}

	//GETTERS/SETTERS
	//public static String getName() { return "Issue Note"; }
	

	//PARSE CONVERT
	
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

		byte[] reference = null;
		if (!asPack) {
			//READ REFERENCE
			reference = Arrays.copyOfRange(data, position, position + REFERENCE_LENGTH);
			position += REFERENCE_LENGTH;
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
		byte[] signatureBytes = Arrays.copyOfRange(data, position, position + SIGNATURE_LENGTH);
		position += SIGNATURE_LENGTH;
		
		//READ NOTE
		// note parse without reference - if is = signature
		NoteCls note = NoteFactory.getInstance().parse(Arrays.copyOfRange(data, position, data.length), false);
		position += note.getDataLength(false);
				
		if (!asPack) {
			return new IssueNoteRecord(typeBytes, creator, note, feePow, timestamp, reference, signatureBytes);
		} else {
			return new IssueNoteRecord(typeBytes, creator, note, signatureBytes);
		}
	}	
	
	//VALIDATE
		
	
	//PROCESS/ORPHAN


}