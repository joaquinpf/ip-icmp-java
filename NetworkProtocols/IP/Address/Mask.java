package NetworkProtocols.IP.Address;

import Exceptions.AddressException;

public class Mask {
	private Integer Mask; // mascara de 32 bits, para los and
	private int prefix; // prefijo para no recalcularlo

	// Crea una mascara dado un prefijo de red
	public Mask(Integer pref) throws AddressException {
		prefix = pref.intValue();
		if (prefix < 0 || prefix > 31)
			throw new AddressException("Invalid prefix");
		// Mask 0 es un caso especial que se utiliza para indicar default route
		// en tabla de ruteo
		if (prefix == 0) {
			Mask = new Integer(0);
		} else {
			Mask = new Integer(0xFFFFFFFF << (32 - prefix));
		}
	}

	public boolean equals(Mask f) {
		boolean r = false;
		if (this.Mask.equals(f.Mask))
			r = true;
		return r;
	}

	public Integer toIntegerMask() {
		return Mask;
	}

	public int toInt() {
		return Mask.intValue();
	}

	public int toPrefix() {
		return prefix;
	}

	// Devuelve la mascara en formato decimal con puntos
	public String toString() {
		int y = Mask.intValue();
		int y4 = y & 0x000000ff;
		int y3 = (y >> 8) & 0x000000ff;
		int y2 = (y >> 16) & 0x000000ff;
		int y1 = (y >> 24) & 0x000000ff;
		String yy = y1 + "." + y2 + "." + y3 + "." + y4;
		return yy;
	}
	
	public byte[] getBytesMask(){
		int y = Mask.intValue();
		byte[] ret = new byte[4];
		ret[3] = (new Integer(y & 0x000000ff)).byteValue();
		ret[2] = (new Integer((y >> 8) & 0x000000ff)).byteValue();
		ret[1] = (new Integer((y >> 16) & 0x000000ff)).byteValue();
		ret[0] = (new Integer((y >> 24) & 0x000000ff)).byteValue();
		return ret;
	}
}
