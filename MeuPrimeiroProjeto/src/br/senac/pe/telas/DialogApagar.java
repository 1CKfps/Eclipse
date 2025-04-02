package br.senac.pe.telas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.senac.pe.banco.ConexaoMySQL;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogApagar extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldIdCliente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DialogApagar dialog = new DialogApagar();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void apagarUsuario() {
	    String idCliente = textFieldIdCliente.getText();


	    // Verifica se o campo está vazio
	    if (idCliente.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Por favor, informe o ID do cliente.");
	        return;
	    }


	    try (Connection conn = ConexaoMySQL.getConnection()) {
	        // Verifica se o cliente existe no banco de dados
	        String sqlVerificar = "SELECT * FROM usuario WHERE id = ?";
	        PreparedStatement stmtVerificar = conn.prepareStatement(sqlVerificar);
	        stmtVerificar.setString(1, idCliente);
	        ResultSet rs = stmtVerificar.executeQuery();


	        if (rs.next()) {
	            // Se o cliente existir, perguntar se deseja apagar
	            int confirmacao = JOptionPane.showConfirmDialog(this, 
	                    "Cliente encontrado! Deseja apagar o registro?", 
	                    "Confirmação", 
	                    JOptionPane.YES_NO_OPTION);


	            if (confirmacao == JOptionPane.YES_OPTION) {
	                // Realiza a exclusão do registro
	                String sqlApagar = "DELETE FROM usuario WHERE id = ?";
	                PreparedStatement stmtApagar = conn.prepareStatement(sqlApagar);
	                stmtApagar.setString(1, idCliente);


	                int resultado = stmtApagar.executeUpdate();
	                if (resultado > 0) {
	                    JOptionPane.showMessageDialog(this, "Cliente apagado com sucesso!");
	                } else {
	                    JOptionPane.showMessageDialog(this, "Erro ao apagar cliente!");
	                }
	            } else {
	                JOptionPane.showMessageDialog(this, "Operação cancelada.");
	            }
	        } else {
	            JOptionPane.showMessageDialog(this, "Cliente não encontrado!");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Erro ao acessar o banco de dados!");
	    }
	}

	
	/**
	 * Create the dialog.
	 */
	public DialogApagar() {
		setBounds(100, 100, 274, 188);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textFieldIdCliente = new JTextField();
		textFieldIdCliente.setBounds(29, 26, 86, 20);
		contentPanel.add(textFieldIdCliente);
		textFieldIdCliente.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Id do Usuário:");
		lblNewLabel.setBounds(29, 11, 78, 14);
		contentPanel.add(lblNewLabel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						apagarUsuario();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
