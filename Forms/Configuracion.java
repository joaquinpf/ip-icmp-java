/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Configuracion.java
 *
 * Created on 12/11/2009, 10:38:37
 */

package Forms;

import javax.swing.JOptionPane;

import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;

/**
 *
 * @author Administrator
 */
public class Configuracion extends javax.swing.JFrame {
	private static final long serialVersionUID = 4090957822834807053L;
	@SuppressWarnings("unused")
	private static IpAddress addr1 = null;
	private static IpAddress addrDstSimulada = null;
	private static IpAddress addrSrcSimulada = null;
	private static Interface ifz1 = null;
	private static Mask mask = null;
	private static IP ipProtocol = null;

    /** Creates new form Configuracion */
    public Configuracion() {
        initComponents();
		try {
			mask = new Mask(24);
		} catch (AddressException e1) {
			System.out.println("Error al crear la mascara");
			e1.printStackTrace();
		}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtDireccionIPLocal = new javax.swing.JTextField();
        txtPuertoLocal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDireccionIPRemota = new javax.swing.JTextField();
        txtPuertoRemoto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDireccionIPLocalSimulada = new javax.swing.JTextField();
        txtDireccionIPRemotaSimulada = new javax.swing.JTextField();
        cmdGuardar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        txtDireccionIPLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionIPLocalActionPerformed(evt);
            }
        });

        jLabel1.setText("Direccion IP local:");

        jLabel2.setText("Direccion IP remota:");

        jLabel3.setText("Puerto remoto:");

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 0, 0));
        jLabel4.setText("Configuracion");

        jLabel5.setText("Puerto local:");

        jLabel6.setText("Direccion IP remota:");

        txtDireccionIPLocalSimulada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionIPLocalSimuladaActionPerformed(evt);
            }
        });

        cmdGuardar.setText("Guardar");
        cmdGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGuardarActionPerformed(evt);
            }
        });

        jLabel8.setText("Direccion IP local:");

        jLabel10.setFont(new java.awt.Font("Comic Sans MS", 3, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 0, 0));
        jLabel10.setText("Real");

        jLabel11.setFont(new java.awt.Font("Comic Sans MS", 3, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 0, 0));
        jLabel11.setText("Simulado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(17, 17, 17)
                                        .addComponent(txtDireccionIPLocalSimulada))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDireccionIPRemotaSimulada, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtDireccionIPLocal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(cmdGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txtDireccionIPLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtDireccionIPLocalSimulada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDireccionIPRemotaSimulada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cmdGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDireccionIPLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionIPLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionIPLocalActionPerformed

    private void cmdGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdGuardarActionPerformed
		byte[] dir = new byte[4];
		String[] partes;
		String rem = this.txtDireccionIPRemota.getText().replace('.', 'P');
		partes = rem.split("P");
		for (int i = 0; i < 4; i++)
			dir[i] = (new Integer(Integer.parseInt(partes[i]))).byteValue();
		Interfaces ifzs = new Interfaces();
    	try {
			addr1 = new IpAddress(this.txtDireccionIPLocal.getText().replace(".", "P"));
		} catch (AddressException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear la direccion ip local", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

		try {
			addrDstSimulada = new IpAddress(this.txtDireccionIPRemotaSimulada.getText().replace(".", "P"));
		} catch (AddressException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear la direccion ip remota simulada", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
    	try {
			addrSrcSimulada = new IpAddress(this.txtDireccionIPLocalSimulada.getText().replace(".", "P"));
		} catch (AddressException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear la direccion ip local simulada", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

		try {
			ifz1 = ifzs.addInterface("ifz1", addrSrcSimulada, mask, 280);
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear la interface", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		
		// Esta parte se podria cambiar para hacer mas dinamico la creacin de la red
		Link l1 = null;
		try {
			l1 = new Link(this.txtDireccionIPLocal.getText(), this.txtPuertoLocal.getText(), this.txtDireccionIPRemota.getText(), this.txtPuertoRemoto.getText());
    		l1.setInterface(ifz1);
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el link 1", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		try {
			ipProtocol = new IP(ifzs);
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el protocolo IP", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		ipProtocol.setLocalIpAddress(addrSrcSimulada);
		//cmdGuardar.setEnabled(false);
		ipProtocol.addRoute(addrDstSimulada, mask, true, addrDstSimulada, ifz1);
		try {
			ipProtocol.addDefault(addrDstSimulada, ifz1);
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear la ruta por defecto sobre ip", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		Principal p = new Principal(this);
		p.setVisible(true);
		this.setVisible(false);
    }//GEN-LAST:event_cmdGuardarActionPerformed

	public IP getIP(){
		return ipProtocol;
	}

	public IpAddress getAddressDst() {
		return addrDstSimulada;
	}

	private void txtDireccionIPLocalSimuladaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionIPLocalSimuladaActionPerformed
        // TODO add your handling code here:
	}//GEN-LAST:event_txtDireccionIPLocalSimuladaActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuracion().setVisible(true);
            }
        });
    }

    public static void setAddrDstSimulada(IpAddress addrDstSimulada) {
		Configuracion.addrDstSimulada = addrDstSimulada;
	}

	public IpAddress getAddrDstSimulada() {
		return addrDstSimulada;
	}

	public static void setAddrSrcSimulada(IpAddress addrSrcSimulada) {
		Configuracion.addrSrcSimulada = addrSrcSimulada;
	}

	public IpAddress getAddrSrcSimulada() {
		return addrSrcSimulada;
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField txtDireccionIPLocal;
    private javax.swing.JTextField txtDireccionIPLocalSimulada;
    private javax.swing.JTextField txtDireccionIPRemota;
    private javax.swing.JTextField txtDireccionIPRemotaSimulada;
    private javax.swing.JTextField txtPuertoLocal;
    private javax.swing.JTextField txtPuertoRemoto;
    // End of variables declaration//GEN-END:variables

}
