package NetworkProtocols.IP.Address;

import Exceptions.AddressException;

public class IpAddress {
	private Integer IpAddress; // Dirección IPv4

	// Convierte de notacion decimal con puntos a IpAddress (4 bytes). Genera
	// excepcion si direccion no valida
	public IpAddress(String dottedDecimal) throws AddressException {
		String[] partes;
		Integer[] n = new Integer[4];
		partes = dottedDecimal.split("P");
		if (partes.length != 4)
			throw new AddressException("Bad IP address");
		for (int i = 0; i < 4; i++)
			n[i] = new Integer(partes[i]);
		for (int i = 0; i < 4; i++)
			if (n[i] < 0 || n[i] > 255)
				throw new AddressException("Bad IP address");
		IpAddress = new Integer(((n[0] * 256 + n[1]) * 256 + n[2]) * 256 + n[3]);
	}

	// Convierte un Integer a un IpAddress. Genera una excepcion si invalida
	public IpAddress(Integer x) throws AddressException {
		if ((x < 0) || (x >= 0xFFFFFFFF))
			throw new AddressException("Bad IP Address");
		IpAddress = x;
	}

	// Convierte cuatro bytes a una IpAddress
	public IpAddress(byte a, byte b, byte c, byte d) {
		int cc = (int) (((a << 24) & 0x00ff000000) + ((b << 16) & 0x00ff0000)
				+ ((c << 8) & 0x00ff00) + (d & 0x00ff));
		IpAddress = new Integer(cc);
	}

	// Devuelve true si la direccion parámetro es igual
	public boolean equals(IpAddress f) {
		boolean r = false;
		if (this.IpAddress.equals(f.IpAddress))
			r = true;
		return r;
	}

	// Convierte un IpAddress a cuatro bytes
	public byte[] toByte() {
		byte[] b = new byte[4];
		int y = IpAddress.intValue();
		b[0] = (byte) ((y & 0xff000000) >> 24);
		b[1] = (byte) ((y & 0xff0000) >> 16);
		b[2] = (byte) ((y & 0xff00) >> 8);
		b[3] = (byte) (y & 0xFF);
		return b;
	}

	// Devuelve la dirección IP en formato decimal con puntos
	public String toString() {
		int y = IpAddress.intValue();
		int y4 = y & 0x000000ff;
		int y3 = (y >> 8) & 0x000000ff;
		int y2 = (y >> 16) & 0x000000ff;
		int y1 = (y >> 24) & 0x000000ff;
		String yy = y1 + "." + y2 + "." + y3 + "." + y4;
		return yy;
	}

	// Devuelve un int correspondiente al valor en 32 bits de la IpAddress
	public int toInt() {
		return IpAddress.intValue();
	}

	// Dado un prefijo, devuelve la dirección de red
	// public Integer getNetwork(Integer prefix) {

	// }
}
