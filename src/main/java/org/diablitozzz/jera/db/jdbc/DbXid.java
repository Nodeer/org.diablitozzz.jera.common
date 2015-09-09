package org.diablitozzz.jera.db.jdbc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

import javax.transaction.xa.Xid;

class DbXid implements Xid, Serializable {

	private static final long serialVersionUID = 1L;
	private final byte[] transactionId = new byte[Xid.MAXGTRIDSIZE];
	private final byte[] branch = new byte[Xid.MAXBQUALSIZE];

	DbXid() {
		//for unserialize
	}

	public DbXid(UUID uuid) {

		this.branch[0] = 0;
		byte[] buuid = uuid.toString().getBytes();
		int length = buuid.length > this.transactionId.length ? this.transactionId.length : buuid.length;

		System.arraycopy(buuid, 0, this.transactionId, 0, length);
	}

	@Override
	public boolean equals(Object o) {

		Xid other = (Xid) o;
		if (other.getFormatId() != this.getFormatId()) {
			return false;
		}
		if (!Arrays.equals(other.getBranchQualifier(), this.getBranchQualifier())) {
			return false;
		}
		if (!Arrays.equals(other.getGlobalTransactionId(), this.getGlobalTransactionId())) {
			return false;
		}

		return true;
	}

	@Override
	public byte[] getBranchQualifier() {
		return this.branch;
	}

	@Override
	public int getFormatId() {
		return 0;
	}

	@Override
	public byte[] getGlobalTransactionId() {
		return this.transactionId;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.getGlobalTransactionId());
	}
}
