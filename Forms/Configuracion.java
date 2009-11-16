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

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import Link.Linkreceiver;
import Link.Linksender;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;

/**
 *
 * @author Administrator
 */
public class Configuracion extends javax.swing.JFrame {

	IpAddress addr1 = null;
	Interface ifz1 = null;
	Mask mask = null;
	IP ipProtocol = null;
	
	
    /** Creates new form Configuracion */
    public Configuracion() {
        initComponents();
		try {
			mask = new Mask(8);
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtDireccionIPLocal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDireccionIPRemota = new javax.swing.JTextField();
        txtPuertoRemoto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmdAgregarLink = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listLinks = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        txtPuertoLocal = new javax.swing.JTextField();
        cmdGuardar = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

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
        jLabel4.setText("Configuracion local");

        cmdAgregarLink.setText("Agregar link");
        cmdAgregarLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAgregarLinkActionPerformed(evt);
            }
        });

        listLinks.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listLinks);

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
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(17, 17, 17)
                        .addComponent(txtDireccionIPLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(163, 163, 163)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdAgregarLink)))))
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
                    .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionIPRemota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdAgregarLink))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
			Linksender ls = new Linksender(l1, new DatagramSocket(Integer.parseInt(this.txtPuertoLocal.getText())), Inet4Address.getByAddress(dir), Integer.parseInt(this.txtPuertoRemoto.getText()));
		} catch (NumberFormatException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkSender", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (SocketException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkSender", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (UnknownHostException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkSender", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} 
		
		try {
			Linkreceiver lr = new Linkreceiver(l1, new DatagramSocket(Integer.parseInt(this.txtPuertoLocal.getText())));
		} catch (NumberFormatException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkReceiver NumberFormatException", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (SocketException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkReceiver SocketException", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el linkReceiver NodeException", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		try {
			this.ipProtocol = new IP(ifzs);
		} catch (NodeException e) {
    		JOptionPane.showMessageDialog(this, "Error al crear el protocolo IP", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		this.ipProtocol.setLocalIpAddress(addr1);
		this.cmdGuardar.setEnabled(false);
    }//GEN-LAST:event_cmdGuardarActionPerformed

    private void cmdAgregarLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAgregarLinkActionPerformed
    	if (ifz1 == null){
    		JOptionPane.showMessageDialog(this, "Debe guardar la configuracion IP primero", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
    	}
    	else{
    		Link l1;
    		try {
    			l1 = new Link(this.txtDireccionIPLocal.getText(), this.txtPuertoLocal.getText(), this.txtDireccionIPRemota.getText(), this.txtPuertoRemoto.getText());
        		l1.setInterface(ifz1);
    		} catch (NodeException e) {
        		JOptionPane.showMessageDialog(this, "Error al crear el link", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
    			e.printStackTrace();
    		}
    		//Mostrar link en la JList
    	}
    }//GEN-LAST:event_cmdAgregarLinkActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

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
    private javax.swing.JButton cmdAgregarLink;
    private javax.swing.JButton cmdGuardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList listLinks;
    private javax.swing.JTextField txtDireccionIPLocal;
    private javax.swing.JTextField txtDireccionIPRemota;
    private javax.swing.JTextField txtPuertoLocal;
    private javax.swing.JTextField txtPuertoRemoto;
    // End of variables declaration//GEN-END:variables

}