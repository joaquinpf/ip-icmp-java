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
public class Configuracion2 extends javax.swing.JFrame {
	private static final long serialVersionUID = 4090957822834807053L;
	private static IpAddress addr1 = null;
	private static IpAddress addrDst = null;
	private static Interface ifz1 = null;
	private static Mask mask = null;
	private static IP ipProtocol = null;

    /** Creates new form Configuracion */
    public Configuracion2() {
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
    private void initComponents() {

        txtDireccionIPLocal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDireccionIPRemota = new javax.swing.JTextField();
        txtPuertoRemoto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPuertoLocal = new javax.swing.JTextField();
        cmdGuardar = new javax.swing.JButton();

        txtDireccionIPLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionIPLocalActionPerformed(evt);
            }
        });

        jLabel1.setText("Direccion IP local:");

        jLabel2.setText("Direccion IP remota:");

        jLabel3.setText("Puerto remoto:");

        jLabel4.setFont(new java.awt.Font("Comic Sans MS", 3, 18));
        jLabel4.setForeground(new java.awt.Color(102, 0, 0));
        jLabel4.setText("Configuracion local");

        jLabel5.setText("Puerto local:");

        cmdGuardar.setText("Guardar");
        cmdGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(17, 17, 17)
                                .addComponent(txtDireccionIPLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                                .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6)
                        .addComponent(cmdGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtDireccionIPLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(cmdGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDireccionIPLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionIPLocalActionPerformed
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
    		JOptionPane.showMessageDialog(this, "Error al crear la ip", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		try {
			ifz1 = ifzs.addInterface("ifz1", addr1, mask, 280);
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
		ipProtocol.setLocalIpAddress(addr1);
		cmdGuardar.setEnabled(false);
		Principal2 p = new Principal2();//new Principal(this);
		p.setVisible(true);
		this.setVisible(false);
	}//GEN-LAST:event_cmdGuardarActionPerformed

	public IP getIP(){
		return ipProtocol;
	}

	public IpAddress getAddressDst() {
		return addrDst;
	}
	
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


	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtDireccionIPLocal;
    private javax.swing.JTextField txtDireccionIPRemota;
    private javax.swing.JTextField txtPuertoLocal;
    private javax.swing.JTextField txtPuertoRemoto;
    // End of variables declaration//GEN-END:variables

}
