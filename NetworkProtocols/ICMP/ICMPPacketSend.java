package NetworkProtocols.ICMP;

import NetworkProtocols.IP.Datagram;
import NetworkProtocols.IP.Address.IpAddress;

public class ICMPPacketSend {
	private byte type;
	private byte code;
	private IpAddress dest;
	private Datagram data;

	public ICMPPacketSend(byte type, byte code, IpAddress dest, Datagram data) {
		this.type = type;
		this.code = code;
		this.dest = dest;
		this.data = data;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getType() {
		return type;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public void setDest(IpAddress dest) {
		this.dest = dest;
	}

	public IpAddress getDest() {
		return dest;
	}

	public void setData(Datagram data) {
		this.data = data;
	}

	public Datagram getData() {
		return data;
	}
}
