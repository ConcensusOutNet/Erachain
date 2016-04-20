package test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ntp.NTP;

import org.apache.log4j.Logger;
import org.junit.Test;

import core.account.Account;
import core.account.PrivateKeyAccount;
import core.block.GenesisBlock;
import core.crypto.Crypto;
import core.item.assets.AssetCls;
import core.item.assets.AssetVenture;
import core.payment.Payment;
import core.transaction.ArbitraryTransactionV3;
import core.transaction.GenesisTransaction;
import core.transaction.MessageTransaction;
import core.transaction.Transaction;
import database.DBSet;



public class TransactionV3Tests {

	static Logger LOGGER = Logger.getLogger(TransactionV3Tests.class.getName());

	byte[] releaserReference = null;

	long OIL_KEY = 1l;
	byte FEE_POWER = (byte)1;
	byte[] assetReference = new byte[64];
	long timestamp = NTP.getTime();
	
	//CREATE EMPTY MEMORY DATABASE
	private DBSet db;
	private GenesisBlock gb;
	
	//CREATE KNOWN ACCOUNT
	byte[] seed = Crypto.getInstance().digest("test".getBytes());
	byte[] privateKey = Crypto.getInstance().createKeyPair(seed).getA();
	PrivateKeyAccount maker = new PrivateKeyAccount(privateKey);
	

	// INIT ASSETS
	private void init() {
		
		db = DBSet.createEmptyDatabaseSet();
		gb = new GenesisBlock();
		gb.process(db);
		
		// OIL FUND
		maker.setLastReference(gb.getGeneratorSignature(), db);
		maker.setConfirmedBalance(OIL_KEY, BigDecimal.valueOf(1).setScale(8), db);

	}

	@Test
	public void validateMessageTransactionV3() 
	{
		
		init();
		
		//CREATE KNOWN ACCOUNT
		byte[] seed = Crypto.getInstance().digest("test".getBytes());
		byte[] privateKey = Crypto.getInstance().createKeyPair(seed).getA();
		
		byte[] data = "test123!".getBytes();
		
		PrivateKeyAccount creator = new PrivateKeyAccount(privateKey);
		Account recipient = new Account("QfreeNWCeaU3BiXUxktaJRJrBB1SDg2k7o");		

		long timestamp = NTP.getTime();
		long key = 2l;
		
		creator.setConfirmedBalance(key, BigDecimal.valueOf(100).setScale(8), db);
				
		MessageTransaction messageTransactionV3 = new MessageTransaction(
				creator, FEE_POWER, //	ATFunding 
				recipient, 
				key, 
				BigDecimal.valueOf(10).setScale(8), 
				data,
				new byte[] { 1 },
				new byte[] { 0 },
				timestamp, creator.getLastReference(db)
				);
		messageTransactionV3.sign(creator, false);
		
		assertEquals(messageTransactionV3.isValid(db, releaserReference), Transaction.VALIDATE_OK);
		
		messageTransactionV3.process(db, false);
		
		assertEquals(BigDecimal.valueOf(0.99999508).setScale(8), creator.getConfirmedBalance(OIL_KEY, db));
		assertEquals(BigDecimal.valueOf(90).setScale(8), creator.getConfirmedBalance(key, db));
		assertEquals(BigDecimal.valueOf(10).setScale(8), recipient.getConfirmedBalance(key, db));
		
		byte[] rawMessageTransactionV3 = messageTransactionV3.toBytes(true, null);
		
		MessageTransaction messageTransactionV3_2 = null;
		try {
			messageTransactionV3_2 = (MessageTransaction) MessageTransaction.Parse(Arrays.copyOfRange(rawMessageTransactionV3, 4, rawMessageTransactionV3.length), releaserReference);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		assertEquals(new String(messageTransactionV3.getData()), new String(messageTransactionV3_2.getData()));
		assertEquals(messageTransactionV3.getCreator(), messageTransactionV3_2.getCreator());
		assertEquals(messageTransactionV3.getRecipient(), messageTransactionV3_2.getRecipient());
		assertEquals(messageTransactionV3.getKey(), messageTransactionV3_2.getKey());
		assertEquals(messageTransactionV3.getAmount(), messageTransactionV3_2.getAmount());
		assertEquals(messageTransactionV3.isEncrypted(), messageTransactionV3_2.isEncrypted());
		assertEquals(messageTransactionV3.isText(), messageTransactionV3_2.isText());
		
		assertEquals(messageTransactionV3.isSignatureValid(), true);
		assertEquals(messageTransactionV3_2.isSignatureValid(), true);		
	}
	
	
	@Test
	public void validateArbitraryTransactionV3() 
	{
		
		//CREATE EMPTY MEMORY DATABASE
		DBSet databaseSet = DBSet.createEmptyDatabaseSet();
		
		//ADD ERM ASSET
		AssetCls ermAsset = new AssetVenture(new GenesisBlock().getGenerator(), "DATACHAINS.world", "This is the simulated ERM asset.", 10000000000L, (byte) 2, true);
		ermAsset.setReference(assetReference);
		AssetCls aTFundingAsset = new AssetVenture(new GenesisBlock().getGenerator(), "ATFunding", "This asset represents the funding of AT team for the integration of a Turing complete virtual machine into ERM.", 250000000L, (byte) 2, true);
		aTFundingAsset.setReference(assetReference);
		databaseSet.getAssetMap().set(0l, ermAsset);
		databaseSet.getAssetMap().set(61l, aTFundingAsset);
    	
		GenesisBlock genesisBlock = new GenesisBlock();
		genesisBlock.process(databaseSet);
		
		//CREATE KNOWN ACCOUNT
		byte[] seed = Crypto.getInstance().digest("test".getBytes());
		byte[] privateKey = Crypto.getInstance().createKeyPair(seed).getA();
		
		byte[] data = "test123!".getBytes();
		
		PrivateKeyAccount creator = new PrivateKeyAccount(privateKey);
		Account recipient1 = new Account("QfreeNWCeaU3BiXUxktaJRJrBB1SDg2k7o");		
		Account recipient2 = new Account("QbVq5kgfYY1kRh9EdLSQfR9XHxVy1fLstQ");		
		Account recipient3 = new Account("QcJCST3wT8t22jKM2FFDhL8zKiH8cuBjEB");		

		long timestamp = NTP.getTime();

		//PROCESS GENESIS TRANSACTION TO MAKE SURE SENDER HAS FUNDS
		Transaction transaction = new GenesisTransaction(creator, BigDecimal.valueOf(1000).setScale(8), NTP.getTime());
		transaction.process(databaseSet, false);
		
		creator.setConfirmedBalance(61l, BigDecimal.valueOf(1000).setScale(8), databaseSet);
		
		List<Payment> payments = new ArrayList<Payment>();
		payments.add(new Payment(recipient1, 61l, BigDecimal.valueOf(110).setScale(8)));
		payments.add(new Payment(recipient2, 61l, BigDecimal.valueOf(120).setScale(8)));
		payments.add(new Payment(recipient3, 61l, BigDecimal.valueOf(201).setScale(8)));
				
		ArbitraryTransactionV3 arbitraryTransactionV3 = new ArbitraryTransactionV3(
				creator, payments, 111,
				data, 
				FEE_POWER,
				timestamp, creator.getLastReference(databaseSet)
				);
		arbitraryTransactionV3.sign(creator, false);
		
		//if (NTP.getTime() < Transaction.getARBITRARY_TRANSACTIONS_RELEASE() || arbitraryTransactionV3.getTimestamp() < Transaction.getPOWFIX_RELEASE())
		if (false)
		{
			assertEquals(arbitraryTransactionV3.isValid(databaseSet, releaserReference), Transaction.NOT_YET_RELEASED);
		}
		else
		{
			assertEquals(arbitraryTransactionV3.isValid(databaseSet, releaserReference), Transaction.VALIDATE_OK);
		}
		
		arbitraryTransactionV3.process(databaseSet, false);
		
		assertEquals(BigDecimal.valueOf(1000).setScale(8), creator.getConfirmedBalance(databaseSet));
		assertEquals(BigDecimal.valueOf(1000-110-120-201).setScale(8), creator.getConfirmedBalance(61l, databaseSet));
		assertEquals(BigDecimal.valueOf(110).setScale(8), recipient1.getConfirmedBalance(61l, databaseSet));
		assertEquals(BigDecimal.valueOf(120).setScale(8), recipient2.getConfirmedBalance(61l, databaseSet));
		assertEquals(BigDecimal.valueOf(201).setScale(8), recipient3.getConfirmedBalance(61l, databaseSet));
		
		byte[] rawArbitraryTransactionV3 = arbitraryTransactionV3.toBytes(true, null);
		
		ArbitraryTransactionV3 arbitraryTransactionV3_2 = null;
		try {
			arbitraryTransactionV3_2 = (ArbitraryTransactionV3) ArbitraryTransactionV3.Parse(Arrays.copyOfRange(rawArbitraryTransactionV3, 0, rawArbitraryTransactionV3.length));
			// already SIGNED - arbitraryTransactionV3_2.sign(creator);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		assertEquals(new String(arbitraryTransactionV3.getData()), new String(arbitraryTransactionV3_2.getData()));
		assertEquals(	arbitraryTransactionV3.getPayments().get(0).toJson().toJSONString(), 
						arbitraryTransactionV3_2.getPayments().get(0).toJson().toJSONString());
		assertEquals(	arbitraryTransactionV3.getPayments().get(1).toJson().toJSONString(), 
						arbitraryTransactionV3_2.getPayments().get(1).toJson().toJSONString());
		assertEquals(	arbitraryTransactionV3.getPayments().get(2).toJson().toJSONString(), 
						arbitraryTransactionV3_2.getPayments().get(2).toJson().toJSONString());
		assertEquals( 	arbitraryTransactionV3.getPayments().size(), arbitraryTransactionV3.getPayments().size());  

		assertEquals(arbitraryTransactionV3.getService(), arbitraryTransactionV3_2.getService());
		assertEquals(arbitraryTransactionV3.getCreator(), arbitraryTransactionV3_2.getCreator());

		assertEquals(arbitraryTransactionV3.isSignatureValid(), true);
		assertEquals(arbitraryTransactionV3_2.isSignatureValid(), true);		
	}	
	
	@Test
	public void validateArbitraryTransactionV3withoutPayments() 
	{
		
		//CREATE EMPTY MEMORY DATABASE
		DBSet databaseSet = DBSet.createEmptyDatabaseSet();
		    	
		GenesisBlock genesisBlock = new GenesisBlock();
		genesisBlock.process(databaseSet);

		//ADD ERM ASSET
		AssetCls ermAsset = new AssetVenture(genesisBlock.getGenerator(), "DATACHAINS.world", "This is the simulated ERM asset.", 10000000000L, (byte) 2, true);
		ermAsset.setReference(assetReference);
		AssetCls aTFundingAsset = new AssetVenture(genesisBlock.getGenerator(), "ATFunding", "This asset represents the funding of AT team for the integration of a Turing complete virtual machine into ERM.", 250000000L, (byte) 2, true);
		aTFundingAsset.setReference(genesisBlock.getGeneratorSignature());
		databaseSet.getAssetMap().set(0l, ermAsset);
		databaseSet.getAssetMap().set(61l, aTFundingAsset);

		//CREATE KNOWN ACCOUNT
		byte[] seed = Crypto.getInstance().digest("test".getBytes());
		byte[] privateKey = Crypto.getInstance().createKeyPair(seed).getA();
		
		byte[] data = "test123!".getBytes();
		
		PrivateKeyAccount creator = new PrivateKeyAccount(privateKey);
		
		long timestamp = NTP.getTime();

		//PROCESS GENESIS TRANSACTION TO MAKE SURE SENDER HAS FUNDS
		Transaction transaction = new GenesisTransaction(creator, BigDecimal.valueOf(1000).setScale(8), NTP.getTime());
		transaction.process(databaseSet, false);
		
		creator.setConfirmedBalance(61l, BigDecimal.valueOf(1000).setScale(8), databaseSet);
		
		List<Payment> payments = new ArrayList<Payment>();
				
		ArbitraryTransactionV3 arbitraryTransactionV3 = new ArbitraryTransactionV3(
				creator, payments, 111,
				data, 
				FEE_POWER,
				timestamp, creator.getLastReference(databaseSet)
				);
		arbitraryTransactionV3.sign(creator, false);
		
		//if (NTP.getTime() < Transaction.getARBITRARY_TRANSACTIONS_RELEASE() || arbitraryTransactionV3.getTimestamp() < Transaction.getPOWFIX_RELEASE())
		if (false)
		{
			assertEquals(arbitraryTransactionV3.isValid(databaseSet, releaserReference), Transaction.NOT_YET_RELEASED);
		}
		else
		{
			assertEquals(arbitraryTransactionV3.isValid(databaseSet, releaserReference), Transaction.VALIDATE_OK);
		}
		
		arbitraryTransactionV3.process(databaseSet, false);
		
		assertEquals(BigDecimal.valueOf(999.999988).setScale(8), creator.getConfirmedBalance(databaseSet));
		assertEquals(BigDecimal.valueOf(1000).setScale(8), creator.getConfirmedBalance(61l, databaseSet));

		
		byte[] rawArbitraryTransactionV3 = arbitraryTransactionV3.toBytes(true, null);
		
		ArbitraryTransactionV3 arbitraryTransactionV3_2 = null;
		try {
			arbitraryTransactionV3_2 = (ArbitraryTransactionV3) ArbitraryTransactionV3.Parse(Arrays.copyOfRange(rawArbitraryTransactionV3, 4, rawArbitraryTransactionV3.length));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		assertEquals(new String(arbitraryTransactionV3.getData()), new String(arbitraryTransactionV3_2.getData()));

		assertEquals( 	arbitraryTransactionV3.getPayments().size(), arbitraryTransactionV3.getPayments().size());  

		assertEquals(arbitraryTransactionV3.getService(), arbitraryTransactionV3_2.getService());
		assertEquals(arbitraryTransactionV3.getCreator(), arbitraryTransactionV3_2.getCreator());

		assertEquals(arbitraryTransactionV3.isSignatureValid(), true);
		assertEquals(arbitraryTransactionV3_2.isSignatureValid(), true);		
	}	
	
}